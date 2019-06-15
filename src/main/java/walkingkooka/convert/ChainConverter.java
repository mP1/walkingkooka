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
 * A {@link Converter} that chains the output from the first converter to a second.
 */
final class ChainConverter implements Converter {

    /**
     * Creates a new {@link ChainConverter}
     */
    static ChainConverter with(final Converter first,
                               final Class<?> intermediateTargetType,
                               final Converter last) {
        Objects.requireNonNull(first, "first");
        Objects.requireNonNull(intermediateTargetType, "intermediateTargetType");
        Objects.requireNonNull(last, "last");

        return new ChainConverter(first, intermediateTargetType, last);
    }

    /**
     * Private ctor use factory
     */
    private ChainConverter(Converter first, Class<?> intermediateTargetType, Converter last) {
        this.first = first;
        this.intermediateTargetType = intermediateTargetType;
        this.last = last;
    }

    @Override
    public boolean canConvert(final Object value, final Class<?> type, final ConverterContext context) {
        return this.first.canConvert(value, this.intermediateTargetType, context);
    }

    @Override
    public <T> T convert(final Object value, final Class<T> type, final ConverterContext context) {
        return this.last.convert(this.first.convert(value, this.intermediateTargetType, context),
                type,
                context);
    }

    private final Converter first;
    private final Class<?> intermediateTargetType;
    private final Converter last;

    @Override
    public String toString() {
        return this.first + "->" + this.last;
    }
}
