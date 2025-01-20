package org.hsse.news.api.schemas.response.error.topic;

import org.jetbrains.annotations.NotNull;

public record TooManyTopicsForUserResponse(@NotNull String message, int limit) {
}
