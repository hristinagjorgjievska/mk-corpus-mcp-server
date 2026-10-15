package mk.ukim.finki.mcptoolbackend.mcp;

import mk.ukim.finki.mcptoolbackend.model.dto.DisplayResourceDto;
import mk.ukim.finki.mcptoolbackend.service.application.ResourceApplicationService;
import mk.ukim.finki.mcptoolbackend.service.domain.ToolInvocationLogService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

/**
 * MCP tool for the data-analysis step.
 */
@Service
public class ResourceAnalysisTool {
    private final ResourceApplicationService resourceApplicationService;
    private final ToolInvocationLogService toolInvocationLogService;

    public ResourceAnalysisTool(ResourceApplicationService resourceApplicationService,
                                ToolInvocationLogService toolInvocationLogService) {
        this.resourceApplicationService = resourceApplicationService;
        this.toolInvocationLogService = toolInvocationLogService;
    }

    @Tool(name = "analyze_resource",
        description = "Run data analysis on a stored resource (summary, keywords, sentence/word counts and "
            + "Macedonian-language confidence) and return it enriched with the analysis.")
    public DisplayResourceDto analyzeResource(
        @ToolParam(description = "The id of the stored resource to analyze") Long id) {
        DisplayResourceDto resourceDto = resourceApplicationService.analyze(id)
                .orElseThrow(() -> new IllegalArgumentException("Ресурсот со id " + id + " не е пронајден"));

        toolInvocationLogService.log(
                "analyze_resource",
                String.format("\"id\":%d", id),
                String.format("title=%s", resourceDto.title()),
                true,
                null
        );

        return resourceDto;
    }
}
