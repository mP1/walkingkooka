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

import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.ToStringBuilderOption;
import walkingkooka.build.tostring.UsesToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.cursor.parser.select.NodeSelectorExpressionParserToken;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.select.NodeSelector;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Base class that may be used to represent rich text, some nodes with styling properties and others with plain text.
 */
public abstract class TextNode implements Node<TextNode, TextNodeName, TextPropertyName<?>, Object>,
        UsesToStringBuilder {
    
    /**
     * No children constant
     */
    public final static List<TextNode> NO_CHILDREN = Lists.empty();

    /**
     * No or empty attributes
     */
    public final static Map<TextPropertyName<?>, Object> NO_ATTRIBUTES = Maps.empty();

    // public factory methods..........................................................................................

    /**
     * {@see TextPropertiesNode}
     */
    public static TextPropertiesNode properties(final List<TextNode> children) {
        return TextPropertiesNode.with(children);
    }

    /**
     * {@see TextStyledNode}
     */
    public static TextStyledNode styled(final TextStyleName styleName) {
        return TextStyledNode.with(styleName);
    }

    /**
     * {@see Text}
     */
    public static Text text(final String value) {
        return Text.with(value);
    }

    /**
     * Constant that represents no parent.
     */
    private final static Optional<TextNode> NO_PARENT = Optional.empty();

    /**
     * Package private ctor to limit sub classing.
     */
    TextNode(final int index) {
        this.parent = NO_PARENT;
        this.index = index;
    }

    // parent .........................................................................................................

    @Override
    public final Optional<TextNode> parent() {
        return this.parent;
    }

    /**
     * This setter is used to recreate the entire graph including parents of parents receiving new children.
     * It is only ever called by a parent node and is used to adopt new children.
     */
    final TextNode setParent(final Optional<TextNode> parent, final int index) {
        final TextNode copy = this.replace(index);
        copy.parent = parent;
        return copy;
    }

    private Optional<TextNode> parent;

    /**
     * Sub classes should call this and cast.
     */
    final TextNode removeParent0() {
        return this.isRoot() ?
                this :
                this.replace(NO_INDEX);
    }

    /**
     * Sub classes must create a new copy of the parent and replace the identified child using its index or similar,
     * and also sets its parent after creation, returning the equivalent child at the same index.
     */
    abstract TextNode setChild(final TextNode newChild);

    /**
     * Only ever called after during the completion of a setChildren, basically used to recreate the parent graph
     * containing this child.
     */
    final TextNode replaceChild(final Optional<TextNode> previousParent) {
        return previousParent.isPresent() ?
                previousParent.get()
                        .setChild(this)
                        .children()
                        .get(this.index()) :
                this;
    }

    // index........................................................................................................

    @Override
    public final int index() {
        return this.index;
    }

    final int index;

    /**
     * Replaces the index, retaining other properties.
     */
    abstract TextNode replace(final int index);

    // is...............................................................................................................

    /**
     * Only {@link TextPropertiesNode} returns true
     */
    public abstract boolean isProperties();

    /**
     * Only {@link TextStyledNode} returns true
     */
    public abstract boolean isStyleName();

    /**
     * Only {@link Text} returns true
     */
    public abstract boolean isText();

    // helper............................................................................................................

    /**
     * Helperful to assist casting, typically widening a {@link TextNode} to a sub class.
     */
    final <T extends TextNode> T cast() {
        return Cast.to(this);
    }

    // Object ..........................................................................................................

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final TextNode other) {
        return this.equalsAncestors(other) &&
                this.equalsDescendants0(other);
    }

    private boolean equalsAncestors(final TextNode other) {
        boolean result = this.equalsIgnoringParentAndChildren(other);

        if (result) {
            final Optional<TextNode> parent = this.parent();
            final Optional<TextNode> otherParent = other.parent();
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

    final boolean equalsDescendants(final TextNode other) {
        return this.equalsIgnoringParentAndChildren(other) &&
                this.equalsDescendants0(other);
    }

    abstract boolean equalsDescendants0(final TextNode other);

    /**
     * Sub classes should do equals but ignore the parent and children properties.
     */
    abstract boolean equalsIgnoringParentAndChildren(final TextNode other);

    // Object ..........................................................................................................

    @Override
    public final String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    public final void buildToString(final ToStringBuilder b) {
        b.defaults();
        b.enable(ToStringBuilderOption.ESCAPE);
        b.labelSeparator(": ");
        b.valueSeparator(", ");
        b.separator("");

        this.buildToString0(b);
    }

    abstract void buildToString0(final ToStringBuilder b);

    // NodeSelector ....................................................................................................

    /**
     * {@see NodeSelector#absolute}
     */
    public static NodeSelector<TextNode, TextNodeName, TextPropertyName<?>, Object> absoluteNodeSelector() {
        return NodeSelector.absolute();
    }

    /**
     * {@see NodeSelector#relative}
     */
    public static NodeSelector<TextNode, TextNodeName, TextPropertyName<?>, Object> relativeNodeSelector() {
        return NodeSelector.relative();
    }

    /**
     * Creates a {@link NodeSelector} for {@link TextNode} from a {@link NodeSelectorExpressionParserToken}.
     */
    public static NodeSelector<TextNode, TextNodeName, TextPropertyName<?>, Object> nodeSelectorExpressionParserToken(final NodeSelectorExpressionParserToken token,
                                                                                                                      final Predicate<ExpressionNodeName> functions) {
        return NodeSelector.parserToken(token,
                n -> TextNodeName.with(n.value()),
                functions,
                TextNode.class);
    }
}