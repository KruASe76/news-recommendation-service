package org.hsse.news.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hsse.news.api.authorizers.Authorizer;
import org.hsse.news.database.website.WebsiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;
import spark.route.HttpMethod;

public final class WebsiteController implements Controller {
    private static final Logger LOG = LoggerFactory.getLogger(WebsiteController.class);
    private static final String WEBSITES_PREFIX = "/websites";
    private static final String ACCEPT_TYPE = "application/json";

    private final String routePrefix;
    private final Service service;
    private final WebsiteService websiteService; // NOPMD - suppressed UnusedPrivateField - TODO not yet implemented
    private final ObjectMapper objectMapper; // NOPMD - suppressed UnusedPrivateField - TODO not yet implemented
    private final Authorizer authorizer;

    public WebsiteController(
            final String apiPrefix,
            final Service service,
            final WebsiteService websiteService,
            final ObjectMapper objectMapper,
            final Authorizer authorizer
    ) {
        this.routePrefix = apiPrefix + WEBSITES_PREFIX;
        this.service = service;
        this.websiteService = websiteService;
        this.objectMapper = objectMapper;
        this.authorizer = authorizer;
    }

    @Override
    public void initializeEndpoints() {
        get();
        put();
        createCustom();
        deleteCustom();
    }

    private void get() {
        final String path = routePrefix;

        authorizer.enableOptionalAuthorization(path, HttpMethod.get);
        service.get(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    LOG.error("Not implemented"); // NOPMD - suppressed AvoidDuplicateLiterals - TODO temporal behaviour

                    service.halt(501, "Not Implemented"); // NOPMD - suppressed AvoidDuplicateLiterals - TODO temporal behaviour
                    return null;
                }
        );
    }

    private void put() {
        final String path = routePrefix;

        authorizer.enableAuthorization(path, HttpMethod.put);
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

    private void createCustom() {
        final String path = routePrefix + "/custom";

        authorizer.enableAuthorization(path, HttpMethod.post);
        service.post(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    LOG.error("Not implemented");

                    service.halt(501, "Not Implemented");
                    return null;
                }
        );
    }

    private void deleteCustom() {
        final String path = routePrefix + "/custom/:id";

        authorizer.enableAuthorization(path, HttpMethod.delete);
        service.delete(
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
