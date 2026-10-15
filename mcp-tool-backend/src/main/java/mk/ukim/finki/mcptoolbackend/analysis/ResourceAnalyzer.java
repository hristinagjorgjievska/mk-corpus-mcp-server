package mk.ukim.finki.mcptoolbackend.analysis;

/**
 * The data-analysis seam of the MCP tool: turns a resource's raw text into a
 * summary, keywords and simple statistics.
 */
public interface ResourceAnalyzer {
    /**
     * Analyzes the given resource text.
     *
     * @param title   the resource title (may be null)
     * @param content the resource's text content
     */
    AnalysisOutcome analyze(String title, String content);
}
