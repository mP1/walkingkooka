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

package walkingkooka;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import walkingkooka.test.Fake;
import walkingkooka.test.Testing;
import walkingkooka.text.CharSequences;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Mixing that may be used to test toString implementations.
 */
public interface ToStringTesting<T> extends Testing {

    @Test
    default void testCheckToStringOverridden() {
        final Class<T> type = this.type();

        if (false == Fake.class.isAssignableFrom(type)) {
            boolean notOverridden = true;

            try {
                final Method method = type.getMethod("toString");
                if (method.getDeclaringClass() != Object.class) {
                    notOverridden = false;
                }
            } catch (final NoSuchMethodException cause) {
                // nop
            }

            if (notOverridden) {
                Assertions.fail(type.getName() + " did not override Object.toString");
            }
        }
    }

    Class<T> type();

    default void toStringAndCheck(final Object object, final String expected) {
        assertEquals(expected, object.toString());
    }

    default void toStringContainsCheck(final Object object, final String... components) {
        final String toString = object.toString();

        String s = toString;

        for (final String c : components) {
            final int foundIndex = s.indexOf(c);
            assertNotEquals(-1, foundIndex, () -> "Cannot find " + CharSequences.quote(c) + " within " + CharSequences.quote(toString));
            s = s.substring(0, foundIndex) + s.substring(foundIndex + c.length());
        }
    }
}
