package org.hsse.news.api.util;

import org.hsse.news.api.schemas.response.article.ArticleResponse;
import org.hsse.news.database.article.models.Article;
import org.hsse.news.database.topic.TopicService;
import org.hsse.news.database.website.WebsiteService;
import org.hsse.news.database.website.models.WebsiteId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ArticleCastUtil {
    private final TopicService topicService;
    private final WebsiteService websiteService;

    public ArticleCastUtil(TopicService topicService, WebsiteService websiteService) {
        this.topicService = topicService;
        this.websiteService = websiteService;
    }

    public ArticleResponse fromArticle(final Article article) {
        return new ArticleResponse(
                article.title(),
                article.url(),
                String.valueOf(article.createdAt()),
                new ArrayList<>(Arrays.asList(topicService.getTopicNameById(article.topicId()))), // NOPMD
                websiteService.getDescriptionById(article.websiteId()) // NOPMD
        );
    }
}
