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

package walkingkooka.tree.search;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.CharSequences;
import walkingkooka.text.HasText;
import walkingkooka.tree.Node;
import walkingkooka.tree.TraversableHasTextOffset;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.select.parser.NodeSelectorExpressionParserToken;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A {@link Node} supporting numerous value types, that can be searched over.
 */
public abstract class SearchNode implements Node<SearchNode, SearchNodeName, SearchNodeAttributeName, String>,
        HasText,
        TraversableHasTextOffset<SearchNode>  {

    /**
     * An empty list with no children.
     */
    @SuppressWarnings("WeakerAccess")
    public final static List<SearchNode> NO_CHILDREN = Lists.empty();

    /**
     * {@see SearchBigDecimalNode}
     */
    public static SearchBigDecimalNode bigDecimal(final String text, final BigDecimal value) {
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
     * {@see SearchIgnoredNode}
     */
    public static SearchIgnoredNode ignored(final SearchNode child) {
        return SearchIgnoredNode.with(child);
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
     * {@see SearchMetaNode}
     */
    public static SearchMetaNode meta(final SearchNode child, final Map<SearchNodeAttributeName, String> attributes) {
        return SearchMetaNode.with(child, attributes);
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
     * Makes a defensive copy of the provided attributes.
     */
    static Map<SearchNodeAttributeName, String> copyAttributes(final Map<SearchNodeAttributeName, String> attributes) {
        Objects.requireNonNull(attributes, "attributes");

        return Maps.immutable(attributes);
    }

    /**
     * Package private ctor to limit sub classing.
     */
    SearchNode(final int index, final SearchNodeName name) {
        super();
        this.parent = NO_PARENT;
        this.index = index;
        this.name = name;
    }

    private final static Optional<SearchNode> NO_PARENT = Optional.empty();

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
        final SearchNode copy = this.replace(index, this.name);
        copy.parent = parent;
        return copy;
    }

    private Optional<SearchNode> parent;

    /**
     * Sub classes should call this method and cast to their same reflect.
     */
    final SearchNode removeParent0() {
        return this.isRoot() ?
                this :
                this.removeParent1();
    }

    /**
     * Factory overridden by sub classes to create an equivalent {@link SearchNode} without a parent.
     */
    abstract SearchNode removeParent1();

    @Override
    public final int index() {
        return this.index;
    }

    final int index;

    /**
     * Sub classes must create a new copy of the parent and replace the identified child using its index or similar,
     * and also sets its parent after creation, returning the equivalent child at the same index.
     */
    abstract SearchNode setChild(final SearchNode newChild, final int index);

    /**
     * Only ever called after during the completion of a setChildren, basically used to recreate the parent graph
     * containing this child.
     */
    final SearchNode replaceChild(final Optional<SearchNode> previousParent, final int index) {
        return previousParent.isPresent() ?
                previousParent.get()
                        .setChild(this, index)
                        .children()
                        .get(index) :
                this;
    }

    @Override
    public final SearchNodeName name() {
        return this.name;
    }

    /**
     * Only used by {@link #toString()} so that non default names are added.
     */
    abstract SearchNodeName defaultName();

    /**
     * Would be setter that that returns an instance of this node, with the given name creating a new instance if
     * necessary.
     */
    abstract public SearchNode setName(final SearchNodeName name);

    /**
     * Handles the core of implementing the setName functionality.
     */
    final SearchNode setName0(final SearchNodeName name) {
        Objects.requireNonNull(name, "name");

        return this.name.equals(name) ?
                this :
                this.replace(this.index, name);
    }

    final SearchNodeName name;

    // factory .......................................................................................

    /**
     * Factory that creates a new {@link SearchNode} with only the given properties.
     */
    abstract SearchNode replace(final int index, final SearchNodeName name);

    // attributes.......................................................................................................

    /**
     * Would be setter that potentially wraps this node in a {@link SearchMetaNode}.
     */
    @Override
    public final SearchNode setAttributes(final Map<SearchNodeAttributeName, String> attributes) {
        final Map<SearchNodeAttributeName, String> copy = copyAttributes(attributes);
        return copy.isEmpty() ?
                this :
                this.setAttributes0(attributes);
    }

    /**
     * Most classes except for {@link SearchMetaNode} create a new {@link SearchMetaNode}.
     */
    abstract SearchMetaNode setAttributes0(final Map<SearchNodeAttributeName, String> attributes);

    // replace ...............................................................................................

    /**
     * Replaces part of all of the text of this node with another {@link SearchNode}.
     */
    public final SearchNode replace(final int beginOffset, final int endOffset, final SearchNode replace) {
        final String text = this.checkBeginOffsetEndOffset(beginOffset, endOffset);
        Objects.requireNonNull(replace, "replace");

        return 0 == beginOffset && text.length() == endOffset ?
                this.replaceAll(replace) :
                this.replace0(beginOffset, endOffset, replace, text);
    }

    abstract SearchNode replaceAll(final SearchNode replace);

    final SearchNode replaceAll0(final SearchNode replace) {
        final Optional<SearchNode> parent = this.parent();
        return parent.isPresent() ?
                replace.replaceChild(parent, this.index()) :
                replace;
    }

    abstract SearchNode replace0(final int beginOffset, final int endOffset, final SearchNode replace, final String text);

    /**
     * Extracts the {@link SearchNode} that matches the begin and end offset.
     */
    final SearchNode extract(final int beginOffset, final int endOffset) {
        final String text = this.checkBeginOffsetEndOffset(beginOffset, endOffset);
        return 0 == beginOffset && text.length() == endOffset ?
                this :
                this.extract0(beginOffset, endOffset, text);
    }

    abstract SearchNode extract0(final int beginOffset, final int endOffset, final String text);

    /**
     * Verifies the begin and end offsets are valid for the text belonging to this node.
     */
    private String checkBeginOffsetEndOffset(final int beginOffset, final int endOffset) {
        final String text = this.text();
        final int textLength = text.length();

        if (beginOffset < 0 || beginOffset >= textLength) {
            throw new IllegalArgumentException("Begin offset " + beginOffset + " not between 0 and " + textLength + " text=" + CharSequences.quoteAndEscape(text));
        }
        if (endOffset < beginOffset || endOffset > textLength) {
            throw new IllegalArgumentException("End offset " + endOffset + " not between " + beginOffset + " and " + textLength + " text=" + CharSequences.quoteAndEscape(text));
        }
        return text;
    }

    final SearchNode text1(final int beginOffset, final int endOffset, final String text) {
        return this.text0(text.substring(beginOffset, endOffset));
    }

    /**
     * Factory which creates a node with the given text.
     */
    final SearchNode text0(final String text) {
        return text(text, text);
    }

    /**
     * Only {@link SearchBigDecimalNode} returns true.
     */
    public final boolean isBigDecimal() {
        return this instanceof SearchBigDecimalNode;
    }

    /**
     * Only {@link SearchBigIntegerNode} returns true.
     */
    public final boolean isBigInteger() {
        return this instanceof SearchBigIntegerNode;
    }

    /**
     * Only {@link SearchDoubleNode} returns true.
     */
    public final boolean isDouble() {
        return this instanceof SearchDoubleNode;
    }

    /**
     * Only {@link SearchIgnoredNode} returns true.
     */
    @SuppressWarnings("WeakerAccess")
    public final boolean isIgnored() {
        return this instanceof SearchIgnoredNode;
    }

    /**
     * Only {@link SearchLocalDateNode} returns true.
     */
    public final boolean isLocalDate() {
        return this instanceof SearchLocalDateNode;
    }

    /**
     * Only {@link SearchLocalDateTimeNode} returns true.
     */
    public final boolean isLocalDateTime() {
        return this instanceof SearchLocalDateTimeNode;
    }

    /**
     * Only {@link SearchLocalTimeNode} returns true.
     */
    public final boolean isLocalTime() {
        return this instanceof SearchLocalTimeNode;
    }

    /**
     * Only {@link SearchLongNode} returns true.
     */
    public final boolean isLong() {
        return this instanceof SearchLongNode;
    }

    /**
     * Only {@link SearchMetaNode} returns true.
     */
    public final boolean isMeta() {
        return this instanceof SearchMetaNode;
    }

    /**
     * Only {@link SearchSequenceNode} returns true.
     */
    public final boolean isSequence() {
        return this instanceof SearchSequenceNode;
    }

    /**
     * Only {@link SearchSelectNode} returns true.
     */
    @SuppressWarnings("WeakerAccess")
    public final boolean isSelect() {
        return this instanceof SearchSelectNode;
    }

    /**
     * Only {@link SearchTextNode} returns true.
     */
    public final boolean isText() {
        return this instanceof SearchTextNode;
    }

    /**
     * Optimisation to help gather text for all {@link SearchNode} by parents.
     */
    abstract void appendText(final StringBuilder b);

    // helper............................................................................................................

    final <T extends SearchNode> T cast() {
        return Cast.to(this);
    }

    // SearchQuery ...............................................................................................

    /**
     * A factory used during selecting that wraps this {@link SearchNode} in a {@link SearchIgnoredNode}.
     */
    abstract public SearchIgnoredNode ignored();

    abstract void select(final SearchQuery query, final SearchQueryContext context);

    /**
     * A factory used during selecting that wraps this {@link SearchNode} in a {@link SearchSelectNode}.
     */
    abstract public SearchSelectNode selected();

    /**
     * Used to visit and potentially replace zero or more of the {@link SearchSelectNode} given to the function.
     * The function can return the initial token to indicate no replacement or replace with anything it wishes.
     */
    final public SearchNode replaceSelected(final Function<SearchSelectNode, SearchNode> replacer) {
        Objects.requireNonNull(replacer, "replacer");

        return this.replaceSelected0(replacer);
    }

    abstract SearchNode replaceSelected0(final Function<SearchSelectNode, SearchNode> replacer);

    // Visitor .......................................................................................................

    /**
     * Begins the visiting process.
     */
    public abstract void accept(final SearchNodeVisitor visitor);

    // Object ..........................................................................................................

    @Override
    public abstract int hashCode();

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(Object other);

    private boolean equals0(final SearchNode other) {
        return this.equalsAncestors(other) &&
                this.equalsDescendants0(other);
    }

    private boolean equalsAncestors(final SearchNode other) {
        boolean result = this.equalsIgnoringParentAndChildren(other);

        if (result) {
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
     * Compares all properties except for the parent and children. For all search nodes this includes the name.
     */
    final boolean equalsIgnoringParentAndChildren(final SearchNode other) {
        return this.name.equals(other.name) &&
                this.equalsIgnoringParentAndChildren0(other);
    }

    /**
     * Sub classes should do equals but ignore the parent and children properties.
     */
    abstract boolean equalsIgnoringParentAndChildren0(final SearchNode other);

    // Object .......................................................................................................

    @Override
    public final String toString() {
        final StringBuilder b = new StringBuilder();
        this.toString0(b);
        return b.toString();
    }

    final void toString0(final StringBuilder b) {
        final SearchNodeName name = this.name;
        if (!name.equals(this.defaultName())) {
            b.append(name).append(this.toStringNameSuffix());
        }
        this.toString1(b);
    }

    abstract String toStringNameSuffix();

    abstract void toString1(final StringBuilder b);

    // NodeSelector .......................................................................................................

    /**
     * {@see NodeSelector#absolute}
     */
    public static NodeSelector<SearchNode, SearchNodeName, SearchNodeAttributeName, String> absoluteNodeSelector() {
        return NodeSelector.absolute();
    }

    /**
     * {@see NodeSelector#relative}
     */
    public static NodeSelector<SearchNode, SearchNodeName, SearchNodeAttributeName, String> relativeNodeSelector() {
        return NodeSelector.relative();
    }

    /**
     * Creates a {@link NodeSelector} for {@link SearchNode} from a {@link NodeSelectorExpressionParserToken}.
     */
    public static NodeSelector<SearchNode, SearchNodeName, SearchNodeAttributeName, String> nodeSelectorExpressionParserToken(final NodeSelectorExpressionParserToken token,
                                                                                                                              final Predicate<ExpressionNodeName> functions) {
        return NodeSelector.parserToken(token,
                n -> SearchNodeName.with(n.value()),
                functions,
                SearchNode.class);
    }
}
