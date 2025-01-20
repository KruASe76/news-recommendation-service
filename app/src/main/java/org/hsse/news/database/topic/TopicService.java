package org.hsse.news.database.topic;

import org.hsse.news.database.topic.models.TopicId;

public final class TopicService {
    public String getTopicNameById(final TopicId id) {
        return id.toString();
    }
}
