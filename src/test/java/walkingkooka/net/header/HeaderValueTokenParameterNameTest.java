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


import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.naming.NameTestCase;
import walkingkooka.text.CharSequences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

final public class HeaderValueTokenParameterNameTest extends NameTestCase<HeaderValueTokenParameterName<?>> {

    @Test(expected = IllegalArgumentException.class)
    public void testControlCharacterFails() {
        HeaderValueTokenParameterName.with("parameter\u0001;");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpaceFails() {
        HeaderValueTokenParameterName.with("parameter ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTabFails() {
        HeaderValueTokenParameterName.with("parameter\t");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonAsciiFails() {
        HeaderValueTokenParameterName.with("parameter\u0100;");
    }

    @Test
    public void testValid() {
        this.createNameAndCheck("Custom");
    }

    @Test
    public void testConstantNameReturnsConstant() {
        assertSame(HeaderValueTokenParameterName.Q, HeaderValueTokenParameterName.with(HeaderValueTokenParameterName.Q.value()));
    }

    @Test
    public void testConstantNameCaseInsensitiveReturnsConstant() {
        final String differentCase = HeaderValueTokenParameterName.Q.value().toLowerCase();
        assertNotEquals(differentCase, HeaderValueTokenParameterName.Q.value());
        assertSame(HeaderValueTokenParameterName.Q, HeaderValueTokenParameterName.with(differentCase));
    }

    // parameterValue...........................................................................................

    @Test(expected = NullPointerException.class)
    public void testParameterValueNullFails() {
        HeaderValueTokenParameterName.Q.parameterValue(null);
    }

    @Test
    public void testParameterValueFloat() {
        this.parameterValueAndCheck(HeaderValueTokenParameterName.Q,
                "0.75",
                0.75f);
    }

    @Test
    public void testParameterValueString() {
        this.parameterValueAndCheck(Cast.to(HeaderValueTokenParameterName.with("xyz")),
                "abc",
                "abc");
    }

    private <T> void parameterValueAndCheck(final HeaderValueTokenParameterName<T> name,
                                            final String headerValue,
                                            final T value) {
        assertEquals(name + "=" + CharSequences.quoteIfNecessary(headerValue),
                value,
                name.parameterValue(headerValue));
    }

    // toString.................................................................................................

    @Test
    public void testToString() {
        final String name = "parameter123";
        assertEquals(name, HeaderValueTokenParameterName.with(name).toString());
    }

    @Override
    protected HeaderValueTokenParameterName createName(final String name) {
        return HeaderValueTokenParameterName.with(name);
    }

    @Override
    protected Class<HeaderValueTokenParameterName<?>> type() {
        return Cast.to(HeaderValueTokenParameterName.class);
    }
}
