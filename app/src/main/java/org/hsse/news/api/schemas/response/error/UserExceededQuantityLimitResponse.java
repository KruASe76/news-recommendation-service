package org.hsse.news.api.schemas.response.error;

import org.jetbrains.annotations.NotNull;

public record UserExceededQuantityLimitResponse(@NotNull String detail, @NotNull Integer limit) {}
