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
 * A {@link Converter} which only accepts a single source type and a single target type, with an offset which is
 * added to the date component.
 */
abstract class LocalDateOrLocalDateTimeNumberConverter<S, T> extends FixedSourceTypeConverter<S> {

    LocalDateOrLocalDateTimeNumberConverter(final long offset) {
        super();
        this.offset = offset;
    }

    final long offset;

    @Override
    final String toStringSuffix() {
        return this.targetType().getSimpleName() + toStringOffset(this.offset);
    }

    abstract Class<T> targetType();

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
