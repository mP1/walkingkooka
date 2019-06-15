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

import walkingkooka.Value;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;

import java.util.Objects;
import java.util.Optional;

/**
 * A positive whole number
 */
public final class NumberLength extends Length<Long> implements HasJsonNode, Value<Long> {

    /**
     * Parses text that contains a number measurement.
     */
    public static NumberLength parseNumber(final String text) {
        checkText(text);

        return parseNumber0(text);
    }

    static NumberLength parseNumber0(final String text) {
        checkText(text);

        try {
            return with(Long.parseLong(text));
        } catch (final NumberFormatException cause) {
            throw new IllegalArgumentException("Invalid text " + CharSequences.quoteAndEscape(text), cause);
        }
    }

    static NumberLength with(final Long value) {
        Objects.requireNonNull(value, "value");

        if(value < 0) {
            throw new IllegalArgumentException("Invalid value " + value);
        }

        return new NumberLength(value);
    }

    private NumberLength(final Long value) {
        super();
        this.value = value;
    }

    @Override
    public Long value() {
        return this.value;
    }

    private final Long value;

    @Override
    double doubleValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    long longValue() {
        return this.value;
    }

    // unit.............................................................................................................

    @Override
    public Optional<LengthUnit<Long, Length<Long>>> unit() {
        return Optional.empty();
    }

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
        return true;
    }

    @Override
    public boolean isPixel() {
        return false;
    }

    @Override
    void pixelOrFail() {
        this.pixelOrFail0();
    }

    @Override
    void normalOrPixelOrFail() {
        this.normalOrPixelOrFail0();
    }

    @Override
    void numberFail() {
        // number
    }

    // LengthVisitor....................................................................................................

    @Override
    void accept(final LengthVisitor visitor) {
        visitor.visit(this);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Long.hashCode(this.value);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NumberLength;
    }

    @Override
    boolean equals0(final Length other) {
        return 0 == Long.compare(this.value, other.longValue());
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    // HasJsonNode......................................................................................................

    /**
     * Accepts a json string holding a number and px unit suffix.
     */
    public static NumberLength fromJsonNodeNumber(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        try {
            return parseNumber(node.stringValueOrFail());
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    @Override
    public JsonNode toJsonNode() {
        return JsonNode.string(this.toString());
    }
}
