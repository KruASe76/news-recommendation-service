package org.hsse.news.api.schemas.request.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hsse.news.database.article.models.ArticleId;
import org.jetbrains.annotations.NotNull;

public record ArticleDeleteRequest(
        @JsonProperty("article_id") @NotNull ArticleId articleId
) {
    public ArticleDeleteRequest(
            @JsonProperty("article_id") final @NotNull ArticleId articleId
    ) {
        this.articleId = articleId;
    }
}
