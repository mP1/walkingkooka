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

/**
 * A {@link Converter} that matches source of the given type and target type requests and ALWAYS fails.
 */
final class FailConverter<S, T> extends FixedSourceTypeTargetTypeConverter<S, T> {

    static <S, T> FailConverter<S, T> with(final Class<S> source, final Class<T> target) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(target, "target");

        return new FailConverter<>(source, target);
    }

    private FailConverter(final Class<S> source, final Class<T> target) {
        this.source = source;
        this.target = target;
    }

    @Override
    Class<S> sourceType() {
        return this.source;
    }

    private final Class<S> source;

    @Override
    Class<T> targetType() {
        return this.target;
    }

    private final Class<T> target;

    @Override
    T convert1(final S value, final ConverterContext context) {
        return this.failConversion(value);
    }

    @Override
    String toStringSuffix() {
        return "";
    }
}
