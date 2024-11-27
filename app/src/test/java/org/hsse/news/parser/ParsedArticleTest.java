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
          "StackOverflow");
  private final int testHash =
      new ParsedArticle("1", "1", "1", "1", Set.of("1"), "1", "1").hashCode();

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
            "StackOverflow"),
        "Articles must be equal");
  }

  @Test
  void shouldToStringArticle() {
    final String testString =
        "Website: StackOverflow\n"
            + "name: Meet the guy\n"
            + "author: Eira May\n"
            + "topics: [gaming]\n"
            + "date: 15 ноября 2024\n"
            + "description: Chris Fowler, Director of Engine for Call of Duty.  https://stackoverflow.blog/2024/11/15/meet-the-guy-responsible-for-building-the-call-of-duty-game-engine/";
    assertEquals(testString, testArticle.toString(), "String of articles must be equal");
  }

  @Test
  void shouldReturnEqualHash() {
    final ParsedArticle article = new ParsedArticle("1", "1", "1", "1", Set.of("1"), "1", "1");
    assertEquals(article.hashCode(), testHash, "Hachcodes must be equal");
  }
}
