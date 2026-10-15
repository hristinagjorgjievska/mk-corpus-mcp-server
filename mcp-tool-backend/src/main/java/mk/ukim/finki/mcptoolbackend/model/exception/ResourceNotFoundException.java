package mk.ukim.finki.mcptoolbackend.model.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Long id) {
        super("A resource with id %d does not exist.".formatted(id));
    }
}
