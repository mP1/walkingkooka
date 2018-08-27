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

import walkingkooka.tree.visit.Visiting;
import walkingkooka.tree.visit.Visitor;

import java.util.Objects;

/**
 * A {@link Visitor} for a graph of {@link SearchNode}.
 */
public abstract class SearchNodeVisitor extends Visitor<SearchNode> {

    protected SearchNodeVisitor() {
        super();
    }

    // SearchNode.......................................................................

    public final void accept(final SearchNode node) {
        Objects.requireNonNull(node, "node");

        if(Visiting.CONTINUE == this.startVisit(node)) {
            node.accept(this);
        }
        this.endVisit(node);
    }

    protected Visiting startVisit(final SearchNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SearchNode node) {
        // nop
    }

    protected Visiting startVisit(final SearchSelectNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SearchSelectNode node) {
        // nop
    }

    protected Visiting startVisit(final SearchSequenceNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SearchSequenceNode node) {
        // nop
    }

    protected void visit(final SearchBigDecimalNode node) {
        // nop
    }

    protected void visit(final SearchBigIntegerNode node) {
        // nop
    }

    protected void visit(final SearchDoubleNode node) {
        // nop
    }

    protected void visit(final SearchLocalDateNode node) {
        // nop
    }

    protected void visit(final SearchLocalDateTimeNode node) {
        // nop
    }

    protected void visit(final SearchLocalTimeNode node) {
        // nop
    }

    protected void visit(final SearchLongNode node) {
        // nop
    }

    protected void visit(final SearchTextNode node) {
        // nop
    }
}
