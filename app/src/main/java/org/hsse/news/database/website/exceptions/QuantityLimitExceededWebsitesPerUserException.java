package org.hsse.news.database.website.exceptions;

import org.hsse.news.database.user.models.UserId;

public class QuantityLimitExceededWebsitesPerUserException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "User with id=%s exceeded quantity limit websites";

    public QuantityLimitExceededWebsitesPerUserException(final UserId websiteId) {
        super(String.format(DEFAULT_MESSAGE, websiteId));
    }
}
