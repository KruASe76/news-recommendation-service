package org.hsse.news.api.schemas.response.website;

import org.jetbrains.annotations.NotNull;

public record WebsiteResponse(
    @NotNull Long websiteId, @NotNull String url, @NotNull String description
) {
}
