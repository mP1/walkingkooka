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

/**
 * A measurement in pixels.
 */
public final class Pixels extends Length implements HasJsonNode, Value<Double> {

    final static String UNIT = "px";

    /**
     * Parses text that contains a pixel measurement, note the unit is required.
     */
    public static Pixels parsePixels(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");

        return parsePixels0(text);
    }

    static Pixels parsePixels0(final String text) {
        if (!text.endsWith(UNIT)) {
            throw new IllegalArgumentException("Text " + CharSequences.quoteAndEscape(text) + " missing " + CharSequences.quoteAndEscape(UNIT));
        }

        try {
            return with(Double.parseDouble(text.substring(0, text.length() - 2)));
        } catch (final NumberFormatException cause) {
            throw new IllegalArgumentException("Invalid pixel count " + CharSequences.quoteAndEscape(text), cause);
        }
    }

    static Pixels with(final double value) {
        return new Pixels(value);
    }

    private Pixels(final double value) {
        super();
        this.value = value;
    }

    @Override
    public Double value() {
        return this.value;
    }

    private final double value;

    @Override
    double doubleValue() {
        return this.value;
    }

    @Override
    public boolean isPixels() {
        return true;
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof Pixels;
    }

    @Override
    boolean equals0(final Length other) {
        return 0 == Double.compare(this.value, other.doubleValue());
    }

    @Override
    public String toString() {
        final double value = this.value;
        final long longValue = Math.round(value);
        return longValue == value ?
                longValue + UNIT :
                doubleValue() + UNIT;
    }

    // HasJsonNode......................................................................................................

    /**
     * Accepts a json string holding a number and px unit suffix.
     */
    public static Pixels fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        try {
            return parsePixels(node.stringValueOrFail());
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    @Override
    public JsonNode toJsonNode() {
        return JsonNode.string(this.toString());
    }
}
