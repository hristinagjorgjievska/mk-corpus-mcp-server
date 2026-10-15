package mk.ukim.finki.mcptoolbackend.web.controller;

import java.util.List;
import mk.ukim.finki.mcptoolbackend.model.dto.DisplayToolInvocationLogDto;
import mk.ukim.finki.mcptoolbackend.service.application.ToolInvocationLogApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Read-only access to the MCP tool-invocation trace. Fully provided — the
 * frontend polls this to render a live log of what the MCP server has done.
 */
@RestController
@RequestMapping("/api/tool-logs")
public class ToolInvocationLogController {
    private final ToolInvocationLogApplicationService toolInvocationLogApplicationService;

    public ToolInvocationLogController(ToolInvocationLogApplicationService toolInvocationLogApplicationService) {
        this.toolInvocationLogApplicationService = toolInvocationLogApplicationService;
    }

    @GetMapping
    public ResponseEntity<List<DisplayToolInvocationLogDto>> findAll(@RequestParam(required = false) Long searchRunId) {
        List<DisplayToolInvocationLogDto> logs = searchRunId == null
            ? toolInvocationLogApplicationService.findAll()
            : toolInvocationLogApplicationService.findBySearchRun(searchRunId);
        return ResponseEntity.ok(logs);
    }
}
