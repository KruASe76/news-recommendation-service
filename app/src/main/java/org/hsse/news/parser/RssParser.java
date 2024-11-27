package org.hsse.news.parser;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.rometools.rome.io.XmlReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class RssParser {
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMMM yyyy");
  private static final ZoneId ZONE_ID = ZoneId.systemDefault();

  private RssParser() {}

  public static List<ParsedArticle> parse(final URL url, final String articleTag)
      throws IOException, FeedException {
    final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    setConnection(connection);
    try (XmlReader reader = new XmlReader(connection.getInputStream(), true, UTF_8.name())) {
      final List<ParsedArticle> articles = parse(reader, articleTag);
      reader.close();
      return articles;
    }
  }

  public static List<ParsedArticle> parse(final File file, final String articleTag)
      throws IOException, FeedException {
    try (XmlReader reader = new XmlReader(file)) {
      final List<ParsedArticle> articles = parse(reader, articleTag);
      reader.close();
      return articles;
    }
  }

  private static List<ParsedArticle> parse(final XmlReader reader, final String articleTag)
      throws FeedException {
    final List<ParsedArticle> articles = new ArrayList<>();
    final SyndFeedInput syndFeedInput = new SyndFeedInput();
    syndFeedInput.setAllowDoctypes(true);
    final SyndFeed feed = syndFeedInput.build(reader);
    for (final SyndEntry entry : feed.getEntries()) {
      final String name = entry.getTitle();
      final String description = getDescription(entry);
      final Instant date = toInstantDate(entry.getPublishedDate());
      final String link = entry.getLink();
      final Set<String> topics = getTopics(entry.getCategories());
      final String author = entry.getAuthor();
      articles.add(
          new ParsedArticle(
              name,
              description,
              formatDate(LocalDate.ofInstant(date, ZONE_ID)),
              link,
              topics,
              author,
              articleTag));
    }
    return articles;
  }

  private static String formatDate(final LocalDate localDate) {
    return localDate.format(DATE_FORMAT);
  }

  private static void setConnection(final HttpURLConnection connection) {
    connection.setRequestProperty("User-agent", "Mozilla/5.0");
  }

  private static Set<String> getTopics(final List<SyndCategory> categoryList) {
    final Set<String> categories = new HashSet<>();
    for (final SyndCategory category : categoryList) {
      final String categoryName = category.getName();
      final Document document = parseText(categoryName);

      categories.add(parseDocument(document));
    }
    return categories;
  }

  private static Instant toInstantDate(final Date date) {
    return date.toInstant();
  }

  private static String getDescription(final SyndEntry entry) {
    final SyndContent content = entry.getDescription();
    return parseDescription(content);
  }

  private static Document parseText(final String text) {
    return Jsoup.parse(text);
  }

  private static String parseDocument(final Document document) {
    return document.text();
  }

  private static String parseDescription(final SyndContent content) {
    final String text = content.getValue();
    final Document document = parseText(text);
    return parseDocument(document);
  }
}