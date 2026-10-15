package mk.ukim.finki.mcptoolbackend.model.enums;

/**
 * Lifecycle of a {@code SearchRun}:
 * PENDING -> RUNNING -> COMPLETED | FAILED
 */
public enum SearchStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED
}
