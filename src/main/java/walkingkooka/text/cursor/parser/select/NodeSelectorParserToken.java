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
 *
 */
package walkingkooka.text.cursor.parser.select;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.text.Whitespace;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a token within the xpath query grammar.
 */
public abstract class NodeSelectorParserToken implements ParserToken {

    /**
     * Factory used by all sub classes to create their {@link ParserTokenNodeName} constants.
     */
    static ParserTokenNodeName parserTokenNodeName(final Class<? extends NodeSelectorParserToken> type) {
        final String name = type.getSimpleName();
        return ParserTokenNodeName.with(name.substring(0, name.length() - ParserToken.class.getSimpleName().length()));
    }

    /**
     * {@see NodeSelectorAbsoluteParserToken}
     */
    public static NodeSelectorAbsoluteParserToken absolute(final String value, final String text) {
        return NodeSelectorAbsoluteParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorAncestorParserToken}
     */
    public static NodeSelectorAncestorParserToken ancestor(final String value, final String text) {
        return NodeSelectorAncestorParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorAndParserToken}
     */
    public static NodeSelectorAndParserToken and(final List<ParserToken> value, final String text) {
        return NodeSelectorAndParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorAndSymbolParserToken}
     */
    public static NodeSelectorAndSymbolParserToken andSymbol(final String value, final String text) {
        return NodeSelectorAndSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorAtSignSymbolParserToken}
     */
    public static NodeSelectorAtSignSymbolParserToken atSignSymbol(final String value, final String text) {
        return NodeSelectorAtSignSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorAttributeNameParserToken}
     */
    public static NodeSelectorAttributeNameParserToken attributeName(final NodeSelectorAttributeName value, final String text) {
        return NodeSelectorAttributeNameParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorBracketOpenSymbolParserToken}
     */
    public static NodeSelectorBracketOpenSymbolParserToken bracketOpenSymbol(final String value, final String text) {
        return NodeSelectorBracketOpenSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorBracketCloseSymbolParserToken}
     */
    public static NodeSelectorBracketCloseSymbolParserToken bracketCloseSymbol(final String value, final String text) {
        return NodeSelectorBracketCloseSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorChildParserToken}
     */
    public static NodeSelectorChildParserToken child(final String value, final String text) {
        return NodeSelectorChildParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorDescendantParserToken}
     */
    public static NodeSelectorDescendantParserToken descendant(final String value, final String text) {
        return NodeSelectorDescendantParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorEqualsParserToken}
     */
    public static NodeSelectorEqualsParserToken equalsParserToken(final List<ParserToken> value, final String text) {
        return NodeSelectorEqualsParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorEqualsSymbolParserToken}
     */
    public static NodeSelectorEqualsSymbolParserToken equalsSymbol(final String value, final String text) {
        return NodeSelectorEqualsSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorExpressionParserToken}
     */
    public static NodeSelectorExpressionParserToken expression(final List<ParserToken> value, final String text) {
        return NodeSelectorExpressionParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorFirstChildParserToken}
     */
    public static NodeSelectorFirstChildParserToken firstChild(final String value, final String text) {
        return NodeSelectorFirstChildParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorFollowingParserToken}
     */
    public static NodeSelectorFollowingParserToken following(final String value, final String text) {
        return NodeSelectorFollowingParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorFollowingSiblingParserToken}
     */
    public static NodeSelectorFollowingSiblingParserToken followingSibling(final String value, final String text) {
        return NodeSelectorFollowingSiblingParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorFunctionParserToken}
     */
    public static NodeSelectorFunctionParserToken function(final List<ParserToken> value, final String text) {
        return NodeSelectorFunctionParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorFunctionNameParserToken}
     */
    public static NodeSelectorFunctionNameParserToken functionName(final NodeSelectorFunctionName value, final String text) {
        return NodeSelectorFunctionNameParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorGreaterThanParserToken}
     */
    public static NodeSelectorGreaterThanParserToken greaterThan(final List<ParserToken> value, final String text) {
        return NodeSelectorGreaterThanParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorGreaterThanSymbolParserToken}
     */
    public static NodeSelectorGreaterThanSymbolParserToken greaterThanSymbol(final String value, final String text) {
        return NodeSelectorGreaterThanSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorGreaterThanEqualsParserToken}
     */
    public static NodeSelectorGreaterThanEqualsParserToken greaterThanEquals(final List<ParserToken> value, final String text) {
        return NodeSelectorGreaterThanEqualsParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorGreaterThanEqualsSymbolParserToken}
     */
    public static NodeSelectorGreaterThanEqualsSymbolParserToken greaterThanEqualsSymbol(final String value, final String text) {
        return NodeSelectorGreaterThanEqualsSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorLastChildParserToken}
     */
    public static NodeSelectorLastChildParserToken lastChild(final String value, final String text) {
        return NodeSelectorLastChildParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorLessThanParserToken}
     */
    public static NodeSelectorLessThanParserToken lessThan(final List<ParserToken> value, final String text) {
        return NodeSelectorLessThanParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorLessThanSymbolParserToken}
     */
    public static NodeSelectorLessThanSymbolParserToken lessThanSymbol(final String value, final String text) {
        return NodeSelectorLessThanSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorLessThanEqualsParserToken}
     */
    public static NodeSelectorLessThanEqualsParserToken lessThanEquals(final List<ParserToken> value, final String text) {
        return NodeSelectorLessThanEqualsParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorLessThanEqualsSymbolParserToken}
     */
    public static NodeSelectorLessThanEqualsSymbolParserToken lessThanEqualsSymbol(final String value, final String text) {
        return NodeSelectorLessThanEqualsSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorNodeNameParserToken}
     */
    public static NodeSelectorNodeNameParserToken nodeName(final NodeSelectorNodeName value, final String text) {
        return NodeSelectorNodeNameParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorNotEqualsParserToken}
     */
    public static NodeSelectorNotEqualsParserToken notEquals(final List<ParserToken> value, final String text) {
        return NodeSelectorNotEqualsParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorNotEqualsSymbolParserToken}
     */
    public static NodeSelectorNotEqualsSymbolParserToken notEqualsSymbol(final String value, final String text) {
        return NodeSelectorNotEqualsSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorNumberParserToken}
     */
    public static NodeSelectorNumberParserToken number(final long value, final String text) {
        return NodeSelectorNumberParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorOrParserToken}
     */
    public static NodeSelectorOrParserToken or(final List<ParserToken> value, final String text) {
        return NodeSelectorOrParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorOrSymbolParserToken}
     */
    public static NodeSelectorOrSymbolParserToken orSymbol(final String value, final String text) {
        return NodeSelectorOrSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorParameterSeparatorSymbolParserToken}
     */
    public static NodeSelectorParameterSeparatorSymbolParserToken parameterSeparatorSymbol(final String value, final String text) {
        return NodeSelectorParameterSeparatorSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorParenthesisOpenSymbolParserToken}
     */
    public static NodeSelectorParenthesisOpenSymbolParserToken parenthesisOpenSymbol(final String value, final String text) {
        return NodeSelectorParenthesisOpenSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorParenthesisCloseSymbolParserToken}
     */
    public static NodeSelectorParenthesisCloseSymbolParserToken parenthesisCloseSymbol(final String value, final String text) {
        return NodeSelectorParenthesisCloseSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorParentOfParserToken}
     */
    public static NodeSelectorParentOfParserToken parentOf(final String value, final String text) {
        return NodeSelectorParentOfParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorPrecedingParserToken}
     */
    public static NodeSelectorPrecedingParserToken preceding(final String value, final String text) {
        return NodeSelectorPrecedingParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorPrecedingSiblingParserToken}
     */
    public static NodeSelectorPrecedingSiblingParserToken precedingSibling(final String value, final String text) {
        return NodeSelectorPrecedingSiblingParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorPredicateParserToken}
     */
    public static NodeSelectorPredicateParserToken predicate(final List<ParserToken> value, final String text) {
        return NodeSelectorPredicateParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorQuotedTextParserToken}
     */
    public static NodeSelectorQuotedTextParserToken quotedText(final String value, final String text) {
        return NodeSelectorQuotedTextParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorSelfParserToken}
     */
    public static NodeSelectorSelfParserToken self(final String value, final String text) {
        return NodeSelectorSelfParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorSlashSeparatorSymbolParserToken}
     */
    public static NodeSelectorSlashSeparatorSymbolParserToken slashSeparatorSymbol(final String value, final String text) {
        return NodeSelectorSlashSeparatorSymbolParserToken.with(value, text);
    }

