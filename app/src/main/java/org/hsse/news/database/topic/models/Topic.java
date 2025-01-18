package org.hsse.news.database.topic.models;

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

public record Topic() {
    @JdbiConstructor
    public Topic {}
}
