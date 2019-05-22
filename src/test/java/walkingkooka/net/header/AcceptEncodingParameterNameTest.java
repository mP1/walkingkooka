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
import walkingkooka.collect.map.Maps;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class AcceptEncodingParameterNameTest extends HeaderParameterNameTestCase<AcceptEncodingParameterName<?>,
        AcceptEncodingParameterName<?>> {

    @Test
    public void testControlCharacterFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            AcceptEncodingParameterName.with("parameter\u0001;");
        });
    }

    @Test
    public void testSpaceFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            AcceptEncodingParameterName.with("parameter ");
        });
    }

    @Test
    public void testTabFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            AcceptEncodingParameterName.with("parameter\t");
        });
    }

    @Test
    public void testNonAsciiFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            AcceptEncodingParameterName.with("parameter\u0100;");
        });
    }

    @Test
    public void testValid() {
        this.createNameAndCheck("Custom");
    }

    @Test
    public void testConstantNameReturnsConstant() {
        assertSame(AcceptEncodingParameterName.Q_FACTOR, AcceptEncodingParameterName.with(AcceptEncodingParameterName.Q_FACTOR.value()));
    }

    // parameter value......................................................................................

    @Test
    public void testParameterValueAbsent() {
        this.parameterValueAndCheckAbsent(AcceptEncodingParameterName.Q_FACTOR, AcceptEncoding.BR);
    }

    @Test
    public void testParameterValuePresent() {
        final AcceptEncodingParameterName<Float> parameter = AcceptEncodingParameterName.Q_FACTOR;
        final AcceptEncoding acceptEncoding = AcceptEncoding.nonWildcard("xyz", Maps.of(parameter, 0.75f));

        this.parameterValueAndCheckPresent(parameter, acceptEncoding, 0.75f);
    }

    @Test
    public void testParameterValuePresent2() {
        final AcceptEncodingParameterName<?> parameter = AcceptEncodingParameterName.with("abc");
        final String value = "parameter-value-xyz";
        final AcceptEncoding acceptEncoding = AcceptEncoding.nonWildcard("xyz", Maps.of(parameter, value));

        this.parameterValueAndCheckPresent(parameter, acceptEncoding, Cast.to(value));
    }

    // toValue...........................................................................................

    @Test
    public void testToValueQFactor() {
        this.toValueAndCheck(AcceptEncodingParameterName.Q_FACTOR,
                "0.75",
                0.75f);
    }

    @Test
    public void testToValueString() {
        this.toValueAndCheck(Cast.to(AcceptEncodingParameterName.with("xyz")),
                "abc",
                "abc");
    }

    @Override
    public AcceptEncodingParameterName<Object> createName(final String name) {
        return Cast.to(AcceptEncodingParameterName.with(name));
    }

    @Override
    public Class<AcceptEncodingParameterName<?>> type() {
        return Cast.to(AcceptEncodingParameterName.class);
    }
}
