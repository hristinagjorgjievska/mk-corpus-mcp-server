package mk.ukim.finki.mcptoolbackend.repository;

import java.util.List;
import mk.ukim.finki.mcptoolbackend.model.domain.DonationBatch;
import mk.ukim.finki.mcptoolbackend.model.enums.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationBatchRepository extends JpaRepository<DonationBatch, Long> {
    List<DonationBatch> findAllByOrderByCreatedAtDesc();

    List<DonationBatch> findAllByStatus(DonationStatus status);
}
