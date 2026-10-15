package mk.ukim.finki.mcptoolbackend.source;

import java.time.LocalDateTime;

/**
 * A raw resource fetched from the assigned website, before it is persisted as a
 * {@code Resource} entity. Produced by {@link SourceClient}.
 *
 * @param externalId  the resource's id on the source website (nullable)
 * @param title       a human-readable title
 * @param content     the Macedonian text content
 * @param sourceUrl   the absolute URL the content was found at (provenance)
 * @param publishedAt when the resource was published on the site, if known
 */
public record FetchedResource(
    String externalId,
    String title,
    String content,
    String sourceUrl,
    LocalDateTime publishedAt
) {
}
