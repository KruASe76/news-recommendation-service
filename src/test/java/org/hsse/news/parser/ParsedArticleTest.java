package org.hsse.news.parser;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ParsedArticleTest {
    String testString = "Website: StackOverflow\n" +
            "name: Meet the guy\n" +
            "author: Eira May\n" +
            "topics: [gaming]\n" +
            "date: 15 ноября 2024\n" +
            "description: Chris Fowler, Director of Engine for Call of Duty.  https://stackoverflow.blog/2024/11/15/meet-the-guy-responsible-for-building-the-call-of-duty-game-engine/";
    @Test
    void testtoString(){
        ParsedArticle article = new ParsedArticle("Meet the guy", "Chris Fowler, Director of Engine for Call of Duty.", "15 ноября 2024", "https://stackoverflow.blog/2024/11/15/meet-the-guy-responsible-for-building-the-call-of-duty-game-engine/", Set.of("gaming"),"Eira May", "StackOverflow");
        assertEquals(testString,article.toString());
    }

}