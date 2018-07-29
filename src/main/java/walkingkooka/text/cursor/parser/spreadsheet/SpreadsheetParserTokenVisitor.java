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

import walkingkooka.Cast;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.tree.visit.Visiting;

public abstract class SpreadsheetParserTokenVisitor extends ParserTokenVisitor {

    // SpreadsheetAdditionParserToken....................................................................................

    protected Visiting startVisit(final SpreadsheetAdditionParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SpreadsheetAdditionParserToken token) {
        // nop
    }

    // SpreadsheetCellParserToken....................................................................................

    protected Visiting startVisit(final SpreadsheetCellParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SpreadsheetCellParserToken token) {
        // nop
    }
    
    // SpreadsheetDivisionParserToken....................................................................................

    protected Visiting startVisit(final SpreadsheetDivisionParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SpreadsheetDivisionParserToken token) {
        // nop
    }

    // SpreadsheetFunctionParserToken....................................................................................

    protected Visiting startVisit(final SpreadsheetFunctionParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SpreadsheetFunctionParserToken token) {
        // nop
    }
    
    // SpreadsheetGroupParserToken....................................................................................

    protected Visiting startVisit(final SpreadsheetGroupParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SpreadsheetGroupParserToken token) {
        // nop
    }

    // SpreadsheetMultiplicationParserToken....................................................................................

    protected Visiting startVisit(final SpreadsheetMultiplicationParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SpreadsheetMultiplicationParserToken token) {
        // nop
    }
    
    // SpreadsheetNegativeParserToken....................................................................................

    protected Visiting startVisit(final SpreadsheetNegativeParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SpreadsheetNegativeParserToken token) {
        // nop
    }
    
    // SpreadsheetPercentageParserToken....................................................................................

    protected Visiting startVisit(final SpreadsheetPercentageParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SpreadsheetPercentageParserToken token) {
        // nop
    }

    // SpreadsheetPowerParserToken....................................................................................

    protected Visiting startVisit(final SpreadsheetPowerParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SpreadsheetPowerParserToken token) {
        // nop
    }

    // SpreadsheetRangeParserToken....................................................................................

    protected Visiting startVisit(final SpreadsheetRangeParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SpreadsheetRangeParserToken token) {
        // nop
    }

    // SpreadsheetSubtractionParserToken....................................................................................

    protected Visiting startVisit(final SpreadsheetSubtractionParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SpreadsheetSubtractionParserToken token) {
        // nop
    }

    // SpreadsheetLeafParserToken ....................................................................................

    final void acceptColumn(final SpreadsheetColumnParserToken token) {
        this.visit(token);
    }

    protected void visit(final SpreadsheetColumnParserToken token) {
        // nop
    }
    
    final void acceptDecimal(final SpreadsheetDecimalParserToken token) {
        this.visit(token);
    }

    protected void visit(final SpreadsheetDecimalParserToken token) {
        // nop
    }

    final void acceptDefined(final SpreadsheetLabelNameParserToken token) {
        this.visit(token);
    }

    protected void visit(final SpreadsheetLabelNameParserToken token) {
        // nop
    }

    final void acceptDouble(final SpreadsheetDoubleParserToken token) {
        this.visit(token);
    }

    protected void visit(final SpreadsheetDoubleParserToken token) {
        // nop
    }
    
    final void acceptFunction(final SpreadsheetFunctionNameParserToken token) {
        this.visit(token);
    }

    protected void visit(final SpreadsheetFunctionNameParserToken token) {
        // nop
    }

    final void acceptLong(final SpreadsheetLongParserToken token) {
        this.visit(token);
    }

    protected void visit(final SpreadsheetLongParserToken token) {
        // nop
    }
    
    final void acceptNumber(final SpreadsheetNumberParserToken token) {
        this.visit(token);
    }

    protected void visit(final SpreadsheetNumberParserToken token) {
        // nop
    }

    final void acceptRow(final SpreadsheetRowParserToken token) {
        this.visit(token);
    }

    protected void visit(final SpreadsheetRowParserToken token) {
        // nop
    }

    final void acceptSymbol(final SpreadsheetSymbolParserToken token) {
        this.visit(token);
    }

    protected void visit(final SpreadsheetSymbolParserToken token) {
        // nop
    }
    
    final void acceptText(final SpreadsheetTextParserToken token) {
        this.visit(token);
    }

    protected void visit(final SpreadsheetTextParserToken token) {
        // nop
    }
    
    final void acceptWhitespace(final SpreadsheetWhitespaceParserToken token) {
        this.visit(token);
    }

    protected void visit(final SpreadsheetWhitespaceParserToken token) {
        // nop
    }

    // ParserToken.......................................................................

    private void acceptParserToken(final ParserToken token) {
        if(Visiting.CONTINUE == this.startVisit(token)) {
            if(token instanceof SpreadsheetParserToken) {
                this.acceptSpreadsheetParserToken(Cast.to(token));
            }
        }
    }

    protected Visiting startVisit(final ParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ParserToken token) {
        // nop
    }

    // SpreadsheetParserToken.......................................................................

    private void acceptSpreadsheetParserToken(final SpreadsheetParserToken token) {
        if(Visiting.CONTINUE == this.startVisit(token)) {
            token.accept(this);
        }
    }

    protected Visiting startVisit(final SpreadsheetParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SpreadsheetParserToken token) {
        // nop
    }
}
