package org.hsse.news.database.article.models;

import org.hsse.news.util.AbstractId;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class ArticleId extends AbstractId<UUID> {
    @JdbiConstructor
    public ArticleId(final @ColumnName("article_id") @NotNull UUID value) {
        super(value);
    }
}
