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

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public abstract class HeaderParameterNameTestCase<N extends HeaderParameterName<?>> extends HeaderNameTestCase<N> {

    @Test(expected = NullPointerException.class)
    public final void testParameterValueNullFails() {
        this.createName().parameterValue(null);
    }

    @Test(expected = NullPointerException.class)
    public final void testParameterValueOrFailNullFails() {
        this.createName().parameterValueOrFail(null);
    }

    protected void parameterValueAndCheckAbsent(final N name,
                                                final HeaderValueWithParameters<N> hasParameters) {
        this.parameterValueAndCheck(name, hasParameters, Optional.empty());
    }

    protected <T> void parameterValueAndCheckPresent(final N name,
                                                     final HeaderValueWithParameters<N> hasParameters,
                                                     final T value) {
        this.parameterValueAndCheck(name, hasParameters, Optional.of(value));
    }

    protected <T> void parameterValueAndCheck(final N name,
                                              final HeaderValueWithParameters<N> hasParameters,
                                              final Optional<T> value) {
        assertEquals("wrong parameter value " + name + " in " + hasParameters,
                value,
                name.parameterValue(hasParameters));
    }
}
