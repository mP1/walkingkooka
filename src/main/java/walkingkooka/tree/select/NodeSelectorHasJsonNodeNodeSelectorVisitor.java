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
import walkingkooka.predicate.Predicates;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.select.NodeSelectorParserContext;
import walkingkooka.text.cursor.parser.select.NodeSelectorParserContexts;
import walkingkooka.text.cursor.parser.select.NodeSelectorParsers;
import walkingkooka.text.cursor.parser.select.NodeSelectorPredicateParserToken;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.json.JsonStringNode;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A visitor that creates the json representation for a {@link NodeSelector}.
 */
final class NodeSelectorHasJsonNodeNodeSelectorVisitor<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> extends NodeSelectorVisitor<N, NAME, ANAME, AVALUE> {

    private static final String ABSOLUTE = "absolute";
    private static final String ANCESTOR = "ancestor";
    private static final String ANCESTOR_OR_SELF = "ancestor-or-self";
    private static final String CHILD = "child";
    private static final String DESCENDANT = "descendant";
    private static final String DESCENDANT_OR_SELF = "descendant-or-self";
    private static final String FIRST_CHILD = "first-child";
    private static final String FOLLOWING = "following";
    private static final String FOLLOWING_SIBLING = "following-sibling";
    private static final String LAST_CHILD = "last-child";
    private static final String PARENT = "parent";
    private static final String PRECEDING = "preceding";
    private static final String PRECEDING_SIBLING = "preceding-sibling";
    private static final String SELF = "self";
    private static final String CUSTOM = "custom";
    private static final String EXPRESSION = "expression";
    private static final String NAMED = "named";
    private static final String PREDICATE = "predicate";

    private static final char SEPARATOR = ':';

    /**
     * Accepts a selector and returns its JSON representation.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> fromJsonNode(final Function<JsonNode, NAME> nameFactory,
                                                                      final JsonArrayNode components) {
        NodeSelector<N, NAME, ANAME, AVALUE> selector = NodeSelector.relative();

        for (JsonNode component : components.children()) {
            final String string = component.stringValueOrFail();

            switch (string) {
                case ABSOLUTE:
                    selector = NodeSelector.absolute();
                    break;
                case ANCESTOR:
                    selector = selector.ancestor();
                    break;
                case ANCESTOR_OR_SELF:
                    selector = selector.ancestorOrSelf();
                    break;
                case CHILD:
                    selector = selector.children();
                    break;
                case DESCENDANT:
                    selector = selector.descendant();
                    break;
                case DESCENDANT_OR_SELF:
                    selector = selector.descendantOrSelf();
                    break;
                case FIRST_CHILD:
                    selector = selector.firstChild();
                    break;
                case FOLLOWING:
                    selector = selector.following();
                    break;
                case FOLLOWING_SIBLING:
                    selector = selector.followingSibling();
                    break;
                case LAST_CHILD:
                    selector = selector.lastChild();
                    break;
                case PARENT:
                    selector = selector.parent();
                    break;
                case PRECEDING:
                    selector = selector.preceding();
                    break;
                case PRECEDING_SIBLING:
                    selector = selector.precedingSibling();
                    break;
                case SELF:
                    selector = selector.self();
                    break;
                default:
                    final int separator = string.indexOf(SEPARATOR);
                    if (-1 == separator) {
                        reportUnknownComponent(string, component.parentOrFail());
                    }
                    final String left = string.substring(0, separator);
                    final String right = string.substring(separator + 1);

                    switch (left) {
                        case CUSTOM:
                            selector = selector.setToString(right);
                            break;
                        case EXPRESSION:
                            selector = selector.expression(parseExpression(right));
                            break;
                        case NAMED:
                            selector = selector.named(nameFactory.apply(JsonNode.string(right)));
                            break;
                        case PREDICATE:
                            selector = selector.expression(parseExpression(right));
                            break;
                        default:
                            reportUnknownComponent(string, component.parentOrFail());
                            break;
                    }
            }
        }

        return selector;
    }


    /**
     * Creates a {@link Parser} and parses the {@link String expression} into a {@link NodeSelectorPredicateParserToken} and then that into an {@link ExpressionNode}.
     */
    private static ExpressionNode parseExpression(final String expression) {
        final Parser<NodeSelectorParserContext> parser = NodeSelectorParsers.predicate()
                .orReport(ParserReporters.basic())
                .cast();
        final NodeSelectorPredicateParserToken token = parser.parse(TextCursors.charSequence(expression), NodeSelectorParserContexts.basic())
                .get()
                .cast();

        return ExpressionNodeSelectorNodeSelectorParserTokenVisitor.toExpressionNode(token, Predicates.always());
    }

