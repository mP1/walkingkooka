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
package walkingkooka.text.cursor.parser.json;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.Whitespace;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a token within the grammar.
 */
public abstract class JsonNodeParserToken implements ParserToken {

    /**
     * Factory used by all sub classes to create their {@link ParserTokenNodeName} constants.
     */
    static ParserTokenNodeName parserTokenNodeName(final Class<? extends JsonNodeParserToken> type) {
        final String name = type.getSimpleName();
        return ParserTokenNodeName.with(name.substring(0, name.length() - ParserToken.class.getSimpleName().length()));
    }

    /**
     * {@see JsonNodeArrayParserToken}
     */
    public static JsonNodeArrayParserToken array(final List<ParserToken> value, final String text) {
        return JsonNodeArrayParserToken.with(value, text);
    }

    /**
     * {@see JsonNodeArrayBeginSymbolParserToken}
     */
    public static JsonNodeArrayBeginSymbolParserToken arrayBeginSymbol(final String value, final String text) {
        return JsonNodeArrayBeginSymbolParserToken.with(value, text);
    }

    /**
     * {@see JsonNodeArrayEndSymbolParserToken}
     */
    public static JsonNodeArrayEndSymbolParserToken arrayEndSymbol(final String value, final String text) {
        return JsonNodeArrayEndSymbolParserToken.with(value, text);
    }

    /**
     * {@see JsonNodeBooleanParserToken}
     */
    public static JsonNodeBooleanParserToken booleanParserToken(final boolean value, final String text) {
        return JsonNodeBooleanParserToken.with(value, text);
    }

    /**
     * {@see JsonNodeNullParserToken}
     */
    public static JsonNodeNullParserToken nullParserToken(final String text) {
        return JsonNodeNullParserToken.with(null, text);
    }

    /**
     * {@see JsonNodeNumberParserToken}
     */
    public static JsonNodeNumberParserToken number(final double value, final String text) {
        return JsonNodeNumberParserToken.with(value, text);
    }

    /**
     * {@see JsonNodeObjectParserToken}
     */
    public static JsonNodeObjectParserToken object(final List<ParserToken> value, final String text) {
        return JsonNodeObjectParserToken.with(value, text);
    }

    /**
     * {@see JsonNodeObjectAssignmentSymbolParserToken}
     */
    public static JsonNodeObjectAssignmentSymbolParserToken objectAssignmentSymbol(final String value, final String text) {
        return JsonNodeObjectAssignmentSymbolParserToken.with(value, text);
    }

    /**
     * {@see JsonNodeObjectBeginSymbolParserToken}
     */
    public static JsonNodeObjectBeginSymbolParserToken objectBeginSymbol(final String value, final String text) {
        return JsonNodeObjectBeginSymbolParserToken.with(value, text);
    }

    /**
     * {@see JsonNodeObjectEndSymbolParserToken}
     */
    public static JsonNodeObjectEndSymbolParserToken objectEndSymbol(final String value, final String text) {
        return JsonNodeObjectEndSymbolParserToken.with(value, text);
    }

    /**
     * {@see JsonNodeSeparatorSymbolParserToken}
     */
    public static JsonNodeSeparatorSymbolParserToken separatorSymbol(final String value, final String text) {
        return JsonNodeSeparatorSymbolParserToken.with(value, text);
    }

    /**
     * {@see JsonNodeStringParserToken}
     */
    public static JsonNodeStringParserToken string(final String value, final String text) {
        return JsonNodeStringParserToken.with(value, text);
    }

    /**
     * {@see JsonNodeWhitespaceParserToken}
     */
    public static JsonNodeWhitespaceParserToken whitespace(final String value, final String text) {
        return JsonNodeWhitespaceParserToken.with(value, text);
    }

    static List<ParserToken> copyAndCheckTokens(final List<ParserToken> tokens) {
        Objects.requireNonNull(tokens, "tokens");

        final List<ParserToken> copy = Lists.array();
        copy.addAll(tokens);
        return Lists.readOnly(copy);
    }

