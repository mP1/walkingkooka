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

/**
 * A {@link Converter} which only accepts a single source type and a single target type, with an offset which is
 * added to the date component.
 */
abstract class ConverterTemporal<S, D> extends Converter2 {

    ConverterTemporal(final long offset) {
        super();
        this.offset = offset;
    }

    @Override
    public final boolean canConvert(final Object value,
                                    final Class<?> type,
                                    final ConverterContext context) {
        return this.sourceType().isInstance(value) && this.isTargetType(type);
    }

    abstract boolean isTargetType(final Class<?> type);

    @Override
    final <T> Either<T, String> convert0(final Object value,
                                         final Class<T> type,
                                         final ConverterContext context) {
        return this.convert1(this.sourceType().cast(value),
                type,
                context);
    }

    abstract <T> Either<T, String> convert1(final S value,
                                            final Class<T> type,
                                            final ConverterContext context);

    final long offset;

    @Override
    public final String toString() {
        return this.sourceType().getSimpleName() + "->" + this.targetType().getSimpleName() + toStringOffset(this.offset);
    }

    abstract Class<S> sourceType();

    abstract Class<D> targetType();

    /**
     * Returns the {@link String} as a signed offset including a plus or minus when the value is non zero.
     */
    static String toStringOffset(final long offset) {
        return 0 == offset ?
                "" :
                toStringOffset0(offset);
    }

    private static String toStringOffset0(final long offset) {
        final StringBuilder b = new StringBuilder();
        b.append('(');

        if (offset > 0) {
            b.append('+');
        }
        b.append(offset);
        b.append(')');

        return b.toString();
    }
}
