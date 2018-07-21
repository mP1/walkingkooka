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
 */
package walkingkooka.text.cursor.parser.ebnf;

import walkingkooka.text.cursor.parser.ParserTokenNodeName;

import java.util.List;

/**
 * Represents a list of alternative token in the grammar.
 */
final public class EbnfAlternativeParserToken extends EbnfParentParserToken {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(EbnfAlternativeParserToken.class);

    static EbnfAlternativeParserToken with(final List<EbnfParserToken> tokens, final String text) {
        return new EbnfAlternativeParserToken(copyAndCheckTokens(tokens), checkText(text), WITHOUT_COMPUTE_REQUIRED);
    }

    private EbnfAlternativeParserToken(final List<EbnfParserToken> tokens, final String text, final boolean computeWithout) {
        super(tokens, text, computeWithout);
        this.checkAtLeastTwoTokens();
    }

    @Override
    public EbnfAlternativeParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    EbnfAlternativeParserToken replaceText(final String text) {
        return new EbnfAlternativeParserToken(this.value(), text, WITHOUT_COMPUTE_REQUIRED);
    }

    @Override
    EbnfAlternativeParserToken replaceTokens(final List<EbnfParserToken> tokens) {
        return new EbnfAlternativeParserToken(tokens, this.text(), WITHOUT_USE_THIS);
    }

    @Override
    public boolean isAlternative() {
        return true;
    }

    @Override
    public boolean isConcatenation() {
        return false;
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public boolean isRange() {
        return false;
    }

    @Override
    public boolean isRepeated() {
        return false;
    }

    @Override
    public boolean isRule() {
        return false;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof EbnfAlternativeParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
