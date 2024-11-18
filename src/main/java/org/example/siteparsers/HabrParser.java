package org.example.siteparsers;

import org.example.article.Article;
import org.example.article.ArticleTag;
import org.example.rssparser.RssParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HabrParser implements Parser {
    private final static String SITE_LINK = "https://habr.com/ru/rss/articles/";
    private final ArticleTag articleTag = ArticleTag.HABR;
    private static final Logger log = LoggerFactory.getLogger(HabrParser.class);

    public List<Article> parseArticles() {
        final List<Article> articles = new ArrayList<>();
        try {
            articles.addAll(RssParser.parse(new URL(SITE_LINK), articleTag));
        } catch (Exception e) {
            log.error("Error while parsing articles on page {}", SITE_LINK, e);
        }
        return articles;
    }
}