    static String checkText(final String text) {
        Whitespace.failIfNullOrWhitespace(text, "text");
        return text;
    }

    /**
     * Package private ctor to limit sub classing.
     */
    JsonNodeParserToken(final String text) {
        this.text = text;
    }

    @Override
    public final String text() {
        return this.text;
    }

    private final String text;

    final JsonNodeParserToken setText0(final String text) {
        Objects.requireNonNull(text, "text");
        return this.text.equals(text) ?
                this :
                replaceText(text);
    }

    abstract JsonNodeParserToken replaceText(final String text);

    /**
     * Returns a copy without any symbols or whitespace tokens. The original text form will still contain
     * those tokens as text, but the tokens themselves will be removed.
     */
    abstract public Optional<JsonNodeParserToken> withoutSymbolsOrWhitespace();

    /**
     * Only {@link JsonNodeArrayParserToken} return true
     */
    public abstract boolean isArray();

    /**
     * Only {@link JsonNodeArrayBeginSymbolParserToken} return true
     */
    public abstract boolean isArrayBeginSymbol();

    /**
     * Only {@link JsonNodeArrayEndSymbolParserToken} return true
     */
    public abstract boolean isArrayEndSymbol();

    /**
     * Only {@link JsonNodeBooleanParserToken} return true
     */
    public abstract boolean isBoolean();

    /**
     * Only {@link JsonNodeNullParserToken} return true
     */
    public abstract boolean isNull();

    /**
     * Only {@link JsonNodeNumberParserToken} return true
     */
    public abstract boolean isNumber();

    /**
     * Only {@link JsonNodeObjectParserToken} return true
     */
    public abstract boolean isObject();

    /**
     * Only {@link JsonNodeObjectAssignmentSymbolParserToken} return true
     */
    public abstract boolean isObjectAssignmentSymbol();

    /**
     * Only {@link JsonNodeObjectBeginSymbolParserToken} return true
     */
    public abstract boolean isObjectBeginSymbol();

    /**
     * Only {@link JsonNodeObjectEndSymbolParserToken} return true
     */
    public abstract boolean isObjectEndSymbol();

    /**
     * Only {@link JsonNodeSeparatorSymbolParserToken} return true
     */
    public abstract boolean isSeparatorSymbol();

    /**
     * Only {@link JsonNodeSymbolParserToken} return true
     */
    public abstract boolean isSymbol();

    /**
     * Only {@link JsonNodeStringParserToken} return true
     */
    public abstract boolean isString();

    // Visitor ......................................................................................................

    /**
     * Begins the visiting process.
     */
    public final void accept(final ParserTokenVisitor visitor) {
        final JsonNodeParserTokenVisitor visitor2 = Cast.to(visitor);
        final JsonNodeParserToken token = this;

        if (Visiting.CONTINUE == visitor2.startVisit(token)) {
            this.accept(JsonNodeParserTokenVisitor.class.cast(visitor));
        }
        visitor2.endVisit(token);
    }

    abstract public void accept(final JsonNodeParserTokenVisitor visitor);

    /**
     * Converts this token to its {@link JsonNode} equivalent. Note that {@link JsonNodeWhitespaceParserToken} will
     * be removed
     */
    final public Optional<JsonNode> toJsonNode() {
        return Optional.ofNullable(this.toJsonNodeOrNull());
    }

    /**
     * Returns the {@link JsonNode} form or null.
     */
    abstract JsonNode toJsonNodeOrNull();

    /**
     * Sub classes should add themselves to the list of children.
     */
    abstract void addJsonNode(final List<JsonNode> children);

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

    private boolean equals0(final JsonNodeParserToken other) {
        return this.text().equals(other.text()) &&
                this.equals1(other);
    }

    abstract boolean equals1(JsonNodeParserToken other);

    @Override
    public final String toString() {
        return this.text();
    }
}
