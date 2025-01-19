package org.hsse.news.api.schemas.request.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hsse.news.database.topic.models.TopicId;
import org.hsse.news.database.website.models.WebsiteId;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public record ArticleCreateRequest(
        @JsonProperty("title") @NotNull String newTitle,
        @JsonProperty("url") @NotNull String newUrl,
        @JsonProperty("topic_id") @NotNull TopicId newTopicId,
        @JsonProperty("website_id") @NotNull WebsiteId newWebsiteId
) {
    public ArticleCreateRequest(
            @JsonProperty("title") @NotNull String newTitle,
            @JsonProperty("url") @NotNull String newUrl,
            @JsonProperty("topic_id") @NotNull TopicId newTopicId,
            @JsonProperty("website_id") @NotNull WebsiteId newWebsiteId
    ) {
        Date d = new Date();
        this.newTitle = newTitle;
        this.newUrl = newUrl;
        this.newTopicId = newTopicId;
        this.newWebsiteId = newWebsiteId;
    }
}
