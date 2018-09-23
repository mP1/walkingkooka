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

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * A {@link Converter} which uses a {@link DateTimeFormatter}.
 */
abstract class DateTimeFormatterConverter<S, T> extends FixedSourceTypeTargetTypeConverter<S, T> {

    DateTimeFormatterConverter(final DateTimeFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter");
        this.formatter = formatter;
    }

    final DateTimeFormatter formatter;

    /**
     * Wraps the attempt to convert in {@link #convert3(Object)},
     * capturing any exceptions thrown by the formatter.
     */
    @Override
    final T convert2(final S value) {
        try {
            return this.convert3(value);
        } catch (final DateTimeException cause) {
            return this.failConversion(value, cause);
        }
    }

    abstract T convert3(final S value) throws DateTimeException;

    @Override
    String toStringSuffix() {
        return "";
    }
}
