package mk.ukim.finki.mcptoolbackend.model.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mk.ukim.finki.mcptoolbackend.model.enums.DonationStatus;

/**
 * A batch of {@link Resource}s donated together to doniraj.vezilka.ai.
 *
 * <p>Copied unchanged in spirit from the reference project: the donation
 * workflow is DRAFT -> APPROVED -> SUBMITTED -> ACCEPTED | REJECTED | FAILED,
 * with {@code vezilkaReference} holding the identifier returned by Vezilka.</p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "donation_batches")
public class DonationBatch extends BaseAuditableEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonationStatus status;

    private String vezilkaReference;

    private LocalDateTime submittedAt;

    @OneToMany(mappedBy = "donationBatch")
    private List<Resource> resources = new ArrayList<>();

    public DonationBatch(DonationStatus status) {
        this.status = status;
    }
}
