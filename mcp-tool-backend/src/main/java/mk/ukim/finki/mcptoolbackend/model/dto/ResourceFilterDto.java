package mk.ukim.finki.mcptoolbackend.model.dto;

/**
 * Filter criteria for the paged resource browser
 * ({@code GET /api/resources}). All fields are optional; a {@code null} field
 * means "do not filter by this".
 */
public record ResourceFilterDto(
    Long searchRunId,
    Double minMacedonianConfidence,
    Boolean analyzed,
    Boolean donated,
    String search
) {
}
