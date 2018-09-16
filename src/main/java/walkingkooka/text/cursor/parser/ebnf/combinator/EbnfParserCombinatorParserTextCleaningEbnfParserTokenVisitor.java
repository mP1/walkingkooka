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

package walkingkooka.text.cursor.parser.ebnf.combinator;

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.stack.Stack;
import walkingkooka.collect.stack.Stacks;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfAlternativeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfCommentParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfConcatenationParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfExceptionParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGroupParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfOptionalParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfParserTokenVisitor;
import walkingkooka.text.cursor.parser.ebnf.EbnfRangeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRepeatedParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRuleParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfSymbolParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfTerminalParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfWhitespaceParserToken;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.function.BiFunction;

/**
 * This visitor returns a new grammar, with comments removed, and text normalized, with a goal of reading compact
 * representations of the rule which becomes the toString of created parsers.
 */
final class EbnfParserCombinatorParserTextCleaningEbnfParserTokenVisitor extends EbnfParserTokenVisitor {

    static EbnfGrammarParserToken clean(final EbnfGrammarParserToken grammar) {
        final EbnfParserCombinatorParserTextCleaningEbnfParserTokenVisitor visitor = new EbnfParserCombinatorParserTextCleaningEbnfParserTokenVisitor();
        visitor.accept(grammar);
        return visitor.children.get(0).cast();
    }

    private EbnfParserCombinatorParserTextCleaningEbnfParserTokenVisitor(){
        super();
    }

    @Override
    protected Visiting startVisit(final EbnfGrammarParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfGrammarParserToken token) {
        this.exit(EbnfParserToken::grammar);
    }

    @Override
    protected Visiting startVisit(final EbnfAlternativeParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfAlternativeParserToken token) {
        this.exit(EbnfParserToken::alternative);
    }

    @Override
    protected Visiting startVisit(final EbnfConcatenationParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfConcatenationParserToken token) {
        this.exit(EbnfParserToken::concatenation);
    }

    @Override
    protected Visiting startVisit(final EbnfExceptionParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfExceptionParserToken token) {
        this.exit(EbnfParserToken::exception);
    }

    @Override
    protected Visiting startVisit(final EbnfGroupParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfGroupParserToken token) {
        this.exit(EbnfParserToken::group);
    }

    @Override
    protected Visiting startVisit(final EbnfOptionalParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfOptionalParserToken token) {
        this.exit(EbnfParserToken::optional);
    }

    @Override
    protected Visiting startVisit(final EbnfRangeParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfRangeParserToken token) {
        this.exit(EbnfParserToken::range);
    }

    @Override
    protected Visiting startVisit(final EbnfRepeatedParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfRepeatedParserToken token) {
        this.exit(EbnfParserToken::repeated);
    }

    @Override
    protected Visiting startVisit(final EbnfRuleParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfRuleParserToken token) {
        this.exit(EbnfParserToken::rule);
    }

    @Override
    protected void visit(final EbnfCommentParserToken token) {
        // ignore comments..
    }

    @Override
    protected void visit(final EbnfIdentifierParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final EbnfSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final EbnfTerminalParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final EbnfWhitespaceParserToken token) {
        // only want a single whitespace not more wasteful examples...
        this.add(token.setText(" "));
    }

    // GENERAL PURPOSE .................................................................................................

    private void enter() {
        this.previousChildren = this.previousChildren.push(this.children);
        this.children = Lists.array();
    }

    private void exit(final BiFunction<List<ParserToken>, String, EbnfParserToken> factory) {
        final List<ParserToken> children = this.children;
        this.children = this.previousChildren.peek();
        this.previousChildren = this.previousChildren.pop();
        this.add(factory.apply(children, ParserToken.text(children)));
    }

    private void add(final EbnfParserToken token) {
        if (null == token) {
            throw new NullPointerException("Null token returned for " + token);
        }
        this.children.add(token);
    }

    private Stack<List<ParserToken>> previousChildren = Stacks.arrayList();

    private List<ParserToken> children = Lists.array();

    @Override
    public String toString() {
        return this.children + "," + this.previousChildren;
    }
}
