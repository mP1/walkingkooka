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
package walkingkooka.text.cursor.parser;

import walkingkooka.Cast;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This {@link ParserToken} holds a sequence in order of tokens.
 */
public final class SequenceParserToken extends ParserTemplateToken2 implements SupportsFlat<SequenceParserToken, ParserToken> {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(SequenceParserToken.class);

    /**
     * Factory that wraps many tokens in a {@link SequenceParserToken}.
     */
    static <T extends ParserToken> SequenceParserToken with(final List tokens, final String text) {
        Objects.requireNonNull(tokens, "tokens");
        Objects.requireNonNull(text, "text");

        final int count = tokens.size();
        if(count <= 1) {
            throw new IllegalArgumentException("Expected more than 1 token but got " + count + "=" + tokens);
        }

        return new SequenceParserToken(tokens, text);
    }

    private SequenceParserToken(final List tokens, final String text) {
        super(tokens, text);
    }

    @Override
    public SequenceParserToken setText(final String text){
        return Cast.to(this.setText0(text));
    }

    @Override
    SequenceParserToken replaceText(final String text) {
        return with(this.value(), text);
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    public SequenceParserToken setValue(final List<ParserToken> value) {
        return Cast.to(this.setValue0(value));
    }

    @Override
    final SequenceParserToken replaceValue(final List<ParserToken> value) {
        return new SequenceParserToken(value, this.text());
    }

    /**
     * Removes any missing values, returning a new instance if necessary.
     */
    public SequenceParserToken removeMissing() {
        return this.setValue(this.value().stream()
                .filter( t -> ! t.isMissing())
                .collect(Collectors.toList()));
    }

    @Override
    public void accept(final ParserTokenVisitor visitor){
        if(Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    @Override
    public SequenceParserToken flat() {
        final List<ParserToken> tokens = this.value();
        final List<ParserToken> flat = this.flat(tokens);
        return tokens.equals(flat) ? this : new SequenceParserToken(flat, this.text());
    }

    /**
     * Asserts that the sequence contains the correct number of tokens throwing a {@link IllegalStateException} if the
     * test fails.
     */
    public void checkTokenCount(final int expected) {
        final List<ParserToken> list = this.value();
        final int actual = list.size();
        if(actual != expected ){
            throw new IllegalStateException("Expected " + expected + " but got " + actual + "=" + list);
        }
    }

    public <T extends ParserToken> Optional<T> optional(final int index, final Class<T> type) {
        final ParserToken token = this.token(index);
        return token.isMissing() ?
                Optional.empty() :
                Optional.of(type.cast(token));
    }

    public <T extends ParserToken> T required(final int index, final Class<T> type) {
        final ParserToken token = this.token(index);
        if(token.isMissing()){
            throw new MissingParserTokenException("Token " + index + " missing, tokens=" + this);
        }
        return type.cast(token);
    }

    public ParserToken token(final int index) {
        final List<ParserToken> tokens = this.value();
        try{
            return tokens.get(index);
        } catch (final IndexOutOfBoundsException cause){
            throw new IndexOutOfBoundsException("Invalid index " + index + " must be between 0 and " + tokens.size());
        }
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SequenceParserToken;
    }

    @Override
    public String toString() {
        return this.value().stream()
                .map( e -> String.valueOf(e))
                .collect(Collectors.joining(","));
    }
}
