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

import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Name;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.json.JsonStringNode;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.select.NodeSelectorVisitor;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.function.Predicate;

/**
 * A {@link NodeSelectorVisitor} that creates the json representation for any {@link NodeSelector}.
 */
final class BasicJsonMarshallerTypedNodeSelectorNodeSelectorVisitor<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> extends NodeSelectorVisitor<N, NAME, ANAME, AVALUE> {

    /**
     * Accepts a selector and returns its JSON representation.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> JsonNode toJsonNode(final NodeSelector<N, NAME, ANAME, AVALUE> selector,
                                        final ToJsonNodeContext context) {
        final BasicJsonMarshallerTypedNodeSelectorNodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor = new BasicJsonMarshallerTypedNodeSelectorNodeSelectorVisitor<>(context);
        visitor.accept(selector);
        return visitor.toJsonNode();
    }

    // VisibleForTesting
    BasicJsonMarshallerTypedNodeSelectorNodeSelectorVisitor(final ToJsonNodeContext context) {
        super();
        this.context = context;
    }

    @Override
    protected Visiting startVisitCustom(final NodeSelector<N, NAME, ANAME, AVALUE> selector,
                                        final String custom) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.CUSTOM + BasicJsonMarshallerTypedNodeSelector.SEPARATOR + custom);
    }

    @Override
    protected Visiting startVisitAbsolute(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.ABSOLUTE);
    }

    @Override
    protected Visiting startVisitAncestor(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.ANCESTOR);
    }

    @Override
    protected Visiting startVisitAncestorOrSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.ANCESTOR_OR_SELF);
    }

    @Override
    protected Visiting startVisitChildren(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.CHILD);
    }

    @Override
    protected Visiting startVisitDescendant(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.DESCENDANT);
    }

    @Override
    protected Visiting startVisitDescendantOrSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.DESCENDANT_OR_SELF);
    }

    @Override
    protected Visiting startVisitExpression(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final ExpressionNode expression) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.EXPRESSION + BasicJsonMarshallerTypedNodeSelector.SEPARATOR + expression);
    }

    @Override
    protected Visiting startVisitFirstChild(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.FIRST_CHILD);
    }

    @Override
    protected Visiting startVisitFollowing(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.FOLLOWING);
    }

    @Override
    protected Visiting startVisitFollowingSibling(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.FOLLOWING_SIBLING);
    }

    @Override
    protected Visiting startVisitLastChild(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.LAST_CHILD);
    }

    @Override
    protected Visiting startVisitNamed(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final NAME name) {
        if (null == this.nameType) {
            final Class<?> type = name.getClass();
            this.nameType = this.context.typeName(type)
                    .orElseThrow(() -> new IllegalArgumentException("Name type not registered: " + CharSequences.quote(type.getName())));
        }
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.NAMED + BasicJsonMarshallerTypedNodeSelector.SEPARATOR + name.value());
    }

    private final ToJsonNodeContext context;

    @Override
    protected Visiting startVisitParent(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.PARENT);
    }

    @Override
    protected Visiting startVisitPreceding(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.PRECEDING);
    }

    @Override
    protected Visiting startVisitPrecedingSibling(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.PRECEDING_SIBLING);
    }

    @Override
    protected Visiting startVisitPredicate(final NodeSelector<N, NAME, ANAME, AVALUE> selector, Predicate<N> predicate) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.PREDICATE + BasicJsonMarshallerTypedNodeSelector.SEPARATOR + predicate);
    }

    @Override
    protected Visiting startVisitSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.addComponent(BasicJsonMarshallerTypedNodeSelector.SELF);
    }

    private JsonNode toJsonNode() {
        final JsonObjectNode object = JsonNode.object()
                .set(BasicJsonMarshallerTypedNodeSelector.COMPONENTS_PROPERTY,
                        JsonNode.array()
                                .setChildren(this.components));
        final JsonStringNode nameType = this.nameType;
        return null != nameType ?
                object.set(BasicJsonMarshallerTypedNodeSelector.NAME_TYPE_PROPERTY, nameType) :
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
