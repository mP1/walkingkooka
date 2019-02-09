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
import walkingkooka.type.MemberVisibility;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * An interface with default methods which may be mixed into a test.
 */
public interface ThrowableTesting<T extends Throwable> extends Testing {

    @Test
    default void testClassVisibility() {
        final Class<?> type = this.type();

        assertEquals(MemberVisibility.PUBLIC,
                MemberVisibility.get(type),
                type.getName() + " visibility");
    }

    /**
     * The no args ctor must be protected.
     */
    @Test
    default void testNoDefaultArgumentProtected() throws Throwable {
        Arrays.stream(this.type().getDeclaredConstructors())
                .filter(c -> c.getParameterTypes().length == 0)
                .forEach(c -> assertEquals(MemberVisibility.PROTECTED, MemberVisibility.get(c),
                        () -> "ctor visibility incorrect " + c));
    }

    /**
     * Checks that all ctors are protected.
     */
    @Test
    default void testAllConstructorsVisibility() throws Exception {
        final Class<T> type = this.type();
        Arrays.stream(type.getDeclaredConstructors())
                .filter(c -> c.getParameterTypes().length != 0)
                .forEach(c ->
                        assertEquals(MemberVisibility.PUBLIC,
                                MemberVisibility.get(c),
                                () -> "ctor visibility incorrect " + c));
    }

    default void check(final Throwable throwable, final String message, final Throwable cause) {
        this.checkMessage(throwable, message);
        this.checkCause(throwable, cause);
    }

    default void checkMessage(final Throwable throwable, final String message) {
        assertEquals(message, throwable.getMessage(), "message");
    }

    default void checkCause(final Throwable throwable, final Throwable cause) {
        assertEquals(cause, throwable.getCause(), () -> "cause of " + throwable);
    }

    /**
     * The type being tested.
     */
    Class<T> type();
}
