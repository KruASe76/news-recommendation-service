package org.hsse.news.database.user.models;

import org.jetbrains.annotations.NotNull;

public record AuthenticationCredentials(@NotNull String email, @NotNull String password) {}
