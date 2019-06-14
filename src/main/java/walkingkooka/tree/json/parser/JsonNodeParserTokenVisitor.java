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

package walkingkooka.tree.json.parser;

import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.tree.visit.Visiting;

public abstract class JsonNodeParserTokenVisitor extends ParserTokenVisitor {

    // JsonNodeArrayParserToken....................................................................................

    protected Visiting startVisit(final JsonNodeArrayParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final JsonNodeArrayParserToken token) {
        // nop
    }

    // JsonNodeObjectParserToken....................................................................................

    protected Visiting startVisit(final JsonNodeObjectParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final JsonNodeObjectParserToken token) {
        // nop
    }

    // JsonNodeLeafParserToken ....................................................................................

    protected void visit(final JsonNodeArrayBeginSymbolParserToken token) {
        // nop
    }

    protected void visit(final JsonNodeArrayEndSymbolParserToken token) {
        // nop
    }

    protected void visit(final JsonNodeBooleanParserToken token) {
        // nop
    }

    protected void visit(final JsonNodeNullParserToken token) {
        // nop
    }

    protected void visit(final JsonNodeNumberParserToken token) {
        // nop
    }

    protected void visit(final JsonNodeObjectAssignmentSymbolParserToken token) {
        // nop
    }

    protected void visit(final JsonNodeObjectBeginSymbolParserToken token) {
        // nop
    }

    protected void visit(final JsonNodeObjectEndSymbolParserToken token) {
        // nop
    }

    protected void visit(final JsonNodeSeparatorSymbolParserToken token) {
        // nop
    }

    protected void visit(final JsonNodeStringParserToken token) {
        // nop
    }

    protected void visit(final JsonNodeWhitespaceParserToken token) {
        // nop
    }

    // ParserToken.......................................................................

    protected Visiting startVisit(final ParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ParserToken token) {
        // nop
    }

    // JsonNodeParserToken.......................................................................

    protected Visiting startVisit(final JsonNodeParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final JsonNodeParserToken token) {
        // nop
    }
}
