package mk.ukim.finki.mcptoolbackend.analysis;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.mcptoolbackend.model.domain.Resource;
import mk.ukim.finki.mcptoolbackend.repository.ResourceRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ResourceAnalyzerImpl implements ResourceAnalyzer{

    private final ResourceRepository resourceRepository;

    @Override
    public AnalysisOutcome analyze(String title, String content) {
        if (content == null || content.isBlank())
            return new AnalysisOutcome("", List.of(), 0, 0);

        String[] sentences = content.trim().split("(?<=[.!?])\\s+");
        List<String> words = tokenize(content);

        String summary = String.join(" ", Arrays.copyOf(sentences, Math.min(2, sentences.length)));

        List<Resource> resourceList = resourceRepository.findAll();
        Map<String, Integer> wordFreqInEveryResource = new HashMap<>();

        for (Resource r : resourceList){
            for (String word : new HashSet<>(tokenize(r.getContent()))){
                wordFreqInEveryResource.merge(word, 1, Integer::sum);
            }
        }

        Map<String, Double> scores = new HashMap<>();
        for (String word : words){
            if (word.length() < 3)
                continue;

            double idf = Math.log((resourceList.size() + 1.0) / (1 + wordFreqInEveryResource.getOrDefault(word, 0)));
            scores.merge(word, idf, Double::sum);
        }

        List<String> keywords = scores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();

        return new AnalysisOutcome(summary, keywords, sentences.length, words.size());
    }

    private List<String> tokenize(String text){
        if (text == null)
            return List.of();

        return Arrays.stream(text.toLowerCase().split("[^\\p{L}]+"))
                .filter(word -> !word.isEmpty())
                .toList();
    }
}
