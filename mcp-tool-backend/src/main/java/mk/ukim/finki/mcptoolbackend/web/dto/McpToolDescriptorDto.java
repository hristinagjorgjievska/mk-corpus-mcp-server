package mk.ukim.finki.mcptoolbackend.web.dto;

/**
 * A description of one registered MCP tool, as exposed to the frontend
 * "MCP playground". {@code inputSchema} is the JSON Schema (as a string) that an
 * MCP agent would see.
 */
public record McpToolDescriptorDto(
    String name,
    String description,
    String inputSchema
) {
}
