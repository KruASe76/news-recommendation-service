package org.hsse.news.parser;

import com.rometools.rome.io.FeedException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RssParserTest {
  @Test
  void shouldParseFile() throws IOException, FeedException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    File testFile = new File(classLoader.getResource("RssTestFile.xml").getFile());
    String testString =
        "Website: StackOverflow\n"
            + "name: Meet the guy responsible for building the Call of Duty game engine\n"
            + "author: Eira May\n"
            + "topics: [gaming, se-stackoverflow, game-developer, se-tech, podcast, game-development]\n"
            + "date: 15 ноября 2024\n"
            + "description: Chris Fowler, Director of Engine for Call of Duty, tells Ben and Ryan about his path from marine biology to game development, the ins and outs of game engines, and the technical feats involved in creating massively popular games like Call of Duty. Chris also explains why community feedback is so critical in game development and offers his advice for aspiring game developers.  https://stackoverflow.blog/2024/11/15/meet-the-guy-responsible-for-building-the-call-of-duty-game-engine/";
    List<ParsedArticle> articles = RssParser.parse(testFile, "StackOverflow");
    assertEquals(articles.get(0).toString(), testString);
  }

  @Test
  void shouldParseSite() throws IOException, FeedException {
    List<ParsedArticle> articles =
        RssParser.parse(new URL("https://stackoverflow.blog/feed/"), "StackOverflow");
    assertEquals(articles.get(0).articleTag(), "StackOverflow");
  }

  @Test
  void shouldThrowFileNotFoundException() {
    assertThrows(FileNotFoundException.class, () -> RssParser.parse(new File("1"), "1"));
  }

  @Test
  void shouldThrowIOException() {
    assertThrows(IOException.class, () -> RssParser.parse(new URL("1"), "1"));
  }
}
