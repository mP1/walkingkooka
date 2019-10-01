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

package walkingkooka.convert;

import walkingkooka.Either;
import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A {@link Converter} which tries all collectors to satisfy a request.
 */
final class ConverterCollection implements Converter {

    /**
     * Factory that creates a {@link ConverterCollection} if more than one converter is given.
     * Providing zero will result in an {@link IllegalArgumentException}.
     */
    static Converter with(final List<Converter> converters) {
        Objects.requireNonNull(converters, "converters");

        final List<Converter> copy = Lists.immutable(converters);

        Converter result;
        final int count = copy.size();
        switch (count) {
            case 0:
                throw new IllegalArgumentException("Expected at least 1 converter but got 0");
            case 1:
                result = copy.get(0);
                break;
            default:
                result = new ConverterCollection(copy);
                break;
        }

        return result;
    }

    private ConverterCollection(final List<Converter> converters) {
        this.converters = converters;
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type,
                              final ConverterContext context) {
        return this.converterForType(value, type, context).isPresent();
    }

    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> type,
                                         final ConverterContext context) {
        final Optional<Converter> converter = this.converterForType(value, type, context);
        return converter.map(c -> c.convert(value, type, context))
                .orElse(this.failConversion(value, type));
    }

    private Optional<Converter> converterForType(final Object value,
                                                 final Class<?> type,
                                                 final ConverterContext context) {
        return this.converters.stream()
                .filter(c -> c.canConvert(value, type, context))
                .findFirst();
    }

    private final List<Converter> converters;

    @Override
    public String toString() {
        return this.converters.stream()
                .map(c -> c.toString())
                .collect(Collectors.joining(" | "));
    }
}
