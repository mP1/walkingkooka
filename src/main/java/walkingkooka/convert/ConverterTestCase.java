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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class ConverterTestCase<C extends Converter> extends ClassTestCase<C> {

    @Test
    public void testCheckNaming() {
        this.checkNaming(Converter.class);
    }

    protected abstract C createConverter();

    protected abstract ConverterContext createContext();

    protected void convertAndCheck(final Object value) {
        assertSame(value, this.convertAndCheck(value, value.getClass(), value));
    }

    protected Object convertAndCheck(final Object value, final Class<?> target) {
        final Object result = this.convertAndCheck(this.createConverter(), value, target, value);
        assertSame(result, value);
        return result;
    }

    protected Object convertAndCheck(final Object value, final Class<?> target, final Object expected) {
        return this.convertAndCheck(this.createConverter(), value, target, expected);
    }

    protected Object convertAndCheck(final Object value,
                                     final Class<?> target,
                                     final ConverterContext context,
                                     final Object expected) {
        return this.convertAndCheck(this.createConverter(), value, target, context, expected);
    }

    protected Object convertAndCheck(final Converter converter,
                                     final Object value,
                                     final Class<?> target,
                                     final Object expected) {
        return this.convertAndCheck(converter, value, target, this.createContext(), expected);
    }

    protected Object convertAndCheck(final Converter converter,
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

    protected void checkEquals(final String message, final Object expected, final Object actual) {
        if(expected instanceof Comparable && expected.getClass().isInstance(actual)) {
            final Comparable expectedComparable = Cast.to(expected);
            this.checkEquals0(message, Cast.to(expectedComparable), Cast.to(actual));
        } else {
            assertEquals(expected, actual, message);
        }
    }

    private <C extends Comparable<C>> void checkEquals0(final String message, final C expected, final C actual) {
       if(expected.compareTo(actual) != 0) {
                assertEquals(expected, actual, message);
            }
    }

    protected <T> void convertFails(final Object value, final Class<?> type) {
        this.convertFails(this.createConverter(), value, type);
    }

    protected <T> void convertFails(final Converter converter, final Object value, final Class<?> type) {
        this.convertFails(converter, value, type, this.createContext());
    }

    protected <T> void convertFails(final Converter converter,
                                    final Object value,
                                    final Class<?> type,
                                    final ConverterContext context) {
        try{
            final Object result = converter.convert(value, type, context);
            fail("Expected " + converter + " with " + CharSequences.quoteIfChars(value) + " to " + type.getName() + " to fail but got " + CharSequences.quoteIfChars(result));
        } catch (final ConversionException ignored) {
        }
    }

    protected Object convert(final Object value) {
        return this.convert(value, value.getClass());
    }

    protected Object convert(final Object value, final Class<?> type) {
        return this.createConverter().convert(value, type, this.createContext());
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
