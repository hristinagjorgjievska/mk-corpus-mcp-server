package mk.ukim.finki.mcptoolbackend.analysis;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import mk.ukim.finki.mcptoolbackend.model.domain.Resource;
import mk.ukim.finki.mcptoolbackend.repository.ResourceRepository;
import org.junit.jupiter.api.Test;

class ResourceAnalyzerImplTest {

    private Resource resourceWith(String content) {
        return new Resource(null, "id", "title", content, "url");
    }

    @Test
    void commonWordsAreNotKeywords() {
        ResourceRepository repo = mock(ResourceRepository.class);
        when(repo.findAll()).thenReturn(List.of(
                resourceWith("Владата на седница донесе одлука за буџетот."),
                resourceWith("Фудбалерите на тренинг се подготвуваат за натпревар."),
                resourceWith("Изборите на недела поминаа мирно за граѓаните.")
        ));

        ResourceAnalyzer analyzer = new ResourceAnalyzerImpl(repo);
        AnalysisOutcome outcome = analyzer.analyze("Избори",
                "Изборите поминаа мирно. На изборите гласаа многу граѓани. Изборите завршија.");

        assertEquals(3, outcome.sentenceCount());
        assertEquals("Изборите поминаа мирно. На изборите гласаа многу граѓани.", outcome.summary());
        assertEquals("изборите", outcome.keywords().get(0));
        assertFalse(outcome.keywords().contains("на"));
    }

    @Test
    void emptyContentReturnsEmptyOutcome() {
        ResourceAnalyzer analyzer = new ResourceAnalyzerImpl(mock(ResourceRepository.class));

        AnalysisOutcome outcome = analyzer.analyze(null, "   ");

        assertEquals(0, outcome.sentenceCount());
        assertEquals(0, outcome.wordCount());
        assertTrue(outcome.keywords().isEmpty());
    }
}