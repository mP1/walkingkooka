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

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;
import walkingkooka.naming.Name;
import walkingkooka.test.ToStringTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.util.systemproperty.SystemProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class HeaderValueConverterTestCase2<C extends HeaderValueConverter<T>, T> extends HeaderValueConverterTestCase<C>
        implements ToStringTesting<C> {

    HeaderValueConverterTestCase2() {
        super();
    }

    @Test
    public void testInvalidHeaderValueFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.converter().parse(this.invalidHeaderValue(), this.name());
        });
    }

    abstract String invalidHeaderValue();

    @Test
    public void testCheckNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.check(null);
        });
    }

    @Test
    public void testCheckWrongTypeFails() {
        this.checkTypeFails(this, "Header \"" + this.name() + ": " + this + "\" value type(" + this.getClass().getSimpleName() + ") is not a " + this.valueType());
    }

    @Test
    public void testCheckWrongTypeJavaLangFails() {
        this.checkTypeFails(new StringBuilder(), "Header \"" + this.name() + ": \" value type(StringBuilder) is not a " + this.valueType());
    }

    @Test
    public void testCheckWrongTypeFullyQualifiedTypeNameFails() {
        this.checkTypeFails(SystemProperty.FILE_SEPARATOR, "Header \"" + this.name() + ": " + SystemProperty.FILE_SEPARATOR + "\" value type(" + SystemProperty.class.getName() + ") is not a " + this.valueType());
    }

    private void checkTypeFails(final Object value, final String message) {
        try {
            this.check(value);
            fail(HeaderValueException.class.getName() + " was not thrown");
        } catch (final HeaderValueException expected) {
            expected.printStackTrace();

            assertEquals(message,
                    expected.getMessage(),
                    "message");
        }
    }

    @Test
    public void testCheck() {
        this.check(this.value());
    }

    @Test
    public final void testHttpHeaderNameCast() {
        final C converter = this.converter();

        final HttpHeaderName<?> header = HttpHeaderName.with("X-custom");
        if (this.value() instanceof String) {
            assertSame(header, converter.httpHeaderNameCast(header));
        } else {
            try {
                converter.httpHeaderNameCast(header);
                fail("httpHeaderNameCast() should have failed");
            } catch (final HttpHeaderNameTypeParameterHeaderException expected) {
            }
        }
    }

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.converter(), this.converterToString());
    }

    abstract String converterToString();

    abstract C converter();

    final T parse(final String value) {
        return this.converter().parse(value, this.name());
    }

    final void parseAndToTextAndCheck(final String text, final T value) {
        this.parseAndCheck(text, value);
        this.toTextAndCheck(value, text);
    }

    final void parseAndCheck(final String value, final T expected) {
        this.parseAndCheck(value, this.name(), expected);
    }

    abstract Name name();

    final void parseAndCheck(final String value, final Name name, final T expected) {
        this.parseAndCheck(this.converter(), value, name, expected);
    }

    final void parseAndCheck(final C converter, final String value, final T expected) {
        this.parseAndCheck(converter, value, this.name(), expected);
    }

    final void parseAndCheck(final C converter, final String value, final Name name, final T expected) {
        assertEquals(expected,
                converter.parse(value, name),
                () -> converter + " " + name + " of " + CharSequences.quoteIfChars(value));
    }

    final void check(final Object value) {
        this.converter().check(value, this.name());
    }

    final void toTextAndCheck(final T value, final String expected) {
        this.toTextAndCheck(value, this.name(), expected);
    }

    final void toTextAndCheck(final T value, final Name name, final String expected) {
        this.toTextAndCheck(this.converter(), value, name, expected);
    }

    final void toTextAndCheck(final C converter, final T value, final String expected) {
        this.toTextAndCheck(converter, value, this.name(), expected);
    }

    final void toTextAndCheck(final C converter, final T value, final Name name, final String expected) {
        assertEquals(expected,
                converter.toText(value, name),
                () -> converter + " " + name + " of " + CharSequences.quoteIfChars(value));
    }

    abstract T value();

    /**
     * The value type as a {@link String} which will appear in {@link HeaderValueException} messages.
     */
    abstract String valueType();

    final String valueType(final Class<?> type) {
        return type.getSimpleName();
    }

    final String listValueType(final Class<?> type) {
        return "List of " + this.valueType(type);
    }
}
