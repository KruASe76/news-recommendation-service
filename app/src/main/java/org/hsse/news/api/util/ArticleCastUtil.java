package org.hsse.news.api.util;

import org.hsse.news.api.schemas.response.article.ArticleResponse;
import org.hsse.news.database.article.models.Article;
import org.hsse.news.database.topic.TopicService;
import org.hsse.news.database.website.WebsiteService;

import java.util.ArrayList;
import java.util.List;

public final class ArticleCastUtil {
    private final TopicService topicService;
    private final WebsiteService websiteService;

    public ArticleCastUtil(final TopicService topicService, final WebsiteService websiteService) {
        this.topicService = topicService;
        this.websiteService = websiteService;
    }

    public ArticleResponse fromArticle(final Article article) {
        return new ArticleResponse(
                article.title(),
                article.url(),
                String.valueOf(article.createdAt()),
                new ArrayList<>(List.of(topicService.getTopicNameById(article.topicId()))), // NOPMD
                websiteService.findById(article.websiteId()).get().description() // NOPMD
        );
    }
}
