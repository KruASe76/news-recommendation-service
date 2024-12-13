CREATE SEQUENCE IF NOT EXISTS topics_topic_id_seq;
CREATE SEQUENCE IF NOT EXISTS websites_website_id_seq;

CREATE TABLE IF NOT EXISTS users (
  user_id uuid NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
  email text NOT NULL,
  password text NOT NULL,
  username text NOT NULL
);

CREATE INDEX IF NOT EXISTS index_users_email ON users (email, password);

CREATE TABLE IF NOT EXISTS topics (
  topic_id bigint NOT NULL PRIMARY KEY DEFAULT nextval('topics_topic_id_seq'),
  name text NOT NULL,
  creator_id bigint REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS websites (
  website_id bigint NOT NULL PRIMARY KEY DEFAULT nextval('websites_website_id_seq'),
  url text NOT NULL,
  description text NOT NULL,
  creator_id bigint REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_topics (
  user_id bigint NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
  topic_id bigint NOT NULL REFERENCES topics (topic_id) ON DELETE CASCADE,
  PRIMARY KEY (user_id, topic_id)
);

CREATE INDEX IF NOT EXISTS index_user_topics_user_id ON user_topics (user_id);

CREATE TABLE IF NOT EXISTS user_websites (
  user_id bigint NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
  website_id bigint NOT NULL REFERENCES websites (website_id) ON DELETE CASCADE,
  PRIMARY KEY (user_id, website_id)
);

CREATE INDEX IF NOT EXISTS index_user_websites_user_id ON user_websites (user_id);

CREATE TABLE IF NOT EXISTS articles (
  article_id uuid NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
  title text NOT NULL,
  url text NOT NULL,
  created_at timestamp NOT NULL,
  topic_id bigint NOT NULL REFERENCES topics (topic_id) ON DELETE CASCADE,
  website_id bigint NOT NULL REFERENCES websites (website_id) ON DELETE CASCADE
);