    /**
     * {@see NodeSelectorWhitespaceParserToken}
     */
    public static NodeSelectorWhitespaceParserToken whitespace(final String value, final String text) {
        return NodeSelectorWhitespaceParserToken.with(value, text);
    }

    static List<ParserToken> copyAndCheckTokens(final List<ParserToken> tokens) {
        Objects.requireNonNull(tokens, "tokens");

        final List<ParserToken> copy = Lists.array();
        copy.addAll(tokens);
        return Lists.readOnly(copy);
    }

    static String checkTextNullOrEmpty(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");
        return text;
    }

    static String checkTextNullOrWhitespace(final String text) {
        Whitespace.failIfNullOrWhitespace(text, "text");
        return text;
    }

    /**
     * Package private ctor to limit sub classing.
     */
    NodeSelectorParserToken(final String text) {
        this.text = text;
    }

    @Override
    public final String text() {
        return this.text;
    }

    private final String text;

    final NodeSelectorParserToken setText0(final String text) {
        this.checkText(text);
        return this.text.equals(text) ?
                this :
                replaceText(text);
    }

    abstract void checkText(final String text);

    abstract NodeSelectorParserToken replaceText(final String text);

    /**
     * Returns a copy without any symbols or whitespace tokens. The original text form will still contain
     * those tokens as text, but the tokens themselves will be removed.
     */
    abstract public Optional<NodeSelectorParserToken> withoutSymbols();

