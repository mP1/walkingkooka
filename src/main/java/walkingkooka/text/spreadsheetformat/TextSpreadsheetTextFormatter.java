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

import walkingkooka.text.cursor.parser.BigDecimalParserToken;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserContext;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParsers;

import java.math.MathContext;
import java.util.Optional;

/**
 * A {@link SpreadsheetTextFormatter} that formats a {@link String}.
 */
final class TextSpreadsheetTextFormatter extends SpreadsheetTextFormatterTemplate<String> {

    /**
     * Parses the pattern and creates a {@link TextSpreadsheetTextFormatter}
     */
    static TextSpreadsheetTextFormatter parse(final String pattern) {
        return new TextSpreadsheetTextFormatter(pattern);
    }

    /**
     * Private ctor use static parse.
     */
    private TextSpreadsheetTextFormatter(final String pattern) {
        super(pattern);
        this.token = this.parse(pattern, MathContext.DECIMAL32);
    }

    @Override
    Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> parser(final Parser<BigDecimalParserToken, SpreadsheetFormatParserContext> parser) {
        return SpreadsheetFormatParsers.text(parser);
    }

    @Override
    Optional<SpreadsheetFormattedText> format0(final String value, final SpreadsheetTextFormatContext context) {
        return this.token.isPresent() ?
                Optional.of(TextSpreadsheetTextFormatterSpreadsheetFormatParserTokenVisitor.format(this.token.get(), value, context)) :
                Optional.empty();
    }

    private final Optional<SpreadsheetFormatParserToken> token;
}
