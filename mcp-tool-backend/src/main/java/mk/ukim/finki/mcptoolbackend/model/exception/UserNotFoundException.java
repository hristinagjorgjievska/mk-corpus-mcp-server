package mk.ukim.finki.mcptoolbackend.model.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("A user with the username '%s' does not exist.".formatted(username));
    }
}
