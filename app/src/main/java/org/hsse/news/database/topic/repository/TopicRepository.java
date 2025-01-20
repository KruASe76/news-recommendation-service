package org.hsse.news.database.topic.repository;

import org.hsse.news.database.user.models.UserId;
import org.hsse.news.database.topic.exceptions.TopicAlreadyExistsException;
import org.hsse.news.database.topic.exceptions.TopicNotFoundException;
import org.hsse.news.database.topic.models.Topic;
import org.hsse.news.database.topic.models.TopicId;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface TopicRepository {
  Optional<Topic> findById(@NotNull TopicId topicId);

  @NotNull Topic create(@NotNull Topic topic);

  @NotNull List<Topic> getAll();

  void delete(@NotNull TopicId topicId);
}
