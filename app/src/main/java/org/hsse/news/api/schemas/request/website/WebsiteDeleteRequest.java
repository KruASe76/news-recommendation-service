package org.hsse.news.api.schemas.request.website;

import org.jetbrains.annotations.NotNull;

public record WebsiteDeleteRequest(
        @NotNull Long websiteId
) {
}
