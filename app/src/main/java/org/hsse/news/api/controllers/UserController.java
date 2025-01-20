package org.hsse.news.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hsse.news.api.authorizers.Authorizer;
import org.hsse.news.api.schemas.request.user.UserPasswordChangeRequest;
import org.hsse.news.api.schemas.request.user.UserRegisterRequest;
import org.hsse.news.api.schemas.response.error.ConflictErrorResponse;
import org.hsse.news.api.schemas.shared.UserInfo;
import org.hsse.news.api.util.ControllerUtil;
import org.hsse.news.database.user.UserService;
import org.hsse.news.database.user.exceptions.EmailConflictException;
import org.hsse.news.database.user.exceptions.InvalidCurrentPasswordException;
import org.hsse.news.database.user.exceptions.SameNewPasswordException;
import org.hsse.news.database.user.models.User;
import org.hsse.news.database.user.models.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Response;
import spark.Service;

import java.util.Optional;

public final class UserController implements Controller {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private static final String USERS_PREFIX = "/user";
    private static final String ACCEPT_TYPE = "application/json";

    private final String routePrefix;
    private final Service service;
    private final UserService userService;
    private final ObjectMapper objectMapper;
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
                    ControllerUtil.logRequest(request, path);

                    final UserRegisterRequest userRegisterRequest =
                            ControllerUtil.validateRequestSchema(
                                    request,
                                    UserRegisterRequest.class,
                                    service,
                                    objectMapper
                            );

                    try {
                        final User user = userService.register(
                                new User(
                                        userRegisterRequest.email(),
                                        userRegisterRequest.password(),
                                        userRegisterRequest.username())
                        );

                        LOG.debug("Registered user with id = {}", user.id());
                        response.status(201);
                    } catch (EmailConflictException e) {
                        return processEmailConflict(e, response);
                    }

                    return "";
                }
        );
    }

    private void get() {
        final String path = routePrefix;

        service.get(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    ControllerUtil.logRequest(request, path);

                    final UserId userId = authorizer.authorizeStrict(request);

                    final Optional<User> userOptional = userService.findById(userId);
                    if (userOptional.isEmpty()) {
                        LOG.warn("User not found for id = {}", userId);
                        service.halt(404, "User not found");
                        return "";
                    }

                    LOG.debug("Successfully found user by id = {}", userOptional.get().id());
                    response.status(200);

                    return objectMapper.writeValueAsString(
                            new UserInfo(
                                    userOptional.get().email(),
                                    userOptional.get().username()
                            )
                    );
                }
        );
    }

    private void update() {
        final String path = routePrefix;

        service.put(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    ControllerUtil.logRequest(request, path);

                    final UserId userId = authorizer.authorizeStrict(request);
                    final UserInfo userInfo =
                            ControllerUtil.validateRequestSchema(
                                    request,
                                    UserInfo.class,
                                    service,
                                    objectMapper
                            );

                    try {
                        userService.update(userId, userInfo.email(), userInfo.username());

                        LOG.debug("Successfully updated user with id = {}", userId);
                        response.status(204);
                    } catch (EmailConflictException e) {
                        return processEmailConflict(e, response);
                    }

                    return "";
                }
        );
    }

    private void changePassword() {
        final String path = routePrefix + "/password";

        service.put(
                path,
                ACCEPT_TYPE,
                (request, response) -> {
                    ControllerUtil.logRequest(request, path);

                    final UserId userId = authorizer.authorizeStrict(request);
                    final UserPasswordChangeRequest userPasswordChangeRequest =
                            ControllerUtil.validateRequestSchema(
                                    request,
                                    UserPasswordChangeRequest.class,
                                    service,
                                    objectMapper
                            );

                    try {
                        userService.updatePassword(
                                userId,
                                userPasswordChangeRequest.currentPassword(),
                                userPasswordChangeRequest.newPassword()
                        );

                        LOG.debug("Successfully updated password for user with id = {}", userId);
                        response.status(204);
                    } catch (SameNewPasswordException e) {
                        LOG.debug("Same new password for user with id = {}", userId);
                        service.halt(208, "Valid current password, new password matches it");
                    } catch (InvalidCurrentPasswordException e) {
                        LOG.debug("Invalid current password for user with id = {}", userId);
                        service.halt(412, "Invalid current password");
                    }

                    return "";
                }
        );
    }

    private String processEmailConflict(
            final EmailConflictException e, final Response response
    ) throws JsonProcessingException {
        LOG.debug("Email conflict: {}", e.getMessage());

        response.status(409);
        return objectMapper.writeValueAsString(
                new ConflictErrorResponse("Email already exists")
        );
    }
}
