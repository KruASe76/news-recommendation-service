package org.hsse.news.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class AbstractId<T> {
    private final @NotNull T value;

    public AbstractId(final @NotNull T value) {
        this.value = value;
    }

    public @NotNull T value() {
        return value;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final AbstractId<?> abstractId = (AbstractId<?>) other;
        return value.equals(abstractId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
