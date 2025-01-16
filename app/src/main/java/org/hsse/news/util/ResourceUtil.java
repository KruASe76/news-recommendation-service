package org.hsse.news.util;

import java.io.File;
import java.util.Objects;

public class ResourceUtil { // NOPMD - Util method
  public static String getResource(final String resourcePath) {
    return
        new File(Objects.requireNonNull(
            ResourceUtil.class.getResource(resourcePath)
        ).getFile()).getAbsolutePath();
  }
}
