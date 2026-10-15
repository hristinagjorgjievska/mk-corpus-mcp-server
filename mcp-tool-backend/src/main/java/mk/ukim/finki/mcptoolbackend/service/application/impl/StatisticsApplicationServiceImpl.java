package mk.ukim.finki.mcptoolbackend.service.application.impl;

import mk.ukim.finki.mcptoolbackend.model.dto.DisplayCorpusStatsDto;
import mk.ukim.finki.mcptoolbackend.service.application.StatisticsApplicationService;
import mk.ukim.finki.mcptoolbackend.service.domain.StatisticsService;
import org.springframework.stereotype.Service;

@Service
public class StatisticsApplicationServiceImpl implements StatisticsApplicationService {
    private final StatisticsService statisticsService;

    public StatisticsApplicationServiceImpl(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Override
    public DisplayCorpusStatsDto getStatistics() {
        return statisticsService.getStatistics();
    }
}
