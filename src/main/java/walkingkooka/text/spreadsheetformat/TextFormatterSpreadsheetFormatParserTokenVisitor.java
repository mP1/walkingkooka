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

import walkingkooka.color.Color;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatColorNameParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatColorNumberParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserTokenVisitor;

/**
 * A {@link SpreadsheetFormatParserTokenVisitor} sub classed and used by {@link SpreadsheetTextFormatterTemplate}.
 */
abstract class TextFormatterSpreadsheetFormatParserTokenVisitor extends SpreadsheetFormatParserTokenVisitor {

    /**
     * Package private to limit sub classing.
     */
    TextFormatterSpreadsheetFormatParserTokenVisitor(final SpreadsheetTextFormatContext context) {
        super();
        this.context = context;
    }

    @Override
    protected final void visit(final SpreadsheetFormatColorNameParserToken token) {
        this.color = this.context.colorName(token.value());
    }

    @Override
    protected final void visit(final SpreadsheetFormatColorNumberParserToken token) {
        this.color = this.context.colorNumber(token.value());
    }

    final SpreadsheetTextFormatContext context;

    /**
     * Will be set either the {@link SpreadsheetFormatColorNameParserToken} or {@link SpreadsheetFormatColorNumberParserToken}
     * is encountered.
     */
    Color color = null;
}
