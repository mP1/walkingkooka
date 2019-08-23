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

package walkingkooka.tree.json;

import walkingkooka.text.CharSequences;
import walkingkooka.visit.Visiting;

import java.util.Objects;

/**
 * A {@link JsonNodeVisitor} that does the right thing for the different {@link JsonNode} types.
 */
final class HasJsonNodeMapperJsonNodeVisitor extends JsonNodeVisitor {

    static Object value(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        final HasJsonNodeMapperJsonNodeVisitor visitor = new HasJsonNodeMapperJsonNodeVisitor();
        visitor.accept(node);
        return visitor.value;
    }

    HasJsonNodeMapperJsonNodeVisitor() {
        super();
    }

    @Override
    protected void visit(final JsonBooleanNode node) {
        this.value = node.value();
    }

    @Override
    protected void visit(final JsonNullNode node) {
        this.value = node.value();
    }

    @Override
    protected void visit(final JsonNumberNode node) {
        this.value = node.numberValueOrFail();
    }

    @Override
    protected void visit(final JsonStringNode node) {
        this.value = node.stringValueOrFail();
    }

    @Override
    protected Visiting startVisit(final JsonArrayNode node) {
        throw new FromJsonNodeException("array never hold type", node);
    }

    @Override
    protected Visiting startVisit(final JsonObjectNode node) {
        this.value = node.fromJsonNodeWithType();
        return Visiting.SKIP;
    }

    Object value;

    @Override
    public String toString() {
        return CharSequences.quoteIfChars(this.value).toString();
    }
}
