/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.ThrowableTesting;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final public class OpenCheckerTest implements ClassTesting2<OpenChecker<Exception>>,
    ThrowableTesting,
    ToStringTesting<OpenChecker<Exception>> {

    // constants

    private final static String MESSAGE = "Not Open";

    private final static Function<String, Thrown> THROWABLE_FACTORY = s -> {
        throw new Thrown(s);
    };

    // tests

    @Test
    public void testNullMessageFails() {
        assertThrows(NullPointerException.class, () -> OpenChecker.with(null, THROWABLE_FACTORY));
    }

    @Test
    public void testWhitespaceOnlyMessageFails() {
        assertThrows(IllegalArgumentException.class, () -> OpenChecker.with("   ", THROWABLE_FACTORY));
    }

    @Test
    public void testNullThrowableFactoryFails() {
        assertThrows(NullPointerException.class, () -> OpenChecker.with(MESSAGE, (Function<String, Thrown>) null));
    }

    @Test
    public void testCreateAndCheck() throws Thrown {
        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE, THROWABLE_FACTORY);
        assertFalse(checker.isClosed());
        checker.check();
        checker.check();
    }

    @Test
    public void testCloseThenCheck() throws Thrown {
        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE, THROWABLE_FACTORY);
        checker.check();
        assertFalse(checker.close(), "was previous open");
        assertTrue(checker.isClosed());

        final Thrown expected = assertThrows(Thrown.class, checker::check);
        checkMessage(expected, MESSAGE);
    }

    @Test
    public void testCloseThenCheck2() throws Thrown {
        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE, THROWABLE_FACTORY);
        checker.check();
        assertFalse(checker.close(), "was previous open");
        assertTrue(checker.isClosed());

        final Thrown expected = assertThrows(Thrown.class, () -> checker.check(THROWABLE_FACTORY));
        checkMessage(expected, MESSAGE);
    }

    @Test
    public void testManyClosed() throws Thrown {
        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE, THROWABLE_FACTORY);
        assertFalse(checker.close(), "was previous open");
        assertTrue(checker.close(), "was already closed");
    }

    @Test
    public void testCloseOnce() throws Thrown {
        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE, THROWABLE_FACTORY);
        checker.closeOnce();
        assertTrue(checker.isClosed(), "should be closed");
    }

    @Test
    public void testCloseOnceTwice() throws Thrown {
        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE, THROWABLE_FACTORY);
        checker.closeOnce();

        assertThrows(Thrown.class, checker::closeOnce);
        assertTrue(checker.isClosed(), "should be closed");
    }

    @Test
    public void testOpenToString() throws Thrown {
        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE, THROWABLE_FACTORY);
        this.checkEquals("Open", checker.toString());
    }

    @Test
    public void testClosedToString() throws Thrown {
        final OpenChecker<Thrown> checker = OpenChecker.with(MESSAGE, THROWABLE_FACTORY);
        checker.close();
        this.checkEquals("Closed", checker.toString());
    }

    @Override
    public Class<OpenChecker<Exception>> type() {
        return Cast.to(OpenChecker.class);
    }

    static private class Thrown extends RuntimeException {

        private static final long serialVersionUID = 6911965362818219114L;

        private Thrown(final String message) {
            super(message);
        }
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
