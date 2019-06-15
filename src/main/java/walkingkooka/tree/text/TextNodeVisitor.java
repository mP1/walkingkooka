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

package walkingkooka.tree.text;

import walkingkooka.tree.visit.Visiting;
import walkingkooka.tree.visit.Visitor;

import java.util.Objects;

/**
 * A {@link Visitor} for a graph of {@link TextNode}.
 */
public abstract class TextNodeVisitor extends Visitor<TextNode> {

    protected TextNodeVisitor() {
        super();
    }

    // TextNode.......................................................................

    public final void accept(final TextNode node) {
        Objects.requireNonNull(node, "node");

        if (Visiting.CONTINUE == this.startVisit(node)) {
            node.accept(this);
        }
        this.endVisit(node);
    }

    protected Visiting startVisit(final TextNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final TextNode node) {
        // nop
    }

    protected void visit(final TextPlaceholderNode node) {
        // nop
    }

    protected void visit(final Text node) {
        // nop
    }

    protected Visiting startVisit(final TextStyleNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final TextStyleNode node) {
        // nop
    }

    protected Visiting startVisit(final TextStyleNameNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final TextStyleNameNode node) {
        // nop
    }
}
