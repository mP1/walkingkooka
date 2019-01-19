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

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Base class for testing a {@link HashCodeEqualsDefined} with mostly parameter checking tests.
 */
public interface HashCodeEqualsDefinedTesting<T extends HashCodeEqualsDefined> {

    @Test
    default void testHashCode() {
        final Object object = this.createObject();
        assertEquals("repeated calls to hashCode should return same value",
                object.hashCode(),
                object.hashCode());
    }

    @Test
    default void testEqualsNullIsFalse() {
        final T object = this.createObject();
        if (object.equals(null)) {
            assertEquals(null, object, null);
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

    abstract T createObject();

    default void checkEquals(final Object actual) {
        checkEquals(this.createObject(), actual);
    }

    default void checkEquals(final Object expected, final Object actual) {
        assertNotNull("Expected is null", expected);
        assertNotNull("Actual is null", actual);

        if (false == expected.equals(actual)) {
            assertEquals(null, expected, actual);
        }
        if (false == actual.equals(expected)) {
            assertEquals(null, expected, actual);
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
        assertNotNull("Expected is null", expected);
        assertNotNull("Actual is null", expected);

        if (expected.equals(actual)) {
            Assert.fail(expected + " should not be equal to " + actual);
        }
        if (actual.equals(expected)) {
            Assert.fail(actual + " should not be equal to " + expected);
        }
    }
}
