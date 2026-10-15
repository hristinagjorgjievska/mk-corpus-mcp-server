package mk.ukim.finki.mcptoolbackend.service.domain;

import java.util.List;
import java.util.Optional;
import mk.ukim.finki.mcptoolbackend.model.domain.Resource;
import mk.ukim.finki.mcptoolbackend.model.domain.SearchRun;

/**
 * Domain service for search runs. Speaks entities and orchestrates the
 * data-source and language-detection seams.
 */
public interface SearchRunService {
    List<SearchRun> findAll();

    Optional<SearchRun> findById(Long id);

    /**
     * Runs a search end-to-end: create a {@link SearchRun}, call
     * {@code SourceClient.search}, annotate each hit with
     * {@code LanguageDetector.macedonianConfidence}, persist the resulting
     * {@link Resource}s, and mark the run COMPLETED (or FAILED on error).
     */
    SearchRun run(String query, int limit);

    List<Resource> findResources(Long searchRunId);
}
