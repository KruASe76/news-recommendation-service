package org.example.rssparser;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import org.example.article.Article;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import com.rometools.rome.io.XmlReader;
import org.example.article.ArticleTag;
import org.jsoup.Jsoup;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RssParser {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    public static List<Article> parse(URL url, ArticleTag articleTag) throws IOException, FeedException {
        final List<Article> articles = new ArrayList<>();
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-agent", "Mozilla/5.0");
        final XmlReader reader = new XmlReader(connection.getInputStream(), true, UTF_8.name());
        final SyndFeedInput syndFeedInput = new SyndFeedInput();
        syndFeedInput.setAllowDoctypes(true);
        final SyndFeed feed = syndFeedInput.build(reader);
        for (SyndEntry entry : feed.getEntries()) {
            final String name = entry.getTitle();
            final String description = Jsoup.parse(entry.getDescription().getValue()).text();
            final Instant date = entry.getPublishedDate().toInstant();
            final String link = entry.getLink();
            final Set<String> topics = getTopics(entry.getCategories());
            final String author = entry.getAuthor();
            articles.add(new Article(name, description, LocalDate.ofInstant(date, ZoneId.systemDefault()).format(DATE_FORMAT), link, topics, author, articleTag));
        }
        return articles;
    }

    private static Set<String> getTopics(List<SyndCategory> categoryList) {
        Set<String> categories = new HashSet<>();
        for (SyndCategory category : categoryList) {
            categories.add(Jsoup.parse(category.getName()).text());
        }
        return categories;
    }
}
