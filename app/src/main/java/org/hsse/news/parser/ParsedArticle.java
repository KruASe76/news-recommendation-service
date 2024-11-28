package org.hsse.news.parser;

import java.util.Objects;
import java.util.Set;

public record ParsedArticle(
    String name,
    String description,
    String date,
    String link,
    Set<String> topics,
    String author,
    String websiteUrl) {

  @Override
  public String toString() {
    return "Website: "
        + websiteUrl
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

  @Override
  public int hashCode() {
    return Objects.hash(name, description, date, link, topics, author, websiteUrl);
  }

  @Override
  public boolean equals(final Object object) {
    boolean flag = false;
    if (object instanceof ParsedArticle article) {
      flag =
          name.equals(article.name)
              && description.equals(article.description)
              && date.equals(article.date)
              && link.equals(article.link)
              && topics.equals(article.topics)
              && author.equals(article.author)
              && websiteUrl.equals(article.websiteUrl);
    }
    return flag;
  }
}
