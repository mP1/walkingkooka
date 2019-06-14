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

package walkingkooka.tree.select.parser;

import walkingkooka.io.printer.IndentingPrinters;
import walkingkooka.io.printer.Printers;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.cursor.parser.BigDecimalParserToken;
import walkingkooka.text.cursor.parser.BigIntegerParserToken;
import walkingkooka.text.cursor.parser.CharacterParserToken;
import walkingkooka.text.cursor.parser.DoubleParserToken;
import walkingkooka.text.cursor.parser.DoubleQuotedParserToken;
import walkingkooka.text.cursor.parser.LocalDateParserToken;
import walkingkooka.text.cursor.parser.LocalDateTimeParserToken;
import walkingkooka.text.cursor.parser.LocalTimeParserToken;
import walkingkooka.text.cursor.parser.LongParserToken;
import walkingkooka.text.cursor.parser.OffsetDateTimeParserToken;
import walkingkooka.text.cursor.parser.OffsetTimeParserToken;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.RepeatedParserToken;
import walkingkooka.text.cursor.parser.SequenceParserToken;
import walkingkooka.text.cursor.parser.SignParserToken;
import walkingkooka.text.cursor.parser.SingleQuotedParserToken;
import walkingkooka.text.cursor.parser.StringParserToken;
import walkingkooka.text.cursor.parser.ZonedDateTimeParserToken;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.visit.Visiting;
import walkingkooka.tree.visit.VisitorPrettyPrinter;

final class NodeSelectorParserPrettyNodeSelectorParserTokenVisitor extends NodeSelectorParserTokenVisitor {

    static String toString(final ParserToken token) {
        final StringBuilder b = new StringBuilder();

        new NodeSelectorParserPrettyNodeSelectorParserTokenVisitor(VisitorPrettyPrinter.with(
                IndentingPrinters.printer(Printers.stringBuilder(b, LineEnding.NL)),
                Indentation.with("  "),
                NodeSelectorParserPrettyNodeSelectorParserTokenVisitor::tokenName)).accept(token);
        return b.toString();
    }

    private static String tokenName(final ParserToken token) {
        return VisitorPrettyPrinter.computeFromClassSimpleName(token,
                NodeSelector.class.getSimpleName(),
                ParserToken.class.getSimpleName());
    }

    private NodeSelectorParserPrettyNodeSelectorParserTokenVisitor(final VisitorPrettyPrinter<ParserToken> printer) {
        this.printer = printer;
    }

    @Override
    protected Visiting startVisit(final NodeSelectorAdditionParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorAdditionParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorAndParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorAndParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorAttributeParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorAttributeParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorDivisionParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorDivisionParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorEqualsParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorEqualsParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorExpressionParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorExpressionParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorFunctionParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorFunctionParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorGreaterThanParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorGreaterThanParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorGreaterThanEqualsParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorGroupParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorGroupParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorGreaterThanEqualsParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorLessThanParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorLessThanParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorLessThanEqualsParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorLessThanEqualsParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorModuloParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorModuloParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorMultiplicationParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorMultiplicationParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorNotEqualsParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorNotEqualsParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorOrParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorOrParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorPredicateParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorPredicateParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorSubtractionParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorSubtractionParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected void visit(final NodeSelectorAbsoluteParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorAncestorParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorAncestorOrSelfParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorAndSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorAtSignSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorAttributeNameParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorBracketOpenSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorBracketCloseSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorChildParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorDescendantParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorDescendantOrSelfParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorEqualsSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorFirstChildParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorFollowingParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorFollowingSiblingParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorFunctionNameParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorGreaterThanSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorGreaterThanEqualsSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorLastChildParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorLessThanSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorLessThanEqualsSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorNodeNameParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorNotEqualsSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorNumberParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorOrSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorParameterSeparatorSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorParenthesisOpenSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorParenthesisCloseSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorParentOfParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorPrecedingParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorPrecedingSiblingParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorQuotedTextParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorSelfParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorSlashSeparatorSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorWhitespaceParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final NodeSelectorWildcardParserToken token) {
        this.printer.leaf(token);
    }

    // ParserToken......................................................................................................

    @Override
    protected Visiting startVisit(final RepeatedParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final RepeatedParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final SequenceParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final SequenceParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected void visit(final BigDecimalParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final BigIntegerParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final CharacterParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final DoubleParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final DoubleQuotedParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final LocalDateParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final LocalDateTimeParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final LocalTimeParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final LongParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final OffsetDateTimeParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final OffsetTimeParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final SingleQuotedParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final SignParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final StringParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final ZonedDateTimeParserToken token) {
        this.printer.leaf(token);
    }

    private final VisitorPrettyPrinter<ParserToken> printer;

    @Override
    public String toString() {
        return this.printer.toString();
    }
}
