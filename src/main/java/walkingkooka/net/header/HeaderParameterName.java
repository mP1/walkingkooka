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
import walkingkooka.text.CharacterConstant;

import java.util.Objects;
import java.util.Optional;

/**
 * Base class for any {@link HeaderName}.
 */
abstract class HeaderParameterName<V> extends HeaderName2<V> {

    /**
     * Private ctor to limit sub classing.
     */
    HeaderParameterName(final String name, final HeaderValueConverter<V> converter) {
        super(name);
        this.converter = converter;
    }

    /**
     * Parameter names ending with a <code>*</code> will use encoded text as their value, rather than text or quoted text.
     */
    final boolean isStarParameter() {
        return this.value().endsWith(STAR.string());
    }

    final static CharacterConstant STAR = CharacterConstant.with('*');

    /**
     * Accepts text and converts it into its value.
     */
    @Override
    public final V toValue(final String text) {
        Objects.requireNonNull(text, "text");

        return this.converter.parse(text, this);
    }

    /**
     * Validates the value and casts it to its correct type.
     */
    @Override
    public final V checkValue(final Object value) {
        return this.converter.check(value);
    }

    final HeaderValueConverter<V> converter;

    /**
     * Gets a value wrapped in an {@link Optional} in a type safe manner.
     */
    public Optional<V> parameterValue(final HeaderValueWithParameters<?> hasParameters) {
        Objects.requireNonNull(hasParameters, "hasParameters");
        return Optional.ofNullable(Cast.to(hasParameters.parameters().get(this)));
    }

    /**
     * Retrieves the value or throws a {@link HeaderValueException} if absent.
     */
    public V parameterValueOrFail(final HeaderValueWithParameters hasParameters) {
        final Optional<V> value = this.parameterValue(hasParameters);
        if (!value.isPresent()) {
            throw new HeaderValueException("Required value is absent for " + this);
        }
        return value.get();
    }
}
