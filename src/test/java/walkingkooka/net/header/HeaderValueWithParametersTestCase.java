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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class HeaderValueWithParametersTestCase<V extends HeaderValueWithParameters<N>,
        N extends HeaderParameterName<?>> extends HeaderValueTestCase<V>
        implements HeaderValueWithParametersTesting<V, N> {

    HeaderValueWithParametersTestCase() {
        super();
    }

    final void checkParameters(final HeaderValueWithParameters<N> headerValue,
                               final Map<N, ?> parameters) {
        assertEquals(parameters,
                headerValue.parameters(),
                () -> "parameters " + headerValue);
        assertThrows(UnsupportedOperationException.class, () -> {
            headerValue.parameters().put(this.parameterName(), null);
        });
    }

    /**
     * This parameter name should be unique and not used within other tests.
     * It is used to put a new parameter upon the parameter {@link Map} to verify it is readonly.
     */
    abstract N parameterName();
}
