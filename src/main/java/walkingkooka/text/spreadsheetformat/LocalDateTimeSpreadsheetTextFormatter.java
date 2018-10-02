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

import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParsers;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SpreadsheetTextFormatter} that formats a {@link LocalDateTime}.
 */
final class LocalDateTimeSpreadsheetTextFormatter extends SpreadsheetTextFormatterTemplate<LocalDateTime> {

    /**
     * Parses the pattern and creates a {@link LocalDateTimeSpreadsheetTextFormatter}
     */
    static SpreadsheetTextFormatter with(final String pattern, final SpreadsheetTextFormatter general) {
        check(pattern);
        Objects.requireNonNull(general, "general");

        // special case if pattern is General...
        final Optional<SpreadsheetFormatParserToken> maybe = parse(pattern, MathContext.DECIMAL32, SpreadsheetFormatParsers::dateTime);
        if (!maybe.isPresent()) {
            throw new IllegalArgumentException("Unable to parse pattern " + CharSequences.quote(pattern));
        }
        // need to ask for default when General..
        final SpreadsheetFormatParserToken token = maybe.get();
        return token.isGeneral() ?
                general :
                new LocalDateTimeSpreadsheetTextFormatter(pattern, token);
    }

    /**
     * Private ctor use static parse.
     */
    private LocalDateTimeSpreadsheetTextFormatter(final String pattern, final SpreadsheetFormatParserToken token) {
        super(pattern);
        this.token = token;
        this.twelveHour = LocalDateTimeSpreadsheetTextFormatterAmPmSpreadsheetFormatParserTokenVisitor.is12HourTime(token);

    }

    @Override
    Optional<SpreadsheetFormattedText> format0(final LocalDateTime value, final SpreadsheetTextFormatContext context) {
        return LocalDateTimeSpreadsheetTextFormatterFormattingSpreadsheetFormatParserTokenVisitor.format(this.token, value, context, this.twelveHour);
    }

    /**
     * A list of components that add text to the formatted output.
     */
    private final SpreadsheetFormatParserToken token;

    private final boolean twelveHour;
}
