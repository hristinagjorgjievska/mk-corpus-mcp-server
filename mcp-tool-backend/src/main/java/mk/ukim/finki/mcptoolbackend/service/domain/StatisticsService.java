package mk.ukim.finki.mcptoolbackend.service.domain;

import mk.ukim.finki.mcptoolbackend.model.dto.DisplayCorpusStatsDto;

/**
 * Read-only aggregate statistics over the collected corpus.
 *
 * <p>Fully provided — it backs the {@code corpus_stats} MCP tool and the
 * {@code GET /api/stats} endpoint, so the MCP wiring and a real REST endpoint
 * work before you implement anything else.</p>
 */
public interface StatisticsService {
    DisplayCorpusStatsDto getStatistics();
}
