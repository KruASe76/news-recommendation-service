package org.hsse.news.api.schemas.response.error;

import org.jetbrains.annotations.NotNull;

public record WebsiteAlreadyExistsResponse(@NotNull String detail) {}
