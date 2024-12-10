package org.hsse.news.parser;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParsedArticleTest {
  private final ParsedArticle testArticle =
          new ParsedArticle(
                  "Meet the guy",
                  "Chris Fowler, Director of Engine for Call of Duty.",
                  toInstant("2024-11-15T08:40:00Z")
                  ,
                  "https://stackoverflow.blog/2024/11/15/meet-the-guy-responsible-for-building-the-call-of-duty-game-engine/",
                  Set.of("gaming"),
                  "Eira May",
                  "https://stackoverflow.blog");

  private Instant toInstant(final String str) {
    return Instant.parse(str);
  }

  @Test
  void shouldRecordArticle() {
    assertEquals(
            testArticle,
            new ParsedArticle(
                    "Meet the guy",
                    "Chris Fowler, Director of Engine for Call of Duty.",
                    toInstant("2024-11-15T08:40:00Z"),
                    "https://stackoverflow.blog/2024/11/15/meet-the-guy-responsible-for-building-the-call-of-duty-game-engine/",
                    Set.of("gaming"),
                    "Eira May",
                    "https://stackoverflow.blog"),
            "Articles must be equal");
  }
}
