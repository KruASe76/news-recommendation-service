package org.hsse.news.api.schemas.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hsse.news.util.PasswordUtil;
import org.jetbrains.annotations.NotNull;

public record UserPasswordChangeRequest(
        @JsonProperty("current_password") @NotNull String currentPassword,
        @JsonProperty("new_password") @NotNull String newPassword
) {
    public UserPasswordChangeRequest(
            @JsonProperty("current_password") final @NotNull String currentPassword,
            @JsonProperty("new_password") final @NotNull String newPassword
    ) {
        this.currentPassword = PasswordUtil.hashPassword(currentPassword);
        this.newPassword = PasswordUtil.hashPassword(newPassword);
    }
}
