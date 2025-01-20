package org.hsse.news.api.schemas.response.error.topic;

import org.jetbrains.annotations.NotNull;

public record TopicNotFoundResponse(@NotNull String message) {
}
