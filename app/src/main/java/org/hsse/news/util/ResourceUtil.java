package org.hsse.news.util;

import java.io.File;
import java.util.Objects;

public class ResourceUtil {
  public static String getResource(String resourcePath) {
    return
        new File(Objects.requireNonNull(
            ResourceUtil.class.getResource(resourcePath)
        ).getFile()).getAbsolutePath();
  }
}
