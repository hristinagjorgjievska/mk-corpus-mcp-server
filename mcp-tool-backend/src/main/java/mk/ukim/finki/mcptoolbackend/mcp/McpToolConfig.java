package mk.ukim.finki.mcptoolbackend.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers every {@code @Tool}-annotated bean with the MCP server. The Spring
 * AI MCP auto-configuration picks up this {@link ToolCallbackProvider} and
 * exposes the tools over the HTTP+SSE transport (see {@code /sse} in
 * application.properties).
 *
 * <p>This wiring is provided. When you add a new tool class, add it to the
 * {@code toolObjects(...)} call below.</p>
 */
@Configuration
public class McpToolConfig {
    @Bean
    public ToolCallbackProvider vezilkaMcpTools(CorpusStatsTool corpusStatsTool,
                                                ResourceSearchTool resourceSearchTool,
                                                ResourceAnalysisTool resourceAnalysisTool,
                                                DonationTool donationTool,
                                                CorpusGapTool corpusGapTool) {
        return MethodToolCallbackProvider.builder()
            .toolObjects(corpusStatsTool, resourceSearchTool, resourceAnalysisTool, donationTool, corpusGapTool)
            .build();
    }
}
