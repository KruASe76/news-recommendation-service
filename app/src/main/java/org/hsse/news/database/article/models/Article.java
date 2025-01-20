package org.hsse.news.database.article.models;

import org.hsse.news.database.article.exceptions.ArticleInitializationException;
import org.hsse.news.database.topic.models.TopicId;
import org.hsse.news.database.website.models.WebsiteId;
import org.jdbi.v3.core.mapper.Nested;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.util.Objects;

public record Article(
        @Nested @Nullable ArticleId id,
        @NotNull String title,
        @NotNull String url,
        @NotNull Timestamp createdAt,
        @NotNull TopicId topicId,
        @NotNull WebsiteId websiteId
        ) {
    @JdbiConstructor
    public Article {}

    public Article(
            final @NotNull String title,
            final @NotNull String url,
            final @NotNull Timestamp createdAt,
            final @NotNull TopicId topicId,
            final @NotNull WebsiteId websiteId
    ) {
        this(null, title, url, createdAt, topicId, websiteId);
    }

    public Article initializeWithId(final @NotNull ArticleId newId) {
        if (id != null) {
            throw new ArticleInitializationException(newId);
        }

        return new Article(newId, title, url, createdAt, topicId, websiteId);
    }

    public Article withTitle(final @NotNull String newTitle) {
        return new Article(id, newTitle, url, createdAt, topicId, websiteId);
    }

    public Article withUrl(final @NotNull String newUrl) {
        return new Article(id, title, newUrl, createdAt, topicId, websiteId);
    }

    public Article withCreatedAt(final @NotNull Timestamp newCreatedAt) {
        return new Article(id, title, url, newCreatedAt, topicId, websiteId);
    }

    public Article withTopicId(final @NotNull TopicId newTopicId) {
        return new Article(id, title, url, createdAt, newTopicId, websiteId);
    }

    public Article withWebsiteId(final @NotNull WebsiteId newWebsiteId) {
        return new Article(id, title, url, createdAt, topicId, newWebsiteId);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Article article = (Article) o;
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
