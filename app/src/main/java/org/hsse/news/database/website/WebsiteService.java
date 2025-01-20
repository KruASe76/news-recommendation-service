package org.hsse.news.database.website;

import com.typesafe.config.ConfigFactory;
import org.hsse.news.database.user.models.UserId;
import org.hsse.news.database.util.JdbiTransactionManager;
import org.hsse.news.database.util.TransactionManager;
import org.hsse.news.database.website.exceptions.QuantityLimitExceededWebsitesPerUserException;
import org.hsse.news.database.website.exceptions.WebsiteAlreadyExistsException;
import org.hsse.news.database.website.exceptions.WebsiteNotFoundException;
import org.hsse.news.database.website.models.Website;
import org.hsse.news.database.website.models.WebsiteId;
import org.hsse.news.database.website.repositories.JdbiWebsiteRepository;
import org.hsse.news.database.website.repositories.WebsiteRepository;

import java.util.List;
import java.util.Optional;

public final class WebsiteService {
    private static final int MAX_WEBSITES_PER_USER = ConfigFactory.load().getInt("website.max-custom-per-user");

    private final WebsiteRepository websiteRepository;
    private final TransactionManager transactionManager;

    public WebsiteService(
            final WebsiteRepository websiteRepository,
            final TransactionManager transactionManager
    ) {
        this.websiteRepository = websiteRepository;
        this.transactionManager = transactionManager;
    }

    public WebsiteService() {
        this(
                new JdbiWebsiteRepository(),
                new JdbiTransactionManager()
        );
    }

    public Optional<Website> findById(final WebsiteId websiteId) {
        return websiteRepository.findById(websiteId);
    }

    public List<Website> getAll() {
        return websiteRepository.getAll();
    }

    public List<Website> getSubscribedWebsitesByUserId(final UserId userId) {
        return websiteRepository.findSubscribedWebsitesByUserId(userId);
    }

    public List<Website> getUnSubscribedWebsitesByUserId(final UserId userId) {
        return websiteRepository.findUnSubscribedWebsitesByUserId(userId);
    }

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
        if (websites.size() > MAX_WEBSITES_PER_USER) {
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
