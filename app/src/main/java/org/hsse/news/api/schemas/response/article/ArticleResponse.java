package org.hsse.news.api.schemas.response.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ArticleResponse(
        @JsonProperty("title") @NotNull String title,
        @JsonProperty("url") @NotNull String url,
        @JsonProperty("created_at") @NotNull String createdAt,
        @JsonProperty("topics") @NotNull List<String> topics,
        @JsonProperty("website") @NotNull String website
) {
    public ArticleResponse(
            @JsonProperty("title") final @NotNull String title,
            @JsonProperty("url") final @NotNull String url,
            @JsonProperty("created_at") final @NotNull String createdAt,
            @JsonProperty("topics") final @NotNull List<String> topics,
            @JsonProperty("website") final @NotNull String website
    ) {
        this.title = title;
        this.url = url;
        this.createdAt = createdAt;
        this.topics = topics;
        this.website = website;
    }

    public void addTopic(final String topic) {
        topics.add(topic);
    }
}
