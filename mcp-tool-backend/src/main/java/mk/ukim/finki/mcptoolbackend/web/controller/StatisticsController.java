package mk.ukim.finki.mcptoolbackend.web.controller;

import mk.ukim.finki.mcptoolbackend.model.dto.DisplayCorpusStatsDto;
import mk.ukim.finki.mcptoolbackend.service.application.StatisticsApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Corpus statistics for the home dashboard. Fully provided — returns the same
 * data as the {@code corpus_stats} MCP tool.
 */
@RestController
@RequestMapping("/api/stats")
public class StatisticsController {
    private final StatisticsApplicationService statisticsApplicationService;

    public StatisticsController(StatisticsApplicationService statisticsApplicationService) {
        this.statisticsApplicationService = statisticsApplicationService;
    }

    @GetMapping
    public ResponseEntity<DisplayCorpusStatsDto> getStatistics() {
        return ResponseEntity.ok(statisticsApplicationService.getStatistics());
    }
}
