package org.hsse.news.parser;

import java.util.Set;

public record ParsedArticle(
    String name,
    String description,
    String date,
    String link,
    Set<String> topics,
    String author,
    String articleTag) {

  @Override
  public String toString() {
    return "Website: "
        + articleTag
        + "\n"
        + "name: "
        + name
        + "\n"
        + "author: "
        + author
        + "\n"
        + "topics: "
        + topics
        + "\n"
        + "date: "
        + date
        + "\n"
        + "description: "
        + description
        + "  "
        + link;
  }
}
