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

import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenNodeName;

import java.util.List;

/**
 * Represents an grouped token in the grammar.
 */
public final class EbnfGroupParserToken extends EbnfParentParserToken {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(EbnfGroupParserToken.class);

    static EbnfGroupParserToken with(final List<EbnfParserToken> tokens, final String text) {
        return new EbnfGroupParserToken(copyAndCheckTokens(tokens), checkText(text));
    }

    EbnfGroupParserToken(final List<EbnfParserToken> tokens, final String text) {
        super(tokens, text);
    }

    @Override
    public EbnfGroupParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    EbnfGroupParserToken replaceText(final String text) {
        return new EbnfGroupParserToken(this.value(), text);
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
        return true;
    }

    @Override
    public boolean isOptional() {
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
        return other instanceof EbnfGroupParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
