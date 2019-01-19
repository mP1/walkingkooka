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
import walkingkooka.test.HashCodeEqualsDefined;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public abstract class HeaderParameterNameTestCase<N extends HeaderParameterName<?>, C extends Comparable<C> & HashCodeEqualsDefined>
        extends HeaderName2TestCase<N, C> {

    @Test(expected = NullPointerException.class)
    public final void testParameterValueNullFails() {
        this.createName().parameterValue(null);
    }

    @Test(expected = NullPointerException.class)
    public final void testParameterValueOrFailNullFails() {
        this.createName().parameterValueOrFail(null);
    }

    final <VV> void parameterValueAndCheckAbsent(final HeaderParameterName<VV> name,
                                                 final HeaderValueWithParameters<? extends HeaderParameterName<?>> hasParameters) {
        this.parameterValueAndCheck2(name, hasParameters, Optional.empty());
    }

    final <VV> void parameterValueAndCheckPresent(final HeaderParameterName<VV> name,
                                                  final HeaderValueWithParameters<? extends HeaderParameterName<?>> hasParameters,
                                                  final VV value) {
        this.parameterValueAndCheck2(name, hasParameters, Optional.of(value));
    }

    private <VV> void parameterValueAndCheck2(final HeaderParameterName<VV> name,
                                              final HeaderValueWithParameters<? extends HeaderParameterName<?>> hasParameters,
                                              final Optional<VV> value) {
        assertEquals("wrong parameter value " + name + " in " + hasParameters,
                value,
                name.parameterValue(hasParameters));
    }

    @Override
    protected final String nameText() {
        return "param-22";
    }

    @Override
    protected final String differentNameText() {
        return "different";
    }

    @Override
    protected final String nameTextLess() {
        return "param-1";
    }
}
