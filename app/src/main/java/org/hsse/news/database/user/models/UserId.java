package org.hsse.news.database.user.models;

import org.hsse.news.util.AbstractId;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class UserId extends AbstractId<UUID> {
    @JdbiConstructor
    public UserId(final @ColumnName("user_id") @NotNull UUID value) {
        super(value);
    }
}
