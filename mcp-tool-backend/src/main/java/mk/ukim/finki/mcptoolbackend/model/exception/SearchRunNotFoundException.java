package mk.ukim.finki.mcptoolbackend.model.exception;

public class SearchRunNotFoundException extends RuntimeException {
    public SearchRunNotFoundException(Long id) {
        super("A search run with id %d does not exist.".formatted(id));
    }
}
