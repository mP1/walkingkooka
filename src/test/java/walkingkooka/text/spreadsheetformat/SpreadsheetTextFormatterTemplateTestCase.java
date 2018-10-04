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

import org.junit.Test;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.BigDecimalParserToken;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserContext;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserContexts;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserToken;

import java.math.MathContext;

public abstract class SpreadsheetTextFormatterTemplateTestCase<F extends SpreadsheetTextFormatterTemplate<V>,
        V,
        T extends SpreadsheetFormatParserToken>
        extends SpreadsheetTextFormatterTestCase<F, V> {

    @Test(expected = NullPointerException.class)
    public final void testWithNullParserTokenFails() {
        this.createFormatter0(null);
    }

    @Override
    final protected F createFormatter() {
        return this.createFormatter(this.pattern());
    }

    abstract String pattern();

    final F createFormatter(final String pattern) {
        return this.createFormatter0(this.parse(pattern));
    }

    final T parse(final String pattern) {
        return this.parser(Parsers.bigDecimal('.', MathContext.UNLIMITED))
                .orFailIfCursorNotEmpty(ParserReporters.basic())
                .parse(TextCursors.charSequence(pattern), SpreadsheetFormatParserContexts.basic())
                .get()
                .cast();
    }

    abstract Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> parser(final Parser<BigDecimalParserToken, SpreadsheetFormatParserContext> bigDecimal);

    abstract F createFormatter0(T token);
}
