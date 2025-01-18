package org.hsse.news.api.schemas.request.user;

import org.hsse.news.util.PasswordUtil;
import org.jetbrains.annotations.NotNull;

public record UserRegisterRequest(
        @NotNull String email, @NotNull String password, @NotNull String username
) {
    public UserRegisterRequest(
        final @NotNull String email, final @NotNull String password, final @NotNull String username
    ) {
        this.email = email;
        this.password = PasswordUtil.hashPassword(password);
        this.username = username;
    }
}
