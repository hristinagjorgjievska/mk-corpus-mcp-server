package mk.ukim.finki.mcptoolbackend.service.domain;

import java.util.List;
import java.util.Optional;
import mk.ukim.finki.mcptoolbackend.model.domain.DonationBatch;

/**
 * Domain service for the donation workflow towards doniraj.vezilka.ai, driven
 * through the {@code VezilkaClient} seam.
 */
public interface DonationService {
    List<DonationBatch> findAll();

    Optional<DonationBatch> findById(Long id);

    /** Groups the given resources into a new DRAFT batch. */
    DonationBatch create(List<Long> resourceIds);

    /** DRAFT -> APPROVED. */
    DonationBatch approve(Long id);

    /** APPROVED -> SUBMITTED, calling {@code VezilkaClient.submitTextDonation}. */
    DonationBatch submit(Long id);

    /** Polls Vezilka for the status of every SUBMITTED batch (called by the scheduler). */
    void refreshSubmittedStatuses();
}
