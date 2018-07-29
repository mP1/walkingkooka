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

import java.util.List;

public final class SpreadsheetRangeParserTokenTest extends SpreadsheetBinaryParserTokenTestCase<SpreadsheetRangeParserToken> {

    @Override
    SpreadsheetRangeParserToken createToken(final String text, final List<SpreadsheetParserToken> tokens) {
        return SpreadsheetParserToken.range(tokens, text);
    }

    @Override
    SpreadsheetParserToken leftToken() {
        return SpreadsheetParserToken.labelName(SpreadsheetLabelName.with("left"), "left");
    }

    @Override
    SpreadsheetParserToken rightToken() {
        return SpreadsheetParserToken.labelName(SpreadsheetLabelName.with("right"), "right");
    }

    @Override
    SpreadsheetParserToken operatorSymbol() {
        return symbol(":");
    }

    @Override
    protected Class<SpreadsheetRangeParserToken> type() {
        return SpreadsheetRangeParserToken.class;
    }
}
