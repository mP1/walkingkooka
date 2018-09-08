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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Base class for testing a {@link Throwable} with mostly parameter checking tests.
 */
abstract public class PublicThrowableTestCase<T extends Throwable> extends PublicClassTestCase<T> {

    // constants

    private final static String MESSAGE = "message";

    private final static Exception CAUSE = new Exception();

    // tests

    @Test
    public void testNoArgumentsConstructorItNotPublic() throws Throwable {
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
    public void testAllConstructorsVisibility() throws Throwable {
        this.checkConstructorIsProtected(this.constructor());
        this.checkConstructorIsPublic(this.constructor(String.class));
        this.checkConstructorIsPublic(this.constructor(String.class, Throwable.class));
    }

    @Test(expected = NullPointerException.class)
    public void testCreateOnlyNullMessageFails() throws Throwable {
        this.callConstructorString(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOnlyEmptyMessageFails() throws Throwable {
        this.callConstructorString("");
    }

    @Test
    public void testCreateOnlyMessage() throws Throwable {
        final Constructor<T> constructor = this.constructor(String.class);
        final T instance = constructor.newInstance(MESSAGE);
        assertEquals("message", MESSAGE, instance.getMessage());
        Assert.assertNull("cause", instance.getCause());
    }

    @Test(expected = NoSuchMethodException.class)
    public void testShouldntHaveCauseOnlyConstructor() throws Throwable {
        this.constructor(Throwable.class);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateNullMessageAndCauseExceptionFails() throws Throwable {
        callConstructorStringThrowable(null, CAUSE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEmptyMessageAndNonNullCauseFails() throws Throwable {
        callConstructorStringThrowable("", CAUSE);
    }

    @Test(expected = NullPointerException.class)
    public void testMessageAndNullCauseFails() throws Throwable {
        this.callConstructorStringThrowable(MESSAGE, null);
    }

    @Test
    public void testMessageAndCause() throws Throwable {
        this.callConstructorStringThrowable(MESSAGE, CAUSE);
    }

    private void callConstructorString(final String message) throws Throwable {
        try {
            final Constructor<T> constructor = this.constructor(String.class);
            final T instance = constructor.newInstance(message);
            assertEquals("message", message, instance.getMessage());
            assertSame("cause", null, instance.getCause());
        } catch (final InvocationTargetException cause) {
            throw cause.getTargetException();
        }
    }

    private void callConstructorStringThrowable(final String message, final Throwable cause) throws Throwable {
        final Constructor<T> constructor = this.constructor(String.class, Throwable.class);
        final T instance = constructor.newInstance(message, cause);
        assertEquals("message", message, instance.getMessage());
        assertSame("cause", cause, instance.getCause());
    }

    private Constructor<T> constructor(final Class<?>... parameters) throws Throwable {
        final Constructor<T> constructor = this.type().getDeclaredConstructor(parameters);
        constructor.setAccessible(true);
        return constructor;
    }
}
