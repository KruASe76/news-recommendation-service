package org.hsse.news.database.topic.models;

import org.hsse.news.database.user.exceptions.UserInitializationException;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record Topic(
        @Nullable TopicId id, @NotNull String description
) {
    @JdbiConstructor
    public Topic {}

    public Topic(final @NotNull String description){
        this(null, description);
    }

    public Topic initializeWithId(final @NotNull TopicId newId) {
        if (id != null) {
            throw new UserInitializationException("Topic is already initialized");
        }

        return new Topic(newId, description);
    }

    public Topic withDescription(final @NotNull String newDescription) {
        return new Topic(id, newDescription);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Topic topic)) {
            return false;
        }

        return id != null && id.equals(topic.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
