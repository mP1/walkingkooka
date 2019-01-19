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

package walkingkooka.util;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

final public class OpenCheckerTest extends ClassTestCase<OpenChecker<Exception>> {
    // constants

    private final static String MESSAGE = "Not Open";

    private final static Function<String, Thrown> THROWABLE_FACTORY = new Function<String, Thrown>() {
        @Override
        public Thrown apply(final String s) {
            throw new Thrown(s);
        }
    };

    // tests

    @Test
    public void testNullMessageFails() {
        this.withFails(null, THROWABLE_FACTORY);
    }

    @Test
    public void testWhitespaceOnlyMessageFails() {
        this.withFails("   ", THROWABLE_FACTORY);
    }

    @Test
    public void testNullthrowableFactoryFails() {
        this.withFails(MESSAGE, null);
    }

    private void withFails(final String message, final Function<String, Thrown> throwableFactory) {
        try {
            OpenChecker.with(message, throwableFactory);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testCreateAndCheck() throws Thrown {
        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE, THROWABLE_FACTORY);
        assertFalse(checker.isClosed());
        checker.check();
        checker.check();
    }

    // @Test public void testCreateAndCheck2() throws NeverError
    //    {
    //        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE,
    //                THROWABLE_FACTORY);
    //        assertFalse(checker.isClosed());
    //        checker.check(ThrowableFactories.<NeverError>fake());
    //        checker.check(ThrowableFactories.<NeverError>fake());
    //    }

    @Test
    public void testCloseThenCheck() throws Thrown {
        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE, THROWABLE_FACTORY);
        checker.check();
        assertFalse("was previous open", checker.close());
        assertTrue(checker.isClosed());

        try {
            checker.check();
            Assert.fail();
        } catch (final Thrown expected) {
            assertEquals("message", MESSAGE, expected.getMessage());
        }
    }

    @Test
    public void testCloseThenCheck2() throws Thrown {
        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE, THROWABLE_FACTORY);
        checker.check();
        assertFalse("was previous open", checker.close());
        assertTrue(checker.isClosed());

        try {
            checker.check(THROWABLE_FACTORY);
            Assert.fail();
        } catch (final Thrown expected) {
            assertEquals("message", MESSAGE, expected.getMessage());
        }
    }

    @Test
    public void testManyClosed() throws Thrown {
        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE, THROWABLE_FACTORY);
        assertFalse("was previous open", checker.close());
        assertTrue("was already closed", checker.close());
    }

    @Test
    public void testCloseOnce() throws Thrown {
        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE, THROWABLE_FACTORY);
        checker.closeOnce();
        assertTrue("should be closed", checker.isClosed());
    }

    @Test
    public void testCloseOnceTwice() throws Thrown {
        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE, THROWABLE_FACTORY);
        checker.closeOnce();
        try {
            checker.closeOnce();
            Assert.fail();
        } catch (final Thrown expected) {
        }
        assertTrue("should be closed", checker.isClosed());
    }

    @Test
    public void testOpenToString() throws Thrown {
        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE, THROWABLE_FACTORY);
        assertEquals("Open", checker.toString());
    }

    @Test
    public void testClosedToString() throws Thrown {
        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE, THROWABLE_FACTORY);
        checker.close();
        assertEquals("Closed", checker.toString());
    }

    @Override
    protected Class<OpenChecker<Exception>> type() {
        return Cast.to(OpenChecker.class);
    }

    static private class Thrown extends RuntimeException {

        private static final long serialVersionUID = 6911965362818219114L;

        private Thrown(final String message) {
            super(message);
        }
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
