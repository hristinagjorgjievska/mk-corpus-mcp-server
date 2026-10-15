package mk.ukim.finki.mcptoolbackend.source;

import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Placeholder so the application boots before the assignment is implemented.
 */
//@Component
public class StubSourceClient implements SourceClient {
    private final SourceProperties sourceProperties;

    public StubSourceClient(SourceProperties sourceProperties) {
        this.sourceProperties = sourceProperties;
    }

    @Override
    public List<FetchedResource> search(String query, int limit) {
        throw new UnsupportedOperationException("Not implemented: SourceClient.search() for " + sourceProperties.name() + ".");
    }

    @Override
    public FetchedResource fetch(String url) {
        throw new UnsupportedOperationException("Not implemented: SourceClient.fetch().");
    }
}
