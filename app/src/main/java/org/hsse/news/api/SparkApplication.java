package org.hsse.news.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hsse.news.api.authorizers.Authorizer;
import org.hsse.news.api.authorizers.BasicAuthorizer;
import org.hsse.news.api.controllers.ArticleController;
import org.hsse.news.api.controllers.Controller;
import org.hsse.news.api.controllers.TopicController;
import org.hsse.news.api.controllers.UserController;
import org.hsse.news.api.controllers.WebsiteController;
import org.hsse.news.database.article.ArticleService;
import org.hsse.news.database.topic.TopicService;
import org.hsse.news.database.user.UserService;
import org.hsse.news.database.website.WebsiteService;
import org.hsse.news.util.SubApplication;
import spark.Service;

import java.util.List;

public class SparkApplication implements SubApplication {
    private static final String API_PREFIX = "/api/v1";

    private final List<Controller> controllers;

    public SparkApplication(
            final ArticleService articleService,
            final TopicService topicService,
            final UserService userService,
            final WebsiteService websiteService
    ) {
        final Service service = Service.ignite();
        final ObjectMapper objectMapper = new ObjectMapper();

        final Authorizer authorizer = new BasicAuthorizer(service, userService);

        this.controllers = List.of(
                new ArticleController(API_PREFIX, service, articleService, websiteService, topicService, objectMapper, authorizer),
                new TopicController(API_PREFIX, service, topicService, objectMapper, authorizer),
                new UserController(API_PREFIX, service, userService, objectMapper, authorizer),
                new WebsiteController(API_PREFIX, service, websiteService, objectMapper, authorizer)
        );
    }

    @Override
    public void run() {
        controllers.forEach(Controller::initializeEndpoints);
    }
}
