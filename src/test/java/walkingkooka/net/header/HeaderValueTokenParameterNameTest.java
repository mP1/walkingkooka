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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

final public class HeaderValueTokenParameterNameTest extends HeaderParameterNameTestCase<HeaderValueTokenParameterName<?>> {

    @Test(expected = IllegalArgumentException.class)
    public void testWithControlCharacterFails() {
        HeaderValueTokenParameterName.with("parameter\u0001;");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithSpaceFails() {
        HeaderValueTokenParameterName.with("parameter ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTabFails() {
        HeaderValueTokenParameterName.with("parameter\t");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNonAsciiFails() {
        HeaderValueTokenParameterName.with("parameter\u0100;");
    }

    @Test
    public void testWith() {
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

    // toValue...........................................................................................

    @Test
    public void testToValueFloat() {
        this.toValueAndCheck(HeaderValueTokenParameterName.Q,
                "0.75",
                0.75f);
    }

    @Test
    public void testToValueString() {
        this.toValueAndCheck(Cast.to(HeaderValueTokenParameterName.with("xyz")),
                "abc",
                "abc");
    }

    // toString.................................................................................................

    @Test
    public void testToString() {
        final String name = "parameter123";
        assertEquals(name, HeaderValueTokenParameterName.with(name).toString());
    }

    @Override
    protected HeaderValueTokenParameterName<Object> createName(final String name) {
        return Cast.to(HeaderValueTokenParameterName.with(name));
    }

    @Override
    protected String nameText() {
        return "Custom";
    }

    @Override
    protected Class<HeaderValueTokenParameterName<?>> type() {
        return Cast.to(HeaderValueTokenParameterName.class);
    }
}
