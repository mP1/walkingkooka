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

import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * A {@link Converter} which formats a date/time type by calling its format method.
 */
abstract class StringDateTimeFormatterConverter<T> extends FixedTypeConverter<String> {

    StringDateTimeFormatterConverter(final DateTimeFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter");
        this.formatter = formatter;
    }

    @Override
    public final boolean canConvert(final Object value, final Class<?> type) {
        return this.sourceType().isInstance(value) &&
                this.targetType() == type;
    }

    abstract Class<T> sourceType();

    @Override
    final Class<String> targetType() {
        return String.class;
    }

    @Override
    final String convert1(final Object value, final Class<String> type) {
        return this.convert2(this.sourceType().cast(value));
    }

    abstract String convert2(final T value);

    final DateTimeFormatter formatter;
}
