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
 * Value class that holds an opacity value.
 */
public final class Opacity implements Comparable<Opacity>, HashCodeEqualsDefined, Value<Double>, Serializable, HasJsonNode {

    private final static double TRANSPARENT_VALUE = 0;
    private final static double OPAQUE_VALUE = 1;

    private final static String TRANSPARENT_TEXT = "transparent";
    private final static String OPAQUE_TEXT = "opaque";

    private final static double PERCENTAGE_FACTOR = 100;

    /**
     * A constant holding the opacity of TRANSPARENT text
     */
    public final static Opacity TRANSPARENT = new Opacity(TRANSPARENT_VALUE);

    /**
     * A constant holding the opacity of OPAQUE text
     */
    public final static Opacity OPAQUE = new Opacity(OPAQUE_VALUE);

    /**
     * Factory that creates a {@link Opacity}.
     */
    public static Opacity with(final double value) {
        if (value < 0 || value > 1.0) {
            throw new IllegalArgumentException("Value " + value + " out of range 0.0 - 1.0");
        }

        return TRANSPARENT_VALUE == value ?
                TRANSPARENT :
                OPAQUE_VALUE == value ?
                        OPAQUE :
                        new Opacity(value);
    }

    /**
     * Private constructor use static factory
     */
    private Opacity(final double value) {
        super();
        this.value = value;
    }

    /**
     * Getter that returns the weight value as an Double
     */
    @Override
    public Double value() {
        return this.value;
    }

    private final double value;

    // HasJsonNode .....................................................................................................

    /**
     * Factory that creates a {@link Opacity} from the given node.
     */
    public static Opacity fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        try {
            return node.isString() ?
                    fromJsonStringNode(node.stringValueOrFail()) :
                    with(node.numberValueOrFail().doubleValue());
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    private static Opacity fromJsonStringNode(final String value) {
        return OPAQUE_TEXT.equals(value) ? OPAQUE :
                TRANSPARENT_TEXT.equals(value) ? TRANSPARENT :
                        fromJsonStringNode0(value);
    }

    private static Opacity fromJsonStringNode0(final String value) {
        throw new IllegalArgumentException("Unknown opacity " + CharSequences.quote(value));
    }

    @Override
    public JsonNode toJsonNode() {
        return TRANSPARENT_VALUE == this.value ?
                TRANSPARENT_JSON :
                OPAQUE_VALUE == this.value ?
                        OPAQUE_JSON :
                        JsonNode.number(this.value);
    }

    private final static JsonNode TRANSPARENT_JSON = JsonNode.string(TRANSPARENT_TEXT);
    private final static JsonNode OPAQUE_JSON = JsonNode.string(OPAQUE_TEXT);

    static {
        HasJsonNode.register("opacity",
                Opacity::fromJsonNode,
                Opacity.class);
    }

    // Comparable ...................................................................................................

    @Override
    public int compareTo(final Opacity other) {
        Objects.requireNonNull(other, "other");

        return Double.compare(this.value, other.value);
    }

    // Object .........................................................................................................

    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof Opacity &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final Opacity other) {
        return this.value == other.value;
    }

    /**
     * Dumps the opacity as a percentage.
     */
    @Override
    public String toString() {
        return TRANSPARENT == this?
                TRANSPARENT_TEXT :
                OPAQUE == this ?
                        OPAQUE_TEXT :
                        formatPercentage();
    }

    private String formatPercentage() {
        final String percentage = "" + PERCENTAGE_FACTOR * this.value;
        return (percentage.endsWith(".0") ?
                percentage.substring(0, percentage.length() - 2) :
                percentage).concat("%");
    }

    // Serializable ..................................................................................................

    private final static long serialVersionUID = 1L;

    private Object readResolve() {
        final double value = this.value;

        return TRANSPARENT_VALUE == value ?
                TRANSPARENT :
                OPAQUE_VALUE == value ?
                        OPAQUE :
                        this;
    }
}
