package org.hsse.news.api.schemas.request.website;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public record WebsitePutRequest(
    @NotNull ArrayList<Integer> sitesId
) {
}
