package mk.ukim.finki.mcptoolbackend.analysis;

/**
 * Estimates how confidently a piece of text is written in Macedonian. Used to
 * annotate every fetched {@code Resource} so the corpus stays Macedonian.
 */
public interface LanguageDetector {
    /**
     * @return a confidence in the range {@code [0.0, 1.0]} that {@code text} is Macedonian
     */
    double macedonianConfidence(String text);
}
