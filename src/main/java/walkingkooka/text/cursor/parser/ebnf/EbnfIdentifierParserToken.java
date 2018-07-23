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

import walkingkooka.naming.Name;
import walkingkooka.text.cursor.parser.ParserTokenNodeName;

import java.util.Optional;

/**
 * Holds the text for an identifier. Identifiers may appear on the left of a definition or as a reference to another rule definition.
 */
public final class EbnfIdentifierParserToken extends EbnfLeafParserToken<String> implements Name, Comparable<EbnfIdentifierParserToken> {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(EbnfIdentifierParserToken.class);

    final static String OPEN = "(*";
    final static String CLOSE = "*)";

    static EbnfIdentifierParserToken with(final String value, final String text){
        checkValue(value);
        checkText(text);

        return new EbnfIdentifierParserToken(value, text);
    }

    private EbnfIdentifierParserToken(final String value, final String text){
        super(value, text);
    }

    @Override
    public EbnfIdentifierParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    EbnfIdentifierParserToken replaceText(final String text) {
        return new EbnfIdentifierParserToken(this.value, text);
    }

    @Override
    public Optional<EbnfParserToken> withoutCommentsSymbolsOrWhitespace(){
        return Optional.of(this);
    }

    @Override
    public boolean isComment() {
        return false;
    }

    @Override
    public boolean isIdentifier() {
        return true;
    }

    @Override
    public boolean isSymbol() {
        return false;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean isWhitespace() {
        return false;
    }

    @Override
    public void accept(final EbnfParserTokenVisitor visitor){
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof EbnfIdentifierParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    @Override
    public int compareTo(final EbnfIdentifierParserToken other) {
        return this.value.compareTo(other.value);
    }
}
