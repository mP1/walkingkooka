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

package walkingkooka.tree.text;

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value class that holds a font weight.
 */
public final class FontWeight implements Comparable<FontWeight>, HashCodeEqualsDefined, Value<Integer>, Serializable, HasJsonNode {

    private final static int NORMAL_VALUE = 400;
    private final static int BOLD_VALUE = 700;

    private final static String NORMAL_TEXT = "normal";
    private final static String BOLD_TEXT = "bold";

    /**
     * A constant holding the font-weight of normal text
     */
    public final static FontWeight NORMAL = new FontWeight(NORMAL_VALUE);

    /**
     * A constant holding the font-weight of bold text
     */
    public final static FontWeight BOLD = new FontWeight(BOLD_VALUE);

    /**
     * Factory that creates a {@link FontWeight}.
     */
    public static FontWeight with(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Invalid font-weight value=" + value);
        }

        return NORMAL_VALUE == value ?
                NORMAL :
                BOLD_VALUE == value ?
                        BOLD :
                        new FontWeight(value);
    }

    /**
     * Private constructor use static factory
     */
    private FontWeight(final int value) {
        super();
        this.value = value;
    }

    /**
     * Getter that returns the weight value as an integer
     */
    @Override
    public Integer value() {
        return this.value;
    }

    private final int value;

    // HasJsonNode .....................................................................................................

    /**
     * Factory that creates a {@link FontWeight} from the given node.
     */
    public static FontWeight fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        try {
            return node.isString() ?
                    fromJsonStringNode(node.stringValueOrFail()) :
                    with(node.numberValueOrFail().intValue());
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    private static FontWeight fromJsonStringNode(final String value) {
        return BOLD_TEXT.equals(value) ? BOLD :
                NORMAL_TEXT.equals(value) ? NORMAL :
                        fromJsonStringNode0(value);
    }

    private static FontWeight fromJsonStringNode0(final String value) {
        throw new IllegalArgumentException("Unknown font weight " + CharSequences.quote(value));
    }

    @Override
    public JsonNode toJsonNode() {
        return NORMAL_VALUE == this.value ?
                NORMAL_JSON :
                BOLD_VALUE == this.value ?
                        BOLD_JSON :
                        JsonNode.number(this.value);
    }

    private final static JsonNode NORMAL_JSON = JsonNode.string(NORMAL_TEXT);
    private final static JsonNode BOLD_JSON = JsonNode.string(BOLD_TEXT);

    static {
        HasJsonNode.register("font-weight",
                FontWeight::fromJsonNode,
                FontWeight.class);
    }

    // Comparable ...................................................................................................

    @Override
    public int compareTo(final FontWeight weight) {
        Objects.requireNonNull(weight, "weight");

        return this.value - weight.value;
    }

    // Object .........................................................................................................

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof FontWeight &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final FontWeight other) {
        return this.value == other.value;
    }

    /**
     * Dumps the weight value.
     */
    @Override
    public String toString() {
        final int value = this.value;
        return  NORMAL_VALUE == value ?
                NORMAL_TEXT :
                BOLD_VALUE == value ?
                        BOLD_TEXT :
                        String.valueOf(value);
    }

    // Serializable ..................................................................................................

    private final static long serialVersionUID = 1L;

    private Object readResolve() {
        final int value = this.value;

        return NORMAL_VALUE == value ?
                NORMAL :
                BOLD_VALUE == value ?
                        BOLD :
                        this;
    }
}
