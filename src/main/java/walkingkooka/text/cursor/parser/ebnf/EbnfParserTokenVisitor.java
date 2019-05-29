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

package walkingkooka.text.cursor.parser.ebnf;

import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.tree.visit.Visiting;

public abstract class EbnfParserTokenVisitor extends ParserTokenVisitor {

    // EbnfGrammarParserToken....................................................................................

    protected Visiting startVisit(final EbnfGrammarParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final EbnfGrammarParserToken token) {
        // nop
    }

    // EbnfParentParserTokens.........................................................................................

    // EbnfAlternativeParserToken ....................................................................................

    protected Visiting startVisit(final EbnfAlternativeParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final EbnfAlternativeParserToken token) {
        // nop
    }

    // EbnfConcatenationParserToken....................................................................................

    protected Visiting startVisit(final EbnfConcatenationParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final EbnfConcatenationParserToken token) {
        // nop
    }

    // EbnfExceptionParserToken....................................................................................

    protected Visiting startVisit(final EbnfExceptionParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final EbnfExceptionParserToken token) {
        // nop
    }

    // EbnfGroupParserToken....................................................................................

    protected Visiting startVisit(final EbnfGroupParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final EbnfGroupParserToken token) {
        // nop
    }

    // EbnfOptionalParserToken....................................................................................

    protected Visiting startVisit(final EbnfOptionalParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final EbnfOptionalParserToken token) {
        // nop
    }

    // EbnfRangeParserToken....................................................................................

    protected Visiting startVisit(final EbnfRangeParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final EbnfRangeParserToken token) {
        // nop
    }

    // EbnfRepeatedParserToken....................................................................................

    protected Visiting startVisit(final EbnfRepeatedParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final EbnfRepeatedParserToken token) {
        // nop
    }

    // EbnfRuleParserToken....................................................................................

    protected Visiting startVisit(final EbnfRuleParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final EbnfRuleParserToken token) {
        // nop
    }

    // EbnfLeafParserToken ....................................................................................

    final void acceptComment(final EbnfCommentParserToken token) {
        if (Visiting.CONTINUE == this.startVisit(token)) {
            this.visit(token);
        }
    }

    protected void visit(final EbnfCommentParserToken token) {
        // nop
    }

    final void acceptIdentifier(final EbnfIdentifierParserToken token) {
        if (Visiting.CONTINUE == this.startVisit(token)) {
            this.visit(token);
        }
        this.endVisit(token);
    }

    protected void visit(final EbnfIdentifierParserToken token) {
        // nop
    }

    protected void visit(final EbnfSymbolParserToken token) {
        // nop
    }

    protected void visit(final EbnfTerminalParserToken token) {
        // nop
    }

    protected void visit(final EbnfWhitespaceParserToken token) {
        // nop
    }

    // ParserToken.......................................................................

    protected Visiting startVisit(final ParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ParserToken token) {
        // nop
    }

    // EbnfParserToken.......................................................................

    protected Visiting startVisit(final EbnfParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final EbnfParserToken token) {
        // nop
    }
}
