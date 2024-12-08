package org.hsse.news.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hsse.news.api.util.Authorizer;
import org.hsse.news.database.topic.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;

public final class TopicController implements Controller {
    private static final Logger LOG = LoggerFactory.getLogger(TopicController.class);
    private static final String TOPICS_PREFIX = "/topics";
    private static final String ACCEPT_TYPE = "application/json";

    private final String routePrefix;
    private final Service service;
    private final TopicService topicService; // NOPMD - suppressed UnusedPrivateField - TODO not yet implemented
    private final ObjectMapper objectMapper; // NOPMD - suppressed UnusedPrivateField - TODO not yet implemented
    private final Authorizer authorizer;

    public TopicController(
            final String apiPrefix,
            final Service service,
            final TopicService topicService,
            final ObjectMapper objectMapper,
            final Authorizer authorizer
    ) {
        this.routePrefix = apiPrefix + TOPICS_PREFIX;
        this.service = service;
        this.topicService = topicService;
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

        authorizer.enableOptionalAuthorization(path);
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

    private void createCustom() {
        final String path = routePrefix + "/custom";

        authorizer.enableAuthorization(path);
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

        authorizer.enableAuthorization(path);
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
