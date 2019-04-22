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

package walkingkooka.convert;

import walkingkooka.Cast;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.text.CharSequences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public interface ConverterTesting<C extends Converter> extends ToStringTesting<C>,
        TypeNameTesting<C> {

    C createConverter();

    ConverterContext createContext();

    default void convertAndCheck(final Object value) {
        assertSame(value, this.convertAndCheck(value, value.getClass(), value));
    }

    default Object convertAndCheck(final Object value, final Class<?> target) {
        final Object result = this.convertAndCheck(this.createConverter(), value, target, value);
        assertSame(result, value);
        return result;
    }

    default Object convertAndCheck(final Object value, final Class<?> target, final Object expected) {
        return this.convertAndCheck(this.createConverter(), value, target, expected);
    }

    default Object convertAndCheck(final Object value,
                                   final Class<?> target,
                                   final ConverterContext context,
                                   final Object expected) {
        return this.convertAndCheck(this.createConverter(), value, target, context, expected);
    }

    default Object convertAndCheck(final Converter converter,
                                   final Object value,
                                   final Class<?> target,
                                   final Object expected) {
        return this.convertAndCheck(converter, value, target, this.createContext(), expected);
    }

    default Object convertAndCheck(final Converter converter,
                                   final Object value,
                                   final Class<?> target,
                                   final ConverterContext context,
                                   final Object expected) {
        assertTrue(converter.canConvert(value, target, context),
                converter + " can convert(" + value.getClass().getName() + "," + target.getName() + ") returned false for " + value);

        final Object result = converter.convert(value, target, context);
        checkEquals("Failed to convert " + value.getClass().getName() + "=" + value + " to " + target.getName(), expected, result);

        return result;
    }

    default void checkEquals(final String message, final Object expected, final Object actual) {
        if (expected instanceof Comparable && expected.getClass().isInstance(actual)) {
            final Comparable expectedComparable = Cast.to(expected);
            checkEquals0(message, Cast.to(expectedComparable), Cast.to(actual));
        } else {
            assertEquals(expected, actual, message);
        }
    }

    static <C extends Comparable<C>> void checkEquals0(final String message, final C expected, final C actual) {
        if (expected.compareTo(actual) != 0) {
            assertEquals(expected, actual, message);
        }
    }

    default <T> void convertFails(final Object value, final Class<?> type) {
        this.convertFails(this.createConverter(), value, type);
    }

    default <T> void convertFails(final Converter converter, final Object value, final Class<?> type) {
        this.convertFails(converter, value, type, this.createContext());
    }

    default <T> void convertFails(final Converter converter,
                                  final Object value,
                                  final Class<?> type,
                                  final ConverterContext context) {
        try {
            final Object result = converter.convert(value, type, context);
            fail("Expected " + converter + " with " + CharSequences.quoteIfChars(value) + " to " + type.getName() + " to fail but got " + CharSequences.quoteIfChars(result));
        } catch (final ConversionException ignored) {
        }
    }

    default Object convert(final Object value) {
        return this.convert(value, value.getClass());
    }

    default Object convert(final Object value, final Class<?> type) {
        return this.createConverter().convert(value, type, this.createContext());
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return Converter.class.getSimpleName();
    }
}
