//package org.hsse.news.tracker;
//import ai.onnxruntime.OrtException;
//import com.rometools.rome.io.FeedException;
//import org.hsse.news.application.OnnxApplication;
//import org.hsse.news.database.article.ArticleService;
//import org.hsse.news.database.article.models.Article;
//import org.hsse.news.database.topic.TopicService;
//import org.hsse.news.database.topic.models.Topic;
//import org.hsse.news.database.website.WebsiteService;
//import org.hsse.news.database.website.models.Website;
//import org.hsse.news.database.website.models.WebsiteId;
//import org.hsse.news.parser.ParsedArticle;
//import org.hsse.news.parser.RssParser;
//
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledFuture;
//
//import static java.util.concurrent.TimeUnit.*;
//public class RssTracker {
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//    private final OnnxApplication onnxApplication;
//    private final TopicService topicService;
//    private final ArticleService articleService;
//    private final WebsiteService websiteService;
//    private static final Float MINIMUM_PERCENT = 20F;
//
//    public RssTracker(OnnxApplication onnxApplication, TopicService topicService, ArticleService articleService, WebsiteService websiteService) {
//        this.onnxApplication = onnxApplication;
//        this.topicService = topicService;
//        this.articleService = articleService;
//        this.websiteService = websiteService;
//    }
//
//    void start(long intervalSeconds){
//        Runnable runnable = () -> {
//            try {
//                addNewArticles();
//            } catch (FeedException | IOException | OrtException e) {
//                throw new RuntimeException(e);
//            }
//        };
//        ScheduledFuture<?> handle = scheduler.scheduleAtFixedRate(runnable, 10, intervalSeconds, SECONDS);
//
//    }
//
//    /*private List<Website> getAllWebsites(){
//        return websiteService.getAll();
//    }
//    */
//    /*private List<Topic> getAllTopics(){
//        return topicService.getAll();
//     }
//    */
//
//    private List<ParsedArticle> getWebsiteArticles(final String url) throws IOException, FeedException {
//        return RssParser.parse(new URL(url), url);
//    }
//
//    /*private void matchTopics(final ParsedArticle parsedArticle, final List<Topic> topicList, final WebsiteId websiteId) throws OrtException {
//        final List<String> nameOfTopics = new ArrayList<>();
//        for (Topic topic : topicList){
//            nameOfTopics.add(topic.name());
//        }
//        final Map<String,Float> mapOfTopics = onnxApplication.predict(parsedArticle.description(), nameOfTopics);
//        for (Topic topic : topicList){
//            if (mapOfTopics.get(topic.name()).compareTo(MINIMUM_PERCENT)>0){
//                articleService.create(new Article(null,parsedArticle.name(),parsedArticle.link(),Timestamp.from(parsedArticle.date()), topic.id(), websiteId));
//            }
//        }
//    }
//    */
//    private void addNewArticles() throws FeedException, IOException, OrtException {
//        final List<Website> websites = List.of();//getAllWebsites();
//        final List<Topic> topics = List.of();//getAllTopics()
//        for (Website website : websites){
//            List<ParsedArticle> parsedArticles = getWebsiteArticles("");//"" -> website.url()
//            for (ParsedArticle parsedArticle : parsedArticles){
//                //matchTopics(parsedArticle, topics, website.id());
//            }
//        }
//    }
//}
