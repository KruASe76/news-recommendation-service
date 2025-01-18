package org.hsse.news.api.authorizers;

import spark.route.HttpMethod;

public interface Authorizer {
    String USER_ID_ATTRIBUTE = "User-Id";

    void enableAuthorization(String path, HttpMethod method);
    void enableOptionalAuthorization(String path, HttpMethod method);
}
