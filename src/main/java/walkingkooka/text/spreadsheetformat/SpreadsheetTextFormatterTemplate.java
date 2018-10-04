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

import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.BigDecimalParserToken;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserException;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserContext;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserContexts;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParsers;

import java.math.MathContext;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Base class for all {@link SpreadsheetTextFormatter} implementations.
 */
abstract class SpreadsheetTextFormatterTemplate<T> implements SpreadsheetTextFormatter<T> {

    static void check(final String pattern) {
        Objects.requireNonNull(pattern, "pattern");
    }

    static void check(final MathContext mathContext) {
        Objects.requireNonNull(mathContext, "mathContext");
    }

    /**
     * Package private to limit sub classing.
     */
    SpreadsheetTextFormatterTemplate(final String pattern) {
        super();

        this.pattern = pattern;
    }

    /**
     * Parses the pattern completely into a {@link SpreadsheetFormatParserToken} or fails.
     */
    static Optional<SpreadsheetFormatParserToken> parse(final String pattern,
                                                       final MathContext mathContext,
                                                       final Function<Parser<BigDecimalParserToken, SpreadsheetFormatParserContext>, Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext>> parserFactory) {
        try {
            final TextCursor cursor = TextCursors.charSequence(pattern);
            return parserFactory.apply(Parsers.bigDecimal(SpreadsheetFormatParsers.DECIMAL_POINT, mathContext))
                    .orFailIfCursorNotEmpty(ParserReporters.basic())
                    .parse(cursor, SpreadsheetFormatParserContexts.basic());
        } catch (final ParserException fail) {
            throw new IllegalArgumentException(fail.getMessage(), fail);
        }
    }

    /**
     * Accepts a value and uses the {@link TextFormatterSpreadsheetFormatParserTokenVisitor} to produce the formatted text.
     */
    @Override
    public final Optional<SpreadsheetFormattedText> format(final T value, final SpreadsheetTextFormatContext context) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(context, "context");

        return this.format0(value, context);
    }

    abstract Optional<SpreadsheetFormattedText> format0(final T value, final SpreadsheetTextFormatContext context);

    /**
     * Returns the original pattern.
     */
    @Override
    public final String toString() {
        return this.pattern;
    }

    private final String pattern;
}
