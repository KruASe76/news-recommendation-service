package org.hsse.news.database.website.repositories;

import org.hsse.news.database.user.models.UserId;
import org.hsse.news.database.website.exceptions.WebsiteAlreadyExistsException;
import org.hsse.news.database.website.exceptions.WebsiteNotFoundException;
import org.hsse.news.database.website.models.Website;
import org.hsse.news.database.website.models.WebsiteId;
import org.hsse.news.util.JdbiProvider;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class JdbiWebsiteRepository implements WebsiteRepository {
    private final Jdbi jdbi;

    public JdbiWebsiteRepository(final Jdbi jdbi) { this.jdbi = jdbi; }

    public JdbiWebsiteRepository() {
        this(JdbiProvider.get());
    }

    @Override
    public Optional<Website> findById(final @NotNull WebsiteId websiteId) {
        return jdbi.inTransaction(handle ->
                handle.createQuery("SELECT * FROM websites WHERE website_id = :website_id")
                        .bind("website_id", websiteId.value()) // NOPMD - suppressed AvoidDuplicateLiterals - irrelevant
                        .mapTo(Website.class)
                        .findFirst()
        );
    }

    @Override
    public @NotNull Website create(final @NotNull Website website) {
        return jdbi.inTransaction(handle -> {
            try {
                return website.initializeWithId(handle.createUpdate(
                                "INSERT INTO websites (url, description, creator_id) " +
                                        "VALUES (:url, :description, :creator_id)"
                        )
                        .bind("url", website.url()) // NOPMD - suppressed AvoidDuplicateLiterals - irrelevant
                        .bind("description", website.description()) // NOPMD - suppressed AvoidDuplicateLiterals - irrelevant
                        .bind("creator_id", website.creatorId()) // NOPMD - suppressed AvoidDuplicateLiterals - irrelevant
                        .executeAndReturnGeneratedKeys("website_id") // NOPMD - suppressed AvoidDuplicateLiterals - irrelevant
                        .mapTo(WebsiteId.class)
                        .one()
                );
            } catch (UnableToExecuteStatementException e) {
                throw new WebsiteAlreadyExistsException(website.id(), website.url()); // NOPMD - suppressed PreserveStackTrace - irrelevant
            }
        });
    }

    @Override
    public @NotNull List<Website> getAll() {
        return jdbi.inTransaction(handle ->
                handle.createQuery("SELECT * FROM websites WHERE creator_id IS NULL")
                        .mapTo(Website.class)
                        .list()
        );
    }

    @Override
    public @NotNull List<Website> findSubscribedWebsitesByUserId(final @NotNull UserId creatorId) {
        return jdbi.inTransaction(handle -> handle.createQuery("SELECT * FROM websites JOIN user_websites " +
                        "WHERE websites.website_id = user_websites.website_id AND user_websites.user_id = :user_id")
                .bind("user_id", creatorId.value()) // NOPMD - suppressed AvoidDuplicateLiterals - irrelevant
                .mapTo(Website.class)
                .list());
    }

    @Override
    public @NotNull List<Website> findUnSubscribedWebsitesByUserId(final @NotNull UserId creatorId) {
        return jdbi.inTransaction(handle -> handle.createQuery("SELECT * FROM websites LEFT JOIN user_websites " +
                        "WHERE websites.website_id = user_websites.website_id AND user_websites.user_id = :user_id")
                .bind("user_id", creatorId.value()) // NOPMD - suppressed AvoidDuplicateLiterals - irrelevant
                .mapTo(Website.class)
                .list());
    }

    @Override
    public void update(final @NotNull Website website) {
        if (website.id() == null) {
            throw new WebsiteNotFoundException(null);
        }

        jdbi.useTransaction(handle -> {
            try {
                handle.createUpdate("UPDATE websites SET url = :url, description = :description WHERE website_id = :website_id")
                        .bind("url", website.url()) // NOPMD - suppressed AvoidDuplicateLiterals - irrelevant
                        .bind("description", website.description()) // NOPMD - suppressed AvoidDuplicateLiterals - irrelevant
                        .bind("website_id", website.id().value()) // NOPMD - suppressed AvoidDuplicateLiterals - irrelevant
                        .execute();
            } catch (UnableToExecuteStatementException e) {
                throw new WebsiteAlreadyExistsException(website.id(), website.url()); // NOPMD - suppressed PreserveStackTrace - irrelevant
            }
        });
    }

    @Override
    public void delete(final @NotNull WebsiteId websiteId, final @NotNull UserId creatorId) {
        jdbi.useTransaction(handle ->
                handle.createUpdate("DELETE FROM websites WHERE website_id = :website_id, creator_id = :creator_id")
                        .bind("website_id", websiteId.value()) // NOPMD - suppressed AvoidDuplicateLiterals - irrelevant
                        .bind("creator_id", creatorId.value()) // NOPMD - suppressed AvoidDuplicateLiterals - irrelevant
                        .execute()
        );
    }

    @Override
    public void updateSubscribedWebsites(final @NotNull List<WebsiteId> websites, final @NotNull UserId userId) {
        jdbi.useTransaction(handle -> {
            handle.createUpdate("DELETE FROM user_websites WHERE user_id = :user_id")
                    .bind("user_id", userId.value()) // NOPMD - suppressed AvoidDuplicateLiterals - irrelevant
                    .execute();
            for (final WebsiteId website : websites) {
                handle.createUpdate("INSERT INTO user_websites (user_id, website_id) VALUES (:user_id, :website_id)")
                        .bind("user_id", userId.value()) // NOPMD - suppressed AvoidDuplicateLiterals - irrelevant
                        .bind("website_id", website.value()) // NOPMD - suppressed AvoidDuplicateLiterals - irrelevant
                        .execute();
            }
        });
    }
}
