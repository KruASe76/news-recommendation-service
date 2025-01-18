package org.hsse.news.database.website.models;

import org.hsse.news.util.AbstractId;
import org.jetbrains.annotations.NotNull;

public final class WebsiteId extends AbstractId<Long> {
    public WebsiteId(final @NotNull Long value) {
        super(value);
    }
}
