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
import walkingkooka.naming.PathSeparator;
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
public abstract class NodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> {

    /**
     * Creates a {@link NodeSelector} from a {@link NodeSelectorParserToken}.
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    NodeSelector<N, NAME, ANAME, AVALUE> parserToken(final NodeSelectorExpressionParserToken token,
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
     * {@see AbsoluteNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> AbsoluteNodeSelector<N, NAME, ANAME, AVALUE> absolute(final PathSeparator separator) {
        return AbsoluteNodeSelector.with(separator);
    }

    /**
     * {@see AncestorNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    AncestorNodeSelector<N, NAME, ANAME, AVALUE> ancestor() {
        return AncestorNodeSelector.get();
    }

    /**
     * {@see AncestorOrSelfNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    AncestorOrSelfNodeSelector<N, NAME, ANAME, AVALUE> ancestorOrSelf() {
        return AncestorOrSelfNodeSelector.get();
    }

    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    NodePredicateNodeSelector<N, NAME, ANAME, AVALUE> attributeValueContains(final ANAME name, final AVALUE value) {
        return nodePredicate(NodeAttributeValuePredicate.<N, NAME, ANAME, AVALUE>contains(name, value));
    }

    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    NodePredicateNodeSelector<N, NAME, ANAME, AVALUE> attributeValueEndsWith(final ANAME name, final AVALUE value) {
        return nodePredicate(NodeAttributeValuePredicate.<N, NAME, ANAME, AVALUE>endsWith(name, value));
    }

    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    NodePredicateNodeSelector<N, NAME, ANAME, AVALUE> attributeValueEquals(final ANAME name, final AVALUE value) {
        return nodePredicate(NodeAttributeValuePredicate.<N, NAME, ANAME, AVALUE>equalsPredicate(name, value));
    }

    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    NodePredicateNodeSelector<N, NAME, ANAME, AVALUE> attributeValueStartsWith(final ANAME name, final AVALUE value) {
        return nodePredicate(NodeAttributeValuePredicate.<N, NAME, ANAME, AVALUE>startsWith(name, value));
    }

    /**
     * {@see ChildrenNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    ChildrenNodeSelector<N, NAME, ANAME, AVALUE> children() {
        return ChildrenNodeSelector.get();
    }

    /**
     * {@see CustomToStringNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    NodeSelector<N, NAME, ANAME, AVALUE> customToString(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final String toString) {
        return CustomToStringNodeSelector.with(selector, toString);
    }

    /**
     * {@see DescendanNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    DescendantNodeSelector<N, NAME, ANAME, AVALUE> descendant() {
        return DescendantNodeSelector.get();
    }

    /**
     * {@see DescendantOrSelfNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    DescendantOrSelfNodeSelector<N, NAME, ANAME, AVALUE> descendantOrSelf(final PathSeparator separator) {
        return DescendantOrSelfNodeSelector.with(separator);
    }

    /**
     * {@link ExpressionNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    ExpressionNodeSelector<N, NAME, ANAME, AVALUE> expression(final ExpressionNode expression) {
        return ExpressionNodeSelector.with(expression);
    }

    /**
     * {@see FirstChildNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    FirstChildNodeSelector<N, NAME, ANAME, AVALUE> firstChild() {
        return FirstChildNodeSelector.get();
    }

    /**
     * {@see FollowingNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name, AVALUE>
    FollowingNodeSelector<N, NAME, ANAME, AVALUE> following() {
        return FollowingNodeSelector.get();
    }

    /**
     * {@see FollowingSiblingNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    FollowingSiblingNodeSelector<N, NAME, ANAME, AVALUE> followingSibling() {
        return FollowingSiblingNodeSelector.get();
    }

    /**
     * {@see IndexedChildNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    IndexedChildNodeSelector<N, NAME, ANAME, AVALUE> indexedChild(final int index) {
        return IndexedChildNodeSelector.with(index);
    }

    /**
     * {@see LastChildNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    LastChildNodeSelector<N, NAME, ANAME, AVALUE> lastChild() {
        return LastChildNodeSelector.get();
    }

    /**
     * {@link NamedNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    NamedNodeSelector<N, NAME, ANAME, AVALUE> name(final NAME name, final PathSeparator separator) {
        return NamedNodeSelector.with(name, separator);
    }

    /**
     * {@link NodePredicateNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    NodePredicateNodeSelector<N, NAME, ANAME, AVALUE> nodePredicate(final Predicate<N> predicate) {
        return NodePredicateNodeSelector.with(predicate);
    }

    /**
     * {@see ParentNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    ParentNodeSelector<N, NAME, ANAME, AVALUE> parent() {
        return ParentNodeSelector.get();
    }

    /**
     * {@see PrecedingNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    PrecedingNodeSelector<N, NAME, ANAME, AVALUE> preceding() {
        return PrecedingNodeSelector.get();
    }

    /**
     * {@see PrecedingSiblingNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    PrecedingSiblingNodeSelector<N, NAME, ANAME, AVALUE> precedingSibling() {
        return PrecedingSiblingNodeSelector.get();
    }

    /**
     * This method is only ever called by {@link Node#selector()}
     */
    // Node.path
    public static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    NodeSelector<N, NAME, ANAME, AVALUE> path(final N node) {
        return PathNodeSelector.with(node);
    }

