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

import org.junit.Test;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserTestCase3;
import walkingkooka.text.cursor.parser.ParserToken;

public abstract class SpreadsheetParserTestCase<P extends Parser<T, SpreadsheetParserContext>,
        T extends SpreadsheetParserToken> extends
        ParserTestCase3<P, T, SpreadsheetParserContext> {

    @Test
    public final void testCheckNaming() {
        this.checkNamingStartAndEnd("Spreadsheet", Parser.class);
    }

    @Override
    protected SpreadsheetParserContext createContext() {
        return new SpreadsheetParserContext();
    }

    @Override
    protected String toString(final ParserToken token) {
        return SpreadsheetParserPrettySpreadsheetParserTokenVisitor.toString(token);
    }
}
