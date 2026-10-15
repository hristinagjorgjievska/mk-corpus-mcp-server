package mk.ukim.finki.mcptoolbackend.service.domain;

import java.util.Optional;
import mk.ukim.finki.mcptoolbackend.model.domain.Resource;
import mk.ukim.finki.mcptoolbackend.model.dto.ResourceFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Domain service for resources: paged/filtered browsing, lookup, deletion, and
 * the analysis step (via the {@code ResourceAnalyzer} / {@code LanguageDetector}
 * seams).
 */
public interface ResourceService {
    Page<Resource> findAll(ResourceFilterDto filter, Pageable pageable);

    Optional<Resource> findById(Long id);

    Optional<Resource> deleteById(Long id);

    /**
     * Analyzes one resource: run {@code ResourceAnalyzer.analyze}, persist a
     * {@code ResourceAnalysis}, and return the updated resource.
     */
    Resource analyze(Long id);
}
