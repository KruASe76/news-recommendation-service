package org.hsse.news.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hsse.news.api.authorizers.Authorizer;
import org.hsse.news.api.util.ControllerUtil;
import org.hsse.news.database.article.ArticleService;
import org.hsse.news.database.article.models.ArticleId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;
import spark.route.HttpMethod;

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
    }

    private void get() {
        final String path = routePrefix;

        authorizer.enableAuthorization(path, HttpMethod.get);
        service.get(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    ControllerUtil.logRequest(request, path);

                    LOG.error("Not implemented");

                    service.halt(501, "Not Implemented");
                    return null;
                }
        );
    }
}
