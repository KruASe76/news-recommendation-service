package org.hsse.news.api.util;

import org.hsse.news.api.schemas.response.article.ArticleResponse;
import org.hsse.news.database.article.models.Article;

public final class ArticleCastUtil {
    public static ArticleResponse fromArticle(final Article article) {
        return new ArticleResponse(
                article.title(),
                article.url(),
                String.valueOf(article.createdAt()),
                String.valueOf(article.topicId().value()),
                String.valueOf(article.websiteId().value())
        );
    }

    private ArticleCastUtil() {

    }
}
