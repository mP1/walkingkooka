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

import org.junit.jupiter.api.Test;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserContext;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserContexts;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserToken;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SpreadsheetTextFormatterTemplate2TestCase<F extends SpreadsheetTextFormatterTemplate2<V, T>,
        V,
        T extends SpreadsheetFormatParserToken>
        extends SpreadsheetTextFormatterTemplateTestCase<F, V> {

    @Test
    public final void testWithNullParserTokenFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createFormatter0(null);
        });
    }

    @Override
    final protected F createFormatter() {
        return this.createFormatter(this.pattern());
    }

    abstract String pattern();

    final F createFormatter(final String pattern) {
        return this.createFormatter0(this.parsePatternOrFail(this.parser(), pattern).cast());
    }

    final T parsePatternOrFail(final String pattern) {
        return this.parsePatternOrFail(this.parser(), pattern).cast();
    }

    final SpreadsheetFormatParserToken parsePatternOrFail(final Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> parser, final String pattern) {
        return parser.orFailIfCursorNotEmpty(ParserReporters.basic())
                .parse(TextCursors.charSequence(pattern), SpreadsheetFormatParserContexts.basic(ParserContexts.basic(decimalNumberContext())))
                .get();
    }

    private DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.basic("$", '.', 'E', ',', '-', '%','+');
    }

    abstract Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> parser();

    abstract F createFormatter0(T token);
}
