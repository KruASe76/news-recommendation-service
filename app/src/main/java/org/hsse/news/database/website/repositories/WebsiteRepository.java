package org.hsse.news.database.website.repositories;

import org.hsse.news.database.user.models.UserId;
import org.hsse.news.database.website.exceptions.WebsiteAlreadyExistsException;
import org.hsse.news.database.website.exceptions.WebsiteNotFoundException;
import org.hsse.news.database.website.models.Website;
import org.hsse.news.database.website.models.WebsiteId;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface WebsiteRepository {
    Optional<Website> findById(@NotNull WebsiteId websiteId);

    /**
     * @throws WebsiteAlreadyExistsException if the website exist
     */
    @NotNull Website create(@NotNull Website website);

    @NotNull List<Website> getAll();

    @NotNull List<Website> findSubscribedWebsitesByUserId(@NotNull UserId creatorId);

    @NotNull List<Website> findUnSubscribedWebsitesByUserId(@NotNull UserId creatorId);

    /**
     * @throws WebsiteNotFoundException if the website does not exist
     * @throws WebsiteAlreadyExistsException if the website exist
     */
    void update(@NotNull Website website);

    /**
     * @throws WebsiteNotFoundException if the website does not exist
     */
    void delete(@NotNull WebsiteId userId, @NotNull UserId creatorId);

    void updateSubscribedWebsites(@NotNull List<WebsiteId> websites, @NotNull UserId userId);
}
