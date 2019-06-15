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

package walkingkooka.color.parser;

import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.tree.visit.Visiting;

public abstract class ColorFunctionParserTokenVisitor extends ParserTokenVisitor {

    protected ColorFunctionParserTokenVisitor() {
        super();
    }

    // ParserToken......................................................................................................

    protected Visiting startVisit(final ColorFunctionParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ColorFunctionParserToken token) {
    }

    protected Visiting startVisit(final ColorFunctionFunctionParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ColorFunctionFunctionParserToken token) {
    }

    protected void visit(final ColorFunctionDegreesUnitSymbolParserToken token) {
        // nop
    }

    protected void visit(final ColorFunctionFunctionNameParserToken token) {
        // nop
    }

    protected void visit(final ColorFunctionNumberParserToken token) {
        // nop
    }

    protected void visit(final ColorFunctionParenthesisCloseSymbolParserToken token) {
        // nop
    }

    protected void visit(final ColorFunctionParenthesisOpenSymbolParserToken token) {
        // nop
    }

    protected void visit(final ColorFunctionPercentageParserToken token) {
        // nop
    }

    protected void visit(final ColorFunctionSeparatorSymbolParserToken token) {
        // nop
    }

    protected void visit(final ColorFunctionWhitespaceParserToken token) {
        // nop
    }
}