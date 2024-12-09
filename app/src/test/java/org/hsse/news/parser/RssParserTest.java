package org.hsse.news.parser;

import com.rometools.rome.io.FeedException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RssParserTest {
  private static final String TEST_WEBSITE_URL = "StackOverflow";

  @Test
  void shouldParseFile() throws IOException, FeedException {
    final File testFile = new File(this.getClass().getResource("/RssTestFile.xml").getFile());
    final ParsedArticle testArticle =
        new ParsedArticle(
            "Meet the guy responsible for building the Call of Duty game engine",
            "Chris Fowler, Director of Engine for Call of Duty, tells Ben and Ryan about his path from marine biology to game development, the ins and outs of game engines, and the technical feats involved in creating massively popular games like Call of Duty. Chris also explains why community feedback is so critical in game development and offers his advice for aspiring game developers.",
            "15 November 2024",
            "https://stackoverflow.blog/2024/11/15/meet-the-guy-responsible-for-building-the-call-of-duty-game-engine/",
            Set.of(
                "gaming",
                "se-stackoverflow",
                "game-developer",
                "se-tech",
                "podcast",
                "game-development"),
            "Eira May",
            TEST_WEBSITE_URL);
    final List<ParsedArticle> articles = RssParser.parse(testFile, TEST_WEBSITE_URL);
    final ParsedArticle article = getFirstArticle(articles);
    assertEquals(article, testArticle, "Articles must be equal");
  }

  private ParsedArticle getFirstArticle(final List<ParsedArticle> articles) {
    return articles.get(0);
  }

  private String getWebsiteUrl(final ParsedArticle article) {
    return article.websiteUrl();
  }

  @Test
  void shouldParseSite() throws IOException, FeedException {
    final List<ParsedArticle> articles =
        RssParser.parse(new URL("https://stackoverflow.blog/feed/"), TEST_WEBSITE_URL);
    final ParsedArticle article = getFirstArticle(articles);
    assertEquals(getWebsiteUrl(article), TEST_WEBSITE_URL, "ArticleTags must be equal");
  }

  @Test
  void shouldThrowFileNotFoundException() {
    assertThrows(
        FileNotFoundException.class,
        () -> RssParser.parse(new File("1"), "1"),
        "check for file incorrectness");
  }

  @Test
  void shouldThrowIOException() {
    assertThrows(
        IOException.class,
        () -> RssParser.parse(new URL("1"), "1"),
        "\n" + "checking for incorrect URL");
  }
}
