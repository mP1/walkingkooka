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

package walkingkooka.tree.search;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.text.HasText;
import walkingkooka.tree.Node;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A {@link Node} supporting numerous value types, that can be searched over.
 */
public abstract class SearchNode implements Node<SearchNode, SearchNodeName, Name, Object>, HasText {

    public final static List<SearchNode> NO_CHILDREN = Lists.empty();

    final static Optional<SearchNode> NO_PARENT = Optional.empty();
    final static int NO_PARENT_INDEX = -1;
    
    /**
     * {@see SearchBigDecimalNode}
     */
    public static SearchBigDecimalNode bigDecimal(final String text, final BigDecimal value){
        return SearchBigDecimalNode.with(text, value);
    }

    /**
     * {@see SearchBigIntegerNode}
     */
    public static SearchBigIntegerNode bigInteger(final String text, final BigInteger value) {
        return SearchBigIntegerNode.with(text, value);
    }
    
    /**
     * {@see SearchDoubleNode}
     */
    public static SearchDoubleNode doubleNode(final String text, final double value) {
        return SearchDoubleNode.with(text, value);
    }

    /**
     * {@see SearchLocalDateNode}
     */
    public static SearchLocalDateNode localDate(final String text, final LocalDate value) {
        return SearchLocalDateNode.with(text, value);
    }

    /**
     * {@see SearchLocalDateTimeNode}
     */
    public static SearchLocalDateTimeNode localDateTime(final String text, final LocalDateTime value) {
        return SearchLocalDateTimeNode.with(text, value);
    }

    /**
     * {@see SearchLocalTimeNode}
     */
    public static SearchLocalTimeNode localTime(final String text, final LocalTime value) {
        return SearchLocalTimeNode.with(text, value);
    }

    /**
     * {@see SearchLongNode}
     */
    public static SearchLongNode longNode(final String text, final long value) {
        return SearchLongNode.with(text, value);
    }
    
    /**
     * {@see SearchSelectNode}
     */
    public static SearchSelectNode select(final SearchNode child) {
        return SearchSelectNode.with(child);
    }
    
    /**
     * {@see SearchSequenceNode}
     */
    public static SearchSequenceNode sequence(final List<SearchNode> children) {
        return SearchSequenceNode.with(children);
    }

    /**
     * {@see SearchTextNode}
     */
    public static SearchTextNode text(final String text, final String value) {
        return SearchTextNode.with(text, value);
    }

    /**
     * Package private ctor to limit sub classing.
     */
    SearchNode(final int index) {
        this.parent = NO_PARENT;
        this.index = index;
    }

    // parent ..................................................................................................

    @Override
    public final Optional<SearchNode> parent() {
        return this.parent;
    }

    /**
     * This setter is used to recreate the entire graph including parents of parents receiving new children.
     * It is only ever called by a parent node and is used to adopt new children.
     */
    final SearchNode setParent(final Optional<SearchNode> parent, final int index) {
        final SearchNode copy = this.wrap(index);
        copy.parent = parent;
        return copy;
    }

    Optional<SearchNode> parent;

    @Override
    public final int index() {
        return this.index;
    }

    final int index;

    /**
     * Sub classes must create a new copy of the parent and replace the identified child using its index or similar,
     * and also sets its parent after creation, returning the equivalent child at the same index.
     */
    abstract SearchNode setChild(final SearchNode newChild);

    /**
     * Only ever called after during the completion of a setChildren, basically used to recreate the parent graph
     * containing this child.
     */
    final SearchNode replaceChild(final Optional<SearchNode> previousParent) {
        return previousParent.isPresent() ?
                previousParent.get()
                        .setChild(this)
                        .children()
                        .get(this.index()) :
                this;
    }

    abstract SearchNode wrap(final int index);

    // attributes.......................................................................................................

    @Override
    public final Map<Name, Object> attributes() {
        return Maps.empty();
    }

    @Override
    public final SearchNode setAttributes(final Map<Name, Object> attributes) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Only {@link SearchBigDecimalNode} returns true.
     */
    public abstract boolean isBigDecimal();

    /**
     * Only {@link SearchBigIntegerNode} returns true.
     */
    public abstract boolean isBigInteger();

    /**
     * Only {@link SearchDoubleNode} returns true.
     */
    public abstract boolean isDouble();

    /**
     * Only {@link SearchLocalDateNode} returns true.
     */
    public abstract boolean isLocalDate();

    /**
     * Only {@link SearchLocalDateTimeNode} returns true.
     */
    public abstract boolean isLocalDateTime();

    /**
     * Only {@link SearchLocalTimeNode} returns true.
     */
    public abstract boolean isLocalTime();

    /**
     * Only {@link SearchLongNode} returns true.
     */
    public abstract boolean isLong();

    /**
     * Only {@link SearchSequenceNode} returns true.
     */
    public abstract boolean isSequence();

    /**
     * Only {@link SearchSelectNode} returns true.
     */
    public abstract boolean isSelect();

    /**
     * Only {@link SearchTextNode} returns true.
     */
    public abstract boolean isText();

    // helper............................................................................................................

    final <T extends SearchNode> T cast() {
        return Cast.to(this);
    }

    // SearchQuery ...............................................................................................

    abstract void select(final SearchQuery query, final SearchQueryContext context);

    /**
     * A factory used during selecting that wraps this {@link SearchNode} in a {@link SearchSelectNode}.
     */
    abstract SearchSelectNode selected();

    // Visitor .......................................................................................................
    /**
     * Begins the visiting process.
     */
    public abstract void accept(final SearchNodeVisitor visitor);

    // Object .......................................................................................................

    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(Object other);

    final boolean equals0(final SearchNode other) {
        return this.equalsAncestors(other) &&
                this.equalsDescendants0(other);
    }

    private boolean equalsAncestors(final SearchNode other) {
        boolean result = this.equalsIgnoringParentAndChildren(other);

        if(result) {
            final Optional<SearchNode> parent = this.parent();
            final Optional<SearchNode> otherParent = other.parent();
            final boolean hasParent = parent.isPresent();
            final boolean hasOtherParent = otherParent.isPresent();

            if (hasParent) {
                if (hasOtherParent) {
                    result = parent.get().equalsAncestors(otherParent.get());
                }
            } else {
                // result is only true if other is false
                result = !hasOtherParent;
            }
        }

        return result;
    }

    final boolean equalsDescendants(final SearchNode other) {
        return this.equalsIgnoringParentAndChildren(other) &&
                this.equalsDescendants0(other);
    }

    abstract boolean equalsDescendants0(final SearchNode other);

    /**
     * Sub classes should do equals but ignore the parent and children properties.
     */
    abstract boolean equalsIgnoringParentAndChildren(final SearchNode other);

    // Object .......................................................................................................

    @Override
    public final String toString() {
        final StringBuilder b = new StringBuilder();
        this.toString0(b);
        return b.toString();
    }

    abstract void toString0(final StringBuilder b);
}
