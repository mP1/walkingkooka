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

package walkingkooka.compare;

import walkingkooka.Cast;
import walkingkooka.InvalidCharacterException;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.visit.Visitable;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A {@link Range} contains a lower and upper bound of values that may be used for testing...
 * To create a {@link Range} with both a lower and upper bound it is necessary to create a range for both
 * and then intersect them.
 */
public final class Range<C extends Comparable<C>> implements Predicate<C>,
        HashCodeEqualsDefined,
        HasJsonNode,
        Visitable {

    /**
     * Assumes a character is a separator and uses the factory to create each component of the {@link Range}.
     * The separator must be present otherwise an {@link IllegalArgumentException} will be thrown.
     */
    public static <C extends Comparable<C>> Range<C> parse(final String text,
                                                           final char separator,
                                                           final Function<String, C> factory) {
        CharSequences.failIfNullOrEmpty(text, "text");
        Objects.requireNonNull(factory, "factory");

        final int separatorIndex = text.indexOf(separator);
        if (-1 == separatorIndex) {
            throw new IllegalArgumentException("Missing separator " + CharSequences.quoteIfChars(separator) + " in " + CharSequences.quoteAndEscape(text));
        }

        if (0 == separatorIndex) {
            throw new IllegalArgumentException("Empty lower range in " + CharSequences.quoteAndEscape(text));
        }
        if (text.length() - 1 == separatorIndex) {
            throw new IllegalArgumentException("Empty upper range in " + CharSequences.quoteAndEscape(text));
        }

        return greaterThanEquals(parse0(text, 0, separatorIndex, factory))
                .and(lessThanEquals(parse0(text, separatorIndex + 1, text.length(), factory)));
    }

    private static <C extends Comparable<C>> C parse0(final String text,
                                                      final int start,
                                                      final int end,
                                                      final Function<String, C> factory) {
        try {
            return factory.apply(text.substring(start, end));
        } catch (final InvalidCharacterException cause) {
            throw cause.setTextAndPosition(text, start);
        } catch (final RuntimeException cause) {
            throw new IllegalArgumentException(cause);
        }
    }

    /**
     * A {@link Range} that matches all values.
     */
    public static <C extends Comparable<C>> Range<C> all() {
        return Cast.to(ALL);
    }

    /**
     * The ALL singleton instance.
     */
    @SuppressWarnings("unchecked")
    private final static Range<?> ALL = new Range(RangeBound.all(), RangeBound.all());

    /**
     * A {@link Range} that holds a single value.
     */
    public static <C extends Comparable<C>> Range<C> singleton(final C value) {
        return new Range<C>(RangeBound.inclusive(value), RangeBound.inclusive(value));
    }

    /**
     * A {@link Range} that matches all values less than but not including the given value.
     */
    public static <C extends Comparable<C>> Range<C> lessThan(final C value) {
        return new Range<C>(RangeBound.all(), RangeBound.exclusive(value));
    }

    /**
     * A {@link Range} that matches all values less than and including the given value.
     */
    public static <C extends Comparable<C>> Range<C> lessThanEquals(final C value) {
        return new Range<C>(RangeBound.all(), RangeBound.inclusive(value));
    }

    /**
     * A {@link Range} that matches all values greater than but not including the given value.
     */
    public static <C extends Comparable<C>> Range<C> greaterThan(final C value) {
        return new Range<C>(RangeBound.exclusive(value), RangeBound.all());
    }

    /**
     * A {@link Range} that matches all values greater than and including the given value.
     */
    public static <C extends Comparable<C>> Range<C> greaterThanEquals(final C value) {
        return new Range<C>(RangeBound.inclusive(value), RangeBound.all());
    }

    /**
     * Checks that the other range is not null.
     */
    private static void checkOther(final Range<?> other) {
        Objects.requireNonNull(other, "other");
    }

    /**
     * Private ctor use factory
     */
    private Range(final RangeBound<C> lower, final RangeBound<C> upper) {
        super();

        this.lower = lower;
        this.upper = upper;
    }

    /**
     * Returns a {@link Range} that matches true to both {@link Range ranges}.
     */
    public Range<C> and(final Range<C> other) {
        checkOther(other);

        final RangeBound<C> lower = this.lower.max(other.lower);
        final RangeBound<C> upper = this.upper.min(other.upper);
        return this.equals1(lower, upper) ?
                this :
                other.equals1(lower, upper) ?
                        other :
                        lower.equals(upper) ?
                                this.replace(lower, other) :
                                this.replace0(lower, upper);
    }

    /**
     * Creates a new {@link Range} after verifying the lower is less than the  upper bounds.
     */
    private Range<C> replace(final RangeBound<C> lower, final Range<C> other) {
        if (lower.isExclusive()) {
            throw new IllegalArgumentException("Invalid range bounds " + this + " < " + other);
        }

        return new Range<C>(lower, lower);
    }

    private Range<C> replace0(final RangeBound<C> lower, final RangeBound<C> upper) {
        if (lower.equals(upper) && lower.isExclusive()) {
            throw new IllegalArgumentException("Invalid range bounds " + upper + " < " + lower);
        }

        if (lower.min(upper) != lower) {
            throw new IllegalArgumentException("Invalid range bounds " + upper + " < " + lower);
        }

        if (upper.max(lower) != upper) {
            throw new IllegalArgumentException("Invalid range bounds " + lower + " > " + upper);
        }

        return new Range<C>(lower, upper);
    }


    /**
     * Getter that returns the lower bound of the range.
     */
    public RangeBound<C> lowerBound() {
        return this.lower;
    }

    final RangeBound<C> lower;

    /**
     * Getter that returns the upper bound of the range.
     */
    public RangeBound<C> upperBound() {
        return this.upper;
    }

    final RangeBound<C> upper;

    // Predicate .............................................................................................

    /**
     * Returns true if the given value is within this range honouring the lower or upper bounds if either is present.
     */
    @Override
    public boolean test(final C c) {
        return this.lower.lowerTest(c) &&
                this.upper.upperTest(c);
    }

    // isOverlapping.......................................................................................................

    /**
     * Returns true if the two ranges overlap.
     */
    public boolean isOverlapping(final Range<C> other) {
        checkOther(other);

        return this.lower.max(other.lower)
                .lessThanOrEqual(this.upper.min(other.upper));
    }

    // Visitor...............................................................................

    /**
     * Begins the visiting process.
     */
    public void accept(final RangeVisitor<C> visitor) {
        Objects.requireNonNull(visitor, "visitor");

        visitor.traverse(this);
    }

    // HasJsonNode...............................................................................

    /**
     * Accepts a json string holding a range definition.
     * <pre>
     *  {
     * 	lowerBound {
     * 		Exclusive {
     * 			type=big
     * 			value=123
     *                }* 	},
     * 	upperBound {
     * 		Inclusive {
     * 			type=big
     * 			value=123
     *        }
     *    }
     * }
     * </pre>
     */
    @SuppressWarnings("unchecked")
    static <C extends Comparable<C>> Range<C> fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        RangeBound<?> lower = null;
        RangeBound<?> upper = null;

        for (JsonNode child : node.objectOrFail().children()) {
            final JsonNodeName name = child.name();
            switch (name.value()) {
                case LOWER_BOUND:
                    lower = RangeBound.fromJsonNode(child.objectOrFail());
                    break;
                case UPPER_BOUND:
                    upper = RangeBound.fromJsonNode(child.objectOrFail());
                    break;
                default:
                    HasJsonNode.unknownPropertyPresent(name, node);
            }
        }

        if (null == lower) {
            HasJsonNode.requiredPropertyMissing(LOWER_BOUND_PROPERTY, node);
        }
        if (null == upper) {
            HasJsonNode.requiredPropertyMissing(UPPER_BOUND_PROPERTY, node);
        }

        return new Range(lower, upper);
    }

    @Override
    public JsonNode toJsonNode() {
        return JsonNode.object()
                .set(LOWER_BOUND_PROPERTY, this.lower.toJsonNode())
                .set(UPPER_BOUND_PROPERTY, this.upper.toJsonNode());
    }

    final static String LOWER_BOUND = "lower-bound";
    final static String UPPER_BOUND = "upper-bound";

    final static JsonNodeName LOWER_BOUND_PROPERTY = JsonNodeName.with(LOWER_BOUND);
    final static JsonNodeName UPPER_BOUND_PROPERTY = JsonNodeName.with(UPPER_BOUND);

    static {
        HasJsonNode.register("range",
                Range::fromJsonNode,
                Range.class);
    }

    // Object..........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.lower, this.upper);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof Range &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final Range<?> other) {
        return this.equals1(other.lower, other.upper);
    }

    private boolean equals1(final RangeBound<?> lower, final RangeBound<?> upper) {
        return this.lower.equals(lower) &&
                this.upper.equals(upper);
    }

    @Override
    public String toString() {
        return this.lower.rangeToString(this);
    }
}
