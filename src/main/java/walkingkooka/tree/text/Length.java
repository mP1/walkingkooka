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
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;

import java.util.Objects;
import java.util.Optional;

/**
 * Base class for any measure.
 */
public abstract class Length<V> implements HashCodeEqualsDefined,
        HasJsonNode{

    /**
     * Parses text that contains a support measurement mostly a number and unit.
     */
    public static Length<?> parse(final String text) {
        checkText(text);

        return NoneLength.TEXT.equals(text) ?
                NoneLength.INSTANCE :
                NormalLength.TEXT.equals(text) ?
                        NormalLength.INSTANCE :
                        LengthUnit.tryAllParse(text);
    }

    static void checkText(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");
    }

    /**
     * {@see NoneLength}
     */
    public static NoneLength none() {
        return NoneLength.INSTANCE;
    }

    /**
     * {@see NormalLength}
     */
    public static NormalLength normal() {
        return NormalLength.INSTANCE;
    }

    /**
     * {@see NumberLength}
     */
    public static NumberLength number(final long value) {
        return NumberLength.with(value);
    }
    
    /**
     * {@see PixelLength}
     */
    public static PixelLength pixel(final Double value) {
        return PixelLength.with(value);
    }

    /**
     * Package private to limit subclassing
     */
    Length() {
    }

    abstract double doubleValue();

    abstract long longValue();

    /**
     * The unit portion of this length.
     */
    abstract public Optional<LengthUnit<V, Length<V>>> unit();

    // is ..............................................................................................................

    /**
     * Only {@link NoneLength} returns true.
     */
    public abstract boolean isNone();

    /**
     * Only {@link NormalLength} returns true.
     */
    public abstract boolean isNormal();

    /**
     * Only {@link NumberLength} returns true.
     */
    public abstract boolean isNumber();

    /**
     * Only {@link PixelLength} returns true.
     */
    public abstract boolean isPixel();

    // parameter checking...............................................................................................

    abstract void pixelOrFail();

    final void pixelOrFail0() {
        throw new IllegalArgumentException("Expected pixel length but got " + this);
    }

    abstract void normalOrPixelOrFail();

    final void normalOrPixelOrFail0() {
        throw new IllegalArgumentException("Expected normal or pixel length but got " + this);
    }

    abstract void numberFail();

    final void numberFail0() {
        throw new IllegalArgumentException("Expected number but got " + this);
    }

    // LengthVisitor....................................................................................................

    abstract void accept(final LengthVisitor visitor);

    // Object ..........................................................................................................

    @Override
    public abstract int hashCode();

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    abstract boolean equals0(final Length other);

    static {
        HasJsonNode.register("length",
                Length::fromJsonNode,
                Length.class, NoneLength.class, NormalLength.class, NumberLength.class, PixelLength.class);
    }

    // HasJsonNode......................................................................................................

    /**
     * Accepts a json string holding a number and unit.
     */
    public static Length fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        return parse(node.stringValueOrFail());
    }
}
