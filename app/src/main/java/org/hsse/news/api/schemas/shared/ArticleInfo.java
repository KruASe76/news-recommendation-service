package org.hsse.news.api.schemas.shared;

import org.jetbrains.annotations.NotNull;

public record ArticleInfo(
        @NotNull String title, @NotNull String url,
        @NotNull String createdAt, @NotNull String topic
) {
    public ArticleInfo(
            final @NotNull String title, final @NotNull String url,
            final @NotNull String createdAt, final @NotNull String topic
    ) {
        this.title = title;
        this.url = url;
        this.createdAt = createdAt;
        this.topic = topic;
    }
}
