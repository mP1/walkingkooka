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

import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.visit.Visiting;

import java.util.List;

/**
 * Represents a list of alternative token in the grammar.
 */
final public class EbnfRangeParserToken extends EbnfParentParserToken<EbnfRangeParserToken> {

    static EbnfRangeParserToken with(final List<ParserToken> tokens, final String text) {
        final List<ParserToken> copy = copyAndCheckTokens(tokens);
        checkText(text);

        return new EbnfRangeParserToken(copy, text);
    }

    private EbnfRangeParserToken(final List<ParserToken> tokens,
                                 final String text) {
        super(tokens, text);
        this.checkOnlyTwoTokens();

        final EbnfRangeParserTokenConsumer checker = EbnfRangeParserTokenConsumer.with();
        tokens.stream()
                .filter(t -> t instanceof EbnfParserToken)
                .map(EbnfParserToken.class::cast)
                .forEach(checker);

        final EbnfParserToken begin = checker.begin;
        if (null == begin) {
            throw new IllegalArgumentException("Range missing begin|identifier=" + text);
        }
        final EbnfParserToken end = checker.end;
        if (null == end) {
            throw new IllegalArgumentException("Range missing end terminal|identifier=" + text);
        }

        this.begin = begin;
        this.end = end;
    }

    public EbnfParserToken begin() {
        return this.begin;
    }

    private final EbnfParserToken begin;

    public EbnfParserToken end() {
        return this.end;
    }

    private final EbnfParserToken end;

    // EbnfParserTokenVisitor............................................................................................

    @Override
    public void accept(final EbnfParserTokenVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // Object...........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof EbnfRangeParserToken;
    }

}
