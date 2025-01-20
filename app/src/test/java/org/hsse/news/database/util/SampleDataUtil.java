package org.hsse.news.database.util;

import org.hsse.news.database.user.models.User;
import org.hsse.news.database.user.models.UserId;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.UUID;

public final class SampleDataUtil {
    public static final User DEFAULT_USER =
            new User(
                    new UserId(UUID.fromString("11111111-1111-1111-1111-111111111111")),
                    "test@example.com",
                    "test_password",
                    "TestUser"
            );
    public static final User NEW_USER =
            new User(
                    new UserId(UUID.fromString("22222222-2222-2222-2222-222222222222")),
                    "new@example.com",
                    "new_password",
                    "NewUser"
            );

    @SuppressWarnings("SqlWithoutWhere")
    public static void clearDatabase(final Jdbi jdbi) {
        jdbi.useHandle(handle -> {
            final List<String> tables =
                    handle.createQuery(
                            "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'"
                            )
                            .mapTo(String.class)
                            .list();
            tables.remove("flyway_schema_history");

            // noinspection SqlSourceToSinkFlow
            tables.forEach(table -> handle.execute("DELETE FROM " + table));
        });
    }

    public static void prepareUsers(final Jdbi jdbi) {
        assert DEFAULT_USER.id() != null;
        jdbi.useTransaction(handle ->
                handle.createUpdate(
                        "INSERT INTO users (user_id, email, password, username) " +
                                "VALUES (:user_id, :email, :password, :username)"
                        )
                        .bind("user_id", DEFAULT_USER.id().value())
                        .bind("email", DEFAULT_USER.email())
                        .bind("password", DEFAULT_USER.password())
                        .bind("username", DEFAULT_USER.username())
                        .execute()
        );
    }

    private SampleDataUtil() {}
}
