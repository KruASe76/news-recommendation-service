package org.hsse.news.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hsse.news.api.authorizers.Authorizer;
import org.hsse.news.api.schemas.response.article.ArticleListResponse;
import org.hsse.news.api.schemas.response.article.ArticleResponse;
import org.hsse.news.api.util.ArticleCastUtil;
import org.hsse.news.api.util.ControllerUtil;
import org.hsse.news.database.article.ArticleService;
import org.hsse.news.database.article.models.Article;
import org.hsse.news.database.topic.TopicService;
import org.hsse.news.database.user.models.UserId;
import org.hsse.news.database.website.WebsiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ArticleController implements Controller {
    private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);
    private static final String ARTICLES_PREFIX = "/articles";
    private static final String ACCEPT_TYPE = "application/json";

    private final String routePrefix;
    private final Service service;
    private final ArticleService articleService; // NOPMD - suppressed UnusedPrivateField - TODO not yet implemented
    private final WebsiteService websiteService;
    private final TopicService topicService;
    private final ObjectMapper objectMapper; // NOPMD - suppressed UnusedPrivateField - TODO not yet implemented
    private final Authorizer authorizer;

    public ArticleController(
            final String apiPrefix,
            final Service service,
            final ArticleService articleService, final WebsiteService websiteService, final TopicService topicService,
            final ObjectMapper objectMapper,
            final Authorizer authorizer
    ) {
        this.routePrefix = apiPrefix + ARTICLES_PREFIX;
        this.service = service;
        this.articleService = articleService;
        this.websiteService = websiteService;
        this.topicService = topicService;
        this.objectMapper = objectMapper;
        this.authorizer = authorizer;
    }

    @Override
    public void initializeEndpoints() {
        get();
    }

    private void get() {
        final String path = routePrefix;

        service.get(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    final UserId userId = authorizer.authorizeStrict(request);
                    ControllerUtil.logRequest(request, path);

                    final ArticleCastUtil castUtil = new ArticleCastUtil(topicService, websiteService);
                    final List<Article> articleList = articleService.getAllUnknown(userId);
                    final Map<String, ArticleResponse> responses = new ConcurrentHashMap<>();

                    for (final Article article : articleList) {
                        if (responses.containsKey(article.url())) {
                            responses.get(article.url()).addTopic(topicService.getTopicNameById(article.topicId()));
                        } else {
                            responses.put(article.url(), castUtil.fromArticle(article));
                        }

                    }

                    LOG.debug("Successfully get all articles");
                    response.status(200);

                    return objectMapper.writeValueAsString(
                            new ArticleListResponse(
                                    responses.size(),
                                    responses.values().stream().toList()
                            )
                    );
                }
        );
    }
}
