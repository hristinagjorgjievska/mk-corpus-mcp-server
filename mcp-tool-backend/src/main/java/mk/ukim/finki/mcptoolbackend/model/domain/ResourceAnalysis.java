package mk.ukim.finki.mcptoolbackend.model.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The result of the data-analysis step on a single {@link Resource}: a summary,
 * extracted keywords and simple corpus statistics. Produced by the
 * {@code analyze_resource} MCP tool via the {@code ResourceAnalyzer} seam.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "resource_analyses")
public class ResourceAnalysis extends BaseAuditableEntity {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resource_id", nullable = false, unique = true)
    private Resource resource;

    @Column(columnDefinition = "text")
    private String summary;

    @Column(columnDefinition = "text")
    private String keywords;

    private Integer sentenceCount;

    private Double macedonianConfidence;

    private LocalDateTime analyzedAt;

    public ResourceAnalysis(Resource resource) {
        this.resource = resource;
    }
}
