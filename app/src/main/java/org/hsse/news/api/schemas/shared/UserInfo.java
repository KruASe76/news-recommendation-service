package org.hsse.news.api.schemas.shared;

import org.jetbrains.annotations.NotNull;

public record UserInfo(@NotNull String email, @NotNull String username) {}
