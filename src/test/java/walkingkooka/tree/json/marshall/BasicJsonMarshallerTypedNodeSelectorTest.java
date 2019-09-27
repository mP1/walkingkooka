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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.select.parser.NodeSelectorAttributeName;

import java.math.BigDecimal;

public final class BasicJsonMarshallerTypedNodeSelectorTest extends BasicJsonMarshallerTypedTestCase2<BasicJsonMarshallerTypedNodeSelector, NodeSelector> {

    @Test
    public void testAbsoluteFromJson() {
        this.unmarshallAndCheck(this.jsonNode(JsonNode.string("absolute"), JsonNode.string("self")),
                TestNode.absoluteNodeSelector().self());
    }

    @Test
    public void testAbsoluteMarshall() {
        this.marshallAndCheck(TestNode.absoluteNodeSelector().self(),
                this.jsonNode(JsonNode.string("absolute"), JsonNode.string("self")));
    }

    @Test
    public void testAncestorFromJson() {
        this.unmarshallAndCheck2(JsonNode.string("ancestor"), TestNode.relativeNodeSelector().ancestor());
    }

    @Test
    public void testAncestorMarshall() {
        this.marshallAndCheck2(TestNode.relativeNodeSelector().ancestor(), JsonNode.string("ancestor"));
    }

    @Test
    public void testAncestorOrSelfFromJson() {
        this.unmarshallAndCheck2(JsonNode.string("ancestor-or-self"), TestNode.relativeNodeSelector().ancestorOrSelf());
    }

    @Test
    public void testAncestorOrSelfMarshall() {
        this.marshallAndCheck2(TestNode.relativeNodeSelector().ancestorOrSelf(), JsonNode.string("ancestor-or-self"));
    }

    @Test
    public void testAttributeValueContainsFromJson() {
        this.unmarshallAndCheck2(JsonNode.string(ATTRIBUTE_CONTAINS), this.function("contains"));
    }

    @Test
    public void testAttributeValueContainsMarshall() {
        this.marshallAndCheck2(this.attributeContains(), JsonNode.string(ATTRIBUTE_CONTAINS));
    }

    private NodeSelector<TestNode, StringName, StringName, Object> attributeContains() {
        return TestNode.relativeNodeSelector().attributeValueContains(Names.string("abc123"), "text456");
    }

    private final String ATTRIBUTE_CONTAINS = "predicate:contains(@abc123,\"text456\")";

    @Test
    public void testAttributeValueEndsWithFromJson() {
        this.unmarshallAndCheck2(JsonNode.string(ATTRIBUTE_ENDS_WITH), this.function("ends-with"));
    }

    @Test
    public void testAttributeValueEndsWithMarshall() {
        this.marshallAndCheck2(this.attributeEndsWith(), JsonNode.string(ATTRIBUTE_ENDS_WITH));
    }

    private NodeSelector<TestNode, StringName, StringName, Object> attributeEndsWith() {
        return TestNode.relativeNodeSelector().attributeValueEndsWith(Names.string("abc123"), "text456");
    }

    private final String ATTRIBUTE_ENDS_WITH = "predicate:ends-with(@abc123,\"text456\")";

    @Test
    public void testAttributeValueEqualsWithFromJson() {
        this.unmarshallAndCheck2(JsonNode.string(ATTRIBUTE_EQUALS),
                TestNode.relativeNodeSelector().expression(ExpressionNode.equalsNode(reference(), text())));
    }

    @Test
    public void testAttributeValueEqualsWithMarshall() {
        this.marshallAndCheck2(this.attributeEquals(), JsonNode.string(ATTRIBUTE_EQUALS));
    }

    private NodeSelector<TestNode, StringName, StringName, Object> attributeEquals() {
        return TestNode.relativeNodeSelector().attributeValueEquals(Names.string("abc123"), "text456");
    }

    private final String ATTRIBUTE_EQUALS = "predicate:@abc123=\"text456\"";

    @Test
    public void testAttributeValueStartsWithFromJson() {
        this.unmarshallAndCheck2(JsonNode.string(ATTRIBUTE_STARTS_WITH), this.function("starts-with"));
    }

    @Test
    public void testAttributeValueStartsWithMarshall() {
        this.marshallAndCheck2(this.attributeStartsWith(), JsonNode.string(ATTRIBUTE_STARTS_WITH));
    }

    private NodeSelector<TestNode, StringName, StringName, Object> attributeStartsWith() {
        return TestNode.relativeNodeSelector().attributeValueStartsWith(Names.string("abc123"), "text456");
    }

    private final String ATTRIBUTE_STARTS_WITH = "predicate:starts-with(@abc123,\"text456\")";

    private NodeSelector<TestNode, StringName, StringName, Object> function(final String functionName) {
        return TestNode.relativeNodeSelector()
                .expression(ExpressionNode.function(ExpressionNodeName.with(functionName),
                        Lists.of(this.reference(), this.text())));
    }

