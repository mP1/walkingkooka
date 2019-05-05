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
 */

package walkingkooka.tree.select;

import walkingkooka.naming.Name;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharacterConstant;
import walkingkooka.text.cursor.parser.select.NodeSelectorExpressionParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorNodeName;
import walkingkooka.text.cursor.parser.select.NodeSelectorParserToken;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.expression.ExpressionNodeName;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A select maybe used to select zero or more nodes within a tree given a {@link Node}.
 */
public abstract class NodeSelector<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> implements HashCodeEqualsDefined {

    /**
     * Path separator
     */
    final static CharacterConstant SEPARATOR = CharacterConstant.with('/');

    /**
     * Creates a {@link NodeSelector} from a {@link NodeSelectorParserToken}.
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> parserToken(final NodeSelectorExpressionParserToken token,
                                                                     final Function<NodeSelectorNodeName, NAME> nameFactory,
                                                                     final Predicate<ExpressionNodeName> functions,
                                                                     final Class<N> nodeType) {

        return NodeSelectorNodeSelectorParserTokenVisitor.with(token, nameFactory, functions, nodeType);
    }

    /**
     * All index or positions in xpath are ONE based not ZERO.
     */
    public final static int INDEX_BIAS = 1;

    // static factories ......................................................................................

    /**
     * Creates a {@link NodeSelector} that will begin with the root of the {@link Node}.
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> absolute() {
        return AbsoluteNodeSelector.get();
    }

    /**
     * Creates a selector which matches any given {@link Node}.
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> relative() {
        return TerminalNodeSelector.get();
    }

    /**
     * Creates a {@link TerminalNodeSelector}.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> terminal() {
        return TerminalNodeSelector.get();
    }

    /**
     * Package private to limit sub classing.
     */
    NodeSelector() {
    }

    // builder...........................................................................................................

    /**
     * Appends an ancestor axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> ancestor() {
        return this.append(AncestorNodeSelector.get());
    }

    /**
     * Appends an ancestor-or-self axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> ancestorOrSelf() {
        return this.append(AncestorOrSelfNodeSelector.get());
    }

    /**
     * Appends an attribute value contains test.
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> attributeValueContains(final ANAME name, final AVALUE value) {
        return this.predicate(NodeSelectorNodeAttributeValueContainsPredicate.<N, NAME, ANAME, AVALUE>contains(name, value));
    }

    /**
     * Appends an attribute value ends with test.
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> attributeValueEndsWith(final ANAME name, final AVALUE value) {
        return this.predicate(NodeSelectorNodeAttributeValueEndsWithPredicate.<N, NAME, ANAME, AVALUE>endsWith(name, value));
    }

    /**
     * Appends an attribute value equals test.
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> attributeValueEquals(final ANAME name, final AVALUE value) {
        return this.predicate(NodeSelectorNodeAttributeValueEqualsPredicate.<N, NAME, ANAME, AVALUE>equalsPredicate(name, value));
    }

    /**
     * Appends an attribute value starts with test.
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> attributeValueStartsWith(final ANAME name, final AVALUE value) {
        return this.predicate(NodeSelectorNodeAttributeValueStartsWithPredicate.<N, NAME, ANAME, AVALUE>startsWith(name, value));
    }

    /**
     * Appends a child axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> children() {
        return this.append(ChildrenNodeSelector.get());
    }

    /**
     * Appends a descendant axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> descendant() {
        return this.append(DescendantNodeSelector.get());
    }

    /**
     * Appends a descendant-or-self axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> descendantOrSelf() {
        return this.append(DescendantOrSelfNodeSelector.get());
    }

    /**
     * Appends an expression
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> expression(final ExpressionNode expression) {
        return TRUE.equals(expression) ?
                this.setToString(this.toString() + "[true]") :
                this.append(ExpressionNodeSelector.with(expression));
    }

    private final static ExpressionNode TRUE = ExpressionNode.booleanNode(true);

    /**
     * Appends a first-child axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> firstChild() {
        return this.append(FirstChildNodeSelector.get());
    }

    /**
     * Appends a following axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> following() {
        return this.append(FollowingNodeSelector.get());
    }

    /**
     * Appends a following-sibling axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> followingSibling() {
        return this.append(FollowingSiblingNodeSelector.get());
    }

    /**
     * Appends a last-child
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> lastChild() {
        return this.append(LastChildNodeSelector.get());
    }

    /**
     * Appends a name test
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> named(final NAME name) {
        return this.append(NamedNodeSelector.with(name));
    }

    /**
     * Appends a parent axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> parent() {
        return this.append(ParentNodeSelector.get());
    }

    /**
     * Appends a preceding axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> preceding() {
        return this.append(PrecedingNodeSelector.get());
    }

    /**
     * Appends a preceding-sibling axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> precedingSibling() {
        return this.append(PrecedingSiblingNodeSelector.get());
    }

    /**
     * Appends a predicate.
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> predicate(final Predicate<N> predicate) {
        return this.append(NodePredicateNodeSelector.with(predicate));
    }

    /**
     * Appends a self axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> self() {
        return this.append(SelfNodeSelector.get());
    }

    final NodeSelector<N, NAME, ANAME, AVALUE> append(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        Objects.requireNonNull(selector, "select");

        return this.append0(selector);
    }

    abstract NodeSelector<N, NAME, ANAME, AVALUE> append0(final NodeSelector<N, NAME, ANAME, AVALUE> selector);

    // accept...........................................................................................................

    /**
     * Accepts a starting {@link Node} anywhere in a tree returning all matching nodes.
     * The {@link Consumer} is invoked for each and every {@link Node} prior to any test and continued traversal. It may be
     * used to abort the visiting process by throwing an {@link RuntimeException}
     */
    final public N accept(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        Objects.requireNonNull(node, "node");
        Objects.requireNonNull(context, "context");

        return this.accept0(node, context);
    }

