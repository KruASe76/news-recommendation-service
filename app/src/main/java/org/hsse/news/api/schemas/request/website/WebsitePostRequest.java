package org.hsse.news.api.schemas.request.website;

import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

public record WebsitePostRequest(
       URI url, String description
) {
}
