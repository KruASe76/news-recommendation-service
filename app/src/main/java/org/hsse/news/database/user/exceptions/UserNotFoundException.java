package org.hsse.news.database.user.exceptions;

import org.hsse.news.database.user.models.UserId;

public final class UserNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "User with id=%s does not exist";

    public UserNotFoundException(final UserId userId) {
        super(String.format(DEFAULT_MESSAGE, userId));
    }
}
