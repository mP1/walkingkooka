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

package walkingkooka.text.spreadsheetformat;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SpreadsheetTextFormatter} that tests if a value to be formatted is of the given type, and calls the first
 * {@link SpreadsheetTextFormatter} if it is or defaults to the second if not.
 */
final class TypedSpreadsheetTextFormatter<T> implements SpreadsheetTextFormatter<T> {

    /**
     * Creates a new {@link TypedSpreadsheetTextFormatter}
     */
    static <T> TypedSpreadsheetTextFormatter with(final Class<T> type,
                                                  final SpreadsheetTextFormatter<T> formatter,
                                                  final SpreadsheetTextFormatter<? extends Object> formatter2) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(formatter, "formatter");
        Objects.requireNonNull(formatter2, "formatter2");

        return new TypedSpreadsheetTextFormatter(type, formatter, formatter2);
    }

    /**
     * Private ctor use factory
     */
    private TypedSpreadsheetTextFormatter(final Class<T> type,
                                          final SpreadsheetTextFormatter<T> formatter,
                                          final SpreadsheetTextFormatter<Object> formatter2) {
        this.type = type;
        this.formatter = formatter;
        this.formatter2 = formatter2;
    }

    @Override
    public Optional<SpreadsheetFormattedText> format(final Object value, final SpreadsheetTextFormatContext context) {
        return this.formatter(value).format(value, context);
    }

    private SpreadsheetTextFormatter formatter(final Object value) {
        return this.type.isInstance(value) ?
                this.formatter :
                this.formatter2;
    }

    private final Class<T> type;
    private final SpreadsheetTextFormatter<T> formatter;
    private final SpreadsheetTextFormatter<Object> formatter2;

    @Override
    public String toString() {
        return this.formatter + " | " + this.formatter2;
    }
}
