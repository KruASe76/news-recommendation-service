package org.hsse.news.database.website.exceptions;

import org.hsse.news.database.website.models.WebsiteId;

public class WebsiteAlreadyExistsException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Website with id=%s url=%s already exists";

    public WebsiteAlreadyExistsException(final WebsiteId websiteId, final String url) {
        super(String.format(DEFAULT_MESSAGE, websiteId, url));
    }
}
