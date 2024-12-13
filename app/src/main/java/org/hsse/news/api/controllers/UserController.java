package org.hsse.news.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hsse.news.api.util.Authorizer;
import org.hsse.news.database.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;

public final class UserController implements Controller {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private static final String USERS_PREFIX = "/user";
    private static final String ACCEPT_TYPE = "application/json";

    private final String routePrefix;
    private final Service service;
    private final UserService userService; // NOPMD - suppressed UnusedPrivateField - TODO not yet implemented
    private final ObjectMapper objectMapper; // NOPMD - suppressed UnusedPrivateField - TODO not yet implemented
    private final Authorizer authorizer;

    public UserController(
            final String apiPrefix,
            final Service service,
            final UserService userService,
            final ObjectMapper objectMapper,
            final Authorizer authorizer
    ) {
        this.routePrefix = apiPrefix + USERS_PREFIX;
        this.service = service;
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.authorizer = authorizer;
    }

    @Override
    public void initializeEndpoints() {
        register();
        get();
        update();
        changePassword();
    }

    private void register() {
        final String path = routePrefix + "/register";

        service.post(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    LOG.error("Not implemented"); // NOPMD - suppressed AvoidDuplicateLiterals - TODO temporal behaviour

                    service.halt(501, "Not Implemented"); // NOPMD - suppressed AvoidDuplicateLiterals - TODO temporal behaviour
                    return null;
                }
        );
    }

    private void get() {
        final String path = routePrefix;

        authorizer.enableAuthorization(path);
        service.get(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    LOG.error("Not implemented");

                    service.halt(501, "Not Implemented");
                    return null;
                }
        );
    }

    private void update() {
        final String path = routePrefix;

        authorizer.enableAuthorization(path);
        service.put(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    LOG.error("Not implemented");

                    service.halt(501, "Not Implemented");
                    return null;
                }
        );
    }

    private void changePassword() {
        final String path = routePrefix + "/password";

        authorizer.enableAuthorization(path);
        service.put(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    LOG.error("Not implemented");

                    service.halt(501, "Not Implemented");
                    return null;
                }
        );
    }
}
