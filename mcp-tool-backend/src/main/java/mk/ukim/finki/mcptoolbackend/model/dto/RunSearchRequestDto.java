package mk.ukim.finki.mcptoolbackend.model.dto;

/**
 * Request body for {@code POST /api/search-runs/run} and the input of the
 * {@code search_resources} MCP tool.
 *
 * @param query the free-text search query (Macedonian or English)
 * @param limit the maximum number of resources to fetch (nullable → a default)
 */
public record RunSearchRequestDto(
    String query,
    Integer limit
) {
}
