package org.hsse.news.api.schemas.request.topic;

import org.jetbrains.annotations.NotNull;

public record DeleteCustomTopicRequest(
    @NotNull Long topicId
) {
}