    /**
     * Sub classes must call this method which calls the observer and then immediately calls {@link #accept1(Node, NodeSelectorContext)}
     */
    final N accept0(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        context.potential(node);
        return this.accept1(node, context);
    }

    /**
     * Sub classes must implement this to contain the core logic in testing if a node is actually selected.
     */
    abstract N accept1(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context);

    // select...........................................................................................................

    /**
     * Selects all preceding siblings of the given {@link Node}.
     */
    final N selectPrecedingSiblings(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        N result = node;

        final Optional<N> parent = node.parent();

        if (parent.isPresent()) {
            N current = node;
            for (; ; ) {
                final Optional<N> next = current.previousSibling();
                if (!next.isPresent()) {
                    result = current.parentOrFail().children().get(node.index());
                    break;
                }
                current = this.select(next.get(), context);
            }
        }

        return result;
    }

    /**
     * Selects all following siblings of the given {@link Node}.
     */
    final N selectFollowingSiblings(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        N result = node;

        final Optional<N> parent = node.parent();
        if (parent.isPresent()) {

            N current = node;
            for (; ; ) {
                final Optional<N> next = current.nextSibling();
                if (!next.isPresent()) {
                    result = current.parentOrFail().children().get(node.index());
                    break;
                }
                current = this.select(next.get(), context);
            }
        }
        return result;
    }

    /**
     * Selects all direct children of the given {@link Node node}.`
     */
    final N selectChildren(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        N result = node;

        Optional<N> next = node.firstChild();
        if(next.isPresent()) {
            for(;;) {
                result = this.select(next.get(), context);
                next = result.nextSibling();
                if(!next.isPresent()) {
                    result = result.parentOrFail();
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Matches the parent only if one is present.
     */
    final N selectParent(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        return node.parent()
                .map(parent -> this.select(parent, context).children().get(node.index()))
                .orElse(node);
    }

    /**
     * Handles a selected {@link Node}
     */
    abstract N select(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context);

    /**
     * Force sub classes to implement.
     */
    @Override
    public final String toString() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        this.toString0(b);
        return b.build();
    }

    abstract void toString0(final NodeSelectorToStringBuilder b);

    /**
     * Returns a {@link NodeSelector} with the given {@link String toString}, without changing functionality.
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> setToString(final String toString) {
        return CustomToStringNodeSelector.with(this, toString);
    }

    /**
     * All sub classes except for {@link CustomToStringNodeSelector} return this.
     */
    abstract NodeSelector<N, NAME, ANAME, AVALUE> unwrapIfCustomToStringNodeSelector();
}
