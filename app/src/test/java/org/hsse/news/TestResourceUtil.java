package org.hsse.news;

import java.io.File;
import java.util.Objects;

public class TestResourceUtil { // NOPMD - Util method
  public static String getResource(final String resourcePath) { // NOPMD - Util method
    return
        new File(Objects.requireNonNull(
            TestResourceUtil.class.getResource(resourcePath)
        ).getFile()).getAbsolutePath();
  }
}
