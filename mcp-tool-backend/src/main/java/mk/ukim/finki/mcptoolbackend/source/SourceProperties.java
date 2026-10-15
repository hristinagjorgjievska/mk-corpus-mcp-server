package mk.ukim.finki.mcptoolbackend.source;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration of the specific Macedonian-resource website assigned to you,
 * bound from the {@code source.*} properties.
 *
 * @param name    a short human-readable name of the website
 * @param baseUrl the website's base URL
 * @param apiKey  an optional API key/token, if your site exposes an authenticated API
 */
@ConfigurationProperties(prefix = "source")
public record SourceProperties(
    String name,
    String baseUrl,
    String apiKey
) {
}
