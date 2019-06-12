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

public abstract class HeaderValueHandlerTestCase2<C extends HeaderValueHandler<T>, T> extends HeaderValueHandlerTestCase<C>
        implements ToStringTesting<C> {

    HeaderValueHandlerTestCase2() {
        super();
    }

    @Test
    public void testInvalidHeaderValueFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.handler().parse(this.invalidHeaderValue(), this.name());
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
        final C handler = this.handler();

        final HttpHeaderName<?> header = HttpHeaderName.with("X-custom");
        if (this.value() instanceof String) {
            assertSame(header, handler.httpHeaderNameCast(header));
        } else {
            try {
                handler.httpHeaderNameCast(header);
                fail("httpHeaderNameCast() should have failed");
            } catch (final HttpHeaderNameTypeParameterHeaderException expected) {
            }
        }
    }

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.handler(), this.handlerToString());
    }

    abstract String handlerToString();

    abstract C handler();

    final T parse(final String value) {
        return this.handler().parse(value, this.name());
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
        this.parseAndCheck(this.handler(), value, name, expected);
    }

    final void parseAndCheck(final C handler, final String value, final T expected) {
        this.parseAndCheck(handler, value, this.name(), expected);
    }

    final void parseAndCheck(final C handler, final String value, final Name name, final T expected) {
        assertEquals(expected,
                handler.parse(value, name),
                () -> handler + " " + name + " of " + CharSequences.quoteIfChars(value));
    }

    final void check(final Object value) {
        this.handler().check(value, this.name());
    }

    final void toTextAndCheck(final T value, final String expected) {
        this.toTextAndCheck(value, this.name(), expected);
    }

    final void toTextAndCheck(final T value, final Name name, final String expected) {
        this.toTextAndCheck(this.handler(), value, name, expected);
    }

    final void toTextAndCheck(final C handler, final T value, final String expected) {
        this.toTextAndCheck(handler, value, this.name(), expected);
    }

    final void toTextAndCheck(final C handler, final T value, final Name name, final String expected) {
        assertEquals(expected,
                handler.toText(value, name),
                () -> handler + " " + name + " of " + CharSequences.quoteIfChars(value));
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
