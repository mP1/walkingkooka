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

package walkingkooka.text.cursor.parser.json;

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
import walkingkooka.tree.visit.Visiting;
import walkingkooka.tree.visit.VisitorPrettyPrinter;

final class JsonParserPrettyJsonNodeParserTokenVisitor extends JsonNodeParserTokenVisitor {

    static String toString(final ParserToken token) {
        final StringBuilder b = new StringBuilder();

        new JsonParserPrettyJsonNodeParserTokenVisitor(VisitorPrettyPrinter.with(
                IndentingPrinters.printer(Printers.stringBuilder(b, LineEnding.NL)),
                Indentation.with("  "),
                JsonParserPrettyJsonNodeParserTokenVisitor::tokenName)).accept(token);
        return b.toString();
    }

    private static String tokenName(final ParserToken token) {
        return VisitorPrettyPrinter.computeFromClassSimpleName(token, "JsonNode", ParserToken.class.getSimpleName());
    }

    private JsonParserPrettyJsonNodeParserTokenVisitor(final VisitorPrettyPrinter<ParserToken> printer) {
        this.printer = printer;
    }

    @Override
    protected Visiting startVisit(final JsonNodeArrayParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final JsonNodeArrayParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected Visiting startVisit(final JsonNodeObjectParserToken token) {
        this.printer.enter(token);
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final JsonNodeObjectParserToken token) {
        this.printer.exit(token);
    }

    @Override
    protected void visit(final JsonNodeArrayBeginSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final JsonNodeArrayEndSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final JsonNodeBooleanParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final JsonNodeNullParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final JsonNodeNumberParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final JsonNodeObjectAssignmentSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final JsonNodeObjectBeginSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final JsonNodeObjectEndSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final JsonNodeSeparatorSymbolParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final JsonNodeStringParserToken token) {
        this.printer.leaf(token);
    }

    @Override
    protected void visit(final JsonNodeWhitespaceParserToken token) {
        this.printer.leaf(token);
    }

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
