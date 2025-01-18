package org.hsse.news.database.util;

import org.hsse.news.util.JdbiProvider;
import org.jdbi.v3.core.Jdbi;

import java.util.function.Supplier;

public final class JdbiTransactionManager implements TransactionManager {
    private final Jdbi jdbi;

    public JdbiTransactionManager(final Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public JdbiTransactionManager() {
        this(JdbiProvider.get());
    }

    @Override
    public <R> R inTransaction(final Supplier<R> supplier) {
        return jdbi.inTransaction((handle) -> supplier.get());
    }

    @Override
    public void useTransaction(final Runnable runnable) {
        jdbi.useTransaction((handle) -> runnable.run());
    }
}
