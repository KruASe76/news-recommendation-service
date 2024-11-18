package org.example.siteparsers;

import org.example.article.Article;

import java.util.List;

public interface Parser {
    List<Article> parseArticles();
}

