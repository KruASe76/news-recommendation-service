package org.hsse.news.database.article.models;

import org.hsse.news.util.AbstractId;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class ArticleId extends AbstractId<UUID> {
    public ArticleId(final @NotNull UUID value) {
        super(value);
    }
}
