package org.hsse.news.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Service;

import java.util.List;
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

    public static <T> @NotNull T validateParamSchema(
            final Request request, final Class<T> schemaType, final String paramName,
            final Service service, final ObjectMapper objectMapper
    ) {
        final T schema;

        try {
            schema = objectMapper.readValue(request.params(paramName), schemaType);
        } catch (final JsonProcessingException exc) {
            LOG.debug("Invalid param: {}", exc.getMessage());
            service.halt(422, "Validation error");
            return null;
        }

        return schema;
    }

    public static <T> @NotNull List<T> validateRequestSchemas(
            final Request request, final Class<T> schemaType,
            final Service service, final ObjectMapper objectMapper
    ) {
        try {
            return objectMapper.readerForListOf(schemaType).readValue(request.body());
        } catch (JsonProcessingException e) {
            LOG.debug("Invalid JSON: {}", e.getMessage());
            service.halt(422, "Validation error");
            return List.of();
        }
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

    private ControllerUtil() {}
}
