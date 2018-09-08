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

import walkingkooka.test.Fake;
import walkingkooka.tree.visit.Visiting;

public class FakeJsonNodeVisitor extends JsonNodeVisitor implements Fake {

    protected FakeJsonNodeVisitor() {
        super();
    }

    @Override
    protected Visiting startVisit(final JsonNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final JsonNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final JsonBooleanNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final JsonNullNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final JsonNumberNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final JsonStringNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final JsonArrayNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final JsonArrayNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final JsonObjectNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final JsonObjectNode node) {
        throw new UnsupportedOperationException();
    }
}
