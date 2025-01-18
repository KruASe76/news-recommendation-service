package org.hsse.news.api.authorizers;

import org.hsse.news.database.user.models.UserId;
import spark.Request;

import java.util.Optional;

public interface Authorizer {
    UserId authorizeStrict(Request request);
    Optional<UserId> authorizeOptional(Request request);
}
