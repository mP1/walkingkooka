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
 */

package walkingkooka.test;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Base class for testing a {@link HashCodeEqualsDefined} with mostly parameter checking tests.
 */
abstract public class HashCodeEqualsDefinedEqualityTestCase<T extends HashCodeEqualsDefined>
        extends TestCase {

    protected HashCodeEqualsDefinedEqualityTestCase() {
        super();
    }

    @Test
    public void testHashCode() {
        final Object object = this.createObject();
        assertEquals("repeated calls to hashCode should return same value",
                object.hashCode(),
                object.hashCode());
    }

    @Test
    final public void testNullIsFalse() {
        final T object = this.createObject();
        if (object.equals(null)) {
            assertEquals(null, object, null);
        }
    }

    @Test
    final public void testDifferentType() {
        checkNotEquals(this.createObject(), new Object());
    }

    @Test
    final public void testSelf() {
        final Object object = this.createObject();
        checkEqualsAndHashCode(object, object);
    }

    @Test
    public void testEquals() {
        final Object object1 = this.createObject();
        final Object object2 = this.createObject();
        checkEqualsAndHashCode(object1, object2);
    }

    abstract protected T createObject();

    protected void checkEquals(final Object actual) {
        checkEquals(this.createObject(), actual);
    }

    static public void checkEquals(final Object expected, final Object actual) {
        Assert.assertNotNull("Expected is null", expected);
        Assert.assertNotNull("Actual is null", actual);

        if (false == expected.equals(actual)) {
            assertEquals(null, expected, actual);
        }
        if (false == actual.equals(expected)) {
            assertEquals(null, expected, actual);
        }
    }

    protected void checkEqualsAndHashCode(final Object actual) {
        checkEqualsAndHashCode(this.createObject(), actual);
    }

    static public void checkEqualsAndHashCode(final Object expected, final Object actual) {
        checkEquals(expected, actual);

        final int expectedHashCode = expected.hashCode();
        final int actualHashCode = expected.hashCode();

        if (expectedHashCode != actualHashCode) {
            assertEquals("Hashcode not equal",
                    expectedHashCode + "=" + expected,
                    actualHashCode + "=" + actual);
        }
    }

    protected void checkNotEquals(final Object actual) {
        checkNotEquals(this.createObject(), actual);
    }

    static public void checkNotEquals(final Object expected, final Object actual) {
        Assert.assertNotNull("Expected is null", expected);
        Assert.assertNotNull("Actual is null", expected);

        if (expected.equals(actual)) {
            Assert.fail(expected + " should not be equal to " + actual);
        }
        if (actual.equals(expected)) {
            Assert.fail(actual + " should not be equal to " + expected);
        }
    }
}