    /**
     * Only {@link NodeSelectorAbsoluteParserToken} return true
     */
    public abstract boolean isAbsolute();

    /**
     * Only {@link NodeSelectorAncestorParserToken} return true
     */
    public abstract boolean isAncestor();

    /**
     * Only {@link NodeSelectorAndParserToken} return true
     */
    public abstract boolean isAnd();

    /**
     * Only {@link NodeSelectorAndSymbolParserToken} return true
     */
    public abstract boolean isAndSymbol();

    /**
     * Only {@link NodeSelectorAtSignSymbolParserToken} return true
     */
    public abstract boolean isAtSignSymbol();

    /**
     * Only {@link NodeSelectorAttributeNameParserToken} return true
     */
    public abstract boolean isAttributeName();

    /**
     * Only {@link NodeSelectorBracketOpenSymbolParserToken} return true
     */
    public abstract boolean isBracketOpenSymbol();

    /**
     * Only {@link NodeSelectorBracketCloseSymbolParserToken} return true
     */
    public abstract boolean isBracketCloseSymbol();

    /**
     * Only {@link NodeSelectorChildParserToken} return true
     */
    public abstract boolean isChild();

    /**
     * Only {@link NodeSelectorDescendantParserToken} return true
     */
    public abstract boolean isDescendant();

    /**
     * Only {@link NodeSelectorEqualsParserToken} return true
     */
    public abstract boolean isEquals();

    /**
     * Only {@link NodeSelectorEqualsSymbolParserToken} return true
     */
    public abstract boolean isEqualsSymbol();

    /**
     * Only {@link NodeSelectorExpressionParserToken} return true
     */
    public abstract boolean isExpression();

    /**
     * Only {@link NodeSelectorFirstChildParserToken} return true
     */
    public abstract boolean isFirstChild();

    /**
     * Only {@link NodeSelectorFollowingParserToken} return true
     */
    public abstract boolean isFollowing();

    /**
     * Only {@link NodeSelectorFollowingSiblingParserToken} return true
     */
    public abstract boolean isFollowingSibling();

    /**
     * Only {@link NodeSelectorFunctionParserToken} return true
     */
    public abstract boolean isFunction();

    /**
     * Only {@link NodeSelectorFunctionNameParserToken} return true
     */
    public abstract boolean isFunctionName();

    /**
     * Only {@link NodeSelectorGreaterThanParserToken} return true
     */
    public abstract boolean isGreaterThan();

    /**
     * Only {@link NodeSelectorGreaterThanSymbolParserToken} return true
     */
    public abstract boolean isGreaterThanSymbol();

