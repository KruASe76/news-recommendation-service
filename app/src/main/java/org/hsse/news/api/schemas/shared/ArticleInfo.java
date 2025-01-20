package org.hsse.news.api.schemas.shared;

import org.hsse.news.database.topic.models.TopicId;
import org.hsse.news.database.website.models.WebsiteId;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

public record ArticleInfo(
        @NotNull String title,
        @NotNull String url,
        @NotNull Timestamp createdAt,
        @NotNull TopicId topicId,
        @NotNull WebsiteId websiteId
) {
    public ArticleInfo(
            final @NotNull String title,
            final @NotNull String url,
            final @NotNull Timestamp createdAt,
            final @NotNull TopicId topicId,
            final @NotNull WebsiteId websiteId
            ) {
        this.title = title;
        this.url = url;
        this.createdAt = createdAt;
        this.topicId = topicId;
        this.websiteId = websiteId;
    }
}
