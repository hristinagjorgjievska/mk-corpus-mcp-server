package mk.ukim.finki.mcptoolbackend.service.application.impl;

import java.util.List;
import java.util.Optional;
import mk.ukim.finki.mcptoolbackend.model.dto.CreateDonationBatchDto;
import mk.ukim.finki.mcptoolbackend.model.dto.DisplayDonationBatchDto;
import mk.ukim.finki.mcptoolbackend.service.application.DonationApplicationService;
import mk.ukim.finki.mcptoolbackend.service.domain.DonationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DonationApplicationServiceImpl implements DonationApplicationService {
    private final DonationService donationService;

    public DonationApplicationServiceImpl(DonationService donationService) {
        this.donationService = donationService;
    }

    @Override
    public List<DisplayDonationBatchDto> findAll() {
        return DisplayDonationBatchDto.from(donationService.findAll());

    }

    @Override
    public Optional<DisplayDonationBatchDto> findById(Long id) {
        return donationService.findById(id)
                .map(DisplayDonationBatchDto::from);
    }

    @Override
    public Optional<DisplayDonationBatchDto> create(CreateDonationBatchDto request) {
        return Optional.of(DisplayDonationBatchDto.from(
                donationService.create(request.resourceIds())
        ));
    }

    @Override
    public Optional<DisplayDonationBatchDto> approve(Long id) {
        return Optional.of(DisplayDonationBatchDto.from(
                donationService.approve(id)
        ));
    }

    @Override
    public Optional<DisplayDonationBatchDto> submit(Long id) {
        return Optional.of(DisplayDonationBatchDto.from(
                donationService.submit(id)
        ));
    }
}