    /**
     * Only {@link NodeSelectorGreaterThanEqualsParserToken} return true
     */
    public abstract boolean isGreaterThanEquals();

    /**
     * Only {@link NodeSelectorGreaterThanEqualsSymbolParserToken} return true
     */
    public abstract boolean isGreaterThanEqualsSymbol();

    /**
     * Only {@link NodeSelectorLastChildParserToken} return true
     */
    public abstract boolean isLastChild();

    /**
     * Only {@link NodeSelectorLessThanParserToken} return true
     */
    public abstract boolean isLessThan();

    /**
     * Only {@link NodeSelectorLessThanSymbolParserToken} return true
     */
    public abstract boolean isLessThanSymbol();

    /**
     * Only {@link NodeSelectorLessThanEqualsParserToken} return true
     */
    public abstract boolean isLessThanEquals();

    /**
     * Only {@link NodeSelectorLessThanEqualsSymbolParserToken} return true
     */
    public abstract boolean isLessThanEqualsSymbol();

    /**
     * Only {@link NodeSelectorNodeNameParserToken} return true
     */
    public abstract boolean isNodeName();

    /**
     * Only {@link NodeSelectorNotEqualsParserToken} return true
     */
    public abstract boolean isNotEquals();

    /**
     * Only {@link NodeSelectorNotEqualsSymbolParserToken} return true
     */
    public abstract boolean isNotEqualsSymbol();

    /**
     * Only {@link NodeSelectorNumberParserToken} return true
     */
    public abstract boolean isNumber();

    /**
     * Only {@link NodeSelectorOrParserToken} return true
     */
    public abstract boolean isOr();

    /**
     * Only {@link NodeSelectorOrSymbolParserToken} return true
     */
    public abstract boolean isOrSymbol();

    /**
     * Only {@link NodeSelectorParameterSeparatorSymbolParserToken} return true
     */
    public abstract boolean isParameterSeparatorSymbol();

    /**
     * Only {@link NodeSelectorParenthesisOpenSymbolParserToken} return true
     */
    public abstract boolean isParenthesisOpenSymbol();

    /**
     * Only {@link NodeSelectorParenthesisCloseSymbolParserToken} return true
     */
    public abstract boolean isParenthesisCloseSymbol();

    /**
     * Only {@link NodeSelectorParentOfParserToken} return true
     */
    public abstract boolean isParentOf();

    /**
     * Only {@link NodeSelectorPrecedingParserToken} return true
     */
    public abstract boolean isPreceding();

    /**
     * Only {@link NodeSelectorPrecedingSiblingParserToken} return true
     */
    public abstract boolean isPrecedingSibling();

    /**
     * Only {@link NodeSelectorPredicateParserToken} return true
     */
    public abstract boolean isPredicate();

    /**
     * Only {@link NodeSelectorQuotedTextParserToken} return true
     */
    public abstract boolean isQuotedText();

    /**
     * Only {@link NodeSelectorSelfParserToken} return true
     */
    public abstract boolean isSelf();

    /**
     * Only {@link NodeSelectorSlashSeparatorSymbolParserToken} return true
     */
    public abstract boolean isSlashSeparatorSymbol();

    /**
     * Only {@link NodeSelectorSymbolParserToken} return true
     */
    public abstract boolean isSymbol();

    // Visitor ......................................................................................................

    /**
     * Begins the visiting process.
     */
    public final void accept(final ParserTokenVisitor visitor) {
        final NodeSelectorParserTokenVisitor visitor2 = Cast.to(visitor);
        final NodeSelectorParserToken token = this;

        if (Visiting.CONTINUE == visitor2.startVisit(token)) {
            this.accept(NodeSelectorParserTokenVisitor.class.cast(visitor));
        }
        visitor2.endVisit(token);
    }

    abstract public void accept(final NodeSelectorParserTokenVisitor visitor);

    // Object ...........................................................................................................

    @Override
    public final int hashCode() {
        return this.text().hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final NodeSelectorParserToken other) {
        return this.text().equals(other.text()) &&
                this.equals1(other);
    }

    abstract boolean equals1(NodeSelectorParserToken other);

    @Override
    public final String toString() {
        return this.text();
    }
}
