package org.hsse.news.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hsse.news.api.util.Authorizer;
import org.hsse.news.api.util.SimpleHttpClient;
import org.hsse.news.database.website.WebsiteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spark.Service;

import java.io.IOException;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class WebsiteControllerTest {
    private static final String API_PREFIX = "/test/api";

    private Service service;
    private ObjectMapper objectMapper;  // NOPMD - suppressed SingularField - TODO not yet implemented
    private WebsiteController websiteController; // NOPMD - suppressed SingularField - TODO not yet implemented
    private SimpleHttpClient client;
    private String baseUrl;

    @Mock
    private WebsiteService websiteService;

    @Mock
    private Authorizer authorizer;

    @BeforeEach
    void setUp() {
        service = Service.ignite();

        objectMapper = new ObjectMapper();
        websiteController = new WebsiteController(
                API_PREFIX,
                service,
                websiteService,
                objectMapper,
                authorizer
        );

        websiteController.initializeEndpoints();
        service.awaitInitialization();

        baseUrl = "http://localhost:" + service.port() + API_PREFIX + "/websites";
        client = new SimpleHttpClient();
    }

    @AfterEach
    void tearDown() {
        service.stop();
        service.awaitStop();
    }

    @Test
    void should501OnGet() throws IOException, InterruptedException {
        final HttpResponse<String> response = client.get(baseUrl);

        assertEquals(501, response.statusCode(), "should be 501 (Not Implemented)"); // NOPMD - suppressed AvoidDuplicateLiterals - TODO temporal behaviour
    }

    @Test
    void should501OnPut() throws IOException, InterruptedException {
        final HttpResponse<String> response = client.put(
                baseUrl,
                """
                     [
                         42,
                         69,
                         228
                     ]
                     """
        );

        assertEquals(501, response.statusCode(), "should be 501 (Not Implemented)"); // NOPMD - suppressed AvoidDuplicateLiterals - TODO temporal behaviour
    }

    @Test
    void should501OnCreateCustom() throws IOException, InterruptedException {
        final HttpResponse<String> response = client.post(
                baseUrl + "/custom",
                """
                        {
                            "name": "New Topic"
                        }
                     """
        );

        assertEquals(501, response.statusCode(), "should be 501 (Not Implemented)"); // NOPMD - suppressed AvoidDuplicateLiterals - TODO temporal behaviour
    }

    @Test
    void should501OnDeleteCustom() throws IOException, InterruptedException {
        final HttpResponse<String> response = client.delete(baseUrl + "/custom/" + 42);

        assertEquals(501, response.statusCode(), "should be 501 (Not Implemented)"); // NOPMD - suppressed AvoidDuplicateLiterals - TODO temporal behaviour
    }
}
