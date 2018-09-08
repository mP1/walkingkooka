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

package walkingkooka.tree.json;

import walkingkooka.tree.visit.Visiting;
import walkingkooka.tree.visit.Visitor;

import java.util.Objects;

/**
 * A {@link Visitor} for a graph of {@link JsonNode}.
 */
public abstract class JsonNodeVisitor extends Visitor<JsonNode> {

    protected JsonNodeVisitor() {
        super();
    }


    // JsonNode.......................................................................

    public final void accept(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        if(Visiting.CONTINUE == this.startVisit(node)) {
            node.accept(this);
        }
        this.endVisit(node);
    }

    protected Visiting startVisit(final JsonNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final JsonNode node) {
        // nop
    }

    protected void visit(final JsonBooleanNode node) {
        // nop
    }

    protected void visit(final JsonNullNode node) {
        // nop
    }

    protected void visit(final JsonNumberNode node) {
        // nop
    }

    protected void visit(final JsonStringNode node) {
        // nop
    }

    protected Visiting startVisit(final JsonArrayNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final JsonArrayNode node) {
        // nop
    }

    protected Visiting startVisit(final JsonObjectNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final JsonObjectNode node) {
        // nop
    }
}
