package org.hsse.news.parser;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParsedArticleTest {
  private final ParsedArticle testArticle =
      new ParsedArticle(
          "Meet the guy",
          "Chris Fowler, Director of Engine for Call of Duty.",
          "15 ноября 2024",
          "https://stackoverflow.blog/2024/11/15/meet-the-guy-responsible-for-building-the-call-of-duty-game-engine/",
          Set.of("gaming"),
          "Eira May",
          "https://stackoverflow.blog");

  @Test
  void shouldRecordArticle() {
    assertEquals(
        testArticle,
        new ParsedArticle(
            "Meet the guy",
            "Chris Fowler, Director of Engine for Call of Duty.",
            "15 ноября 2024",
            "https://stackoverflow.blog/2024/11/15/meet-the-guy-responsible-for-building-the-call-of-duty-game-engine/",
            Set.of("gaming"),
            "Eira May",
            "https://stackoverflow.blog"),
        "Articles must be equal");
  }
}
