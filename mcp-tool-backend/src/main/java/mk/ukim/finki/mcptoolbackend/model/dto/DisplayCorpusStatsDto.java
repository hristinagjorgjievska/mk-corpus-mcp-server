package mk.ukim.finki.mcptoolbackend.model.dto;

/**
 * Aggregate statistics over everything the MCP tool has collected — the return
 * value of both the {@code GET /api/stats} endpoint and the provided
 * {@code corpus_stats} MCP tool.
 */
public record DisplayCorpusStatsDto(
    long searchRuns,
    long resources,
    long analyzedResources,
    long donationBatches,
    long donatedResources
) {
}
