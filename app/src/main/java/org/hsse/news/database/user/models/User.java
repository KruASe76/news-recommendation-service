package org.hsse.news.database.user.models;

import org.hsse.news.database.user.exceptions.UserInitializationException;
import org.jdbi.v3.core.mapper.Nested;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record User(
        @Nested @Nullable UserId id,
        @NotNull String email, @NotNull String password, @NotNull String username
) {
    @JdbiConstructor
    public User {}

    public User(
            final @NotNull String email, final @NotNull String password,
            final @NotNull String username
    ) {
        this(null, email, password, username);
    }

    public User initializeWithId(final @NotNull UserId newId) {
        if (id != null) {
            throw new UserInitializationException("User is already initialized");
        }

        return new User(newId, email, password, username);
    }

    public User withEmail(final @NotNull String newEmail) {
        return new User(id, newEmail, password, username);
    }

    public User withPassword(final @NotNull String newPassword) {
        return new User(id, email, newPassword, username);
    }

    public User withUsername(final @NotNull String newUsername) {
        return new User(id, email, password, newUsername);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof User user)) {
            return false;
        }

        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
