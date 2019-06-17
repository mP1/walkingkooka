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
import walkingkooka.Cast;
import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.map.Maps;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class EncodingParameterNameTest extends HeaderParameterNameTestCase<EncodingParameterName<?>,
        EncodingParameterName<?>> {

    @Test
    public void testControlCharacterFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            EncodingParameterName.with("parameter\u0001;");
        });
    }

    @Test
    public void testSpaceFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            EncodingParameterName.with("parameter ");
        });
    }

    @Test
    public void testTabFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            EncodingParameterName.with("parameter\t");
        });
    }

    @Test
    public void testNonAsciiFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            EncodingParameterName.with("parameter\u0100;");
        });
    }

    @Test
    public void testValid() {
        this.createNameAndCheck("Custom");
    }

    @Test
    public void testConstantNameReturnsConstant() {
        assertSame(EncodingParameterName.Q_FACTOR, EncodingParameterName.with(EncodingParameterName.Q_FACTOR.value()));
    }

    // parameter value......................................................................................

    @Test
    public void testParameterValueAbsent() {
        this.parameterValueAndCheckAbsent(EncodingParameterName.Q_FACTOR, Encoding.BR);
    }

    @Test
    public void testParameterValuePresent() {
        final EncodingParameterName<Float> parameter = EncodingParameterName.Q_FACTOR;
        final Encoding encoding = Encoding.nonWildcard("xyz", Maps.of(parameter, 0.75f));

        this.parameterValueAndCheckPresent(parameter, encoding, 0.75f);
    }

    @Test
    public void testParameterValuePresent2() {
        final EncodingParameterName<?> parameter = EncodingParameterName.with("abc");
        final String value = "parameter-value-xyz";
        final Encoding encoding = Encoding.nonWildcard("xyz", Maps.of(parameter, value));

        this.parameterValueAndCheckPresent(parameter, encoding, Cast.to(value));
    }

    // toValue...........................................................................................

    @Test
    public void testToValueQFactor() {
        this.toValueAndCheck(EncodingParameterName.Q_FACTOR,
                "0.75",
                0.75f);
    }

    @Test
    public void testToValueString() {
        this.toValueAndCheck(Cast.to(EncodingParameterName.with("xyz")),
                "abc",
                "abc");
    }

    @Override
    public EncodingParameterName<Object> createName(final String name) {
        return Cast.to(EncodingParameterName.with(name));
    }

    @Override
    public Class<EncodingParameterName<?>> type() {
        return Cast.to(EncodingParameterName.class);
    }
}
