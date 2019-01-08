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
import walkingkooka.Value;
import walkingkooka.collect.map.Maps;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Base class for all implementations of {@link HeaderValueWithParameters} in this package.
 */
abstract class HeaderValueWithParameters2<H extends HeaderValueWithParameters2<H, P, V>,
        P extends HeaderParameterName<?>,
        V> implements HeaderValueWithParameters<P>,
        Value<V> {

    /**
     * Package private to limit sub classing.
     */
    HeaderValueWithParameters2(final V value,
                               final Map<P, Object> parameters) {
        super();
        this.value = value;
        this.parameters = parameters;
    }

    // Value................................................................................................

    /**
     * Getter that returns the value.
     */
    @Override
    public final V value() {
        return this.value;
    }

    final V value;

    // parameters ...............................................................................................

    /**
     * Retrieves the parameters.
     */
    @Override
    public final Map<P, Object> parameters() {
        return this.parameters;
    }

    /**
     * Would be setter that returns an instance with the given parameters creating a new instance if necessary.
     */
    public final H setParameters(final Map<P, Object> parameters) {
        final Map<P, Object> copy = checkParameters(parameters);
        return this.parameters.equals(copy) ?
                Cast.to(this) :
                this.replace(copy);
    }

    final Map<P, Object> parameters;

    /**
     * While checking the parameters (name and value) makes a defensive copy.
     */
    private static <P extends HeaderParameterName<?>> Map<P, Object> checkParameters(final Map<P, Object> parameters) {
        Objects.requireNonNull(parameters, "parameters");

        final Map<P, Object> copy = Maps.sorted();
        for (Entry<P, Object> nameAndValue : parameters.entrySet()) {
            final P name = nameAndValue.getKey();
            copy.put(name,
                    name.checkValue(nameAndValue.getValue()));
        }
        return copy;
    }

    // replace................................................................................................................

    abstract H replace(final Map<P, Object> parameters);

    // qWeight .................................................................................................

    /**
     * Package private helper because not all sub classes need this.
     */
    final Optional<Float> qFactorWeight(final P parameter) {
        return Optional.ofNullable(Float.class.cast(this.parameters().get(parameter)));
    }

    // headerValue..................................................................................................

    /**
     * Reproduces this header as text.
     */
    @Override
    public final String toHeaderText() {
        return this.toHeaderTextValue() +
                this.parameters.entrySet()
                        .stream()
                        .map(this::toHeaderTextParameter)
                        .collect(Collectors.joining());
    }

    /**
     * Provides an opportunity for special casing how values are included in their parent header value.
     */
    abstract String toHeaderTextValue();

    /**
     * Helper that converts the parameter key/value into text using the name's {@link HeaderValueConverter}.
     */
    private String toHeaderTextParameter(final Entry<P, Object> nameAndValue) {
        final P name = nameAndValue.getKey();
        return this.toHeaderTextParameterSeparator() +
                name.value() +
                PARAMETER_NAME_VALUE_SEPARATOR.character() +
                name.converter.toText(Cast.to(nameAndValue.getValue()), name);
    }

    /**
     * Most header values return {@link #PARAMETER_SEPARATOR} + a space.
     */
    abstract String toHeaderTextParameterSeparator();

    /**
     * The default parameter separator and space.
     */
    final static String TO_HEADERTEXT_PARAMETER_SEPARATOR = PARAMETER_SEPARATOR.character() + " ";

    // Object................................................................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(this.hashCode0(this.value), this.parameters);
    }

    abstract int hashCode0(final V value);

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEquals(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEquals(final Object other);

    private boolean equals0(final HeaderValueWithParameters2<H, P, V> other) {
        return this.equals1(this.value, other.value) && //
                this.parameters.equals(other.parameters);
    }

    abstract boolean equals1(final V value, final V otherValue);

    @Override
    public final String toString() {
        return this.toHeaderText();
    }
}
