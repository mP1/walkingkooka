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

package walkingkooka.text.cursor.parser.color;

import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.tree.visit.Visiting;

public abstract class ColorParserTokenVisitor extends ParserTokenVisitor {

    protected ColorParserTokenVisitor() {
        super();
    }

    // ParserToken.......................................................................

    protected void visit(final ColorParserToken token) {
        // nop
    }

    protected void visit(final HslParserToken token) {
        // nop
    }

    protected void visit(final HsvParserToken token) {
        // nop
    }

    // ColorParserToken.......................................................................

    protected Visiting startVisit(final ColorHslOrHsvParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ColorHslOrHsvParserToken token) {
        // nop
    }
}