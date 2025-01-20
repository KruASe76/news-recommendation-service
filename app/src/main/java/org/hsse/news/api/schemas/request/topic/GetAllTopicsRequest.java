package org.hsse.news.api.schemas.request.topic;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public record GetAllTopicsRequest(
    @NotNull ArrayList<Integer> topicsId
) {
}
