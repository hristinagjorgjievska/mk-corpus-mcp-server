package mk.ukim.finki.mcptoolbackend.service.domain.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import mk.ukim.finki.mcptoolbackend.integration.vezilka.DonationReceipt;
import mk.ukim.finki.mcptoolbackend.integration.vezilka.TextDonationRequest;
import mk.ukim.finki.mcptoolbackend.integration.vezilka.VezilkaClient;
import mk.ukim.finki.mcptoolbackend.model.domain.DonationBatch;
import mk.ukim.finki.mcptoolbackend.model.domain.Resource;
import mk.ukim.finki.mcptoolbackend.model.enums.DonationStatus;
import mk.ukim.finki.mcptoolbackend.model.exception.DonationBatchNotFoundException;
import mk.ukim.finki.mcptoolbackend.model.exception.InvalidDonationStateException;
import mk.ukim.finki.mcptoolbackend.model.exception.VezilkaIntegrationException;
import mk.ukim.finki.mcptoolbackend.repository.DonationBatchRepository;
import mk.ukim.finki.mcptoolbackend.repository.ResourceRepository;
import mk.ukim.finki.mcptoolbackend.service.domain.DonationService;
import mk.ukim.finki.mcptoolbackend.source.SourceProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Enforces the DRAFT -> APPROVED -> SUBMITTED lifecycle (throws
 * {@code InvalidDonationStateException} on illegal transitions). On
 * {@code submit} it sends one {@code TextDonationRequest} per resource through
 * {@link VezilkaClient} and stores the returned references on the batch.
 */
@Service
public class DonationServiceImpl implements DonationService {
    private final DonationBatchRepository donationBatchRepository;
    private final ResourceRepository resourceRepository;
    private final VezilkaClient vezilkaClient;
    private final SourceProperties sourceProperties;

    private static final double DONATION_CONTENT_RATIO = 0.3;

    public DonationServiceImpl(DonationBatchRepository donationBatchRepository,
                               ResourceRepository resourceRepository,
                               VezilkaClient vezilkaClient,
                               SourceProperties sourceProperties) {
        this.donationBatchRepository = donationBatchRepository;
        this.resourceRepository = resourceRepository;
        this.vezilkaClient = vezilkaClient;
        this.sourceProperties = sourceProperties;
    }

    @Override
    public List<DonationBatch> findAll() {
        return donationBatchRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Optional<DonationBatch> findById(Long id) {
        return donationBatchRepository.findById(id);
    }

    @Override
    @Transactional
    public DonationBatch create(List<Long> resourceIds) {
        List<Resource> resourceList = resourceRepository.findAllByIdIn(resourceIds).stream()
                .filter(r -> r.getDonationBatch() == null)
                .toList();

        if (resourceList.isEmpty()){
            throw new IllegalArgumentException("Нема слободни статии за нов пакет, сите се распоредени во други пакети.");
        }
        DonationBatch batch = donationBatchRepository.save(new DonationBatch(DonationStatus.DRAFT));

        for (Resource resource : resourceList){
            resource.setDonationBatch(batch);
            batch.getResources().add(resource);
        }

        resourceRepository.saveAll(resourceList);

        return batch;
    }

    @Override
    @Transactional
    public DonationBatch approve(Long id) {
        DonationBatch batch = getBatch(id);

        if (batch.getStatus() != DonationStatus.DRAFT){
            throw new InvalidDonationStateException(id, batch.getStatus());
        }

        batch.setStatus(DonationStatus.APPROVED);
        return donationBatchRepository.save(batch);
    }

    @Override
    @Transactional
    public DonationBatch submit(Long id) {
        DonationBatch batch = getBatch(id);

        if (batch.getStatus() != DonationStatus.APPROVED){
            throw new InvalidDonationStateException(id, batch.getStatus());
        }

        try {
            List<String> references = new ArrayList<>();
            for (Resource resource : batch.getResources()){
                TextDonationRequest donationRequest = new TextDonationRequest(
                        resource.getTitle(),
                        prepareContent(resource.getContent(), resource.getSourceUrl()),
                        resource.getSourceUrl()
                );
                DonationReceipt receipt = vezilkaClient.submitTextDonation(donationRequest);

                if (receipt.reference() == null){
                    batch.setStatus(DonationStatus.REJECTED);
                    return donationBatchRepository.save(batch);
                }
                references.add(receipt.reference());
            }

            batch.setVezilkaReference(String.join(",", references));
            batch.setStatus(DonationStatus.SUBMITTED);
            batch.setSubmittedAt(LocalDateTime.now());
        } catch (VezilkaIntegrationException exception){
            batch.setStatus(DonationStatus.FAILED);
        }

        return donationBatchRepository.save(batch);
    }

    @Override
    @Transactional
    public void refreshSubmittedStatuses() {
        for (DonationBatch batch : donationBatchRepository.findAllByStatus(DonationStatus.SUBMITTED)){
            List<DonationStatus> statusList = Arrays.stream(batch.getVezilkaReference().split(","))
                    .map(vezilkaClient::checkStatus)
                    .toList();

            if (statusList.contains(DonationStatus.FAILED)){
                batch.setStatus(DonationStatus.FAILED);
            }
            else if (statusList.contains(DonationStatus.REJECTED)){
                batch.setStatus(DonationStatus.REJECTED);
            }
            else if (statusList.stream().allMatch(s -> s == DonationStatus.ACCEPTED)){
                batch.setStatus(DonationStatus.ACCEPTED);
            }
        }
    }

    private DonationBatch getBatch(Long id){
        return donationBatchRepository.findById(id)
                .orElseThrow(() -> new DonationBatchNotFoundException(id));
    }

    private String prepareContent(String content, String sourceUrl){
        if (content == null || content.isBlank())
            return content;

        int maxLength = (int) (content.length() * DONATION_CONTENT_RATIO);
        int cut = content.lastIndexOf(" ", maxLength);
        String excerpt = content.substring(0, cut > 0 ? cut : maxLength).trim();

        //Името и доменот на изворот се читаат од .env, за кодот да не зависи од конкретен сајт
        String domain = sourceProperties.baseUrl().replaceFirst("^https?://", "");
        return excerpt + "...\n\nИзвор: " + sourceProperties.name() + " (" + domain + ") - " + sourceUrl;
    }
}
