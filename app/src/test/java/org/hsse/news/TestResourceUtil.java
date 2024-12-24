package org.hsse.news;

import java.io.File;
import java.util.Objects;

public class TestResourceUtil {
  public static String getResource(String resourcePath) {
    return
        new File(Objects.requireNonNull(
            TestResourceUtil.class.getResource(resourcePath)
        ).getFile()).getAbsolutePath();
  }
}
