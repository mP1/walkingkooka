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

import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.visit.Visiting;

public class FakeJsonNodeParserTokenVisitor extends JsonNodeParserTokenVisitor {

    @Override
    protected Visiting startVisit(final JsonNodeArrayParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final JsonNodeArrayParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final JsonNodeObjectParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final JsonNodeObjectParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final JsonNodeArrayBeginSymbolParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final JsonNodeArrayEndSymbolParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final JsonNodeBooleanParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final JsonNodeNullParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final JsonNodeNumberParserToken token) {
        super.visit(token);
    }

    @Override
    protected void visit(final JsonNodeObjectAssignmentSymbolParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final JsonNodeObjectBeginSymbolParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final JsonNodeObjectEndSymbolParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final JsonNodeSeparatorSymbolParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final JsonNodeStringParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final JsonNodeWhitespaceParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(ParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(ParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final JsonNodeParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final JsonNodeParserToken token) {
        throw new UnsupportedOperationException();
    }
}
