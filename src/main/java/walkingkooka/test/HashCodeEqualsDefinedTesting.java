/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package walkingkooka.test;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Base class for testing a {@link HashCodeEqualsDefined} with mostly parameter checking tests.
 */
public interface HashCodeEqualsDefinedTesting<T extends HashCodeEqualsDefined> extends Testing {

    @Test
    default void testHashCode() {
        final Object object = this.createObject();
        assertEquals(object.hashCode(),
                object.hashCode(),
                () -> "repeated calls to hashCode should return same value: " + object);
    }

    @Test
    default void testEqualsNullIsFalse() {
        final T object = this.createObject();
        if (object.equals(null)) {
            assertEquals(object, null);
        }
    }

    @Test
    default void testEqualsDifferentType() {
        checkNotEquals(this.createObject(), new Object());
    }

    @Test
    default void testEqualsSelf() {
        final Object object = this.createObject();
        checkEqualsAndHashCode(object, object);
    }

    @Test
    default void testEquals() {
        final Object object1 = this.createObject();
        final Object object2 = this.createObject();
        checkEqualsAndHashCode(object1, object2);
    }

    T createObject();

    default void checkEquals(final Object actual) {
        checkEquals(this.createObject(), actual);
    }

    default void checkEquals(final Object expected, final Object actual) {
        assertNotNull(expected, "Expected is null");
        assertNotNull(actual, "Actual is null");

        if (false == expected.equals(actual)) {
            assertEquals(expected, actual);
        }
        if (false == actual.equals(expected)) {
            assertEquals(expected, actual);
        }
    }

    default void checkEqualsAndHashCode(final Object actual) {
        checkEqualsAndHashCode(this.createObject(), actual);
    }

    default void checkEqualsAndHashCode(final Object expected, final Object actual) {
        checkEquals(expected, actual);

        final int expectedHashCode = expected.hashCode();
        final int actualHashCode = expected.hashCode();

        if (expectedHashCode != actualHashCode) {
            assertEquals("Hashcode not equal",
                    expectedHashCode + "=" + expected,
                    actualHashCode + "=" + actual);
        }
    }

    default void checkNotEquals(final Object actual) {
        checkNotEquals(this.createObject(), actual);
    }

    default void checkNotEquals(final Object expected, final Object actual) {
        Objects.requireNonNull(expected, "expected");
        Objects.requireNonNull(actual, "actual");

        assertNotEquals(expected, actual);
        assertNotEquals(actual, expected);
    }
}
