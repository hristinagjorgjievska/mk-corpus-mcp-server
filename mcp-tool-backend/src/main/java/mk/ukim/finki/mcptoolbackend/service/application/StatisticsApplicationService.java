package mk.ukim.finki.mcptoolbackend.service.application;

import mk.ukim.finki.mcptoolbackend.model.dto.DisplayCorpusStatsDto;

public interface StatisticsApplicationService {
    DisplayCorpusStatsDto getStatistics();
}
