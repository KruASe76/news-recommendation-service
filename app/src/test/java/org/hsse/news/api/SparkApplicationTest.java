package org.hsse.news.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hsse.news.api.controllers.ArticleController;
import org.hsse.news.api.controllers.Controller;
import org.hsse.news.api.controllers.TopicController;
import org.hsse.news.api.controllers.UserController;
import org.hsse.news.api.controllers.WebsiteController;
import org.hsse.news.api.util.Authorizer;
import org.hsse.news.api.util.SimpleHttpClient;
import org.hsse.news.database.article.ArticleService;
import org.hsse.news.database.topic.TopicService;
import org.hsse.news.database.user.UserService;
import org.hsse.news.database.website.WebsiteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Service;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SparkApplicationTest {
    private static final String API_PREFIX = "/test/api";

    private Service service;
    private ObjectMapper objectMapper; // NOPMD - suppressed SingularField - TODO not yet implemented
    private String baseUrl;
    private SimpleHttpClient client;

    @BeforeEach
    void setUp() {
        final ArticleService articleService = new ArticleService();
        final TopicService topicService = new TopicService();
        final UserService userService = new UserService();
        final WebsiteService websiteService = new WebsiteService();

        service = Service.ignite();
        objectMapper = new ObjectMapper();

        final Authorizer authorizer = new Authorizer(service, userService);

        List.of(
                new ArticleController(API_PREFIX, service, articleService, objectMapper, authorizer),
                new TopicController(API_PREFIX, service, topicService, objectMapper, authorizer),
                new UserController(API_PREFIX, service, userService, objectMapper, authorizer),
                new WebsiteController(API_PREFIX, service, websiteService, objectMapper, authorizer)
        ).forEach(Controller::initializeEndpoints);
        service.awaitInitialization();

        baseUrl = "http://localhost:" + service.port() + API_PREFIX;
        client = new SimpleHttpClient();
    }

    @AfterEach
    void tearDown() {
        service.stop();
        service.awaitStop();
    }

    @Test
    void endToEnd() throws IOException, InterruptedException {
        final HttpResponse<String> response = client.get(baseUrl + "/articles");

        assertEquals(501, response.statusCode(), "should be 501 (Not Implemented)");
    }
}
