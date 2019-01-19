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

import org.junit.Test;
import walkingkooka.type.MemberVisibility;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Base class for testing a {@link Throwable} with mostly parameter checking tests.
 */
abstract public class PublicThrowableTestCase<T extends Throwable> extends ClassTestCase<T> {

    // constants

    private final static String MESSAGE = "message";

    private final static Exception CAUSE = new Exception();

    // tests

    @Test
    public void testNoArgumentsConstructorItNotPublic() throws Throwable {
        final Constructor<T> constructor = this.constructor();
        final int modifier = constructor.getModifiers();
        assertFalse(Modifier.isPublic(modifier));

        constructor.newInstance();
    }

    /**
     * Check that only four constructors are available and that they are public except for the no
     * args.
     */
    @Test
    public void testAllConstructorsVisibility() throws Throwable {
        this.checkConstructorVisibility(this.constructor(), MemberVisibility.PROTECTED);
        this.checkConstructorVisibility(this.constructor(String.class), MemberVisibility.PUBLIC);
        this.checkConstructorVisibility(this.constructor(String.class, Throwable.class), MemberVisibility.PUBLIC);
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
        this.check(this.constructor(String.class).newInstance(MESSAGE),
                MESSAGE,
                null);
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
            this.check(this.constructor(String.class).newInstance(message),
                    message,
                    null);
        } catch (final InvocationTargetException cause) {
            throw cause.getTargetException();
        }
    }

    private void callConstructorStringThrowable(final String message, final Throwable cause) throws Throwable {
        try {
            this.check(this.constructor(String.class, Throwable.class).newInstance(message, cause),
                    message,
                    cause);
        } catch (final InvocationTargetException targetException) {
            throw targetException.getTargetException();
        }
    }

    private Constructor<T> constructor(final Class<?>... parameters) throws Throwable {
        final Constructor<T> constructor = this.type().getDeclaredConstructor(parameters);
        constructor.setAccessible(true);
        return constructor;
    }

    protected void check(final Runnable throwable, final String message, final Throwable cause) {
        this.check(andCatch(throwable), message, cause);
    }

    protected Throwable andCatch(final Runnable run) {
        try {
            run.run();
        } catch (final Throwable cause) {
            return cause;
        }
        throw new AssertionError("Did not throw");
    }

    protected void check(final Throwable throwable, final String message, final Throwable cause) {
        this.checkMessage(throwable, message);
        this.checkCause(throwable, cause);
    }

    protected void checkMessage(final Throwable throwable, final String message) {
        assertEquals("message", message, throwable.getMessage());
    }

    protected void checkCause(final Throwable throwable, final Throwable cause) {
        assertEquals("cause of " + throwable, cause, throwable.getCause());
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
