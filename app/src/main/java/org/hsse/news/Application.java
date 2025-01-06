package org.hsse.news;

import ai.onnxruntime.OrtException;
import java.io.IOException;
import org.hsse.news.api.SparkApplication;
import org.hsse.news.application.OnnxApplication;
import org.hsse.news.database.article.ArticleService;
import org.hsse.news.database.topic.TopicService;
import org.hsse.news.database.user.UserService;
import org.hsse.news.database.website.WebsiteService;
import org.hsse.news.util.SubApplication;

import java.util.List;

public final class Application {
    private final List<SubApplication> subApplications;

    private Application(final List<SubApplication> subApplications) {
        this.subApplications = subApplications;
    }

    private void start() {
        subApplications.forEach(SubApplication::run);
    }

    public static void main(final String[] args) throws IOException, OrtException {
        final ArticleService articleService = new ArticleService();
        final TopicService topicService = new TopicService();
        final UserService userService = new UserService();
        final WebsiteService websiteService = new WebsiteService();

        final SparkApplication sparkApplication =
                new SparkApplication(articleService, topicService, userService, websiteService);

        final Application application = new Application(
                List.of(
                        sparkApplication
                )
        );

        application.start();
    }
}
