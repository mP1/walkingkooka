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
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.NameTestCase;
import walkingkooka.text.CharSequences;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

final public class HeaderParameterNameTest extends NameTestCase<HeaderParameterName<?>> {

    @Test(expected = IllegalArgumentException.class)
    public void testControlCharacterFails() {
        HeaderParameterName.with("parameter\u0001;");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpaceFails() {
        HeaderParameterName.with("parameter ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTabFails() {
        HeaderParameterName.with("parameter\t");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonAsciiFails() {
        HeaderParameterName.with("parameter\u0100;");
    }

    @Test
    public void testValid() {
        this.createNameAndCheck("Custom");
    }

    @Test
    public void testConstantNameReturnsConstant() {
        assertSame(HeaderParameterName.Q, HeaderParameterName.with(HeaderParameterName.Q.value()));
    }

    @Test
    public void testConstantNameCaseInsensitiveReturnsConstant() {
        final String differentCase = HeaderParameterName.Q.value().toLowerCase();
        assertNotEquals(differentCase, HeaderParameterName.Q.value());
        assertSame(HeaderParameterName.Q, HeaderParameterName.with(differentCase));
    }

    @Test(expected = NullPointerException.class)
    public void testParameterValueNullFails() {
        HeaderParameterName.Q.parameterValue(null);
    }

    @Test
    public void testParameterValueFloat() {
        this.parameterValueAndCheck(HeaderParameterName.Q,
                "0.75",
                Optional.of(0.75f));
    }

    @Test
    public void testParameterValueString() {
        this.parameterValueAndCheck(Cast.to(HeaderParameterName.with("xyz")),
                "abc",
                Optional.of("abc"));
    }

    @Test
    public void testParameterValueAbsent() {
        this.parameterValueAndCheck(Cast.to(HeaderParameterName.with("parameter-absent")),
                null,
                HeaderParameterName.VALUE_ABSENT);
    }

    private <T> void parameterValueAndCheck(final HeaderParameterName<T> name,
                                            final String headerValue,
                                            final Optional<T> value) {
        assertEquals(name + "=" + CharSequences.quoteIfNecessary(headerValue),
                value,
                name.parameterValue(Maps.one(name, headerValue)));
    }

    @Test
    public void testToString() {
        final String name = "parameter123";
        assertEquals(name, HeaderParameterName.with(name).toString());
    }

    @Override
    protected HeaderParameterName createName(final String name) {
        return HeaderParameterName.with(name);
    }

    @Override
    protected Class<HeaderParameterName<?>> type() {
        return Cast.to(HeaderParameterName.class);
    }
}
