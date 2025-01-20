package org.hsse.news.api.schemas.request.topic;

import org.jetbrains.annotations.NotNull;

public record CreateCustomTopicRequest(
    @NotNull Long topicId
) {
}
