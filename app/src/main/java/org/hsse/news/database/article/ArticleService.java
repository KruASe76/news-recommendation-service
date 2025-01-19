package org.hsse.news.database.article;

import org.hsse.news.database.article.exceptions.ArticleNotFoundException;
import org.hsse.news.database.article.models.Article;
import org.hsse.news.database.article.models.ArticleId;
import org.hsse.news.database.article.repositories.ArticleRepository;
import org.hsse.news.database.article.repositories.JdbiArticleRepository;
import org.hsse.news.database.util.JdbiTransactionManager;
import org.hsse.news.database.util.TransactionManager;

import java.util.Optional;

public final class ArticleService {
    private final ArticleRepository articleRepository;
    private final TransactionManager transactionManager;

    public ArticleService(
            final ArticleRepository articleRepository, final TransactionManager transactionManager
    ) {
        this.articleRepository = articleRepository;
        this.transactionManager = transactionManager;
    }

    public ArticleService() {
        this(new JdbiArticleRepository(), new JdbiTransactionManager());
    }

    public Optional<Article> findById(final ArticleId articleId) {
        return articleRepository.findById(articleId);
    }

    public Article create(final Article article) {
        return articleRepository.create(article);
    }

    public void update(final ArticleId articleId,
                       final String title,
                       final String url,
                       final String createdAt,
                       final String topic
    ) {
        transactionManager.useTransaction(() -> {
            final Article articleToUpdate =
                    articleRepository.findById(articleId)
                            .orElseThrow(() -> new ArticleNotFoundException(articleId));

            articleRepository.update(
                    articleToUpdate
                            .withTitle(title)
                            .withUrl(url)
                            .withCreatedAt(createdAt)
                            .withTopic(topic)
            );
        });
    }

    public void delete(final ArticleId articleId) {
        articleRepository.delete(articleId);
    }
}
