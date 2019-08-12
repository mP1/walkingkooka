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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A {@link Converter} which tries all collectors catching all {@link ConversionException} until success. If none
 * succeed the last {@link ConversionException} will be thrown.
 */
final class TryConverter implements Converter {

    static Converter with(final List<Converter> converters) {
        Objects.requireNonNull(converters, "converters");

        final List<Converter> copy = converters.stream()
                .flatMap(TryConverter::flatMap)
                .collect(Collectors.toList());

        Converter result;
        switch (copy.size()) {
            case 0:
                throw new IllegalArgumentException("Expected at least 1 converter but got 0");
            case 1:
                result = copy.get(0);
                break;
            default:
                result = new TryConverter(copy);
                break;
        }

        return result;
    }

    private static Stream<Converter> flatMap(final Converter converter) {
        return converter instanceof TryConverter ?
                TryConverter.class.cast(converter).stream() :
                Stream.of(converter);
    }

    private TryConverter(final List<Converter> converters) {
        super();
        this.converters = converters;
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type,
                              final ConverterContext context) {
        return this.converters.stream()
                .anyMatch(c -> c.canConvert(value, type, context));
    }

    @Override
    public <T> T convert(final Object value, final Class<T> type, final ConverterContext context) {
        T converted = null;
        ConversionException last = null;

        for (Converter converter : this.converters) {
            try {
                converted = converter.convert(value, type, context);
                break;
            } catch (final ConversionException next) {
                last = next;
            }
        }
        if (null == converted) {
            throw last;
        }

        return converted;
    }

    private Stream<Converter> stream() {
        return this.converters.stream();
    }

    final List<Converter> converters;

    @Override
    public String toString() {
        return this.converters.stream()
                .map(c -> c.toString())
                .collect(Collectors.joining(", "));
    }
}
