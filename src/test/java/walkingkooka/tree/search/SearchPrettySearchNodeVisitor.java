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

package walkingkooka.tree.search;

import walkingkooka.io.printer.IndentingPrinter;
import walkingkooka.io.printer.IndentingPrinters;
import walkingkooka.io.printer.Printers;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.Node;
import walkingkooka.tree.visit.Visiting;

/**
 * Takes a {@link SearchNode} and pretty prints the nodes, making
 */
final class SearchPrettySearchNodeVisitor extends SearchNodeVisitor {

    static String toString(final SearchNode node) {
        final StringBuilder b = new StringBuilder();
        final IndentingPrinter printer = IndentingPrinters.printer(Printers.stringBuilder(b, LineEnding.NL));
        new SearchPrettySearchNodeVisitor(printer, Indentation.with("  ")).accept(node);
        return b.toString();
    }

    private SearchPrettySearchNodeVisitor(final IndentingPrinter printer, final Indentation indentation) {
        this.printer = printer;
        this.indentation = indentation;
    }

    @Override
    protected Visiting startVisit(final SearchSelectNode node) {
        this.enter(node);
        return super.startVisit(node);
    }

    @Override
    protected void endVisit(final SearchSelectNode node) {
        this.exit(node);
        super.endVisit(node);
    }

    @Override
    protected Visiting startVisit(final SearchSequenceNode node) {
        this.enter(node);
        return super.startVisit(node);
    }

    @Override
    protected void endVisit(final SearchSequenceNode node) {
        this.exit(node);
        super.endVisit(node);
    }

    @Override
    protected void visit(final SearchBigDecimalNode node) {
        this.leaf(node);
    }

    @Override
    protected void visit(final SearchBigIntegerNode node) {
        this.leaf(node);
    }

    @Override
    protected void visit(final SearchDoubleNode node) {
        this.leaf(node);
    }

    @Override
    protected void visit(final SearchLocalDateNode node) {
        this.leaf(node);
    }

    @Override
    protected void visit(final SearchLocalDateTimeNode node) {
        this.leaf(node);
    }

    @Override
    protected void visit(final SearchLocalTimeNode node) {
        this.leaf(node);
    }

    @Override
    protected void visit(final SearchLongNode node) {
        this.leaf(node);
    }

    @Override
    protected void visit(final SearchTextNode node) {
        this.leaf(node);
    }

    private void enter(final SearchNode node) {
        this.printer.print(this.typeName(node));
        this.printer.print(this.printer.lineEnding());
        this.printer.indent(this.indentation);
    }

    private void exit(final SearchNode node) {
        this.printer.outdent();
    }

    private void leaf(final SearchNode node) {
        this.printer.print(this.typeName(node) + "=" + node);
        this.printer.print(this.printer.lineEnding());
    }

    private String typeName(final SearchNode node) {
        String typeName = node.getClass().getSimpleName();
        final String nodeName = Node.class.getSimpleName();
        if(typeName.endsWith(nodeName)) {
            typeName = typeName.substring(0, typeName.length() - nodeName.length());
        }
        final String search = "Search";
        if(typeName.startsWith(search)){
            typeName = typeName.substring(search.length());
        }
        return typeName;
    }

    private final IndentingPrinter printer;
    private final Indentation indentation;
}
