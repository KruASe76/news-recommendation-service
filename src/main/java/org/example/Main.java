package org.example;

import org.example.article.Article;
import org.example.siteparsers.HabrParser;
import org.example.siteparsers.StackOverflowParser;
import org.example.siteparsers.TechCrunchParser;
import org.example.siteparsers.TechRadarParser;

import java.util.List;
import java.util.Stack;

public class Main {
    public static void main(String[] args){
        List<Article> articles = new StackOverflowParser().parseArticles();
        for(Article article : articles){
            System.out.println(article);
            System.out.println();
        }
    }
}