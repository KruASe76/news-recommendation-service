package org.hsse.news.parser;

import java.util.Set;

public record ParsedArticle(
    String name,
    String description,
    String date,
    String link,
    Set<String> topics,
    String author,
    String websiteUrl) {}
