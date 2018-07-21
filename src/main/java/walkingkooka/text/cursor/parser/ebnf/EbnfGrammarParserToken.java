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

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserTokenNodeName;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A grammar holds all the rules and is the root of the graph.
 */
public final class EbnfGrammarParserToken extends EbnfParserToken {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(EbnfGrammarParserToken.class);

    static EbnfGrammarParserToken with(final List<EbnfRuleParserToken> rules, final String text) {
        Objects.requireNonNull(rules, "rules");
        checkText(text);

        final List<EbnfRuleParserToken> copy = Lists.array();
        copy.addAll(rules);

        return new EbnfGrammarParserToken(copy, text);
    }

    EbnfGrammarParserToken(final List<EbnfRuleParserToken> rules, final String text) {
        super(text);
        this.rules = rules;
        this.identifierToParser = Maps.sorted();
    }

    @Override
    public EbnfGrammarParserToken setText(String text) {
        return this.setText0(text).cast();
    }

    @Override
    EbnfGrammarParserToken replaceText(final String text) {
        return new EbnfGrammarParserToken(this.rules, text);
    }

    /// need a get parser for identifier

    private final List<EbnfRuleParserToken> rules;

    private final Map<EbnfIdentifierParserToken, Parser<?, ?>> identifierToParser;

    public Optional<EbnfParserToken> withoutCommentsSymbolsOrWhitespace(){
        return Optional.of(this);
    }

    @Override
    public boolean isAlternative() {
        return false;
    }

    @Override
    public boolean isComment() {
        return false;
    }

    @Override
    public boolean isConcatenation() {
        return false;
    }

    @Override
    public boolean isGrammar() {
        return true;
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public boolean isIdentifier() {
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
    public ParserTokenNodeName name() {
        return NAME;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof EbnfGrammarParserToken;
    }

    @Override
    boolean equals1(final EbnfParserToken other) {
        return this.equals2(Cast.to(other));
    }

    private boolean equals2(final EbnfGrammarParserToken other) {
        return this.rules.equals(other.rules);
    }
}
