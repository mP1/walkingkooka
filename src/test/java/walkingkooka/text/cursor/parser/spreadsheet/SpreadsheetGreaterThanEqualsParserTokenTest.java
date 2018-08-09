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

import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;

public final class SpreadsheetGreaterThanEqualsParserTokenTest extends SpreadsheetBinaryParserTokenTestCase2<SpreadsheetGreaterThanEqualsParserToken> {

    @Override
    SpreadsheetGreaterThanEqualsParserToken createToken(final String text, final List<ParserToken> tokens) {
        return SpreadsheetParserToken.greaterThanEquals(tokens, text);
    }

    @Override
    SpreadsheetParserToken operatorSymbol() {
        return SpreadsheetParserToken.greaterThanEqualsSymbol(">=", ">=");
    }

    @Override
    protected Class<SpreadsheetGreaterThanEqualsParserToken> type() {
        return SpreadsheetGreaterThanEqualsParserToken.class;
    }
}
