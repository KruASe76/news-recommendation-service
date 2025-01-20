/*
package org.hsse.news.database.topic.repository;

import org.hsse.news.database.user.models.UserId;
import org.hsse.news.database.topic.exceptions.TopicAlreadyExistsException;
import org.hsse.news.database.topic.exceptions.TopicNotFoundException;
import org.hsse.news.database.topic.models.Topic;
import org.hsse.news.database.topic.models.TopicId;
import org.hsse.news.util.JdbiProvider;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class JdbiTopicRepository implements TopicRepository {
  private final Jdbi jdbi;

  public JdbiTopicRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  public JdbiTopicRepository() {
    this(JdbiProvider.get());
  }

  @Override
  public Optional<Topic> findById(@NotNull TopicId websiteId) {
    return jdbi.inTransaction(handle ->
        handle.createQuery("SELECT * FROM topics WHERE topic_id = :topic_id")
            .bind("website_id", websiteId.value()) // NOPMD - suppressed AvoidDuplicateLiterals - irrelevant
            .mapTo(Topic.class)
            .findFirst()
    );
  }
}
 */
