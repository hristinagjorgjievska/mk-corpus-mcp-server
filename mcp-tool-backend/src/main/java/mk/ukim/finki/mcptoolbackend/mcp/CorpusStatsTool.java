package mk.ukim.finki.mcptoolbackend.mcp;

import mk.ukim.finki.mcptoolbackend.model.dto.DisplayCorpusStatsDto;
import mk.ukim.finki.mcptoolbackend.service.application.StatisticsApplicationService;
import mk.ukim.finki.mcptoolbackend.service.domain.ToolInvocationLogService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

/**
 * MCP tool that reads aggregate corpus statistics from the database and
 * returns them.
 *
 * <p>Connect the MCP Inspector to {@code http://localhost:8080/sse} and invoke
 * {@code corpus_stats}: you should get a JSON object back.</p>
 */
@Service
public class CorpusStatsTool {
    private final StatisticsApplicationService statisticsApplicationService;
    private final ToolInvocationLogService toolInvocationLogService;

    public CorpusStatsTool(StatisticsApplicationService statisticsApplicationService,
                           ToolInvocationLogService toolInvocationLogService) {
        this.statisticsApplicationService = statisticsApplicationService;
        this.toolInvocationLogService = toolInvocationLogService;
    }

    @Tool(name = "corpus_stats",
        description = "Return aggregate statistics about the collected Macedonian-resource corpus: "
            + "the number of search runs, resources, analyzed resources, donation batches and donated resources.")
    public DisplayCorpusStatsDto corpusStats() {
        DisplayCorpusStatsDto stats = statisticsApplicationService.getStatistics();
        toolInvocationLogService.log(
            "corpus_stats",
            "{}",
            "resources=%d, searchRuns=%d, donations=%d".formatted(stats.resources(), stats.searchRuns(), stats.donationBatches()),
            true,
            null
        );
        return stats;
    }
}
