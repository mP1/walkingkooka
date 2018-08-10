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
import java.util.function.Consumer;

/**
 * Used to filter the whitespace and symbols to return only the tokens.
 */
final class SpreadsheetUnaryParserTokenConsumer implements Consumer<SpreadsheetParserToken> {

    @Override
    public void accept(final SpreadsheetParserToken token) {
        if(!token.isNoise()) {
            if(null ==this.value) {
                this.value = token;
            }
        }
    }

    SpreadsheetParserToken value(final List<ParserToken> tokens) {
        if(null ==this.value) {
            throw new IllegalArgumentException("value token missing from " + tokens);
        }
        return value;
    }
    private SpreadsheetParserToken value;
}
