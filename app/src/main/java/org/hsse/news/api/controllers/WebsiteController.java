package org.hsse.news.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hsse.news.api.authorizers.Authorizer;
import org.hsse.news.api.schemas.request.website.WebsitePostRequest;
import org.hsse.news.api.schemas.response.error.UserExceededQuantityLimitResponse;
import org.hsse.news.api.schemas.response.error.WebsiteAlreadyExistsResponse;
import org.hsse.news.api.schemas.response.error.WebsiteNotFoundResponse;
import org.hsse.news.api.schemas.response.website.WebsiteResponse;
import org.hsse.news.api.util.ControllerUtil;
import org.hsse.news.database.user.models.UserId;
import org.hsse.news.database.website.WebsiteService;
import org.hsse.news.database.website.exceptions.QuantityLimitExceededWebsitesPerUserException;
import org.hsse.news.database.website.exceptions.WebsiteAlreadyExistsException;
import org.hsse.news.database.website.exceptions.WebsiteNotFoundException;
import org.hsse.news.database.website.models.Website;
import org.hsse.news.database.website.models.WebsiteId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Response;
import spark.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public final class WebsiteController implements Controller {
    private static final Logger LOG = LoggerFactory.getLogger(WebsiteController.class);
    private static final String WEBSITES_PREFIX = "/websites";
    private static final String ACCEPT_TYPE = "application/json";
    private static final String ID_URL_PARAM = "id";

    private final String routePrefix;
    private final Service service;
    private final WebsiteService websiteService;
    private final ObjectMapper objectMapper;
    private final Authorizer authorizer;
    private final Integer maxWebsitesPerUser;

    public WebsiteController(
            final String apiPrefix,
            final Service service,
            final WebsiteService websiteService,
            final ObjectMapper objectMapper,
            final Authorizer authorizer,
            final Integer maxWebsitesPerUser
    ) {
        this.maxWebsitesPerUser = maxWebsitesPerUser;
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

        service.get(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    ControllerUtil.logRequest(request, path);

                    final Optional<UserId> userId = authorizer.authorizeOptional(request);

                    if (userId.isEmpty()) {
                        Stream<WebsiteResponse> websites = websiteService.getAll()
                                .stream()
                                .map(website -> new WebsiteResponse(
                                        website.id().value(),
                                        website.url(),
                                        website.description()
                                ));
                        return objectMapper.writeValueAsString(websites.toList());
                    }

                    Stream<WebsiteResponse> subscribed = websiteService.getSubscribedWebsitesByUserId(userId.get())
                            .stream()
                            .map(website -> new WebsiteResponse(
                                    website.id().value(),
                                    website.url(),
                                    website.description()
                            ));

                    Stream<WebsiteResponse> other = websiteService.getUnSubscribedWebsitesByUserId(userId.get())
                            .stream()
                            .map(website -> new WebsiteResponse(
                                    website.id().value(),
                                    website.url(),
                                    website.description()
                            ));

                    return objectMapper.writeValueAsString(
                        Map.of("subscribed", subscribed.toList(), "other", other.toList())
                    );
                }
        );
    }

    private void put() {
        final String path = routePrefix;

        service.put(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    ControllerUtil.logRequest(request, path);

                    final UserId userId = authorizer.authorizeStrict(request);

                    final List<WebsiteId> websites = ControllerUtil.validateRequestSchemas(
                            request,
                            WebsiteId.class,
                            service,
                            objectMapper
                    );

                    try {
                        websiteService.tryUpdateSubscribedWebsites(websites, userId);
                        response.status(201);
                    } catch (QuantityLimitExceededWebsitesPerUserException exc) {
                        return processQuantityLimitException(exc, response);
                    }

                    return "";
                }
        );
    }

    private void createCustom() {
        final String path = routePrefix + "/custom";

        service.post(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    ControllerUtil.logRequest(request, path);

                    final UserId userId = authorizer.authorizeStrict(request);

                    final WebsitePostRequest rawWebsite =
                            ControllerUtil.validateRequestSchema(
                                    request,
                                    WebsitePostRequest.class,
                                    service,
                                    objectMapper
                            );

                    Website website;
                    try {
                        website = websiteService.create(
                                new Website(rawWebsite.url().toString(), rawWebsite.description(), userId)
                        );
                        response.status(201);
                    } catch (WebsiteNotFoundException exc) {
                        return processWebsiteNotFound(exc, response);
                    } catch (WebsiteAlreadyExistsException exc) {
                        return processWebsiteAlreadyExists(exc, response);
                    }

                    return objectMapper.writeValueAsString(
                            new WebsiteResponse(
                                    website.id().value(),
                                    website.url(),
                                    rawWebsite.description()
                            )
                    );
                }
        );
    }

    private void deleteCustom() {
        final String path = routePrefix + "/custom/:" + ID_URL_PARAM;

        service.delete(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    ControllerUtil.logRequest(request, path);

                    final UserId userId = authorizer.authorizeStrict(request);

                    final WebsiteId websiteId =
                            ControllerUtil.validateParamSchema(
                                    request,
                                    WebsiteId.class,
                                    ID_URL_PARAM,
                                    service,
                                    objectMapper
                            );

                    try {
                        websiteService.delete(websiteId, userId);
                        response.status(204);
                    } catch (WebsiteNotFoundException exc) {
                        return processWebsiteNotFound(exc, response);
                    }

                    return "";
                }
        );
    }

    private Object processQuantityLimitException(
            final QuantityLimitExceededWebsitesPerUserException exc, final Response response
    ) throws JsonProcessingException {
        LOG.debug("User exceeded quantity limit: {}", exc.getMessage());
        response.status(406);
        return objectMapper.writeValueAsString(
            new UserExceededQuantityLimitResponse("User exceeded quantity limit subscribed websites", maxWebsitesPerUser)
        );
    }

    private String processWebsiteAlreadyExists(
            final WebsiteAlreadyExistsException exc, final Response response
    ) throws JsonProcessingException {
        LOG.debug("Website already exists: {}", exc.getMessage());
        response.status(409);
        return objectMapper.writeValueAsString(
                new WebsiteAlreadyExistsResponse("Website already exists")
        );
    }

    private String processWebsiteNotFound(
            final WebsiteNotFoundException exc, final Response response
    ) throws JsonProcessingException {
        LOG.debug("Website not found: {}", exc.getMessage());

        response.status(404);
        return objectMapper.writeValueAsString(
                new WebsiteNotFoundResponse("Website not found")
        );
    }
}
