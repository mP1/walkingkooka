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

import java.util.Objects;

/**
 * A {@link Converter} which forwards requests to another but substitutes the original type for a more derived.
 * This helps solve the problem of wishing to convert something to Number, but only having more derived converters,
 * eg Longa vailable.
 */
final class ForwardingConverter<S, T> extends FixedTargetTypeConverter<T> {

    /**
     * Factory that creates a new {@link ForwardingConverter}
     */
    static <S, T> ForwardingConverter<S, T> with(final Converter converter, final Class<S> sourceType, final Class<T> targetType) {
        Objects.requireNonNull(converter, "converter");
        Objects.requireNonNull(sourceType, "sourceType");
        Objects.requireNonNull(targetType, "targetType");

        return new ForwardingConverter<S, T>(converter, sourceType, targetType);
    }

    /**
     * Private ctor use factory
     */
    private ForwardingConverter(final Converter converter, final Class<S> sourceType, final Class<T> targetType) {
        this.converter = converter;
        this.sourceType = sourceType;
        this.targetType = targetType;
    }

    @Override
    public boolean canConvert(final Object value, final Class<?> type, final ConverterContext context) {
        return this.sourceType == type &&
                this.converter.canConvert(value, this.targetType, context);
    }

    private final Class<S> sourceType;

    @Override
    T convert1(final Object value, final Class<T> type, final ConverterContext context) {
        return this.converter.convert(value, this.targetType, context);
    }

    private final Converter converter;

    @Override
    Class<T> targetType() {
        return targetType;
    }

    private final Class<T> targetType;

    @Override
    public String toString() {
        final String toString = this.converter.toString();
        final int arrow = toString.indexOf("->");
        return -1 == arrow ?
               toString :
               toString.substring(0, arrow + 2) + this.sourceType.getSimpleName();
    }
}
