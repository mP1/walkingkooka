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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class TextPropertiesNodeTest extends TextParentNodeTestCase<TextPropertiesNode> {

    @Test
    public void testWithParent() {
        final TextNode child1 = this.text1();

        final TextPropertiesNode parent = this.properties(child1);
        final TextPropertiesNode grandParent = this.properties(parent);

        final TextNode parent2 = grandParent.children().get(0);
        this.checkWithParent(parent2);
        this.childCountCheck(parent2, child1);

        final TextNode grandParent2 = parent2.parentOrFail();
        this.childCountCheck(grandParent2, parent);
        this.checkWithoutParent(grandParent2);
    }

    @Test
    public void testSetDifferentChildren() {
        final TextPropertiesNode node = this.createTextNode();
        final List<TextNode> children = Lists.of(different());

        final TextPropertiesNode different = node.setChildren(children);
        assertNotSame(different, node);

        this.childCountCheck(different, different());
        this.childCountCheck(node, text1(), text2());
    }

    @Test
    public void testSetDifferentChildrenWithParent() {
        final TextNode child1 = this.text1();

        final TextPropertiesNode parent = this.properties(child1);
        final TextPropertiesNode grandParent = this.properties(parent);

        final TextNode child2 = this.text2();
        final TextNode different = grandParent.children().get(0).appendChild(child2);
        this.checkWithParent(different);
        this.childCountCheck(different, child1, child2);

        final TextNode grandParent2 = different.parentOrFail();
        this.childCountCheck(grandParent2, different);
        this.checkWithoutParent(grandParent2);
    }

    @Test
    public void testSetNoChildren() {
        final TextPropertiesNode node = this.createTextNode();
        final List<TextNode> children = TextNode.NO_CHILDREN;

        final TextPropertiesNode different = node.setChildren(children);
        assertNotSame(different, node);
        this.childCountCheck(different);

        this.childCountCheck(node, text1(), text2());
    }

    @Test
    public void testSetDifferentAttributes() {
        final TextPropertiesNode node = this.createTextNode();
        final Map<TextPropertyName<?>, Object> attributes = Maps.of(TextPropertyName.with("abc"), "xyz");

        final TextPropertiesNode different = node.setAttributes(attributes);
        assertNotSame(different, node);
        checkAttributes(different, attributes);

        checkAttributes(node, TextPropertiesNode.NO_ATTRIBUTES);
    }

    @Test
    public void testSetDifferentAttributesTwice() {
        final TextPropertiesNode node = this.createTextNode();
        final Map<TextPropertyName<?>, Object> attributes = Maps.of(TextPropertyName.with("abc"), "xyz");

        final TextPropertiesNode different = node.setAttributes(attributes);
        assertNotSame(different, node);

        final Map<TextPropertyName<?>, Object> attributes2 = Maps.of(TextPropertyName.with("def"), "qrs");
        final TextPropertiesNode different2 = different.setAttributes(attributes2);
        assertNotSame(different, different2);

        checkAttributes(different, attributes);
        checkAttributes(different2, attributes2);
        checkAttributes(node, TextPropertiesNode.NO_ATTRIBUTES);
    }

    @Test
    public void testSetDifferentChildrenDifferentAttributes() {
        final TextPropertiesNode node = this.createTextNode();
        final List<TextNode> children = Lists.of(different());

        final TextPropertiesNode different = node.setChildren(children);
        assertNotSame(different, node);
        this.childCountCheck(different, different());

        final Map<TextPropertyName<?>, Object> attributes = Maps.of(TextPropertyName.with("abc"), "xyz");

        final TextPropertiesNode different2 = node.setAttributes(attributes);
        assertNotSame(different2, different);
        checkAttributes(different2, attributes);

        this.childCountCheck(node, text1(), text2());
        checkAttributes(node, TextPropertiesNode.NO_ATTRIBUTES);
    }

    private static void checkAttributes(final TextNode node, final Map<TextPropertyName<?>, Object> attributes) {
        assertEquals(attributes, node.attributes(), "attributes");
    }

    // HasText..........................................................................................................

    @Test
    public void testText() {
        this.textAndCheck(TextNode.properties(Lists.of(Text.with("a1"), Text.with("b2"))),
                "a1b2");
    }

    // HasTextOffset .....................................................................................................

    @Test
    public void testTextOffsetWithParent() {
        this.textOffsetAndCheck(TextNode.properties(Lists.of(Text.with("a1"),
                TextNode.properties(Lists.of(Text.with("b22")))))
                        .children().get(1),
                2);
    }

    // HasJsonNode .....................................................................................................

    @Test
    public void testToJsonNodeWithoutChildren() {
        this.toJsonNodeAndCheck(TextPropertiesNode.with(TextPropertiesNode.NO_CHILDREN), "{}");
    }

    @Test
    public void testToJsonNodeWithChildren() {
        this.toJsonNodeAndCheck(TextPropertiesNode.with(Lists.of(TextNode.text("text123"))), "{\"values\": [{\"type\": \"text\", \"value\": \"text123\"}]}");
    }

    @Test
    public void testToJsonNodeWithChildren2() {
        this.toJsonNodeAndCheck(TextPropertiesNode.with(Lists.of(TextNode.text("text123"), TextNode.text("text456"))),
                "{\"values\": [{\"type\": \"text\", \"value\": \"text123\"}, {\"type\": \"text\", \"value\": \"text456\"}]}");
    }

    @Test
    public void testToJsonNodeWithProperties() {
        this.toJsonNodeAndCheck(TextPropertiesNode.with(TextPropertiesNode.NO_CHILDREN)
                .setAttributes(Maps.of(TextPropertyName.BACKGROUND_COLOR, Color.fromRgb(0x123456))),
                "{\"properties\": {\"background-color\": \"#123456\"}}");
    }

    @Test
    public void testToJsonNodeWithPropertiesAndChildren() {
        this.toJsonNodeAndCheck(TextPropertiesNode.with(Lists.of(TextNode.text("text123")))
                        .setAttributes(Maps.of(TextPropertyName.BACKGROUND_COLOR, Color.fromRgb(0x123456))),
                "{\"properties\": {\"background-color\": \"#123456\"}, \"values\": [{\"type\": \"text\", \"value\": \"text123\"}]}");
    }
    @Test
    public void testFromJsonNodeWithoutChildren() {
        this.fromJsonNodeAndCheck("{}",
                TextPropertiesNode.with(TextPropertiesNode.NO_CHILDREN));
    }

    @Test
    public void testFromJsonNodeWithChildren() {
        this.fromJsonNodeAndCheck("{\"values\": [{\"type\": \"text\", \"value\": \"text123\"}]}",
                TextPropertiesNode.with(Lists.of(TextNode.text("text123"))));
    }

    @Test
    public void testJsonRoundtrip() {
        this.toJsonNodeRoundTripTwiceAndCheck(TextPropertiesNode.with(Lists.of(
                TextNode.text("text1"),
                TextNode.placeholder(TextPlaceholderName.with("placeholder2")),
                TextNode.properties(Lists.of(TextNode.text("text3"))))));
    }

    @Test
    public void testJsonRoundtrip2() {
        this.toJsonNodeRoundTripTwiceAndCheck(TextPropertiesNode.with(Lists.of(
                TextNode.text("text1"),
                TextPropertiesNode.with(Lists.of(
                        TextNode.placeholder(TextPlaceholderName.with("placeholder2")),
                        TextNode.properties(Lists.of(TextNode.text("text3"))))))));
    }

    @Test
    public void testJsonRoundtripWithProperties() {
        this.toJsonNodeRoundTripTwiceAndCheck(TextPropertiesNode.with(Lists.of(
                TextNode.text("text1"),
                TextNode.placeholder(TextPlaceholderName.with("placeholder2")),
                TextNode.properties(Lists.of(TextNode.text("text3")))))
                .setAttributes(Maps.of(TextPropertyName.BACKGROUND_COLOR, Color.fromRgb(0x123456))));
    }

    @Test
    public void testJsonRoundtripWithProperties2() {
        this.toJsonNodeRoundTripTwiceAndCheck(TextPropertiesNode.with(Lists.of(
                TextNode.text("text1"),
                TextNode.placeholder(TextPlaceholderName.with("placeholder2")),
                TextNode.properties(Lists.of(TextNode.text("text3")))))
                .setAttributes(Maps.of(TextPropertyName.BACKGROUND_COLOR, Color.fromRgb(0x123456), TextPropertyName.TEXT_COLOR, Color.fromRgb(0x789abc))));
    }

    @Test
    public void testJsonRoundtripWithProperties3() {
        final Map<TextPropertyName<?>, Object> properties = Maps.ordered();
        properties.put(TextPropertyName.BACKGROUND_COLOR, Color.fromRgb(0x123456));
        properties.put(TextPropertyName.FONT_FAMILY_NAME, FontFamilyName.with("Antiqua"));
        properties.put(TextPropertyName.FONT_SIZE, FontSize.with(10));
        properties.put(TextPropertyName.FONT_WEIGHT, FontWeight.with(1000));
        properties.put(TextPropertyName.HORIZONTAL_ALIGNMENT, HorizontalAlignment.LEFT);
        properties.put(TextPropertyName.TEXT_COLOR, Color.fromRgb(0x789abc));
        properties.put(TextPropertyName.TEXT_DECORATION, TextDecoration.UNDERLINE);
        properties.put(TextPropertyName.TEXT_WRAPPING, TextWrapping.OVERFLOW);
        properties.put(TextPropertyName.VERTICAL_ALIGNMENT, VerticalAlignment.BOTTOM);

        this.toJsonNodeRoundTripTwiceAndCheck(TextPropertiesNode.with(Lists.of(
                TextNode.text("text1"),
                TextNode.placeholder(TextPlaceholderName.with("placeholder2")),
                TextNode.properties(Lists.of(TextNode.text("text3")))))
                .setAttributes(properties));
    }

    // Visitor .........................................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<TextNode> visited = Lists.array();

        final TextPropertiesNode properties = TextPropertiesNode.with(Lists.of(TextNode.text("a1"), TextNode.text("b2")));
        final Text text1 = Cast.to(properties.children().get(0));
        final Text text2 = Cast.to(properties.children().get(1));

        new FakeTextNodeVisitor() {
            @Override
            protected Visiting startVisit(final TextNode n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final TextNode n) {
                b.append("2");
                visited.add(n);
            }

            @Override
            protected Visiting startVisit(final TextPropertiesNode t) {
                assertSame(properties, t);
                b.append("5");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final TextPropertiesNode t) {
                assertSame(properties, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final Text t) {
                b.append("7");
                visited.add(t);
            }
        }.accept(properties);
        assertEquals("1517217262", b.toString());
        assertEquals(Lists.of(properties, properties,
                text1, text1, text1,
                text2, text2, text2,
                properties, properties),
                visited,
                "visited");
    }

    // toString........................................................................................................

    @Test
    public void testToStringEmpty() {
        this.toStringAndCheck(TextPropertiesNode.with(TextPropertiesNode.NO_CHILDREN), "[]");
    }

    @Test
    public void testToStringWithChild() {
        this.toStringAndCheck(this.properties(text1()), "[\"text-1a\"]");
    }

    @Test
    public void testToStringWithChildren() {
        this.toStringAndCheck(this.properties(text1(), text2()), "[\"text-1a\", \"text-2b\"]");
    }

    @Test
    public void testToStringWithAttributesWithoutChildren() {
        this.toStringAndCheck(this.properties().setAttributes(Maps.of(TextPropertyName.with("abc"), "123")), "{abc: \"123\"}[]");
    }

    @Test
    public void testToStringWithAttributes2() {
        this.toStringAndCheck(this.properties().setAttributes(Maps.of(TextPropertyName.with("abc"), "123", TextPropertyName.with("def"), "456")),
                "{abc: \"123\", def: \"456\"}[]");
    }

    @Test
    public void testToStringWithChildrenAndAttributes() {
        this.toStringAndCheck(this.properties(text1()).setAttributes(Maps.of(TextPropertyName.with("abc"), "123")), "{abc: \"123\"}[\"text-1a\"]");
    }

    @Test
    public void testToStringWithPropertiesWithChildren() {
        this.toStringAndCheck(this.properties(text1(), this.properties(text2())), "[\"text-1a\", [\"text-2b\"]]");
    }

    @Override
    TextPropertiesNode createTextNode() {
        return this.properties(text1(), text2());
    }

    private TextPropertiesNode properties(final TextNode...children) {
        return TextPropertiesNode.with(Lists.of(children));
    }

    @Override
    Class<TextPropertiesNode> textNodeType() {
        return TextPropertiesNode.class;
    }

    // JsonNodeTesting...................................................................................................

    @Override
    public final TextPropertiesNode fromJsonNode(final JsonNode from) {
        return TextPropertiesNode.fromJsonNode(from);
    }
}
