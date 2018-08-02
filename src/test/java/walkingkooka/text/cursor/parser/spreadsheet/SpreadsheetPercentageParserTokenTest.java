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

package walkingkooka.text.cursor.parser.spreadsheet;

import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;

public final class SpreadsheetPercentageParserTokenTest extends SpreadsheetUnaryParserTokenTestCase<SpreadsheetPercentageParserToken> {

    @Override
    SpreadsheetPercentageParserToken createToken(final String text, final List<ParserToken> tokens) {
        return SpreadsheetParserToken.percentage(tokens, text);
    }

    @Override
    String text() {
        return NUMBER1 + "00";
    }

    @Override
    List<ParserToken> tokens() {
        return Lists.of(this.number1());
    }

    @Override
    protected SpreadsheetPercentageParserToken createDifferentToken() {
        return this.createToken(NUMBER2, this.number2());
    }

    @Override
    protected Class<SpreadsheetPercentageParserToken> type() {
        return SpreadsheetPercentageParserToken.class;
    }
}
