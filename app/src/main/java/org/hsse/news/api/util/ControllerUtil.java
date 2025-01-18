package org.hsse.news.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hsse.news.api.authorizers.Authorizer;
import org.hsse.news.database.user.models.UserId;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Service;

import java.util.Locale;

public final class ControllerUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ControllerUtil.class);

    public static void logRequest(final Request request, final String path) {
        LOG.info(
                "{} - {} {}",
                request.ip(),
                request.requestMethod().toUpperCase(Locale.ROOT),
                path
        );
    }

    public static <T> @NotNull T validateRequestSchema(
            final Request request, final Class<T> schemaType,
            final Service service, final ObjectMapper objectMapper
    ) {
        final T schema;

        try {
            schema = objectMapper.readValue(request.body(), schemaType);
        } catch (JsonProcessingException e) {
            LOG.debug("Invalid JSON: {}", e.getMessage());
            service.halt(422, "Validation error");
            return null;
        }

        return schema;
    }

    public static @NotNull UserId extractUserId(final Request request, final Service service) {
        final UserId userId = request.attribute(Authorizer.USER_ID_ATTRIBUTE);

        if (userId == null) {
            LOG.error("UserId attribute is not present");
            service.halt(500, "Internal authorization error");
        }

        return userId;
    }

    private ControllerUtil() {}
}
