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

package walkingkooka.convert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link Converter} that selects the target converter based on the type of the source value.
 */
final class SourceTypeRoutingConverter<T> extends FixedTypeConverter<T> {

    // only called by {@link SourceTypeRoutingConverterBuilder#build
    SourceTypeRoutingConverter(final Class<T> targetType, final List<SourceTypeRoutingConverterTypeAndConverter> routes) {
        this.targetType = targetType;
        this.routes = routes;
    }

    @Override
    Class<T> onlySupportedType() {
        return this.targetType;
    }

    private final Class<T> targetType;

    @Override
    T convert1(final Object value) {
        T result = null;

        final Class<?> type = value.getClass();
        for (SourceTypeRoutingConverterTypeAndConverter possible : this.routes) {
            if (possible.type.isAssignableFrom(type)) {
                result = possible.converter.convert(value, this.targetType);
            }
        }

        if (null == result) {
            this.failConversion(value);
        }

        return result;
    }

    private final List<SourceTypeRoutingConverterTypeAndConverter> routes;

    @Override
    public String toString() {
        return toString0(this.targetType, routes);
    }

    static String toString0(final Class<?> targetType, final List<SourceTypeRoutingConverterTypeAndConverter> converters) {
        return converters.stream()
                .map(c -> c.type.getName())
                .collect(Collectors.joining(" | ", "", "->" + targetType.getName()));
    }
}
