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

package walkingkooka.text.cursor.parser;

import walkingkooka.tree.visit.Visiting;
import walkingkooka.tree.visit.Visitor;

import java.util.List;
import java.util.Objects;

public abstract class ParserTokenVisitor extends Visitor<ParserToken> {

    // ParserToken.......................................................................

    public final void accept(final ParserToken token) {
        Objects.requireNonNull(token, "token");

        if(Visiting.CONTINUE == this.startVisit(token)) {
            token.accept(this);
        }
        this.endVisit(token);
    }

    protected Visiting startVisit(final ParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ParserToken token) {
        // nop
    }

    protected void visit(final CharacterParserToken token) {
        // nop
    }

    protected void visit(final DecimalParserToken token) {
        // nop
    }

    protected void visit(final DoubleParserToken token) {
        // nop
    }

    protected void visit(final DoubleQuotedParserToken token) {
        // nop
    }

    protected void visit(final LongParserToken token) {
        // nop
    }

    protected void visit(final MissingParserToken token) {
        // nop
    }

    protected void visit(final NumberParserToken token) {
        // nop
    }

    protected Visiting startVisit(final RepeatedParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final RepeatedParserToken token) {
        // nop
    }

    protected Visiting startVisit(final SequenceParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SequenceParserToken token) {
        // nop
    }

    protected void visit(final SingleQuotedParserToken token) {
        // nop
    }

    protected void visit(final SignParserToken token) {
        // nop
    }

    protected void visit(final StringParserToken token) {
        // nop
    }
    
    /**
     * Useful to dispatch and visit all the child nodes of a parent.
     */
    protected final void acceptTokens(final List<? extends ParserToken> tokens) {
        tokens.stream().forEach( t -> this.accept(t));
    }
}