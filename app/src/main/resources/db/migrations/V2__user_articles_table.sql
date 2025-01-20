CREATE TABLE IF NOT EXISTS user_articles
(
    user_id  uuid   NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    article_id uuid NOT NULL REFERENCES articles (article_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, article_id)
);

CREATE INDEX IF NOT EXISTS index_user_articles_user_id ON user_articles (user_id);
