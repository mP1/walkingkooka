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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin interface with helpers to assist testing of {@link HeaderValueWithParameters} implementations.
 */
public interface HeaderValueWithParametersTesting<V extends HeaderValueWithParameters<N>,
        N extends HeaderParameterName<?>> extends HeaderValueTesting<V> {

    // setParameters ...........................................................................................

    @Test
    default void testSetParametersNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createHeaderValueWithParameters().setParameters(null);
        });
    }

    @Test
    default void testSetParametersSame() {
        final V headerValueWithParameters = this.createHeaderValueWithParameters();
        assertSame(headerValueWithParameters, headerValueWithParameters.setParameters(headerValueWithParameters.parameters()));
    }

    V createHeaderValueWithParameters();

    @Override
    default V createHeaderValue() {
        return this.createHeaderValueWithParameters();
    }
}
