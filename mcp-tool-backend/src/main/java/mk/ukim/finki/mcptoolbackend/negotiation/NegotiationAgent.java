package mk.ukim.finki.mcptoolbackend.negotiation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.mcptoolbackend.analysis.LanguageDetector;
import mk.ukim.finki.mcptoolbackend.model.domain.DonationBatch;
import mk.ukim.finki.mcptoolbackend.model.domain.Resource;
import mk.ukim.finki.mcptoolbackend.model.domain.SearchRun;
import mk.ukim.finki.mcptoolbackend.model.enums.SearchStatus;
import mk.ukim.finki.mcptoolbackend.repository.ResourceRepository;
import mk.ukim.finki.mcptoolbackend.repository.SearchRunRepository;
import mk.ukim.finki.mcptoolbackend.service.domain.DonationService;
import mk.ukim.finki.mcptoolbackend.source.FetchedResource;
import mk.ukim.finki.mcptoolbackend.source.SourceClient;
import mk.ukim.finki.mcptoolbackend.source.SourceProperties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NegotiationAgent {


    public record Need(String category, int target, int received) {

    }

    public record Message(String from, String text, List<Need> needs) {

    }

    private final ResourceRepository resourceRepository;
    private final SearchRunRepository searchRunRepository;
    private final SourceClient sourceClient;
    private final LanguageDetector languageDetector;
    private final DonationService donationService;
    private final SourceProperties sourceProperties;

    public synchronized List<Message> negotiate() {
        Map<String, List<String>> links = new LinkedHashMap<>();
        List<Need> needs = new ArrayList<>();
        for (Element theme : getPage(sourceProperties.baseUrl(), false).select(".sub-menu a[href*=/tag/]")) {
            String name = theme.text();
            if (links.containsKey(name)) continue;
            String feedUrl = theme.attr("abs:href").replaceAll("/$", "") + "/feed/";
            List<String> urls = getPage(feedUrl, true).select("item > link").eachText();
            int donated = (int) resourceRepository.findAllBySourceUrlIn(urls).stream()
                    .filter(r -> r.getDonationBatch() != null).count();
            links.put(name, urls);
            needs.add(new Need(name, urls.size(), donated));
        }

        double average = needs.stream()
                .mapToDouble(n -> 100.0 * n.received() / Math.max(1, n.target()))
                .average().orElse(0);
        List<Message> chat = new ArrayList<>();
        chat.add(new Message("corpus", "Просечна покриеност: " + Math.round(average) + "%.", needs));

        SearchRun run = new SearchRun("агент");
        run.setStatus(SearchStatus.COMPLETED);
        run = searchRunRepository.save(run);

        List<Long> selected = new ArrayList<>();
        List<Need> after = new ArrayList<>();
        for (Need n : needs) {
            int missing = Math.max(1, (int) Math.ceil(average / 100 * n.target())) - n.received();
            List<Long> picked = pick(links.get(n.category()), missing, selected, run);
            selected.addAll(picked);
            if (missing > 0) {
                chat.add(new Message("agent", n.category() + ": " + n.received() + " од " + n.target()
                        + ", под просекот. Додадов " + picked.size() + ".", null));
            }
            after.add(new Need(n.category(), n.target(), n.received() + picked.size()));
        }

        if (selected.isEmpty()) {
            chat.add(new Message("agent", "Сите теми се на или над просекот.", after));
        } else {
            DonationBatch batch = donationService.create(selected);
            chat.add(new Message("agent", "Направив DRAFT пакет #" + batch.getId() + " со " + selected.size()
                    + " статии. Одобри го на страницата Донации.", after));
        }
        return chat;
    }

    private List<Long> pick(List<String> links, int count, List<Long> selected, SearchRun run) {
        List<Long> picked = new ArrayList<>();
        for (String link : links) {
            if (picked.size() >= count) break;
            Resource r = resourceRepository.findAllBySourceUrlIn(List.of(link)).stream().findFirst().orElse(null);
            if (r == null) r = fetch(link, run);
            if (r == null || r.getDonationBatch() != null || selected.contains(r.getId())) continue;
            if (r.getMacedonianConfidence() == null || r.getMacedonianConfidence() >= 0.8) picked.add(r.getId());
        }
        return picked;
    }

    private Resource fetch(String link, SearchRun run) {
        try {
            FetchedResource f = sourceClient.fetch(link);
            if (f == null) return null;
            Resource r = new Resource(run, f.externalId(), f.title(), f.content(), f.sourceUrl());
            r.setFetchedAt(LocalDateTime.now());
            r.setWordCount(f.content().split("\\s+").length);
            r.setMacedonianConfidence(languageDetector.macedonianConfidence(f.content()));
            r.setLanguage(r.getMacedonianConfidence() >= 0.5 ? "mkd" : "und");
            return resourceRepository.save(r);
        } catch (RuntimeException e) {
            return null;
        }
    }

    private Document getPage(String url, boolean xml) {
        try {
            var connection = Jsoup.connect(url).userAgent("Mozilla/5.0");
            return xml ? connection.parser(Parser.xmlParser()).get() : connection.get();
        } catch (Exception e) {
            throw new RuntimeException(sourceProperties.name() + " не е достапна: " + url, e);
        }
    }
}