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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * Base class for testing a {@link Throwable} with mostly parameter checking tests.
 */
abstract public class PublicThrowableTestCase<T extends Throwable> extends PublicClassTestCase<T> {

    // constants

    private final static String MESSAGE = "message";

    private final static Exception CAUSE = new Exception();

    // tests

    @Test
    public void testNoArgumentsConstructorItNotPublic() throws Exception {
        final Constructor<T> constructor = this.constructor();
        final int modifier = constructor.getModifiers();
        Assert.assertFalse(Modifier.isPublic(modifier));

        constructor.newInstance();
    }

    /**
     * Check that only four constructors are available and that they are public except for the no
     * args.
     */
    @Override
    @Test
    public void testAllConstructorsVisibility() throws Exception {
        this.checkConstructorIsProtected(this.constructor());
        this.checkConstructorIsPublic(this.constructor(String.class));
        this.checkConstructorIsPublic(this.constructor(String.class, Throwable.class));
    }

    @Test
    public void testCreateOnlyNullMessageFails() throws Exception {
        try {
            this.create(null);
            Assert.fail("Passing a null message must fail.");
        } catch (final InvocationTargetException expected) {
            assertEquals(NullPointerException.class, expected.getTargetException().getClass());
        }
    }

    @Test
    public void testCreateOnlyEmptyMessageFails() throws Exception {
        try {
            this.create("");
            Assert.fail("Passing a empty message must fail.");
        } catch (final InvocationTargetException expected) {
            assertEquals(IllegalArgumentException.class, expected.getTargetException().getClass());
        }
    }

    @Test
    public void testCreateOnlyMessage() throws Exception {
        final Constructor<T> constructor = this.constructor(String.class);
        final T instance = constructor.newInstance(MESSAGE);
        Assert.assertEquals("message", MESSAGE, instance.getMessage());
        Assert.assertNull("cause", instance.getCause());
    }

    @Test(expected = NoSuchMethodException.class)
    public void testShouldntHaveCauseOnlyConstructor() throws Exception {
        this.constructor(Throwable.class);
    }

    @Test
    public void testCreateNullMessageAndCauseExceptionFails() throws Exception {
        try {
            create(null, CAUSE);
            Assert.fail("Passing empty String must fail.");
        } catch (final InvocationTargetException expected) {
            assertEquals(NullPointerException.class, expected.getTargetException().getClass());
        }
    }

    @Test
    public void testCreateEmptyMessageAndNonNullCauseFails() throws Exception {
        try {
            create("", CAUSE);
            Assert.fail("Passing empty String must fail.");
        } catch (final InvocationTargetException expected) {
            assertEquals(IllegalArgumentException.class, expected.getTargetException().getClass());
        }
    }

    @Test
    public void testMessageAndNullCauseFails() throws Exception {
        this.create(MESSAGE, null);
    }

    @Test
    public void testMessageAndCause() throws Exception {
        this.create(MESSAGE, CAUSE);
    }

    private void create(final String message) throws Exception {
        final Constructor<T> constructor = this.constructor(String.class);
        final T instance = constructor.newInstance(message);
        Assert.assertEquals("message", message, instance.getMessage());
        assertSame("cause", null, instance.getCause());
    }

    private void create(final String message, final Throwable cause) throws Exception {
        final Constructor<T> constructor = this.constructor(String.class, Throwable.class);
        final T instance = constructor.newInstance(message, cause);
        Assert.assertEquals("message", message, instance.getMessage());
        assertSame("cause", cause, instance.getCause());
    }

    private Constructor<T> constructor(final Class<?>... parameters) throws Exception {
        final Constructor<T> constructor = this.type().getDeclaredConstructor(parameters);
        constructor.setAccessible(true);
        return constructor;
    }
}
