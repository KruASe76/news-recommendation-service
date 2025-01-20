package org.hsse.news.database.article.exceptions;

import org.hsse.news.database.article.models.ArticleId;

public class ArticleInitializationException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Article wih id=%s already initialized";

    public ArticleInitializationException(final ArticleId articleId) {
        super(String.format(DEFAULT_MESSAGE, articleId));
    }
}