    /**
     * {@see SelfNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    SelfNodeSelector<N, NAME, ANAME, AVALUE> self() {
        return SelfNodeSelector.get();
    }

    /**
     * {@see TerminalNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    TerminalNodeSelector<N, NAME, ANAME, AVALUE> terminal() {
        return TerminalNodeSelector.get();
    }
    
    /**
     * Package private to limit sub classing.
     */
    NodeSelector(){
    }

    // NodeSelector

    final NodeSelector<N, NAME, ANAME, AVALUE> append(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        Objects.requireNonNull(selector, "select");

        return this.append0(selector);
    }

    abstract NodeSelector<N, NAME, ANAME, AVALUE> append0(final NodeSelector<N, NAME, ANAME, AVALUE> selector);

    /**
     * Accepts a starting {@link Node} anywhere in a tree returning all matching nodes.
     * The {@link Consumer} is invoked for each and every {@link Node} prior to any test and continued traversal. It may be
     * used to abort the visiting process by throwing an {@link RuntimeException}
     */
    final public void accept(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        Objects.requireNonNull(node, "node");
        Objects.requireNonNull(context, "context");

        this.accept0(node, context);
    }

    /**
     * Sub classes must call this method which calls the observer and then immediately calls {@link #accept1(Node, NodeSelectorContext)}
     */
    final void accept0(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        context.potential(node);
        this.accept1(node, context);
    }

    /**
     * Sub classes must implement this to contain the core logic in testing if a node is actually selected.
     */
    abstract void accept1(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context);

    /**
     * Pushes all siblings {@link Node nodes} until itself is reached.
     */
    final void selectPrecedingSiblings(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        final Optional<N> parent = node.parent();

        if (parent.isPresent()) {
            N current = node;
            for (; ; ) {
                final Optional<N> next = current.previousSibling();
                if (!next.isPresent()) {
                    break;
                }
                current = next.get();
                this.select(current, context);
            }
        }
    }

    /**
     * Pushes all siblings {@link Node nodes} after itself.
     */
    final void selectFollowingSiblings(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        final Optional<N> parent = node.parent();
        if (parent.isPresent()) {

            N current = node;
            for (; ; ) {
                final Optional<N> next = current.nextSibling();
                if (!next.isPresent()) {
                    break;
                }
                current = next.get();
                this.select(current, context);
            }
        }
    }

    /**
     * Pushes all direct children of the {@link Node node}.`
     */
    final void selectChildren(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        for (final N child : node.children()) {
            this.select(child, context);
        }
    }

    /**
     * Matches the parent only if one is present.
     */
    final void selectParent(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        final Optional<N> parent = node.parent();
        if (parent.isPresent()) {
            this.select(parent.get(), context);
        }
    }

    /**
     * Handles a selected {@link Node}
     */
    abstract void select(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context);

    /**
     * Force sub classes to implement.
     */
    @Override
    public final String toString() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        this.toString0(b);
        return b.build();
    }

    abstract void toString0(final NodeSelectorToStringBuilder b);

    /**
     * Returns a {@link NodeSelector} with the given {@link String toString}, without changing functionality.
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> setToString(final String toString) {
        return NodeSelector.customToString(this, toString);
    }

    /**
     * All sub classes except for {@link CustomToStringNodeSelector} return this.
     */
    abstract NodeSelector<N, NAME, ANAME, AVALUE> unwrapIfCustomToStringNodeSelector();
}
