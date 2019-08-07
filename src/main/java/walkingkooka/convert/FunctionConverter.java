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

import java.util.Objects;
import java.util.function.Function;

/**
 * A {@link Converter} passes the given value to a {@link Function} such as a method handle to a static method which performs the conversion.
 */
final class FunctionConverter<S, D> extends FixedSourceTypeTargetTypeConverter<S, D> {

    static <S, D> FunctionConverter<S, D> with(final Class<S> sourceType,
                                               final Class<D> targetType,
                                               final Function<S, D> converter) {
        Objects.requireNonNull(sourceType, "sourceType");
        Objects.requireNonNull(targetType, "targetType");

        if (sourceType.equals(targetType)) {
            throw new IllegalArgumentException("Source and target types are the same" + sourceType.getName());
        }

        return new FunctionConverter<>(sourceType, targetType, converter);
    }

    /**
     * Private ctor use static factory.
     */
    private FunctionConverter(final Class<S> sourceType,
                              final Class<D> targetType,
                              final Function<S, D> converter) {
        super();
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.converter = converter;
    }

    @Override
    Class<S> sourceType() {
        return this.sourceType;
    }

    private final Class<S> sourceType;

    @Override
    Class<D> targetType() {
        return this.targetType;
    }

    private final Class<D> targetType;

    @Override
    D convert1(final S value, final ConverterContext context) {
        return this.converter.apply(value);
    }

    /**
     * A {@link Function} often a method reference that accepts the source value and returns the target, eg {@link Integer#parseInt(String)}.
     */
    private final Function<S, D> converter;

    @Override
    String toStringSuffix() {
        return "";
    }
}
