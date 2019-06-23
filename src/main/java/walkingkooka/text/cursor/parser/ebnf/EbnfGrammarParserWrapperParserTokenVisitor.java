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

import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.function.BiFunction;

final class EbnfGrammarParserWrapperParserTokenVisitor extends EbnfParserTokenVisitor {

    static ParserToken wrap(final ParserToken token,
                            final BiFunction<List<ParserToken>, String, ParserToken> wrapper) {
        final EbnfGrammarParserWrapperParserTokenVisitor visitor = new EbnfGrammarParserWrapperParserTokenVisitor();
        visitor.accept(token);
        return wrapper.apply(visitor.tokens, token.text());
    }

    EbnfGrammarParserWrapperParserTokenVisitor() {
        super();
    }

    @Override
    protected Visiting startVisit(final EbnfParserToken token) {
        this.tokens.add(token);
        return Visiting.SKIP;
    }

    private List<ParserToken> tokens = Lists.array();

    @Override
    public String toString() {
        return this.tokens.toString();
    }
}
