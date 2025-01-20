package org.hsse.news.database.topic.exceptions;

import org.hsse.news.database.topic.models.TopicId;

public class TopicAlreadyExistsException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Website with id=%s description=%s already exists";

    public TopicAlreadyExistsException(final TopicId topicId, final String description) {
        super(String.format(DEFAULT_MESSAGE, topicId, description));
    }
}
