package mk.ukim.finki.mcptoolbackend.source;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SourceClientImpl implements SourceClient{

    private static final String USER_AGENT = "Mozilla/5.0 (compatible; MK-Corpus-MCP-Tool/1.0)";
    private static final int TIMEOUT = 10_000; //ms
    private static final long DELAY = 500; //ms

    private final SourceProperties sourceProperties;

    public SourceClientImpl(SourceProperties sourceProperties) {
        this.sourceProperties = sourceProperties;
    }

    @Override
    public List<FetchedResource> search(String query, int limit) {
        try {
            String url = sourceProperties.baseUrl() + "/?s="
                    + URLEncoder.encode(query, StandardCharsets.UTF_8)
                    + "&feed=rss2";

            Document rss = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT)
                    .parser(Parser.xmlParser())
                    .get();

            List<FetchedResource> results = new ArrayList<>();
            for (Element element : rss.select("item")){
                if (results.size() >= limit)
                    break;

                String link = element.selectFirst("link").text().trim();
                try{
                    FetchedResource resource = fetch(link);

                    if (resource != null)
                        results.add(resource);
                }
                catch (RuntimeException exception){

                }

                Thread.sleep(DELAY);

            }

            return results;
        }
        catch (Exception exception){
            throw new RuntimeException("Пребарувањето на " + sourceProperties.baseUrl() + " е неуспешно", exception);
        }
    }

    @Override
    public FetchedResource fetch(String url) {
        try {
            Document document = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT)
                    .get();

            Element article = document.selectFirst("article[id^=post-]");
            if (article == null)
                return null;

            String externalId = article.id().replace("post-", "");
            String title = document.selectFirst("h1").text();

            Element body = document.selectFirst("div.entry-content");
            body.select("script, style, figure, iframe, .sharedaddy, .jp-relatedposts").remove();

            String content = body.text();

            Element published = document.selectFirst("meta[property=article:published_time]");
            LocalDateTime publishedAt = published != null ? OffsetDateTime.parse(published.attr("content")).toLocalDateTime() : null;

            return new FetchedResource(externalId, title, content, url, publishedAt);
        }
        catch (Exception exception){
            throw new RuntimeException("Преземањето на " + url + " неуспешно", exception);
        }
    }
}


