package org.example.article;

public enum ArticleTag {
    HABR("Habr"),
    TECHCRUNCH("TechCrunch"),
    STACKOVERFLOW("StackOverflow"),
    TECHRADAR("TechRadar"),
    ;

    private final String value;


    ArticleTag(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }


}
