package mk.ukim.finki.mcptoolbackend.analysis;

import java.util.List;

/**
 * The result of analyzing one resource's text. Mapped onto a
 * {@code ResourceAnalysis} entity by the analysis flow.
 *
 * @param summary       a short summary of the content
 * @param keywords      the most salient keywords/terms
 * @param sentenceCount number of sentences detected
 * @param wordCount     number of words detected
 */
public record AnalysisOutcome(
    String summary,
    List<String> keywords,
    Integer sentenceCount,
    Integer wordCount
) {
}
