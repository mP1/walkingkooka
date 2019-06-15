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
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;

import java.util.Objects;
import java.util.Optional;

/**
 * A measurement in pixels.
 */
public final class PixelLength extends Length<Double> implements HasJsonNode, Value<Double> {

    private final static LengthUnit<Double, PixelLength> UNIT = LengthUnit.PIXEL;

    /**
     * Parses text that contains a pixel measurement, note the unit is required.
     */
    public static PixelLength parsePixels(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");

        return parsePixels0(text);
    }

    static PixelLength parsePixels0(final String text) {
        UNIT.parseUnitTextCheck(text);

        try {
            return with(Double.parseDouble(text.substring(0, text.length() - 2)));
        } catch (final NumberFormatException cause) {
            throw new IllegalArgumentException("Invalid pixel count " + CharSequences.quoteAndEscape(text), cause);
        }
    }

    static PixelLength with(final double value) {
        return new PixelLength(value);
    }

    private PixelLength(final double value) {
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
    long longValue() {
        throw new UnsupportedOperationException();
    }

    // unit.............................................................................................................

    @Override
    public Optional<LengthUnit<Double, Length<Double>>> unit() {
        return UNIT_OPTIONAL;
    }

    private final static Optional<LengthUnit<Double, Length<Double>>> UNIT_OPTIONAL = Cast.to(Optional.of(UNIT));

    // isXXX............................................................................................................

    @Override
    public boolean isNone() {
        return false;
    }

    @Override
    public boolean isNormal() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isPixel() {
        return true;
    }

    @Override
    void pixelOrFail() {
        // pixel
    }

    @Override
    void normalOrPixelOrFail() {
        // pixel
    }

    @Override
    void numberFail() {
        this.numberFail0();
    }

    // LengthVisitor....................................................................................................

    @Override
    void accept(final LengthVisitor visitor) {
        visitor.visit(this);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof PixelLength;
    }

    @Override
    boolean equals0(final Length other) {
        return 0 == Double.compare(this.value, other.doubleValue());
    }

    @Override
    public String toString() {
        return UNIT.toString(this.value);
    }

    // HasJsonNode......................................................................................................

    /**
     * Accepts a json string holding a number and px unit suffix.
     */
    public static PixelLength fromJsonNodePixel(final JsonNode node) {
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
