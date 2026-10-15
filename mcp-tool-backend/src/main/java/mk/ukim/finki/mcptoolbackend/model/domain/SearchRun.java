package mk.ukim.finki.mcptoolbackend.model.domain;

import jakarta.persistence.CascadeType;
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
import mk.ukim.finki.mcptoolbackend.model.enums.SearchStatus;

/**
 * One execution of a search against the assigned Macedonian-resource website:
 * a query, its lifecycle status and the resources it produced.
 *
 * <p>Analogous to an "extraction session" in the reference project — it is the
 * unit of work behind the {@code search_resources} MCP tool and the
 * {@code POST /api/search-runs/run} endpoint.</p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "search_runs")
public class SearchRun extends BaseAuditableEntity {
    @Column(nullable = false)
    private String query;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SearchStatus status;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private Integer resultCount;

    @OneToMany(mappedBy = "searchRun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resource> resources = new ArrayList<>();

    public SearchRun(String query) {
        this.query = query;
        this.status = SearchStatus.PENDING;
        this.resultCount = 0;
    }
}
