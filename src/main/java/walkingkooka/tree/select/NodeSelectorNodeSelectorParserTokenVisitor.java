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

package walkingkooka.tree.select;

import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Name;
import walkingkooka.naming.PathSeparator;
import walkingkooka.text.cursor.parser.select.NodeSelectorAbsoluteParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorAncestorOrSelfParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorAncestorParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorChildParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorDescendantOrSelfParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorDescendantParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorExpressionParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorFirstChildParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorFollowingParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorFollowingSiblingParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorLastChildParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorNodeName;
import walkingkooka.text.cursor.parser.select.NodeSelectorNodeNameParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorParentOfParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorParserTokenVisitor;
import walkingkooka.text.cursor.parser.select.NodeSelectorPrecedingParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorPrecedingSiblingParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorPredicateParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorSelfParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorWildcardParserToken;
import walkingkooka.tree.Node;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * This {@link NodeSelectorParserTokenVisitor} helps convert a {@link NodeSelectorParserToken} into a {@link NodeSelector}.
 */
final class NodeSelectorNodeSelectorParserTokenVisitor<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends NodeSelectorParserTokenVisitor {

    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
    NodeSelector<N, NAME, ANAME, AVALUE> with(final NodeSelectorParserToken token,
                                              final Function<NodeSelectorNodeName, NAME> nameFactory,
                                              final Class<N> node) {
        Objects.requireNonNull(token, "token");
        Objects.requireNonNull(nameFactory, "nameFactory");
        Objects.requireNonNull(node, "name");

        return new NodeSelectorNodeSelectorParserTokenVisitor<N, NAME, ANAME, AVALUE>(nameFactory).acceptAndBuild(token);
    }

    private final static PathSeparator SEPARATOR = PathSeparator.requiredAtStart('/');

    /**
     * Private ctor use static factory
     */
    // @VisibleForTesting
    NodeSelectorNodeSelectorParserTokenVisitor(final Function<NodeSelectorNodeName, NAME> nameFactory) {
        super();

        this.builder = NodeSelectorBuilder.relative(SEPARATOR);
        this.nameFactory = nameFactory;
        this.reset();
    }

    private NodeSelector<N, NAME, ANAME, AVALUE> acceptAndBuild(final NodeSelectorParserToken token) {
        this.accept(token);
        this.maybeComplete(NodeSelector.children());
        return this.builder.build()
                .setToString(token.text());
    }

    @Override
    protected void endVisit(final NodeSelectorExpressionParserToken token) {
        this.maybeComplete(null);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorPredicateParserToken token) {
        this.predicates.add(ExpressionNodeSelectorPredicate.with(ExpressionNodeSelectorPredicateNodeSelectorParserTokenVisitor.toExpressionNode(token)));
        return Visiting.SKIP;
    }

    @Override
    protected void endVisit(final NodeSelectorPredicateParserToken token) {
        super.endVisit(token);
    }

    // Leaf tokens...........................................................................................

    @Override
    protected void visit(final NodeSelectorAbsoluteParserToken token) {
        this.builder.absolute();
        this.reset();
    }

    @Override
    protected void visit(final NodeSelectorAncestorParserToken token) {
        this.axis(NodeSelector.ancestor());
    }

    @Override
    protected void visit(final NodeSelectorAncestorOrSelfParserToken token) {
        this.axis(NodeSelector.ancestorOrSelf());
    }

    @Override
    protected void visit(final NodeSelectorChildParserToken token) {
        this.axis(NodeSelector.children());
    }

    @Override
    protected void visit(final NodeSelectorDescendantParserToken token) {
        this.axis(NodeSelector.descendant());
    }

    @Override
    protected void visit(final NodeSelectorDescendantOrSelfParserToken token) {
        this.axis(NodeSelector.descendantOrSelf(SEPARATOR));
    }

    @Override
    protected void visit(final NodeSelectorFirstChildParserToken token) {
        this.axis(NodeSelector.firstChild());
    }

    @Override
    protected void visit(final NodeSelectorFollowingParserToken token) {
        this.axis(NodeSelector.following());
    }

    @Override
    protected void visit(final NodeSelectorFollowingSiblingParserToken token) {
        this.axis(NodeSelector.followingSibling());
    }

    @Override
    protected void visit(final NodeSelectorLastChildParserToken token) {
        this.axis(NodeSelector.lastChild());
    }

    @Override
    protected void visit(final NodeSelectorNodeNameParserToken token) {
        this.maybeComplete(NodeSelector.children());
        this.name = token;
    }

    @Override
    protected void visit(final NodeSelectorParentOfParserToken token) {
        this.axis(NodeSelector.parent());
    }

    @Override
    protected void visit(final NodeSelectorPrecedingParserToken token) {
        this.axis(NodeSelector.preceding());
    }

    @Override
    protected void visit(final NodeSelectorPrecedingSiblingParserToken token) {
        this.axis(NodeSelector.precedingSibling());
    }

    @Override
    protected void visit(final NodeSelectorSelfParserToken token) {
        this.axis(NodeSelector.self());
    }

    @Override
    protected void visit(final NodeSelectorWildcardParserToken token) {
        this.maybeComplete(NodeSelector.children());
        this.wildcard = true;
    }

    /**
     * Resets the state for a single step within a xpath selector.
     */
    private void reset() {
        this.name = null;
        this.wildcard = false;
        this.predicates = Lists.array();
    }

    /**
     * Adds any pending name and predicates.
     */
    private void maybeComplete(final NodeSelector<N, NAME, ANAME, AVALUE> axis) {
        final NodeSelectorNodeNameParserToken name = this.name;
        if(null!=name || this.wildcard) {
            this.complete(name, axis);
        }
    }

    /**
     * Accepts an axis selector, but adding any name/wildcard and predicates first.
     */
    private void axis(final NodeSelector<N, NAME, ANAME, AVALUE> axis) {
        this.complete(this.name, axis);
    }

    private void complete(final NodeSelectorNodeNameParserToken name, final NodeSelector<N, NAME, ANAME, AVALUE> axis) {
        final NodeSelectorBuilder<N, NAME, ANAME, AVALUE> builder = this.builder;
        if(null!=name) {
            builder.named(this.nameFactory.apply(name.value()));
        }
        this.predicates.stream()
                .forEach(p -> builder.predicate(p));

        if(null!=axis) {
            builder.append(axis);
        }
        this.reset();
    }

    /**
     * The name test or nothing for wildcard
     */
    private NodeSelectorNodeNameParserToken name;

    /**
     * If a wildcard is also present in this step.
     */
    private boolean wildcard;

    /**
     * Creates the {@link Name}
     */
    private final Function<NodeSelectorNodeName, NAME> nameFactory;

    /**
     * Zero or more predicates for this step.
     */
    private List<ExpressionNodeSelectorPredicate> predicates;

    /**
     * Builds the selector.
     */
    private final NodeSelectorBuilder<N, NAME, ANAME, AVALUE> builder;

    @Override
    public String toString() {
        return this.builder.toString();
    }
}