    private ExpressionNode reference() {
        return ExpressionNode.reference(NodeSelectorAttributeName.with("abc123"));
    }

    private ExpressionNode text() {
        return ExpressionNode.text("text456");
    }

    @Test
    public void testChildrenFromJson() {
        this.unmarshallAndCheck2(JsonNode.string("child"), TestNode.relativeNodeSelector().children());
    }

    @Test
    public void testChildrenMarshall() {
        this.marshallAndCheck2(TestNode.relativeNodeSelector().children(), JsonNode.string("child"));
    }

    @Test
    public void testCustomToStringFromJson() {
        this.unmarshallAndCheck(this.jsonNode(JsonNode.string("custom:custom-to-string"), JsonNode.string("self")),
                TestNode.relativeNodeSelector().self().setToString("custom-to-string"));
    }

    @Test
    public void testCustomToStringMarshall() {
        this.marshallAndCheck(TestNode.relativeNodeSelector().self().setToString("custom-to-string"),
                this.jsonNode(JsonNode.string("custom:custom-to-string"), JsonNode.string("self")));
    }

    @Test
    public void testDescendantFromJson() {
        this.unmarshallAndCheck2(JsonNode.string("descendant"), TestNode.relativeNodeSelector().descendant());
    }

    @Test
    public void testDescendantMarshall() {
        this.marshallAndCheck2(TestNode.relativeNodeSelector().descendant(), JsonNode.string("descendant"));
    }

    @Test
    public void testDescendantOrSelfFromJson() {
        this.unmarshallAndCheck2(JsonNode.string("descendant-or-self"), TestNode.relativeNodeSelector().descendantOrSelf());
    }

    @Test
    public void testDescendantOrSelfMarshall() {
        this.marshallAndCheck2(TestNode.relativeNodeSelector().descendantOrSelf(), JsonNode.string("descendant-or-self"));
    }

    @Test
    public void testExpressionFromJson() {
        this.unmarshallAndCheck2(JsonNode.string("expression:1+22"), TestNode.relativeNodeSelector().expression(this.sum()));
    }

    @Test
    public void testExpressionMarshall() {
        this.marshallAndCheck2(TestNode.relativeNodeSelector().expression(this.sum()), JsonNode.string("expression:1+22"));
    }

    private ExpressionNode sum() {
        return ExpressionNode.addition(ExpressionNode.bigDecimal(BigDecimal.valueOf(1)), ExpressionNode.bigDecimal(BigDecimal.valueOf(22)));
    }

    @Test
    public void testFirstChildFromJson() {
        this.unmarshallAndCheck2(JsonNode.string("first-child"), TestNode.relativeNodeSelector().firstChild());
    }

    @Test
    public void testFirstChildMarshall() {
        this.marshallAndCheck2(TestNode.relativeNodeSelector().firstChild(), JsonNode.string("first-child"));
    }

    @Test
    public void testFollowingFromJson() {
        this.unmarshallAndCheck2(JsonNode.string("following"), TestNode.relativeNodeSelector().following());
    }

    @Test
    public void testFollowingMarshall() {
        this.marshallAndCheck2(TestNode.relativeNodeSelector().following(), JsonNode.string("following"));
    }

    @Test
    public void testFollowingSiblingFromJson() {
        this.unmarshallAndCheck2(JsonNode.string("following-sibling"), TestNode.relativeNodeSelector().followingSibling());
    }

    @Test
    public void testFollowingSiblingMarshall() {
        this.marshallAndCheck2(TestNode.relativeNodeSelector().followingSibling(), JsonNode.string("following-sibling"));
    }

    @Test
    public void testLastChildFromJson() {
        this.unmarshallAndCheck2(JsonNode.string("last-child"), TestNode.relativeNodeSelector().lastChild());
    }

    @Test
    public void testLastChildMarshall() {
        this.marshallAndCheck2(TestNode.relativeNodeSelector().lastChild(), JsonNode.string("last-child"));
    }

    @Test
    public void testNamedFromJson() {
        this.unmarshallAndCheck(this.jsonNode("string-name", JsonNode.string("named:abc123")),
                TestNode.relativeNodeSelector().named(Names.string("abc123")));
    }

    @Test
    public void testNamedMarshall() {
        this.marshallAndCheck(TestNode.relativeNodeSelector().named(Names.string("abc123")),
                this.jsonNode("string-name", JsonNode.string("named:abc123")));
    }

    @Test
    public void testParentFromJson() {
        this.unmarshallAndCheck2(JsonNode.string("parent"), TestNode.relativeNodeSelector().parent());
    }

    @Test
    public void testParentMarshall() {
        this.marshallAndCheck2(TestNode.relativeNodeSelector().parent(), JsonNode.string("parent"));
    }

    @Test
    public void testPrecedingFromJson() {
        this.unmarshallAndCheck2(JsonNode.string("preceding"), TestNode.relativeNodeSelector().preceding());
    }

