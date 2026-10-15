package mk.ukim.finki.mcptoolbackend.service.domain.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import mk.ukim.finki.mcptoolbackend.analysis.LanguageDetector;
import mk.ukim.finki.mcptoolbackend.model.domain.Resource;
import mk.ukim.finki.mcptoolbackend.model.domain.SearchRun;
import mk.ukim.finki.mcptoolbackend.model.enums.SearchStatus;
import mk.ukim.finki.mcptoolbackend.repository.ResourceRepository;
import mk.ukim.finki.mcptoolbackend.repository.SearchRunRepository;
import mk.ukim.finki.mcptoolbackend.service.domain.SearchRunService;
import mk.ukim.finki.mcptoolbackend.source.FetchedResource;
import mk.ukim.finki.mcptoolbackend.source.SourceClient;
import org.springframework.stereotype.Service;

@Service
public class SearchRunServiceImpl implements SearchRunService {
    private final SearchRunRepository searchRunRepository;
    private final ResourceRepository resourceRepository;
    private final SourceClient sourceClient;
    private final LanguageDetector languageDetector;

    public SearchRunServiceImpl(SearchRunRepository searchRunRepository,
                                ResourceRepository resourceRepository,
                                SourceClient sourceClient,
                                LanguageDetector languageDetector) {
        this.searchRunRepository = searchRunRepository;
        this.resourceRepository = resourceRepository;
        this.sourceClient = sourceClient;
        this.languageDetector = languageDetector;
    }

    @Override
    public List<SearchRun> findAll() {
        return searchRunRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Optional<SearchRun> findById(Long id) {
        return searchRunRepository.findById(id);
    }

    @Override
    public SearchRun run(String query, int limit) {
        SearchRun searchRun = new SearchRun(query);
        searchRun.setStatus(SearchStatus.RUNNING);
        searchRun.setStartedAt(LocalDateTime.now());
        searchRun = searchRunRepository.save(searchRun);

        int savedResourceCount = 0;

        try {
            List<FetchedResource> fetchedResources = sourceClient.search(query, limit);

            for (FetchedResource fetchedResource : fetchedResources) {
                boolean isAlreadySaved = resourceRepository.exists((root, criteriaQuery, cb) ->
                        cb.equal(root.get("sourceUrl"), fetchedResource.sourceUrl()));

                if (isAlreadySaved) continue;

                Resource resource = new Resource(searchRun,
                        fetchedResource.externalId(),
                        fetchedResource.title(),
                        fetchedResource.content(),
                        fetchedResource.sourceUrl());
                resource.setFetchedAt(LocalDateTime.now());
                resource.setWordCount(countWords(fetchedResource.content()));
                double confidence = languageDetector.macedonianConfidence(fetchedResource.content());
                resource.setMacedonianConfidence(confidence);
                resource.setLanguage(confidence >= 0.5 ? "mkd" : "und");
                resourceRepository.save(resource);
                savedResourceCount++;
            }

            searchRun.setResultCount(savedResourceCount);
            searchRun.setStatus(SearchStatus.COMPLETED);
            searchRun.setFinishedAt(LocalDateTime.now());

            return searchRunRepository.save(searchRun);
        } catch (RuntimeException exception) {
            searchRun.setStatus(SearchStatus.FAILED);
            searchRun.setFinishedAt(LocalDateTime.now());
            searchRunRepository.save(searchRun);

            throw exception;
        }
    }

    @Override
    public List<Resource> findResources(Long searchRunId) {
        return resourceRepository.findAllBySearchRunId(searchRunId);
    }

    private int countWords(String content) {
        if (content == null || content.isBlank())
            return 0;

        return content.trim().split("\\s+").length;
    }
}
