package org.hsse.news.util;

import org.hsse.news.database.util.TransactionManager;
import org.mockito.Mockito;

import java.util.function.Supplier;

public final class MockitoUtil {
    public static void setupInTransaction(final TransactionManager mock) {
        // noinspection unchecked
        Mockito.when(mock.inTransaction(Mockito.any(Supplier.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
    }

    public static void setupUseTransaction(final TransactionManager mock) {
        Mockito.doAnswer(invocation -> {
            invocation.getArgument(0, Runnable.class).run();
            return null;
        })
                .when(mock).useTransaction(Mockito.any(Runnable.class));
    }

    private MockitoUtil() {}
}
