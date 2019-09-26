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

package walkingkooka.tree.json.marshall;

import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Name;
import walkingkooka.naming.Names;
import walkingkooka.predicate.Predicates;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.select.parser.NodeSelectorParserContext;
import walkingkooka.tree.select.parser.NodeSelectorParserContexts;
import walkingkooka.tree.select.parser.NodeSelectorParsers;
import walkingkooka.tree.select.parser.NodeSelectorPredicateParserToken;

import java.math.MathContext;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

final class BasicJsonMarshallerTypedNodeSelector extends BasicJsonMarshallerTyped<NodeSelector> {

    static BasicJsonMarshallerTypedNodeSelector instance() {
        return new BasicJsonMarshallerTypedNodeSelector();
    }

    private BasicJsonMarshallerTypedNodeSelector() {
        super();
    }

    @Override
    void register() {
        this.registerTypeNameAndType();

        this.registerTypes(Lists.of(NodeSelector.absolute(),
                NodeSelector.relative().ancestor(),
                NodeSelector.relative().ancestorOrSelf(),
                NodeSelector.relative().children(),
                NodeSelector.relative().self().setToString("custom-123"),
                NodeSelector.relative().descendant(),
                NodeSelector.relative().descendantOrSelf(),
                NodeSelector.relative().expression(ExpressionNode.text("text")),
                NodeSelector.relative().firstChild(),
                NodeSelector.relative().following(),
                NodeSelector.relative().followingSibling(),
                NodeSelector.relative().lastChild(),
                NodeSelector.relative().named(Names.string("ignored")),
                NodeSelector.relative().parent(),
                NodeSelector.relative().preceding(),
                NodeSelector.relative().precedingSibling(),
                NodeSelector.relative().predicate(Predicates.fake()),
                NodeSelector.relative().self())
                .stream()
                .map(s -> s.getClass())
                .collect(Collectors.toList()));
    }

    @Override
    Class<NodeSelector> type() {
        return NodeSelector.class;
    }

    @Override
    String typeName() {
        return "node-selector";
    }

    @Override
    NodeSelector fromJsonNodeNull(final FromJsonNodeContext context) {
        return null;
    }

    @Override
    NodeSelector fromJsonNodeNonNull(final JsonNode node,
                                     final FromJsonNodeContext context) {
        JsonArrayNode components = null;

        for (JsonNode child : node.objectOrFail().children()) {
            final JsonNodeName name = child.name();
            switch (name.value()) {
                case NAME_TYPE:
                    child.stringValueOrFail();
                    break;
                case COMPONENTS:
                    components = child.arrayOrFail();
                    break;
                default:
                    NeverError.unhandledCase(name, NAME_TYPE, COMPONENTS);
            }
        }

        if (null == components) {
            FromJsonNodeContext.requiredPropertyMissing(COMPONENTS_PROPERTY, node);
        }

        return fromJsonNodeNonNull0(
                NAME_TYPE_PROPERTY.fromJsonNodeWithTypeFactory(node.objectOrFail(), Name.class),
                components,
                context);
    }

    /**
     * Accepts a selector and returns its JSON representation.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> fromJsonNodeNonNull0(final BiFunction<JsonNode, FromJsonNodeContext, NAME> nameFactory,
                                                                              final JsonArrayNode components,
                                                                              final FromJsonNodeContext context) {
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
                            selector = selector.named(nameFactory.apply(JsonNode.string(right), context));
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

    final static String ABSOLUTE = "absolute";
    final static String ANCESTOR = "ancestor";
    final static String ANCESTOR_OR_SELF = "ancestor-or-self";
    final static String CHILD = "child";
    final static String DESCENDANT = "descendant";
    final static String DESCENDANT_OR_SELF = "descendant-or-self";
    final static String FIRST_CHILD = "first-child";
    final static String FOLLOWING = "following";
    final static String FOLLOWING_SIBLING = "following-sibling";
    final static String LAST_CHILD = "last-child";
    final static String PARENT = "parent";
    final static String PRECEDING = "preceding";
    final static String PRECEDING_SIBLING = "preceding-sibling";
    final static String SELF = "self";
    final static String CUSTOM = "custom";
    final static String EXPRESSION = "expression";
    final static String NAMED = "named";
    final static String PREDICATE = "predicate";
    final static char SEPARATOR = ':';

    /**
     * Creates a {@link Parser} and parses the {@link String expression} into a {@link NodeSelectorPredicateParserToken} and then that into an {@link ExpressionNode}.
     */
    private static ExpressionNode parseExpression(final String expression) {
        final Parser<NodeSelectorParserContext> parser = NodeSelectorParsers.predicate()
                .orReport(ParserReporters.basic())
                .cast();
        final NodeSelectorPredicateParserToken token = parser.parse(TextCursors.charSequence(expression), NodeSelectorParserContexts.basic(BasicJsonMarshallerTypedNodeSelector::hasMathContext))
                .get()
                .cast();

        return token.toExpressionNode(Predicates.always());
    }

    private static MathContext hasMathContext() {
        return MathContext.DECIMAL32;
    }

    /**
     * Reports an invalid component in the components array of the JSON representation.
     */
    private static void reportUnknownComponent(final String component, final JsonNode from) {
        throw new IllegalArgumentException("Unknown component: " + CharSequences.quoteAndEscape(component) + " in " + from);
    }

    @Override
    JsonNode toJsonNodeNonNull(final NodeSelector value,
                               final ToJsonNodeContext context) {
        return BasicJsonMarshallerTypedNodeSelectorNodeSelectorVisitor.toJsonNode(value, context);
    }

    final static String NAME_TYPE = "name-type";
    final static String COMPONENTS = "components";

    final static JsonNodeName NAME_TYPE_PROPERTY = JsonNodeName.with(NAME_TYPE);
    final static JsonNodeName COMPONENTS_PROPERTY = JsonNodeName.with(COMPONENTS);
}
