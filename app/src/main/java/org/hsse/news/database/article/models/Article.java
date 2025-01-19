package org.hsse.news.database.article.models;

import org.hsse.news.database.article.exceptions.ArticleInitializationException;
import org.jdbi.v3.core.mapper.Nested;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

public record Article(
        @Nested @Nullable ArticleId id,
        @NotNull String title, @NotNull String url,
        @NotNull String createdAt, @NotNull String topic
) {
    @JdbiConstructor
    public Article {}

    public Article(
            final @NotNull String title, final @NotNull String url,
            final @NotNull String createdAt, final @NotNull String topic
    ) {
        this(null, title, url, createdAt, topic);
    }

    public Article initializeWithId(final @NotNull ArticleId newId) {
        if (id != null) {
            throw new ArticleInitializationException(newId);
        }

        return new Article(newId, title, url, createdAt, topic);
    }

    public Article withTitle(final @NotNull String newTitle) {
        return new Article(id, newTitle, url, createdAt, topic);
    }

    public Article withUrl(final @NotNull String newUrl) {
        return new Article(id, title, newUrl, createdAt, topic);
    }

    public Article withCreatedAt(final @NotNull String newCreatedAt) {
        return new Article(id, title, url, newCreatedAt, topic);
    }

    public Article withTopic(final @NotNull String newTopic) {
        return new Article(id, title, url, createdAt, newTopic);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
