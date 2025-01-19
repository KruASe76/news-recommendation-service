package org.hsse.news.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hsse.news.api.authorizers.Authorizer;
import org.hsse.news.api.schemas.request.article.ArticleCreateRequest;
import org.hsse.news.api.schemas.request.article.ArticleDeleteRequest;
import org.hsse.news.api.schemas.shared.ArticleInfo;
import org.hsse.news.api.util.ControllerUtil;
import org.hsse.news.database.article.ArticleService;
import org.hsse.news.database.article.models.Article;
import org.hsse.news.database.article.models.ArticleId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;
import spark.route.HttpMethod;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

public final class ArticleController implements Controller {
    private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);
    private static final String ARTICLES_PREFIX = "/articles";
    private static final String ACCEPT_TYPE = "application/json";

    private final String routePrefix;
    private final Service service;
    private final ArticleService articleService; // NOPMD - suppressed UnusedPrivateField - TODO not yet implemented
    private final ObjectMapper objectMapper; // NOPMD - suppressed UnusedPrivateField - TODO not yet implemented
    private final Authorizer authorizer;

    public ArticleController(
            final String apiPrefix,
            final Service service,
            final ArticleService articleService,
            final ObjectMapper objectMapper,
            final Authorizer authorizer
    ) {
        this.routePrefix = apiPrefix + ARTICLES_PREFIX;
        this.service = service;
        this.articleService = articleService;
        this.objectMapper = objectMapper;
        this.authorizer = authorizer;
    }

    @Override
    public void initializeEndpoints() {
        get();
        update();
        delete();
        create();
    }

    private void get() {
        final String path = routePrefix;

//        authorizer.enableAuthorization(path, HttpMethod.get);
        service.get(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    ControllerUtil.logRequest(request, path);

                    final ArticleId articleId = ControllerUtil.extractArticleId(request, service);
                    final Optional<Article> articleOptional = articleService.findById(articleId);

                    if (articleOptional.isEmpty()) {
                        LOG.warn("Article not found for id = {}", articleId);
                        service.halt(404, "Article not found");
                        return "";
                    }

                    LOG.debug("Successfully found article by id = {}", articleOptional.get().id());
                    response.status(200);

                    return objectMapper.writeValueAsString(
                            new ArticleInfo(
                                    articleOptional.get().title(),
                                    articleOptional.get().url(),
                                    articleOptional.get().createdAt(),
                                    articleOptional.get().topicId(),
                                    articleOptional.get().websiteId()
                            )
                    );
                }
        );
    }

    private void update() {
        final String path = routePrefix;

//        authorizer.enableAuthorization(path, HttpMethod.put);
        service.put(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    ControllerUtil.logRequest(request, path);

                    final ArticleInfo articleInfo =
                            ControllerUtil.validateRequestSchema(
                                    request,
                                    ArticleInfo.class,
                                    service,
                                    objectMapper
                            );

                    final ArticleId articleId = ControllerUtil.extractArticleId(request, service);

                    articleService.update(articleId, articleInfo.title(),
                            articleInfo.url(), articleInfo.createdAt(),
                            articleInfo.topicId(), articleInfo.websiteId()
                    );

                    LOG.debug("Successfully updated article with id = {}", articleId);
                    response.status(204);

                    return "";
                }
        );
    }

    private void delete() {
        final String path = routePrefix;

//        authorizer.enableAuthorization(path, HttpMethod.delete);
        service.delete(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    ControllerUtil.logRequest(request, path);

                    final ArticleDeleteRequest articleDeleteRequest =
                            ControllerUtil.validateRequestSchema(
                                    request,
                                    ArticleDeleteRequest.class,
                                    service,
                                    objectMapper
                            );

                    final ArticleId articleId = ControllerUtil.extractArticleId(request, service);
                    final Optional<Article> articleOptional = articleService.findById(articleId);

                    if (articleOptional.isEmpty()) {
                        LOG.warn("Article not found for id = {}", articleId);
                        service.halt(404, "Article not found");
                        return "";
                    }

                    LOG.debug("Successfully delete article by id = {}", articleId);
                    response.status(200);

                    return "";
                }
        );
    }

    private void create() {
        final String path = routePrefix + "/create";

//        authorizer.enableAuthorization(path, HttpMethod.post);
        service.post(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    ControllerUtil.logRequest(request, path);

                    final ArticleCreateRequest articleCreateRequest =
                            ControllerUtil.validateRequestSchema(
                                    request,
                                    ArticleCreateRequest.class,
                                    service,
                                    objectMapper
                            );

                    final Article article = articleService.create(
                            new Article(
                                    articleCreateRequest.newTitle(),
                                    articleCreateRequest.newUrl(),
                                    new Timestamp(new Date().getTime()),
                                    articleCreateRequest.newTopicId(),
                                    articleCreateRequest.newWebsiteId()
                            )
                    );

                    LOG.debug("Created article with id = {}", article.id());
                    response.status(201);

                    return "";
                }
        );
    }
}
