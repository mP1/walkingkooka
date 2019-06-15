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

/**
 * A {@link Converter} which only accepts a single source type and a single target type.
 */
abstract class FixedSourceTypeTargetTypeConverter<S, T> extends FixedTargetTypeConverter<T> {

    FixedSourceTypeTargetTypeConverter() {
    }

    @Override
    public final boolean canConvert(final Object value, final Class<?> type, final ConverterContext context) {
        return this.sourceType().isInstance(value) &&
                this.targetType() == type;
    }

    abstract Class<S> sourceType();

    @Override
    final T convert1(final Object value, final Class<T> type, final ConverterContext context) {
        return this.convert2(this.sourceType().cast(value));
    }

    abstract T convert2(final S value);

    @Override
    public final String toString() {

        String b = this.sourceType().getSimpleName() +
                "->" +
                this.targetType().getSimpleName() +
                this.toStringSuffix();
        return b;
    }

    abstract String toStringSuffix();
}
