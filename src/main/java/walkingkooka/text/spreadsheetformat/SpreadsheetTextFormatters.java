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

import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatBigDecimalParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatDateTimeParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatTextParserToken;
import walkingkooka.type.PublicStaticHelper;

import java.math.MathContext;

/**
 * Collection of static factory methods for numerous {@link SpreadsheetTextFormatter}.
 */
public final class SpreadsheetTextFormatters implements PublicStaticHelper {

    /**
     * {@see BigDecimalSpreadsheetTextFormatter}
     */
    public static SpreadsheetTextFormatter bigDecimal(final SpreadsheetFormatBigDecimalParserToken token,
                                                      final MathContext mathContext) {
        return BigDecimalSpreadsheetTextFormatter.with(token, mathContext);
    }

    /**
     * {@see FakeSpreadsheetTextFormatter}
     */
    public static <V> SpreadsheetTextFormatter<V> fake() {
        return new FakeSpreadsheetTextFormatter<V>();
    }

    /**
     * {@see LocalDateTimeSpreadsheetTextFormatter}
     */
    static SpreadsheetTextFormatter localDateTime(final SpreadsheetFormatDateTimeParserToken token) {
        return LocalDateTimeSpreadsheetTextFormatter.with(token);
    }

    /**
     * {@see TextSpreadsheetTextFormatter}
     */
    public static SpreadsheetTextFormatter<String> text(final SpreadsheetFormatTextParserToken token) {
        return TextSpreadsheetTextFormatter.with(token);
    }

    /**
     * Stops creation
     */
    private SpreadsheetTextFormatters() {
        throw new UnsupportedOperationException();
    }
}
