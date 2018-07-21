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
 * Represents an optional token in the grammar.
 */
public final class EbnfOptionalParserToken extends EbnfParentParserToken {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(EbnfOptionalParserToken.class);

    static EbnfOptionalParserToken with(final List<EbnfParserToken> tokens, final String text) {
        return new EbnfOptionalParserToken(copyAndCheckTokens(tokens), checkText(text), WITHOUT_COMPUTE_REQUIRED);
    }

    private EbnfOptionalParserToken(final List<EbnfParserToken> tokens, final String text, final boolean computeWithout) {
        super(tokens, text, computeWithout);
        this.checkOnlyOneToken();
    }

    @Override
    public EbnfOptionalParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    EbnfOptionalParserToken replaceText(final String text) {
        return new EbnfOptionalParserToken(this.value(), text, WITHOUT_COMPUTE_REQUIRED);
    }

    @Override
    EbnfOptionalParserToken replaceTokens(final List<EbnfParserToken> tokens) {
        return new EbnfOptionalParserToken(tokens, this.text(), WITHOUT_USE_THIS);
    }

    @Override
    public boolean isAlternative() {
        return false;
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
        return true;
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
        return other instanceof EbnfOptionalParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
