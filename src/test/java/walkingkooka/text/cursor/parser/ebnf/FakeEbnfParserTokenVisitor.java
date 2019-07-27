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

package walkingkooka.text.cursor.parser.ebnf;

import walkingkooka.test.Fake;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.visit.Visiting;

abstract class FakeEbnfParserTokenVisitor extends EbnfParserTokenVisitor implements Fake {

    @Override
    protected Visiting startVisit(final EbnfGrammarParserToken token) {
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfGrammarParserToken token) {
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final EbnfAlternativeParserToken token) {
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfAlternativeParserToken token) {
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final EbnfConcatenationParserToken token) {
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfConcatenationParserToken token) {
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final EbnfExceptionParserToken token) {
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfExceptionParserToken token) {
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final EbnfGroupParserToken token) {
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfGroupParserToken token) {
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final EbnfOptionalParserToken token) {
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfOptionalParserToken token) {
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final EbnfRangeParserToken token) {
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfRangeParserToken token) {
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final EbnfRepeatedParserToken token) {
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfRepeatedParserToken token) {
        super.endVisit(token);
    }

    @Override
    protected Visiting startVisit(final EbnfRuleParserToken token) {
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfRuleParserToken token) {
        super.endVisit(token);
    }

    @Override
    protected void visit(final EbnfCommentParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final EbnfIdentifierParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final EbnfSymbolParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final EbnfTerminalParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final EbnfWhitespaceParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final ParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final ParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final EbnfParserToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final EbnfParserToken token) {
        throw new UnsupportedOperationException();
    }
}
