package org.hsse.news.util;

import org.jdbi.v3.core.Jdbi;

public final class JdbiProvider {
    private static Jdbi jdbiInstance;

    private JdbiProvider() {}

    public static Jdbi get() {
        if (jdbiInstance == null) {
            throw new IllegalStateException("Jdbi instance has not been initialized");
        }

        return jdbiInstance;
    }

    public static void initialize(final Jdbi jdbi) {
        jdbiInstance = jdbi;
    }
}
