/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.tree.search;

import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinters;
import walkingkooka.text.printer.Printers;
import walkingkooka.tree.Node;
import walkingkooka.visit.Visiting;
import walkingkooka.visit.VisitorPrettyPrinter;

/**
 * Takes a {@link SearchNode} and pretty prints the nodes, making
 */
final class SearchPrettySearchNodeVisitor extends SearchNodeVisitor {

    static String toString(final SearchNode node) {
        final StringBuilder b = new StringBuilder();

        new SearchPrettySearchNodeVisitor(VisitorPrettyPrinter.with(
                IndentingPrinters.printer(Printers.stringBuilder(b, LineEnding.NL), Indentation.with(' ', 2)),
                SearchPrettySearchNodeVisitor::tokenName)).accept(node);
        return b.toString();
    }

    private static String tokenName(final SearchNode token) {
        return VisitorPrettyPrinter.computeFromClassSimpleName(token, "Search", Node.class.getSimpleName());
    }

    private SearchPrettySearchNodeVisitor(final VisitorPrettyPrinter<SearchNode> printer) {
        this.printer = printer;
    }

    @Override
    protected Visiting startVisit(final SearchIgnoredNode node) {
        this.printer.enter(node);
        return super.startVisit(node);
    }

    @Override
    protected void endVisit(final SearchIgnoredNode node) {
        this.printer.exit(node);
        super.endVisit(node);
    }

    @Override
    protected Visiting startVisit(final SearchMetaNode node) {
        this.printer.enter(node);
        return super.startVisit(node);
    }

    @Override
    protected void endVisit(final SearchMetaNode node) {
        this.printer.exit(node);
        super.endVisit(node);
    }

    @Override
    protected Visiting startVisit(final SearchSelectNode node) {
        this.printer.enter(node);
        return super.startVisit(node);
    }

    @Override
    protected void endVisit(final SearchSelectNode node) {
        this.printer.exit(node);
        super.endVisit(node);
    }

    @Override
    protected Visiting startVisit(final SearchSequenceNode node) {
        this.printer.enter(node);
        return super.startVisit(node);
    }

    @Override
    protected void endVisit(final SearchSequenceNode node) {
        this.printer.exit(node);
        super.endVisit(node);
    }

    @Override
    protected void visit(final SearchBigDecimalNode node) {
        this.printer.leaf(node);
    }

    @Override
    protected void visit(final SearchBigIntegerNode node) {
        this.printer.leaf(node);
    }

    @Override
    protected void visit(final SearchDoubleNode node) {
        this.printer.leaf(node);
    }

    @Override
    protected void visit(final SearchLocalDateNode node) {
        this.printer.leaf(node);
    }

    @Override
    protected void visit(final SearchLocalDateTimeNode node) {
        this.printer.leaf(node);
    }

    @Override
    protected void visit(final SearchLocalTimeNode node) {
        this.printer.leaf(node);
    }

    @Override
    protected void visit(final SearchLongNode node) {
        this.printer.leaf(node);
    }

    @Override
    protected void visit(final SearchTextNode node) {
        this.printer.leaf(node);
    }

    private final VisitorPrettyPrinter<SearchNode> printer;

    @Override
    public String toString() {
        return this.printer.toString();
    }
}
