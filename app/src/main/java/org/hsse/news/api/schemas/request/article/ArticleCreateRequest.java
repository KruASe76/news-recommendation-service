package org.hsse.news.api.schemas.request.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public record ArticleCreateRequest(
        @JsonProperty("title") @NotNull String newTitle,
        @JsonProperty("url") @NotNull String newUrl,
        @JsonProperty("created_at") @NotNull String newCreatedAt,
        @JsonProperty("topic") @NotNull String newTopic
) {
    public ArticleCreateRequest(
            @JsonProperty("title") @NotNull String newTitle,
            @JsonProperty("url") @NotNull String newUrl,
            @JsonProperty("created_at") @NotNull String newCreatedAt,
            @JsonProperty("topic") @NotNull String newTopic
    ) {
        this.newTitle = newTitle;
        this.newUrl = newUrl;
        this.newCreatedAt = newCreatedAt;
        this.newTopic = newTopic;
    }
}
