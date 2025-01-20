package org.hsse.news.database.website.models;

import org.hsse.news.database.user.exceptions.UserInitializationException;
import org.hsse.news.database.user.models.UserId;
import org.jdbi.v3.core.mapper.Nested;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record Website(
        @Nested @Nullable WebsiteId id,
        @NotNull String url, @NotNull String description, @NotNull UserId creatorId
) {
    @JdbiConstructor
    public Website {}

    public Website(
            final @NotNull String url, final @NotNull String description,
            final @NotNull UserId creatorId
    ) {
        this(null, url, description, creatorId);
    }

    public Website initializeWithId(final @NotNull WebsiteId newId) {
        if (id != null) {
            throw new UserInitializationException("Website is already initialized");
        }

        return new Website(newId, url, description, creatorId);
    }

    public Website withUrl(final @NotNull String newUrl) {
        return new Website(id, newUrl, description, creatorId);
    }

    public Website withDescription(final @NotNull String newDescription) {
        return new Website(id, url, newDescription, creatorId);
    }

    public Website withCreators(final @NotNull UserId newCreator) {
        return new Website(id, url, description, newCreator);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Website website)) {
            return false;
        }

        return id != null && id.equals(website.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
