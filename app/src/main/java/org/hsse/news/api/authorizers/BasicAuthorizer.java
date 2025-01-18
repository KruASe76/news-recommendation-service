package org.hsse.news.api.authorizers;

import org.hsse.news.database.user.UserService;
import org.hsse.news.database.user.models.AuthenticationCredentials;
import org.hsse.news.database.user.models.UserId;
import org.hsse.news.util.PasswordUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Service;
import spark.route.HttpMethod;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.util.Optional;

public class BasicAuthorizer implements Authorizer {
    private static final Logger LOG = LoggerFactory.getLogger(BasicAuthorizer.class);
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    private final Service service;
    private final UserService userService;

    public BasicAuthorizer(final Service service, final UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @Override
    public void enableAuthorization(final String path, final HttpMethod method) {
        service.before(
                path,
                (request, response) -> {
                    if (HttpMethod.get(request.requestMethod().toLowerCase(Locale.ROOT)) != method) {
                        return;
                    }

                    final AuthenticationCredentials credentials =
                            extractCredentials(request.headers("Authorization"));

                    if (credentials == null) {
                        LOG.debug("No authorization header provided");

                        service.halt(401, "Missing authorization header");
                    }

                    tryAuthorize(request, credentials);
                }
        );
    }

    @Override
    public void enableOptionalAuthorization(final String path, final HttpMethod method) {
        service.before(
                path,
                (request, response) -> {
                    if (HttpMethod.get(request.requestMethod().toLowerCase(Locale.ROOT)) != method) {
                        return;
                    }

                    final AuthenticationCredentials credentials =
                            extractCredentials(request.headers("Authorization"));

                    if (credentials == null) {
                        return;  // fail silently and do not provide UserId header
                    }

                    tryAuthorize(request, credentials);
                }
        );
    }

    private void tryAuthorize(
            final Request request, final @NotNull AuthenticationCredentials credentials
    ) {
        LOG.debug("Attempting to authorize user with email = {}", credentials.email());

        final Optional<UserId> userIdOptional = userService.authenticate(credentials);

        if (userIdOptional.isEmpty()) {
            LOG.debug("Failed to authorize user with email = {}", credentials.email());

            service.halt(401, "Invalid credentials");
        }

        request.attribute(USER_ID_ATTRIBUTE, userIdOptional.get());

        LOG.debug("Successfully authorized user with id = {}", userIdOptional.get());
    }

    private AuthenticationCredentials extractCredentials(final String authorizationHeader) {
        if (authorizationHeader == null) {
            return null;
        }

        final String decodedCredentials = new String(
                DECODER.decode(authorizationHeader.replace("Basic ", "")),
                StandardCharsets.UTF_8
        );
        final String[] emailAndPassword = decodedCredentials.split(":");

        return new AuthenticationCredentials(
                emailAndPassword[0],
                PasswordUtil.hashPassword(emailAndPassword[1])
        );
    }
}
