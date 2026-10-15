package mk.ukim.finki.mcptoolbackend.service.domain.impl;

import mk.ukim.finki.mcptoolbackend.model.dto.DisplayCorpusStatsDto;
import mk.ukim.finki.mcptoolbackend.repository.DonationBatchRepository;
import mk.ukim.finki.mcptoolbackend.repository.ResourceRepository;
import mk.ukim.finki.mcptoolbackend.repository.SearchRunRepository;
import mk.ukim.finki.mcptoolbackend.service.domain.StatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final SearchRunRepository searchRunRepository;
    private final ResourceRepository resourceRepository;
    private final DonationBatchRepository donationBatchRepository;

    public StatisticsServiceImpl(SearchRunRepository searchRunRepository,
                                 ResourceRepository resourceRepository,
                                 DonationBatchRepository donationBatchRepository) {
        this.searchRunRepository = searchRunRepository;
        this.resourceRepository = resourceRepository;
        this.donationBatchRepository = donationBatchRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DisplayCorpusStatsDto getStatistics() {
        return new DisplayCorpusStatsDto(
            searchRunRepository.count(),
            resourceRepository.count(),
            resourceRepository.countByAnalysisIsNotNull(),
            donationBatchRepository.count(),
            resourceRepository.countByDonationBatchIsNotNull()
        );
    }
}
