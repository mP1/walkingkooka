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

package walkingkooka.tree.text;

import walkingkooka.Value;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;

import java.util.Objects;
import java.util.Optional;

/**
 * A normal measurement.
 */
public final class NormalLength extends Length<Void> implements HasJsonNode, Value<Void> {

    final static String TEXT = "normal";

    /**
     * Parses text that contains a normal literal.
     */
    public static NormalLength parseNormal(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");

        return parseNormal0(text);
    }

    static NormalLength parseNormal0(final String text) {
        checkText(text);

        if(!TEXT.equals(text)) {
            throw new IllegalArgumentException("Invalid normal text " + CharSequences.quoteAndEscape(text));
        }
        return INSTANCE;
    }

    final static NormalLength INSTANCE = new NormalLength();

    private NormalLength() {
        super();
    }

    @Override
    public Void value() {
        throw new UnsupportedOperationException();
    }

    @Override
    double doubleValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    long longValue() {
        throw new UnsupportedOperationException();
    }

    // unit.............................................................................................................

    @Override
    public Optional<LengthUnit<Void, Length<Void>>> unit() {
        return Optional.empty();
    }

    // isXXX............................................................................................................

    @Override
    public boolean isNormal() {
        return true;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isPixel() {
        return false;
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NormalLength;
    }

    @Override
    boolean equals0(final Length other) {
        return true;
    }

    @Override
    public String toString() {
        return TEXT;
    }

    // HasJsonNode......................................................................................................

    /**
     * Accepts a json string holding a number and px unit suffix.
     */
    public static NormalLength fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        try {
            return parseNormal(node.stringValueOrFail());
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    @Override
    public JsonNode toJsonNode() {
        return JsonNode.string(this.toString());
    }
}
