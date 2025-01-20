package org.hsse.news.database.article;

import org.hsse.news.database.article.exceptions.ArticleNotFoundException;
import org.hsse.news.database.article.models.Article;
import org.hsse.news.database.article.models.ArticleId;
import org.hsse.news.database.article.repositories.ArticleRepository;
import org.hsse.news.database.article.repositories.JdbiArticleRepository;
import org.hsse.news.database.topic.models.TopicId;
import org.hsse.news.database.user.models.UserId;
import org.hsse.news.database.util.JdbiTransactionManager;
import org.hsse.news.database.util.TransactionManager;
import org.hsse.news.database.website.models.WebsiteId;

import java.sql.Timestamp;
import java.util.List;
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

    public Optional<Article> findById(final ArticleId articleId) { // NOPMD
        return articleRepository.findById(articleId);
    }

    public List<Article> getAllUnknown(final UserId userId) {
        return articleRepository.getAllUnknown(userId);
    }

    public Article create(final Article article) {
        return articleRepository.create(article);
    }

    public void update(final ArticleId articleId, // NOPMD
                       final String title,
                       final String url,
                       final Timestamp createdAt,
                       final TopicId topicId,
                       final WebsiteId websiteId
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
                            .withTopicId(topicId)
                            .withWebsiteId(websiteId)
            );
        });
    }

    public void delete(final ArticleId articleId) {
        articleRepository.delete(articleId);
    }
}
