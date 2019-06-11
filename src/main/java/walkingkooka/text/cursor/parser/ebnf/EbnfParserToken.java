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
import walkingkooka.text.Whitespace;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a token within an EBNF grammar.
 */
public abstract class EbnfParserToken implements ParserToken {

    /**
     * {@see EbnfAlternativeParserToken}
     */
    public static EbnfAlternativeParserToken alternative(final List<ParserToken> tokens, final String text) {
        return EbnfAlternativeParserToken.with(tokens, text);
    }

    /**
     * {@see EbnfCommentParserToken}
     */
    public static EbnfCommentParserToken comment(final String value, final String text) {
        return EbnfCommentParserToken.with(value, text);
    }

    /**
     * {@see EbnfConcatenationParserToken}
     */
    public static EbnfConcatenationParserToken concatenation(final List<ParserToken> tokens, final String text) {
        return EbnfConcatenationParserToken.with(tokens, text);
    }

    /**
     * {@see EbnfExceptionParserToken}
     */
    public static EbnfExceptionParserToken exception(final List<ParserToken> tokens, final String text) {
        return EbnfExceptionParserToken.with(tokens, text);
    }

    /**
     * {@see EbnfGrammarParserToken}
     */
    public static EbnfGrammarParserToken grammar(final List<ParserToken> tokens, final String text) {
        return EbnfGrammarParserToken.with(tokens, text);
    }

    /**
     * {@see EbnfGroupParserToken}
     */
    public static EbnfGroupParserToken group(final List<ParserToken> tokens, final String text) {
        return EbnfGroupParserToken.with(tokens, text);
    }

    /**
     * {@see EbnfIdentifierParserToken}
     */
    public static EbnfIdentifierParserToken identifier(final EbnfIdentifierName value, final String text) {
        return EbnfIdentifierParserToken.with(value, text);
    }

    /**
     * {@see EbnfOptionalParserToken}
     */
    public static EbnfOptionalParserToken optional(final List<ParserToken> tokens, final String text) {
        return EbnfOptionalParserToken.with(tokens, text);
    }

    /**
     * {@see EbnfRangeParserToken}
     */
    public static EbnfRangeParserToken range(final List<ParserToken> tokens, final String text) {
        return EbnfRangeParserToken.with(tokens, text);
    }

    /**
     * {@see EbnfRepeatedParserToken}
     */
    public static EbnfRepeatedParserToken repeated(final List<ParserToken> tokens, final String text) {
        return EbnfRepeatedParserToken.with(tokens, text);
    }

    /**
     * {@see EbnfRuleParserToken}
     */
    public static EbnfRuleParserToken rule(final List<ParserToken> tokens, final String text) {
        return EbnfRuleParserToken.with(tokens, text);
    }

    /**
     * {@see EbnfSymbolParserToken}
     */
    public static EbnfSymbolParserToken symbol(final String value, final String text) {
        return EbnfSymbolParserToken.with(value, text);
    }

    /**
     * {@see EbnfTerminalParserToken}
     */
    public static EbnfTerminalParserToken terminal(final String value, final String text) {
        return EbnfTerminalParserToken.with(value, text);
    }

    /**
     * {@see EbnfWhitespaceParserToken}
     */
    public static EbnfWhitespaceParserToken whitespace(final String value, final String text) {
        return EbnfWhitespaceParserToken.with(value, text);
    }

    static List<ParserToken> copyAndCheckTokens(final List<ParserToken> tokens) {
        Objects.requireNonNull(tokens, "tokens");

        final List<ParserToken> copy = Lists.immutable(tokens);
        if (copy.isEmpty()) {
            throw new IllegalArgumentException("Tokens is empty");
        }
        return copy;
    }

    static String checkText(final String text) {
        Whitespace.failIfNullOrEmptyOrWhitespace(text, "text");
        return text;
    }

    /**
     * {@see EbnfGrammarParser}
     */
    public static Parser<EbnfParserContext> grammarParser() {
        return EbnfGrammarParser.GRAMMAR;
    }

    /**
     * Package private ctor to limit sub classing.
     */
    EbnfParserToken(final String text) {
        this.text = text;
    }

    @Override
    public final String text() {
        return this.text;
    }

    private final String text;

    /**
     * Value getter, used within equals.
     */
    abstract Object value();

    /**
     * Sub classes must override. Not all types of token support this operation, eg this doesnt make sense
     * given a {@link EbnfCommentParserToken} will return empty.
     */
    abstract public Optional<EbnfParserToken> withoutCommentsSymbolsOrWhitespace();

    // isXXX............................................................................................................

    /**
     * Only alternative tokens return true
     */
    public abstract boolean isAlternative();

    /**
     * Only comment tokens return true
     */
    public abstract boolean isComment();

    /**
     * Only concatenation tokens return true
     */
    public abstract boolean isConcatenation();

    /**
     * Only exception tokens return true
     */
    public abstract boolean isException();

    /**
     * Only grouping tokens return true
     */
    public abstract boolean isGroup();

    /**
     * Only grammar tokens return true
     */
    public abstract boolean isGrammar();

    /**
     * Only identifiers return true
     */
    public abstract boolean isIdentifier();

    /**
     * Only optional tokens return true
     */
    public abstract boolean isOptional();

    /**
     * Only range tokens return true
     */
    public abstract boolean isRange();

    /**
     * Only repeating tokens return true
     */
    public abstract boolean isRepeated();

    /**
     * Only rule tokens return true
     */
    public abstract boolean isRule();

    /**
     * Only symbols tokens return true
     */
    public abstract boolean isSymbol();

    /**
     * Only terminals return true
     */
    public abstract boolean isTerminal();

    // EbnfParserTokenVisitor............................................................................................

    public final void accept(final ParserTokenVisitor visitor) {
        final EbnfParserTokenVisitor ebnfParserTokenVisitor = Cast.to(visitor);
        final EbnfParserToken token = this;

        if (Visiting.CONTINUE == ebnfParserTokenVisitor.startVisit(token)) {
            this.accept(EbnfParserTokenVisitor.class.cast(visitor));
        }
        ebnfParserTokenVisitor.endVisit(token);
    }

    abstract public void accept(final EbnfParserTokenVisitor visitor);

    // Object ...........................................................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(this.text, this.value());
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final EbnfParserToken other) {
        return this.text.equals(other.text) &&
                this.value().equals(other.value());
    }

    @Override
    public final String toString() {
        return this.text();
    }
}
