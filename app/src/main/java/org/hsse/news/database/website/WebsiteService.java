package org.hsse.news.database.website;

import org.hsse.news.database.user.models.UserId;
import org.hsse.news.database.util.JdbiTransactionManager;
import org.hsse.news.database.util.TransactionManager;
import org.hsse.news.database.website.exceptions.QuantityLimitExceededWebsitesPerUserException;
import org.hsse.news.database.website.exceptions.WebsiteAlreadyExistsException;
import org.hsse.news.database.website.exceptions.WebsiteNotFoundException;
import org.hsse.news.database.website.models.Website;
import org.hsse.news.database.website.models.WebsiteId;
import org.hsse.news.database.website.repository.JdbiWebsiteRepository;
import org.hsse.news.database.website.repository.WebsiteRepository;

import java.util.List;
import java.util.Optional;

public final class WebsiteService {
    private final WebsiteRepository websiteRepository;
    private final TransactionManager transactionManager;
    private final Integer maxWebsitesPerUser;

    public WebsiteService(
            final WebsiteRepository websiteRepository,
            final TransactionManager transactionManager,
            final Integer maxWebsitesPerUser
    ) {
        this.maxWebsitesPerUser = maxWebsitesPerUser;
        this.websiteRepository = websiteRepository;
        this.transactionManager = transactionManager;
    }

    public WebsiteService(final Integer maxWebsitesPerUser) {
        this(new JdbiWebsiteRepository(), new JdbiTransactionManager(), maxWebsitesPerUser);
    }

    public Optional<Website> findById(final WebsiteId websiteId) {
        return websiteRepository.findById(websiteId);
    }

    public List<Website> getAll() {
        return websiteRepository.getAll();
    }

    public List<Website> getSubscribedWebsitesByUserId(final UserId userId) {
        return websiteRepository.findSubscribedWebsitesByUserId(userId);
    };

    public List<Website> getUnSubscribedWebsitesByUserId(final UserId userId) {
        return websiteRepository.findUnSubscribedWebsitesByUserId(userId);
    };

    public Website create(final Website website) {
        return websiteRepository.create(website);
    }

    /**
     * @throws WebsiteNotFoundException if the website does not exist
     * @throws WebsiteAlreadyExistsException if the website exist
     */
    public void update(final WebsiteId websiteId, final String url, final String description) {
        transactionManager.useTransaction(() -> {
            final Website websiteToUpdate =
                    websiteRepository.findById(websiteId)
                            .orElseThrow(() -> new WebsiteNotFoundException(websiteId));

            websiteRepository.update(
                    websiteToUpdate
                            .withUrl(url)
                            .withDescription(description)
            );
        });
    }

    public void tryUpdateSubscribedWebsites(final List<WebsiteId> websites, final UserId userId) {
        if (websites.size() > maxWebsitesPerUser) {
            throw new QuantityLimitExceededWebsitesPerUserException(userId);
        }

        websiteRepository.updateSubscribedWebsites(websites, userId);
    }

    /**
     * @throws WebsiteNotFoundException if the website does not exist
     */
    public void delete(final WebsiteId websiteId, final UserId creatorId) {
        websiteRepository.delete(websiteId, creatorId);
    }
}
