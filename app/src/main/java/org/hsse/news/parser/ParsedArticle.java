package org.hsse.news.parser;

import java.time.Instant;
import java.util.Set;

public record ParsedArticle(
        String name,
        String description,
        Instant date,
        String link,
        Set<String> topics,
        String author,
        String websiteUrl) {
}
