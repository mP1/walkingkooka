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
import walkingkooka.Cast;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class TokenHeaderValueParameterNameTest extends HeaderParameterNameTestCase<TokenHeaderValueParameterName<?>,
        TokenHeaderValueParameterName<?>> {

    @Test
    public void testWithControlCharacterFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            TokenHeaderValueParameterName.with("parameter\u0001;");
        });
    }

    @Test
    public void testWithSpaceFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            TokenHeaderValueParameterName.with("parameter ");
        });
    }

    @Test
    public void testWithTabFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            TokenHeaderValueParameterName.with("parameter\t");
        });
    }

    @Test
    public void testWithNonAsciiFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            TokenHeaderValueParameterName.with("parameter\u0100;");
        });
    }

    @Test
    public void testWith() {
        this.createNameAndCheck("Custom");
    }

    @Test
    public void testConstantNameReturnsConstant() {
        assertSame(TokenHeaderValueParameterName.Q, TokenHeaderValueParameterName.with(TokenHeaderValueParameterName.Q.value()));
    }

    @Test
    public void testConstantNameCaseInsensitiveReturnsConstant() {
        final String differentCase = TokenHeaderValueParameterName.Q.value().toLowerCase();
        assertNotEquals(differentCase, TokenHeaderValueParameterName.Q.value());
        assertSame(TokenHeaderValueParameterName.Q, TokenHeaderValueParameterName.with(differentCase));
    }

    // toValue...........................................................................................

    @Test
    public void testToValueFloat() {
        this.toValueAndCheck(TokenHeaderValueParameterName.Q,
                "0.75",
                0.75f);
    }

    @Test
    public void testToValueString() {
        this.toValueAndCheck(Cast.to(TokenHeaderValueParameterName.with("xyz")),
                "abc",
                "abc");
    }

    @Override
    public TokenHeaderValueParameterName<Object> createName(final String name) {
        return Cast.to(TokenHeaderValueParameterName.with(name));
    }

    @Override
    public Class<TokenHeaderValueParameterName<?>> type() {
        return Cast.to(TokenHeaderValueParameterName.class);
    }
}
