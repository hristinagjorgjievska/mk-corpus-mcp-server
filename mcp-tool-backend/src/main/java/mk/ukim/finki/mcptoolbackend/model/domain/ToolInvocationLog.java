package mk.ukim.finki.mcptoolbackend.model.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An audit record of a single MCP tool invocation — which tool ran, with what
 * arguments, whether it succeeded and a short summary of the result. Optionally
 * linked to the {@link SearchRun} it belongs to.
 *
 * <p>Analogous to the reference project's "bot action log": it lets the
 * frontend show a live trace of what the MCP server has done. Recording is
 * fully provided (see {@code ToolInvocationLogService}); you do not implement
 * this entity.</p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tool_invocation_logs")
public class ToolInvocationLog extends BaseAuditableEntity {
    @Column(nullable = false)
    private String toolName;

    @Column(columnDefinition = "text")
    private String arguments;

    @Column(columnDefinition = "text")
    private String resultSummary;

    @Column(nullable = false)
    private boolean successful;

    @Column(nullable = false)
    private LocalDateTime occurredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_run_id")
    private SearchRun searchRun;

    public ToolInvocationLog(String toolName, String arguments, String resultSummary, boolean successful) {
        this.toolName = toolName;
        this.arguments = arguments;
        this.resultSummary = resultSummary;
        this.successful = successful;
        this.occurredAt = LocalDateTime.now();
    }
}
