package org.hsse.news.util;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public final class PasswordUtil {
    private static final HashFunction HASH_FUNCTION = Hashing.sha512();

    public static String hashPassword(final @NotNull String password) {
        return HASH_FUNCTION.hashString(password, StandardCharsets.UTF_8).toString();
    }

    private PasswordUtil() {}
}
