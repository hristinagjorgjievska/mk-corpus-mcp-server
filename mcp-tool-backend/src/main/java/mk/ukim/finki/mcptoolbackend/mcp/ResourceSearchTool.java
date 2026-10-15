package mk.ukim.finki.mcptoolbackend.mcp;

import mk.ukim.finki.mcptoolbackend.model.dto.DisplayResourceDto;
import mk.ukim.finki.mcptoolbackend.model.dto.DisplaySearchRunDto;
import mk.ukim.finki.mcptoolbackend.model.dto.RunSearchRequestDto;
import mk.ukim.finki.mcptoolbackend.service.application.ResourceApplicationService;
import mk.ukim.finki.mcptoolbackend.service.application.SearchRunApplicationService;
import mk.ukim.finki.mcptoolbackend.service.domain.ToolInvocationLogService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

/**
 * MCP tools for searching the assigned website and reading stored resources.
 */
@Service
public class ResourceSearchTool {
    private final SearchRunApplicationService searchRunApplicationService;
    private final ResourceApplicationService resourceApplicationService;
    private final ToolInvocationLogService toolInvocationLogService;

    public ResourceSearchTool(SearchRunApplicationService searchRunApplicationService,
                              ResourceApplicationService resourceApplicationService,
                              ToolInvocationLogService toolInvocationLogService) {
        this.searchRunApplicationService = searchRunApplicationService;
        this.resourceApplicationService = resourceApplicationService;
        this.toolInvocationLogService = toolInvocationLogService;
    }

    @Tool(name = "search_resources",
        description = "Search the assigned Macedonian-language website for resources matching the query, "
            + "store them, and return the resulting search run (id, status, result count).")
    public DisplaySearchRunDto searchResources(
        @ToolParam(description = "Free-text search query, in Macedonian or English") String query,
        @ToolParam(description = "Maximum number of resources to fetch", required = false) Integer limit) {
        DisplaySearchRunDto searchRunDto = searchRunApplicationService.run(new RunSearchRequestDto(query, limit)).orElseThrow();

        toolInvocationLogService.log(
                "search_resources",
                String.format("{\"query\":\"%s\",\"limit\":%s}", query, limit),
                String.format("searchRunId=%d, results=%d", searchRunDto.id(), searchRunDto.resultCount()),
                true,
                null
        );

        return searchRunDto;
    }

    @Tool(name = "get_resource",
        description = "Fetch a single stored resource by its id, including its full text content and any analysis.")
    public DisplayResourceDto getResource(
        @ToolParam(description = "The id of the stored resource") Long id) {
        DisplayResourceDto displayResourceDto = resourceApplicationService.findById(id).orElseThrow(() -> new IllegalArgumentException("Ресурсот со id " + id + " не е пронајден"));

        toolInvocationLogService.log(
                "get_resource",
                String.format("{\"id\":%d}", id),
                String.format("title=%s", displayResourceDto.title()),
                true,
                null
        );

        return displayResourceDto;
    }
}
