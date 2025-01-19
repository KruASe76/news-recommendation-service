package org.hsse.news.database.article.repositories;

import org.hsse.news.database.article.exceptions.ArticleNotFoundException;
import org.hsse.news.database.article.models.Article;
import org.hsse.news.database.article.models.ArticleId;
import org.hsse.news.util.JdbiProvider;
import org.jdbi.v3.core.Jdbi;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public final class JdbiArticleRepository implements ArticleRepository {
    private final Jdbi jdbi;

    public JdbiArticleRepository(final @NotNull Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public JdbiArticleRepository() {
        this(JdbiProvider.get());
    }

    @Override
    public Optional<Article> findById(final @NotNull ArticleId articleId) {
        return jdbi.inTransaction( handle ->
                handle.createQuery("SELECT * FROM articles WHERE article_id = :article_id")
                        .bind("article_id", articleId.value()) // NOPMD
                        .mapTo(Article.class)
                        .findFirst()
        );
    }

    @Override
    public @NotNull Article create(final @NotNull Article article) {
        return jdbi.inTransaction(handle -> article.initializeWithId(
                handle.createUpdate(
                        "INSERT INTO articles (title, url, created_at, topic_id, website_id) " +
                                "VALUES (:title, :url, :created_at, :topic_id, :website_id)"
                )
                        .bind("title", article.title())
                        .bind("url", article.url())
                        .bind("created_at", article.createdAt())
                        .bind("topic_id", article.topicId())
                        .bind("website_id", article.websiteId())
                        .executeAndReturnGeneratedKeys("article_id")
                        .mapTo(ArticleId.class)
                        .one()
        ));
    }

    @Override
    public void update(final @NotNull Article article) {
        if (article.id() == null) {
            throw new ArticleNotFoundException(null);
        }

        jdbi.useTransaction(handle ->
            handle.createUpdate("UPDATE articles SET title = :title, url = :url, " +
                            "created_at = :created_at, topic_id = :topic_id, website_id = :website_id" +
                            "WHERE article_id = :article_id")
               .bind("title", article.title())
               .bind("url", article.url())
               .bind("created_at", article.createdAt())
               .bind("topic_id", article.topicId())
               .bind("website_id", article.websiteId())
               .bind("article_id", article.id().value())
               .execute()
        );
    }

    @Override
    public void delete(final @NotNull ArticleId articleId) {
        jdbi.useTransaction(handle ->
            handle.createUpdate("DELETE FROM articles WHERE article_id = :article_id")
                    .bind("article_id", articleId.value())
                    .execute()
        );
    }

    @Override
    public List<Article> getAll() {
        return jdbi.inTransaction(handle ->
                handle.createQuery("SELECT * FROM articles")
                        .mapTo(Article.class)
                        .collectIntoList()
        );
    }
}
