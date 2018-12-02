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

import walkingkooka.Cast;

import java.util.Objects;
import java.util.Optional;

/**
 * Defines a few methods to retrieve header parameters.
 */
public interface HeaderParameterName<V> extends HeaderName<V>{

    /**
     * Gets a value wrapped in an {@link Optional} in a type safe manner.
     */
    default Optional<V> parameterValue(final HeaderValueWithParameters hasParameters) {
        Objects.requireNonNull(hasParameters, "hasParameters");
        return Optional.ofNullable(Cast.to(hasParameters.parameters().get(this)));
    }

    /**
     * Retrieves the value or throws a {@link HeaderValueException} if absent.
     */
    default V parameterValueOrFail(final HeaderValueWithParameters hasParameters) {
        final Optional<V> value = this.parameterValue(hasParameters);
        if (!value.isPresent()) {
            throw new HeaderValueException("Required value is absent for " + this);
        }
        return value.get();
    }
}
