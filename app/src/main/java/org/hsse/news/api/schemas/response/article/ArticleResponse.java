package org.hsse.news.api.schemas.response.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public record ArticleResponse(
        @JsonProperty("title") @NotNull String title,
        @JsonProperty("url") @NotNull String url,
        @JsonProperty("created_at") @NotNull String createdAt,
        @JsonProperty("topic_id") @NotNull String topicId,
        @JsonProperty("website_id") @NotNull String websiteId
) {
}
