package mk.ukim.finki.mcptoolbackend.service.domain;

import java.util.List;
import mk.ukim.finki.mcptoolbackend.model.domain.SearchRun;
import mk.ukim.finki.mcptoolbackend.model.domain.ToolInvocationLog;

/**
 * Records every MCP tool invocation so the frontend can show a live trace.
 *
 * <p>Fully provided (analogous to the reference project's bot-action-log
 * service) — call {@link #log} from your MCP tools; do not reimplement it.</p>
 */
public interface ToolInvocationLogService {
    ToolInvocationLog log(String toolName, String arguments, String resultSummary, boolean successful, SearchRun searchRun);

    List<ToolInvocationLog> findAll();

    List<ToolInvocationLog> findBySearchRun(Long searchRunId);
}