    @Test
    public void testPrecedingMarshall() {
        this.marshallAndCheck2(TestNode.relativeNodeSelector().preceding(), JsonNode.string("preceding"));
    }

    @Test
    public void testPrecedingSiblingFromJson() {
        this.unmarshallAndCheck2(JsonNode.string("preceding-sibling"), TestNode.relativeNodeSelector().precedingSibling());
    }

    @Test
    public void testPrecedingSiblingMarshall() {
        this.marshallAndCheck2(TestNode.relativeNodeSelector().precedingSibling(), JsonNode.string("preceding-sibling"));
    }

    @Test
    public void testSelfFromJson() {
        this.unmarshallAndCheck2(JsonNode.string("self"), TestNode.relativeNodeSelector().self());
    }

    @Test
    public void testSelfMarshall() {
        this.marshallAndCheck2(TestNode.relativeNodeSelector().self(), JsonNode.string("self"));
    }

    private void unmarshallAndCheck2(final JsonNode component,
                                     final NodeSelector<TestNode, StringName, StringName, Object> selector) {
        this.unmarshallAndCheck(this.jsonNode(component),
                selector);
    }

    private void marshallAndCheck2(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                   final JsonNode... components) {
        this.marshallAndCheck(selector,
                this.jsonNode(components));
    }

    @Test
    public void testAbsoluteChildrenNamedChildrenNamedJsonRoundtrip() {
        this.jsonRoundtripAndCheck(TestNode.absoluteNodeSelector()
                .children()
                .named(Names.string("abc123"))
                .children()
                .named(Names.string("DEF456")));
    }

    @Test
    public void testAbsoluteNamedChildrenExpressionJsonRoundtrip() {
        this.jsonRoundtripAndCheck(TestNode.absoluteNodeSelector()
                .children()
                .named(Names.string("abc123"))
                .expression(ExpressionNode.addition(ExpressionNode.bigDecimal(BigDecimal.valueOf(1)), ExpressionNode.text("bcd234"))));
    }

    @Test
    public void testAbsoluteAncestorAncestorOrSelfChildrenDescendantDescendantOrSelfJsonRoundtrip() {
        this.jsonRoundtripAndCheck(TestNode.absoluteNodeSelector()
                .ancestor()
                .ancestorOrSelf()
                .children()
                .descendant()
                .descendantOrSelf());
    }

    @Test
    public void testFirstChildFollowingFollowingSiblingLastChildJsonRoundtrip() {
        this.jsonRoundtripAndCheck(TestNode.absoluteNodeSelector()
                .firstChild()
                .following()
                .followingSibling()
                .lastChild());
    }

    @Test
    public void testParentPrecedingPrecedingSiblingSelfJsonRoundtrip() {
        this.jsonRoundtripAndCheck(TestNode.absoluteNodeSelector()
                .firstChild()
                .preceding()
                .precedingSibling()
                .self());
    }

    @Test
    public void testChildrenCustomToStringChildrenCustomToStringJsonRoundtrip() {
        this.jsonRoundtripAndCheck(TestNode.relativeNodeSelector()
                .children()
                .setToString("Custom1a")
                .children()
                .setToString("Custom2b"));
    }

    @Test
    public void testCustomToStringChildrenCustomToStringChildrenJsonRoundtrip() {
        this.jsonRoundtripAndCheck(TestNode.relativeNodeSelector()
                .setToString("Custom1a")
                .children()
                .setToString("Custom2b")
                .children());
    }

    private void jsonRoundtripAndCheck(final NodeSelector<TestNode, StringName, StringName, Object> selector) {
        final JsonNode jsonNode = this.marshallContext()
                .marshall(selector);
        this.unmarshallAndCheck(jsonNode, selector);
    }

    // helpers..........................................................................................................

    @Override
    BasicJsonMarshallerTypedNodeSelector marshaller() {
        return BasicJsonMarshallerTypedNodeSelector.instance();
    }

    @Override
    NodeSelector<TestNode, StringName, StringName, Object> value() {
        return TestNode.relativeNodeSelector().self();
    }

    @Override
    JsonNode node() {
        return jsonNode(JsonNode.string("self"));
    }

    private JsonObjectNode jsonNode(final JsonNode... components) {
        return JsonNode.object()
                .set(BasicJsonMarshallerTypedNodeSelector.COMPONENTS_PROPERTY,
                        JsonNode.array()
                                .setChildren(Lists.of(components)));
    }

    private JsonNode jsonNode(final String type,
                              final JsonNode... components) {
        return this.jsonNode(components)
                .set(BasicJsonMarshallerTypedNodeSelector.NAME_TYPE_PROPERTY, JsonNode.string(type));
    }

    @Override
    NodeSelector<TestNode, StringName, StringName, Object> jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "node-selector";
    }

    @Override
    Class<NodeSelector> marshallerType() {
        return NodeSelector.class;
    }

    @Override
    public Class<BasicJsonMarshallerTypedNodeSelector> type() {
        return BasicJsonMarshallerTypedNodeSelector.class;
    }
}
