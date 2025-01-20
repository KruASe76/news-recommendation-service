package org.hsse.news.api.schemas.request.website;

import java.net.URI;

public record WebsitePostRequest(
       URI url, String description
) {
}
