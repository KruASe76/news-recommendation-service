package org.hsse.news.api.schemas.response.article;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ArticleListResponse(
        @JsonProperty("count") Integer count,
        @JsonProperty("articles") List<ArticleResponse> articles
        ) {
}
