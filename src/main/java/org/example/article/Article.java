package org.example.article;

import java.util.Set;

public class Article {
    private final String name;
    private final String description;
    private final String date;
    private final String link;
    private final Set<String> topics;
    private final String author;
    private final ArticleTag articleTag;

    public Article(String name, String description, String date, String link, Set<String> topics, String author, ArticleTag articleTag) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.link = link;
        this.topics = topics;
        this.author = author;
        this.articleTag = articleTag;
    }

    public String getName() {
        return name;
    }

    public String getTopics() {
        return topics.toString();
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public ArticleTag getArticleTag() {
        return articleTag;
    }

    @Override
    public String toString() {
        StringBuilder article = new StringBuilder();
        article.append("Website: ").append(articleTag).append("\n").append("name: ").append(name)
                .append("\n").append("author: ").append(author).append("\n").append("topics: ").append(topics).append("\n")
                .append("date: ").append(date).append("\n").append("description: ").append(description).append("  ").append(link);
        return article.toString();
    }
}
