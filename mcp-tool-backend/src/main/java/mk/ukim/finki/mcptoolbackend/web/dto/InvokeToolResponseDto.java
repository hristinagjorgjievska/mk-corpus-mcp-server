package mk.ukim.finki.mcptoolbackend.web.dto;

/**
 * The outcome of invoking an MCP tool from the playground. On success
 * {@code result} holds the tool's JSON output; on failure {@code error} holds
 * the message (e.g. a not-yet-implemented tool).
 */
public record InvokeToolResponseDto(
    String tool,
    String arguments,
    boolean successful,
    String result,
    String error
) {
}
