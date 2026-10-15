package mk.ukim.finki.mcptoolbackend.model.exception;

/**
 * Thrown by the {@code VezilkaClient} when communication with
 * doniraj.vezilka.ai fails.
 */
public class VezilkaIntegrationException extends RuntimeException {
    public VezilkaIntegrationException(String message) {
        super(message);
    }

    public VezilkaIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
