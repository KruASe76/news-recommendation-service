package org.hsse.news.parser;


import com.rometools.rome.io.FeedException;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RssParserTest {
    File testFile = new File("src/main/resources/RssTestFile.xml");

    String testString = "Website: StackOverflow\n" +
            "name: Meet the guy responsible for building the Call of Duty game engine\n" +
            "author: Eira May\n" +
            "topics: [gaming, se-stackoverflow, game-developer, se-tech, podcast, game-development]\n" +
            "date: 15 ноября 2024\n" +
            "description: Chris Fowler, Director of Engine for Call of Duty, tells Ben and Ryan about his path from marine biology to game development, the ins and outs of game engines, and the technical feats involved in creating massively popular games like Call of Duty. Chris also explains why community feedback is so critical in game development and offers his advice for aspiring game developers.  https://stackoverflow.blog/2024/11/15/meet-the-guy-responsible-for-building-the-call-of-duty-game-engine/";
    @Test
    void ParseTest() throws IOException, FeedException {
        List<ParsedArticle> articles = RssParser.parse(testFile,"StackOverflow");
        assertEquals(articles.get(0).toString(),testString);
    }
}