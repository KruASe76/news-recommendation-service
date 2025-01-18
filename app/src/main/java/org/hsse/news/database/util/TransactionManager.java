package org.hsse.news.database.util;

import java.util.function.Supplier;

public interface TransactionManager {
  <T> T inTransaction(Supplier<T> supplier);

  void useTransaction(Runnable runnable);
}
