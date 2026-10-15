package mk.ukim.finki.mcptoolbackend.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import mk.ukim.finki.mcptoolbackend.web.dto.InvokeToolResponseDto;
import mk.ukim.finki.mcptoolbackend.web.dto.McpToolDescriptorDto;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * An in-app "MCP playground": lists the MCP tools registered on this server
 * (with their JSON Schemas) and invokes them by name — the same tools an MCP
 * agent such as the Vezilka host would call. It lets the frontend
 * demonstrate the MCP tools without an external MCP client.
 *
 * <p>A tool that fails returns {@code successful=false} with the error
 * message, so the playground stays usable.</p>
 */
@RestController
@RequestMapping("/api/mcp")
public class McpPlaygroundController {
    private final List<ToolCallback> toolCallbacks;
    private final ObjectMapper objectMapper;

    public McpPlaygroundController(ToolCallbackProvider toolCallbackProvider, ObjectMapper objectMapper) {
        this.toolCallbacks = Arrays.asList(toolCallbackProvider.getToolCallbacks());
        this.objectMapper = objectMapper;
    }

    @GetMapping("/tools")
    public ResponseEntity<List<McpToolDescriptorDto>> tools() {
        List<McpToolDescriptorDto> descriptors = toolCallbacks.stream()
            .map(tc -> new McpToolDescriptorDto(
                tc.getToolDefinition().name(),
                tc.getToolDefinition().description(),
                tc.getToolDefinition().inputSchema()))
            .toList();
        return ResponseEntity.ok(descriptors);
    }

    @PostMapping("/tools/{name}/invoke")
    public ResponseEntity<InvokeToolResponseDto> invoke(@PathVariable String name,
                                                        @RequestBody(required = false) Map<String, Object> arguments) {
        Optional<ToolCallback> maybeTool = toolCallbacks.stream()
            .filter(tc -> tc.getToolDefinition().name().equals(name))
            .findFirst();
        if (maybeTool.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String input;
        try {
            input = objectMapper.writeValueAsString(arguments == null ? Map.of() : arguments);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }

        try {
            String result = maybeTool.get().call(input);
            return ResponseEntity.ok(new InvokeToolResponseDto(name, input, true, result, null));
        } catch (Exception e) {
            // A tool that is not implemented yet (or fails) is reported as an
            // unsuccessful invocation rather than a server error.
            return ResponseEntity.ok(new InvokeToolResponseDto(name, input, false, null, rootMessage(e)));
        }
    }

    private String rootMessage(Throwable t) {
        Throwable cause = t;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause.getMessage() == null ? cause.toString() : cause.getMessage();
    }
}
