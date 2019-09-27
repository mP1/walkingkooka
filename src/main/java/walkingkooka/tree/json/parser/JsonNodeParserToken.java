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
package walkingkooka.tree.json.parser;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.Whitespace;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a token within the grammar.
 */
public abstract class JsonNodeParserToken implements ParserToken {

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
        return Lists.immutable(tokens);
    }

    static String checkText(final String text) {
        Whitespace.failIfNullOrEmptyOrWhitespace(text, "text");
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

    /**
     * Fetches the value which may be a scalar or child tokens.
     */
    abstract Object value();

    // isXXX.............................................................................................................

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
    @Override
    public final void accept(final ParserTokenVisitor visitor) {
        if (visitor instanceof JsonNodeParserTokenVisitor) {
            final JsonNodeParserTokenVisitor visitor2 = Cast.to(visitor);

            if (Visiting.CONTINUE == visitor2.startVisit(this)) {
                this.accept(visitor2);
            }
            visitor2.endVisit(this);
        }
    }

    abstract void accept(final JsonNodeParserTokenVisitor visitor);

    /**
     * Converts this token to its {@link JsonNode} equivalent. Note that {@link JsonNodeWhitespaceParserToken} will
     * be removed
     */
    final public Optional<JsonNode> marshall() {
        return Optional.ofNullable(this.marshallOrNull());
    }

    /**
     * Returns the {@link JsonNode} form or null.
     */
    abstract JsonNode marshallOrNull();

    /**
     * Sub classes should add themselves to the list of children.
     */
    abstract void addJsonNode(final List<JsonNode> children);

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

    private boolean equals0(final JsonNodeParserToken other) {
        return this.text().equals(other.text()) &&
                Objects.equals(this.value(), other.value()); // needs to be null safe because of JsonNodeNullParserToken
    }

    @Override
    public final String toString() {
        return this.text();
    }
}
