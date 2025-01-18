package org.hsse.news.database.website.models;

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

public record Website() {
    @JdbiConstructor
    public Website {}
}
