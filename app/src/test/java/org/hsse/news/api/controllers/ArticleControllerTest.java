package org.hsse.news.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hsse.news.api.authorizers.BasicAuthorizer;
import org.hsse.news.api.schemas.response.article.ArticleListResponse;
import org.hsse.news.api.util.SimpleHttpClient;
import org.hsse.news.database.article.ArticleService;
import org.hsse.news.database.article.models.Article;
import org.hsse.news.database.topic.models.TopicId;
import org.hsse.news.database.website.models.WebsiteId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import spark.Service;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {
    private static final String API_PREFIX = "/test/api";

    private Service service;
    private ObjectMapper objectMapper;  // NOPMD - suppressed SingularField - TODO not yet implemented
    private ArticleController articleController; // NOPMD - suppressed SingularField - TODO not yet implemented
    private SimpleHttpClient client;
    private String baseUrl;

    @Mock
    private ArticleService articleService;

    @Mock
    private BasicAuthorizer authorizer;

    @BeforeEach
    void setUp() {
        service = Service.ignite();

        objectMapper = new ObjectMapper();
        articleController = new ArticleController(
                API_PREFIX,
                service,
                articleService,
                objectMapper,
                authorizer
        );

        articleController.initializeEndpoints();
        service.awaitInitialization();

        baseUrl = "http://localhost:" + service.port() + API_PREFIX + "/articles";
        client = new SimpleHttpClient();
    }

    @AfterEach
    void tearDown() {
        service.stop();
        service.awaitStop();
    }

    @Test
    void should200OnGet() throws IOException, InterruptedException {
        final HttpResponse<String> response = client.get(baseUrl);
        assertEquals(200, response.statusCode(), "should be 200 (OK)");
    }

    @Test
    void shouldArticleTitleOnGet() throws IOException, InterruptedException {
        final List<Article> testArticle = List.of(
                new Article("title", "http://url.ru", new Timestamp(new Date().getTime()), new TopicId(1L), new WebsiteId(2L))
        );
        Mockito.when(articleService.getAll()).thenReturn(testArticle);

        final HttpResponse<String> response = client.get(baseUrl);
        final ArticleListResponse responseArticle = objectMapper.readValue(response.body(), ArticleListResponse.class);

        assertEquals(testArticle.get(0).title(), responseArticle.articles().get(0).title(), "Titles should be same");
    }

    @Test
    void shouldArticleTopicIdOnGet() throws IOException, InterruptedException {
        final List<Article> testArticle = List.of(
                new Article("title", "http://url.ru", new Timestamp(new Date().getTime()), new TopicId(1L), new WebsiteId(2L))
        );
        Mockito.when(articleService.getAll()).thenReturn(testArticle);

        final HttpResponse<String> response = client.get(baseUrl);
        final ArticleListResponse responseArticle = objectMapper.readValue(response.body(), ArticleListResponse.class);

        assertEquals(String.valueOf(testArticle.get(0).topicId()), responseArticle.articles().get(0).topicId(), "Topics should be same");
    }
}
