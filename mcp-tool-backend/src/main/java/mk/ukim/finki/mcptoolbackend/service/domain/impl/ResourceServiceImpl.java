package mk.ukim.finki.mcptoolbackend.service.domain.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import mk.ukim.finki.mcptoolbackend.analysis.AnalysisOutcome;
import mk.ukim.finki.mcptoolbackend.analysis.LanguageDetector;
import mk.ukim.finki.mcptoolbackend.analysis.ResourceAnalyzer;
import mk.ukim.finki.mcptoolbackend.model.domain.Resource;
import mk.ukim.finki.mcptoolbackend.model.domain.ResourceAnalysis;
import mk.ukim.finki.mcptoolbackend.model.dto.ResourceFilterDto;
import mk.ukim.finki.mcptoolbackend.model.exception.ResourceNotFoundException;
import mk.ukim.finki.mcptoolbackend.repository.ResourceRepository;
import mk.ukim.finki.mcptoolbackend.service.domain.ResourceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.JoinType;

/**
 * Filters resources with a {@code Specification<Resource>} built from the
 * non-null fields of {@link ResourceFilterDto}; {@code analyze} delegates to
 * {@link ResourceAnalyzer} and {@link LanguageDetector}.
 */
@Service
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;
    private final ResourceAnalyzer resourceAnalyzer;
    private final LanguageDetector languageDetector;

    public ResourceServiceImpl(ResourceRepository resourceRepository,
                               ResourceAnalyzer resourceAnalyzer,
                               LanguageDetector languageDetector) {
        this.resourceRepository = resourceRepository;
        this.resourceAnalyzer = resourceAnalyzer;
        this.languageDetector = languageDetector;
    }

    @Override
    public Page<Resource> findAll(ResourceFilterDto filter, Pageable pageable) {
        Specification<Resource> specification = (root, query, cb) -> cb.conjunction();

        if (filter.searchRunId() != null){
            specification = specification.and((root, query, cb) -> cb.equal(root.get("searchRun").get("id"), filter.searchRunId()));
        }

        if (filter.minMacedonianConfidence() != null){
            specification = specification.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("macedonianConfidence"), filter.minMacedonianConfidence()));
        }

        if (filter.donated() != null){
            specification = specification.and((root, query, cb) -> filter.donated()
                    ? cb.isNotNull(root.get("donationBatch"))
                    : cb.isNull(root.get("donationBatch")));
        }

        if (filter.search() != null && !filter.search().isBlank()){
            String pattern = "%" + filter.search().toLowerCase() + "%";

            specification = specification.and((root, query, cb) -> cb.or(cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("content")), pattern)));
        }

        if (filter.analyzed() != null){
            specification = specification.and((root, query, cb) -> {
                var analysis = root.join("analysis", JoinType.LEFT);
                return filter.analyzed() ? cb.isNotNull(analysis.get("id")) : cb.isNull(analysis.get("id"));
            });
        }

        return resourceRepository.findAll(specification, pageable);
    }

    @Override
    public Optional<Resource> findById(Long id) {
        return resourceRepository.findById(id);
    }

    @Override
    public Optional<Resource> deleteById(Long id) {
        Optional<Resource> resource = findById(id);
        resource.ifPresent(resourceRepository::delete);
        return resource;
    }

    @Override
    public Resource analyze(Long id) {
        Resource resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

        AnalysisOutcome analysisOutcome = resourceAnalyzer.analyze(resource.getTitle(), resource.getContent());

        ResourceAnalysis resourceAnalysis;
        if (resource.getAnalysis() != null){
            resourceAnalysis = resource.getAnalysis();
        }
        else resourceAnalysis = new ResourceAnalysis(resource);

        resourceAnalysis.setSummary(analysisOutcome.summary());
        resourceAnalysis.setKeywords(String.join(", ", analysisOutcome.keywords()));
        resourceAnalysis.setSentenceCount(analysisOutcome.sentenceCount());
        resourceAnalysis.setMacedonianConfidence(languageDetector.macedonianConfidence(resource.getContent()));
        resourceAnalysis.setAnalyzedAt(LocalDateTime.now());

        resource.setAnalysis(resourceAnalysis);
        return resourceRepository.save(resource);
    }
}
