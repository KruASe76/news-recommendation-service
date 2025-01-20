package org.hsse.news.database.util;

import org.hsse.news.database.user.models.User;
import org.opentest4j.AssertionFailedError;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ComparisonUtil {
    public static void assertDeepEquals(final User expected, final User actual) {
        if (expected == actual) { // NOPMD - suppressed CompareObjectsWithEquals - intentional comparison
            return;
        }

        assertNotNull(expected, "expected User should not be null");
        assertNotNull(actual, "actual User should not be null");

        assertEquals(expected.id(), actual.id(), "ids should be equal");
        assertEquals(expected.email(), actual.email(), "emails should be equal");
        assertEquals(expected.password(), actual.password(), "passwords should be equal");
        assertEquals(expected.username(), actual.username(), "usernames should be equal");
    }

    public static void assertDeepNotEquals(final User expected, final User actual) {
        assertThrows(
                AssertionFailedError.class,
                () -> assertDeepEquals(expected, actual)
        );
    }

    public static void assertDeepEqualsMany(
            final Iterable<User> expectedIterable, final Iterable<User> actualIterable
    ) {
        if (expectedIterable == actualIterable) { // NOPMD - suppressed CompareObjectsWithEquals - intentional comparison
            return;
        }

        assertNotNull(expectedIterable, "expectedIterable should not be null");
        assertNotNull(actualIterable, "actualIterable should not be null");

        final Iterator<User> expectedIterator = expectedIterable.iterator();
        final Iterator<User> actualIterator = actualIterable.iterator();

        while (expectedIterator.hasNext() && actualIterator.hasNext()) {
            assertDeepEquals(expectedIterator.next(), actualIterator.next());
        }

        assertFalse(expectedIterator.hasNext(), "expectedIterator should be empty");
        assertFalse(actualIterator.hasNext(), "actualIterator should be empty");
    }

    private ComparisonUtil() {}
}