    /**
     * Reports an invalid component in the components array of the JSON representation.
     */
    private static void reportUnknownComponent(final String component, final JsonNode from) {
        throw new IllegalArgumentException("Unknown component: " + CharSequences.quoteAndEscape(component) + " in " + from);
    }

    /**
     * Accepts a selector and returns its JSON representation.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> JsonNode acceptAndToJsonNode(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        final NodeSelectorHasJsonNodeNodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor = new NodeSelectorHasJsonNodeNodeSelectorVisitor<>();
        visitor.accept(selector);
        return visitor.toJsonNode();
    }

    // VisibleForTesting
    NodeSelectorHasJsonNodeNodeSelectorVisitor() {
        super();
    }

    @Override
    protected Visiting startVisitCustom(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final String custom) {
        this.addComponent(CUSTOM + SEPARATOR + custom);
        return Visiting.CONTINUE;
    }

    @Override
    protected Visiting startVisitAbsolute(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(ABSOLUTE);
    }

    @Override
    protected Visiting startVisitAncestor(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(ANCESTOR);
    }

    @Override
    protected Visiting startVisitAncestorOrSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(ANCESTOR_OR_SELF);
    }

    @Override
    protected Visiting startVisitChildren(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(CHILD);
    }

    @Override
    protected Visiting startVisitDescendant(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(DESCENDANT);
    }

    @Override
    protected Visiting startVisitDescendantOrSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(DESCENDANT_OR_SELF);
    }

    @Override
    protected Visiting startVisitExpression(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final ExpressionNode expression) {
        return this.addComponent(EXPRESSION + SEPARATOR + ExpressionNodeSelectorToStringExpressionNodeVisitor.toString(expression));
    }

    @Override
    protected Visiting startVisitFirstChild(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(FIRST_CHILD);
    }

    @Override
    protected Visiting startVisitFollowing(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(FOLLOWING);
    }

    @Override
    protected Visiting startVisitFollowingSibling(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(FOLLOWING_SIBLING);
    }

    @Override
    protected Visiting startVisitLastChild(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(LAST_CHILD);
    }

    @Override
    protected Visiting startVisitNamed(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final NAME name) {
        if (null == this.nameType) {
            final Class<?> type = name.getClass();
            this.nameType = HasJsonNode.typeName(type)
                    .orElseThrow(() -> new IllegalArgumentException("Name type not registered: " + CharSequences.quote(type.getName())));
        }
        return this.addComponent(NAMED + SEPARATOR + name.value());
    }

    @Override
    protected Visiting startVisitParent(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(PARENT);
    }

    @Override
    protected Visiting startVisitPreceding(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(PRECEDING);
    }

    @Override
    protected Visiting startVisitPrecedingSibling(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(PRECEDING_SIBLING);
    }

    @Override
    protected Visiting startVisitPredicate(final NodeSelector<N, NAME, ANAME, AVALUE> selector, Predicate<N> predicate) {
        return this.addComponent(PREDICATE + SEPARATOR + predicate);
    }

    @Override
    protected Visiting startVisitSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(SELF);
    }

    private JsonNode toJsonNode() {
        final JsonObjectNode object = JsonNode.object()
                .set(NodeSelector.COMPONENTS_PROPERTY, JsonNode.array().setChildren(this.components));
        final JsonStringNode nameType = this.nameType;
        return null != nameType ?
                object.set(NodeSelector.NAME_TYPE_PROPERTY, nameType) :
                object;
    }

    /**
     * Initially null and upon the first name selector is set.
     */
    private JsonStringNode nameType;

    private Visiting addComponent(final String component) {
        this.components.add(JsonNode.string(component));
        return Visiting.CONTINUE;
    }

    /**
     * Components
     */
    private final List<JsonNode> components = Lists.array();

    @Override
    public String toString() {
        return this.toJsonNode().toString();
    }
}
