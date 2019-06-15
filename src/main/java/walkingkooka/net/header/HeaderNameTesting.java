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

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;
import walkingkooka.naming.NameTesting2;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixing interface to assist testing of {@link HeaderName} implementations.
 */
public interface HeaderNameTesting<N extends HeaderName<?>, C extends Comparable<C> & HashCodeEqualsDefined>
        extends NameTesting2<N, C> {

    // parameterValue...........................................................................................

    @Test
    default void testToValueNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createName().toValue(null);
        });
    }

    default <V> void toValueAndCheck(final HeaderName<V> name,
                                     final String headerValue,
                                     final V value) {
        assertEquals(value,
                name.toValue(headerValue),
                name + "=" + CharSequences.quoteIfNecessary(headerValue));
    }

    // toString.................................................................................

    @Test
    default void testToString() {
        final String nameText = this.nameText();
        this.toStringAndCheck(this.createName(nameText), nameText);
    }

    // checkValue...........................................................................................

    @Test
    default void testCheckValueNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.checkValue(null);
        });
    }

    @Test
    default void testCheckValueInvalidTypeFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.checkValue(this);
        });
    }

    default void checkValue(final Object value) {
        this.createName().checkValue(value);
    }

    default void checkValue(final HeaderName<?> name,
                            final Object value) {
        assertSame(value,
                name.checkValue(value),
                name + " didnt return correct value=" + CharSequences.quoteIfChars(value));
    }

    default N createName() {
        return this.createName(this.nameText());
    }

    String nameText();
}
