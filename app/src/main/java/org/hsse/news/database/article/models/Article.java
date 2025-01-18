package org.hsse.news.database.article.models;

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

public record Article() {
    @JdbiConstructor
    public Article {}
}
