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

package walkingkooka.text.cursor.parser;

import walkingkooka.collect.list.Lists;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

/**
 * The {@link ParserTokenVisitor} used to flat that is replace {@link RepeatedOrSequenceParserToken} with their child tokens.
 */
final class RepeatedOrSequenceParserTokenFlatParserTokenVisitor extends ParserTokenVisitor {

    static List<ParserToken> flat(final ParserToken token) {
        final RepeatedOrSequenceParserTokenFlatParserTokenVisitor visitor = new RepeatedOrSequenceParserTokenFlatParserTokenVisitor();
        visitor.accept(token);
        return visitor.tokens;
    }

    RepeatedOrSequenceParserTokenFlatParserTokenVisitor() {
        super();
    }

    @Override
    protected Visiting startVisit(final ParserToken token) {
        this.add = token;
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final ParserToken token) {
        final ParserToken add = this.add;
        if(null!=add) {
            this.tokens.add(token);
        }
    }

    // filter desc repeat & sequence tokens.

    @Override
    protected void endVisit(final RepeatedParserToken token) {
        this.add = null;
    }

    @Override
    protected void endVisit(final SequenceParserToken token) {
        this.add = null;
    }

    private ParserToken add;

    private final List<ParserToken> tokens = Lists.array();

    @Override
    public String toString() {
        return this.tokens.toString();
    }
}
