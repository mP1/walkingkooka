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

import walkingkooka.io.printer.IndentingPrinter;
import walkingkooka.io.printer.IndentingPrinters;
import walkingkooka.io.printer.Printers;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.visit.Visiting;

/**
 * Pretty prints a graph of {@link SpreadsheetParserToken tokens}.
 */
final class SpreadsheetParserPrettySpreadsheetParserTokenVisitor extends SpreadsheetParserTokenVisitor {

    static String toString(final ParserToken token) {
        final StringBuilder b = new StringBuilder();
        final IndentingPrinter printer = IndentingPrinters.printer(Printers.stringBuilder(b, LineEnding.NL));
        new SpreadsheetParserPrettySpreadsheetParserTokenVisitor(printer, Indentation.with("  ")).accept(token);
        return b.toString();
    }


    private SpreadsheetParserPrettySpreadsheetParserTokenVisitor(final IndentingPrinter printer, final Indentation indentation) {
        this.printer = printer;
        this.indentation = indentation;
    }

    @Override
    protected Visiting startVisit(final SpreadsheetAdditionParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetAdditionParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetCellParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetCellParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetDivisionParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetDivisionParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetEqualsParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetEqualsParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetFunctionParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetFunctionParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetGreaterThanParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetGreaterThanParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetGreaterThanEqualsParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetGreaterThanEqualsParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetGroupParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetGroupParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetLessThanParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetLessThanParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetLessThanEqualsParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetLessThanEqualsParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetMultiplicationParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetMultiplicationParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetNegativeParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetNegativeParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetNotEqualsParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetNotEqualsParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetPercentageParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetPercentageParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetPowerParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetPowerParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetRangeParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetRangeParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final SpreadsheetSubtractionParserToken token) {
        this.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SpreadsheetSubtractionParserToken token) {
        this.exit(token);
        super.endVisit(token);
    }

    // leaf ......................................................................................................

    @Override
    protected void visit(final SpreadsheetBetweenSymbolParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetBigDecimalParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetBigIntegerParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetCloseParenthesisSymbolParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetColumnParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetDivideSymbolParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetDoubleParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetEqualsSymbolParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetFunctionNameParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetFunctionParameterSeparatorSymbolParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetGreaterThanSymbolParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetGreaterThanEqualsSymbolParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetLabelNameParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetLessThanSymbolParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetLessThanEqualsSymbolParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetLongParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetMinusSymbolParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetMultiplySymbolParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetNotEqualsSymbolParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetOpenParenthesisSymbolParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetPercentSymbolParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetPlusSymbolParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetPowerSymbolParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetRowParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetTextParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    @Override
    protected void visit(final SpreadsheetWhitespaceParserToken token) {
        this.leaf(token);
        super.visit(token);
    }

    private void enter(final SpreadsheetParserToken token) {
        this.printer.print(this.typeName(token));
        this.printer.print(this.printer.lineEnding());
        this.printer.indent(this.indentation);
    }

    private void exit(final SpreadsheetParserToken token) {
        this.printer.outdent();
    }

    private void leaf(final SpreadsheetParserToken token) {
        this.printer.print(this.typeName(token) + "=" + token);
        this.printer.print(this.printer.lineEnding());
    }

    private String typeName(final SpreadsheetParserToken token) {
        String typeName = token.getClass().getSimpleName();
        final String parserToken = ParserToken.class.getSimpleName();
        if(typeName.endsWith(parserToken)) {
            typeName = typeName.substring(0, typeName.length() - parserToken.length());
        }
        final String spreadsheet = "Spreadsheet";
        if(typeName.startsWith(spreadsheet)){
            typeName = typeName.substring(spreadsheet.length());
        }
        return typeName;
    }

    private final IndentingPrinter printer;
    private final Indentation indentation;
}
