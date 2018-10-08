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

import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserToken;

import java.util.Objects;
import java.util.Optional;

/**
 * Base class for all {@link SpreadsheetTextFormatter} implementations.
 */
abstract class SpreadsheetTextFormatterTemplate<V, T extends SpreadsheetFormatParserToken> implements SpreadsheetTextFormatter<V> {

    static void check(final SpreadsheetFormatParserToken token) {
        Objects.requireNonNull(token, "token");
    }

    /**
     * Package private to limit sub classing.
     */
    SpreadsheetTextFormatterTemplate(final T token) {
        super();

        this.token = token;
    }

    /**
     * Accepts a value and uses the {@link TextFormatterSpreadsheetFormatParserTokenVisitor} to produce the formatted text.
     */
    @Override
    public final Optional<SpreadsheetFormattedText> format(final V value, final SpreadsheetTextFormatContext context) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(context, "context");

        return this.format0(value, context);
    }

    abstract Optional<SpreadsheetFormattedText> format0(final V value, final SpreadsheetTextFormatContext context);

    /**
     * Returns the original pattern.
     */
    @Override
    public final String toString() {
        return this.token.text() + this.toStringSuffix();
    }

    final T token;

    abstract String toStringSuffix();
}
