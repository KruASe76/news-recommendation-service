package org.hsse.news.api.util;

import org.hsse.news.database.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;

public class Authorizer {
    private static final Logger LOG = LoggerFactory.getLogger(Authorizer.class);

    private final Service service;
    private final UserService userService; // NOPMD - suppressed UnusedPrivateField - TODO not yet implemented

    public Authorizer(final Service service, final UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    public void enableAuthorization(final String path) {
        service.before(
                path,
                (request, response) -> {
                    LOG.info(request.headers("Authorization"));
                }
        );
    }

    public void enableOptionalAuthorization(final String path) {
        service.before(
                path,
                (request, response) -> {
                    LOG.info(request.headers("Authorization"));
                }
        );
    }
}
