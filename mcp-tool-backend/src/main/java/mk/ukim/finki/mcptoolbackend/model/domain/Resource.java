package mk.ukim.finki.mcptoolbackend.model.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A single Macedonian-language resource found on the assigned website: its
 * text content, provenance and a Macedonian-language confidence, optionally
 * enriched with a {@link ResourceAnalysis} and grouped into a
 * {@link DonationBatch}.
 *
 * <p>Analogous to an "extracted post" in the reference project.</p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "resources")
public class Resource extends BaseAuditableEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "search_run_id", nullable = false)
    private SearchRun searchRun;

    private String externalId;

    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @Column(length = 2048)
    private String sourceUrl;

    private String language;

    private Double macedonianConfidence;

    private Integer wordCount;

    private LocalDateTime fetchedAt;

    @OneToOne(mappedBy = "resource", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ResourceAnalysis analysis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_batch_id")
    private DonationBatch donationBatch;

    @Version
    private Long version;

    public Resource(SearchRun searchRun, String externalId, String title, String content, String sourceUrl) {
        this.searchRun = searchRun;
        this.externalId = externalId;
        this.title = title;
        this.content = content;
        this.sourceUrl = sourceUrl;
    }
}
