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

package walkingkooka.net.http;

import org.junit.Test;
import walkingkooka.naming.Name;
import walkingkooka.test.PackagePrivateClassTestCase;
import walkingkooka.text.CharSequences;

import static org.junit.Assert.assertEquals;

public abstract class HttpHeaderValueConverterTestCase<C extends HttpHeaderValueConverter<T>, T> extends PackagePrivateClassTestCase<C> {

    HttpHeaderValueConverterTestCase() {
        super();
    }

    @Test(expected = HttpHeaderValueException.class)
    public void testInvalidHeaderValueFails() {
        this.converter().parse(this.invalidHeaderValue(), this.headerOrParameterName());
    }

    abstract String invalidHeaderValue();

    @Test
    public final void testToString() {
        assertEquals(this.converterToString(), this.converter().toString());
    }

    abstract String converterToString();

    final void parseAndCheck(final String value, final T expected) {
        this.parseAndCheck(value, this.headerOrParameterName(), expected);
    }

    abstract Name headerOrParameterName();

    final void parseAndCheck(final String value, final Name name, final T expected) {
        this.parseAndCheck(this.converter(), value, name, expected);
    }

    final void parseAndCheck(final C converter, final String value, final Name name, final T expected) {
        assertEquals(converter + " " + name + " of " + CharSequences.quoteIfChars(value), expected, converter.parse(value, name));
    }

    abstract C converter();
}
