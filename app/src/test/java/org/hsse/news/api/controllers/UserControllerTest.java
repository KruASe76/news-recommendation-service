package org.hsse.news.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hsse.news.api.util.Authorizer;
import org.hsse.news.api.util.SimpleHttpClient;
import org.hsse.news.database.user.UserService;
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
class UserControllerTest {
    private static final String API_PREFIX = "/test/api";

    private Service service;
    private ObjectMapper objectMapper;  // NOPMD - suppressed SingularField - TODO not yet implemented
    private UserController userController; // NOPMD - suppressed SingularField - TODO not yet implemented
    private SimpleHttpClient client;
    private String baseUrl;

    @Mock
    private UserService userService;

    @Mock
    private Authorizer authorizer;

    @BeforeEach
    void setUp() {
        service = Service.ignite();

        objectMapper = new ObjectMapper();
        userController = new UserController(
                API_PREFIX,
                service,
                userService,
                objectMapper,
                authorizer
        );

        userController.initializeEndpoints();
        service.awaitInitialization();

        baseUrl = "http://localhost:" + service.port() + API_PREFIX + "/user";
        client = new SimpleHttpClient();
    }

    @AfterEach
    void tearDown() {
        service.stop();
        service.awaitStop();
    }

    @Test
    void should501OnRegister() throws IOException, InterruptedException {
        final HttpResponse<String> response = client.post(
                baseUrl + "/register",
                """
                        {
                            "username": "testUser",
                            "email: "test@email.com",
                            "password": "testPassword"
                        }
                     """
        );

        assertEquals(501, response.statusCode(), "should be 501 (Not Implemented)"); // NOPMD - suppressed AvoidDuplicateLiterals - TODO temporal behaviour
    }

    @Test
    void should501OnGet() throws IOException, InterruptedException {
        final HttpResponse<String> response = client.get(baseUrl);

        assertEquals(501, response.statusCode(), "should be 501 (Not Implemented)");
    }

    @Test
    void should501OnUpdate() throws IOException, InterruptedException {
        final HttpResponse<String> response = client.put(
                baseUrl,
                """
                        {
                            "username": "testUserUpdated",
                            "email": "new@email.com"
                        }
                     """
        );

        assertEquals(501, response.statusCode(), "should be 501 (Not Implemented)");
    }

    @Test
    void should501OnChangePassword() throws IOException, InterruptedException {
        final HttpResponse<String> response = client.put(
                baseUrl + "/password",
                """
                        {
                          "current_password": "testPassword",
                          "new_password": "newPassword"
                        }
                        """
        );

        assertEquals(501, response.statusCode(), "should be 501 (Not Implemented)");
    }
}
