package org.hsse.news.database.article.repositories;

import org.hsse.news.database.article.models.Article;
import org.hsse.news.database.article.models.ArticleId;
import org.hsse.news.database.user.models.UserId;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    Optional<Article> findById(@NotNull ArticleId articleId);

    List<Article> getAllUnknown(UserId userId);

    @NotNull Article create(@NotNull Article article);

    void update(@NotNull Article article);

    void delete(@NotNull ArticleId articleId);
}
