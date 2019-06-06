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
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStyleNodeTest extends TextParentNodeTestCase<TextStyleNode> {

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            TextStyleNode.with(null, TextStyleNode.NO_ATTRIBUTES_MAP);
        });
    }

    @Test
    public void testWith() {
        final TextNode child = TextNode.text("child1");
        final TextStyleNode properties = TextStyleNode.with(Lists.of(child), TextStyleNode.NO_ATTRIBUTES_MAP);
        this.childCountCheck(properties, child);
    }

    @Test
    public void testWithChildrenDefensiveCopy() {
        final TextNode child1 = TextNode.text("child1");
        final TextNode child2 = TextNode.text("child2");

        final List<TextNode> children = Lists.array();
        children.add(child1);
        children.add(child2);

        final TextStyleNode properties = TextStyleNode.with(children, TextStyleNode.NO_ATTRIBUTES_MAP);
        children.clear();
        this.childCountCheck(properties, child1, child2);
    }

    @Test
    public void testWithParent() {
        final TextNode child1 = this.text1();

        final TextStyleNode parent = textStyleNode(child1);
        final TextStyleNode grandParent = textStyleNode(parent);

        final TextNode parent2 = grandParent.children().get(0);
        this.checkWithParent(parent2);
        this.childCountCheck(parent2, child1);

        final TextNode grandParent2 = parent2.parentOrFail();
        this.childCountCheck(grandParent2, parent);
        this.checkWithoutParent(grandParent2);
    }

    @Test
    public void testSetDifferentChildren() {
        final TextStyleNode node = this.createTextNode();
        final List<TextNode> children = Lists.of(different());

        final TextStyleNode different = node.setChildren(children);
        assertNotSame(different, node);

        this.childCountCheck(different, different());
        this.childCountCheck(node, text1(), text2());
    }

    @Test
    public void testSetDifferentChildrenWithParent() {
        final TextNode child1 = this.text1();

        final TextStyleNode parent = textStyleNode(child1);
        final TextStyleNode grandParent = textStyleNode(parent);

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
        final TextStyleNode node = this.createTextNode();
        final List<TextNode> children = TextNode.NO_CHILDREN;

        final TextStyleNode different = node.setChildren(children);
        assertNotSame(different, node);
        this.childCountCheck(different);

        this.childCountCheck(node, text1(), text2());
    }

    @Test
    public void testSetDifferentAttributes() {
        final TextStyleNode node = this.createTextNode();
        final Map<TextStylePropertyName<?>, Object> attributes = Maps.of(TextStylePropertyName.with("abc"), "xyz");

        final TextNode different = node.setAttributes(attributes);
        assertNotSame(different, node);
        checkAttributes(different, attributes);

        checkAttributes(node, TextStyleNode.NO_ATTRIBUTES);
    }

    @Test
    public void testSetDifferentAttributesTwice() {
        final TextStyleNode node = this.createTextNode();
        final Map<TextStylePropertyName<?>, Object> attributes = Maps.of(TextStylePropertyName.with("abc"), "xyz");

        final TextNode different = node.setAttributes(attributes);
        assertNotSame(different, node);

        final Map<TextStylePropertyName<?>, Object> attributes2 = Maps.of(TextStylePropertyName.with("def"), "qrs");
        final TextNode different2 = different.setAttributes(attributes2);
        assertNotSame(different, different2);

        checkAttributes(different, attributes);
        checkAttributes(different2, attributes2);
        checkAttributes(node, TextStyleNode.NO_ATTRIBUTES);
    }

    @Test
    public void testSetDifferentChildrenDifferentAttributes() {
        final TextStyleNode node = this.createTextNode();
        final List<TextNode> children = Lists.of(different());

        final TextStyleNode different = node.setChildren(children);
        assertNotSame(different, node);
        this.childCountCheck(different, different());

        final Map<TextStylePropertyName<?>, Object> attributes = Maps.of(TextStylePropertyName.with("abc"), "xyz");

        final TextNode different2 = node.setAttributes(attributes);
        assertNotSame(different2, different);
        checkAttributes(different2, attributes);

        this.childCountCheck(node, text1(), text2());
        checkAttributes(node, TextStyleNode.NO_ATTRIBUTES);
    }

    private static void checkAttributes(final TextNode node, final Map<TextStylePropertyName<?>, Object> attributes) {
        assertEquals(attributes, node.attributes(), "attributes");
    }

    @Test
    public void testSetAttributesAndTextStyle() {
        final Map<TextStylePropertyName<?>, Object> style = Maps.sorted();
        style.put(TextStylePropertyName.BORDER_RIGHT_COLOR, Color.parseColor("blue"));
        style.put(TextStylePropertyName.TEXT_COLOR, Color.parseColor("lime"));
        style.put(TextStylePropertyName.TEXT_ALIGNMENT, TextAlignment.RIGHT);

        final TextNode node = TextNode.style(TextNode.NO_CHILDREN)
                .setAttributes(style);
        assertEquals(TextStyle.with(style), node.textStyle(), "textStyle");
    }

    // HasText..........................................................................................................

    @Test
    public void testText() {
        this.textAndCheck(TextNode.style(Lists.of(Text.with("a1"), Text.with("b2"))),
                "a1b2");
    }

    // HasTextOffset .....................................................................................................

    @Test
    public void testTextOffsetWithParent() {
        this.textOffsetAndCheck(TextNode.style(Lists.of(Text.with("a1"),
                TextNode.style(Lists.of(Text.with("b22")))))
                        .children().get(1),
                2);
    }

    // HasJsonNode .....................................................................................................

    @Test
    public void testToJsonNodeWithoutChildren() {
        this.toJsonNodeAndCheck(textStyleNode(), "{}");
    }

    @Test
    public void testToJsonNodeWithChildren() {
        this.toJsonNodeAndCheck(textStyleNode(TextNode.text("text123")), "{\"values\": [{\"type\": \"text\", \"value\": \"text123\"}]}");
    }

    @Test
    public void testToJsonNodeWithChildren2() {
        this.toJsonNodeAndCheck(textStyleNode(TextNode.text("text123"), TextNode.text("text456")),
                "{\"values\": [{\"type\": \"text\", \"value\": \"text123\"}, {\"type\": \"text\", \"value\": \"text456\"}]}");
    }

    @Test
    public void testToJsonNodeWithProperties() {
        this.toJsonNodeAndCheck(textStyleNode()
                .setAttributes(Maps.of(TextStylePropertyName.BACKGROUND_COLOR, Color.fromRgb(0x123456))),
                "{\"textStyle\": {\"background-color\": \"#123456\"}}");
    }

    @Test
    public void testToJsonNodeWithPropertiesAndChildren() {
        this.toJsonNodeAndCheck(textStyleNode(TextNode.text("text123"))
                        .setAttributes(Maps.of(TextStylePropertyName.BACKGROUND_COLOR, Color.fromRgb(0x123456))),
                "{\"textStyle\": {\"background-color\": \"#123456\"}, \"values\": [{\"type\": \"text\", \"value\": \"text123\"}]}");
    }
    @Test
    public void testFromJsonNodeWithoutChildren() {
        this.fromJsonNodeAndCheck("{}",
                textStyleNode());
    }

    @Test
    public void testFromJsonNodeWithChildren() {
        this.fromJsonNodeAndCheck("{\"values\": [{\"type\": \"text\", \"value\": \"text123\"}]}",
                textStyleNode(TextNode.text("text123")));
    }

    @Test
    public void testJsonRoundtrip() {
        this.toJsonNodeRoundTripTwiceAndCheck(textStyleNode(TextNode.text("text1"),
                TextNode.placeholder(TextPlaceholderName.with("placeholder2")),
                textStyleNode(TextNode.text("text3"))));
    }

    @Test
    public void testJsonRoundtrip2() {
        this.toJsonNodeRoundTripTwiceAndCheck(textStyleNode(
                TextNode.text("text1"),
                textStyleNode(TextNode.placeholder(TextPlaceholderName.with("placeholder2")),
                        textStyleNode(TextNode.text("text3")))));
    }

    @Test
    public void testJsonRoundtripWithProperties() {
        this.toJsonNodeRoundTripTwiceAndCheck(textStyleNode(
                TextNode.text("text1"),
                TextNode.placeholder(TextPlaceholderName.with("placeholder2")),
                textStyleNode(TextNode.text("text3")))
                .setAttributes(Maps.of(TextStylePropertyName.BACKGROUND_COLOR, Color.fromRgb(0x123456))));
    }

    @Test
    public void testJsonRoundtripWithProperties2() {
        this.toJsonNodeRoundTripTwiceAndCheck(textStyleNode(
                TextNode.text("text1"),
                TextNode.placeholder(TextPlaceholderName.with("placeholder2")),
                TextNode.style(Lists.of(TextNode.text("text3"))))
                .setAttributes(Maps.of(TextStylePropertyName.BACKGROUND_COLOR, Color.fromRgb(0x123456), TextStylePropertyName.TEXT_COLOR, Color.fromRgb(0x789abc))));
    }

    @Test
    public void testJsonRoundtripWithProperties3() {
        final Map<TextStylePropertyName<?>, Object> properties = Maps.ordered();
        properties.put(TextStylePropertyName.BACKGROUND_COLOR, Color.fromRgb(0x123456));
        properties.put(TextStylePropertyName.BORDER_COLLAPSE, BorderCollapse.SEPARATE);
        properties.put(TextStylePropertyName.BORDER_SPACING, BorderSpacing.with(Length.pixel(1.0)));
        properties.put(TextStylePropertyName.BORDER_BOTTOM_STYLE, BorderStyle.DASHED);
        properties.put(TextStylePropertyName.BORDER_LEFT_STYLE, BorderStyle.HIDDEN);
        properties.put(TextStylePropertyName.BORDER_RIGHT_STYLE, BorderStyle.DOTTED);
        properties.put(TextStylePropertyName.BORDER_TOP_STYLE, BorderStyle.OUTSET);
        properties.put(TextStylePropertyName.BORDER_BOTTOM_WIDTH, Length.pixel(1.0));
        properties.put(TextStylePropertyName.BORDER_LEFT_WIDTH, Length.pixel(2.0));
        properties.put(TextStylePropertyName.BORDER_RIGHT_WIDTH, Length.pixel(3.0));
        properties.put(TextStylePropertyName.BORDER_TOP_WIDTH, Length.pixel(4.0));
        properties.put(TextStylePropertyName.FONT_FAMILY_NAME, FontFamilyName.with("Antiqua"));
        properties.put(TextStylePropertyName.FONT_KERNING, FontKerning.NORMAL);
        properties.put(TextStylePropertyName.FONT_SIZE, FontSize.with(10));
        properties.put(TextStylePropertyName.FONT_STRETCH, FontStretch.CONDENSED);
        properties.put(TextStylePropertyName.FONT_STYLE, FontStyle.ITALIC);
        properties.put(TextStylePropertyName.FONT_VARIANT, FontVariant.SMALL_CAPS);
        properties.put(TextStylePropertyName.FONT_WEIGHT, FontWeight.with(1000));
        properties.put(TextStylePropertyName.HANGING_PUNCTUATION, HangingPunctuation.LAST);
        properties.put(TextStylePropertyName.HEIGHT, Length.pixel(99.5));
        properties.put(TextStylePropertyName.HORIZONTAL_ALIGNMENT, HorizontalAlignment.LEFT);
        properties.put(TextStylePropertyName.HYPHENS, Hyphens.AUTO);
        properties.put(TextStylePropertyName.LETTER_SPACING, LetterSpacing.with(Length.normal()));
        properties.put(TextStylePropertyName.LINE_HEIGHT, Length.pixel(100.0));
        properties.put(TextStylePropertyName.LIST_STYLE_POSITION, ListStylePosition.INSIDE);
        properties.put(TextStylePropertyName.LIST_STYLE_TYPE, ListStyleType.DECIMAL_LEADING_ZERO);
        properties.put(TextStylePropertyName.MARGIN_BOTTOM, Length.pixel(1.0));
        properties.put(TextStylePropertyName.MARGIN_LEFT, Length.pixel(2.0));
        properties.put(TextStylePropertyName.MARGIN_RIGHT, Length.pixel(3.0));
        properties.put(TextStylePropertyName.MARGIN_TOP, Length.pixel(4.0));
        properties.put(TextStylePropertyName.MAX_HEIGHT, Length.pixel(1024.0));
        properties.put(TextStylePropertyName.MAX_WIDTH, Length.none());
        properties.put(TextStylePropertyName.MIN_HEIGHT, Length.pixel(10.0));
        properties.put(TextStylePropertyName.MIN_WIDTH, Length.pixel(11.0));
        properties.put(TextStylePropertyName.OPACITY, Opacity.with(0.5));
        properties.put(TextStylePropertyName.OUTLINE_COLOR, Color.parseColor("red"));
        properties.put(TextStylePropertyName.OUTLINE_OFFSET, Length.pixel(0.25));
        properties.put(TextStylePropertyName.OUTLINE_STYLE, OutlineStyle.HIDDEN);
        properties.put(TextStylePropertyName.OUTLINE_WIDTH, Length.pixel(0.5));
        properties.put(TextStylePropertyName.OVERFLOW_X, Overflow.AUTO);
        properties.put(TextStylePropertyName.OVERFLOW_Y, Overflow.AUTO);
        properties.put(TextStylePropertyName.PADDING_BOTTOM, Length.pixel(5.0));
        properties.put(TextStylePropertyName.PADDING_LEFT, Length.pixel(6.0));
        properties.put(TextStylePropertyName.PADDING_RIGHT, Length.pixel(7.0));
        properties.put(TextStylePropertyName.PADDING_TOP, Length.pixel(8.0));
        properties.put(TextStylePropertyName.TAB_SIZE, Length.number(12));
        properties.put(TextStylePropertyName.TEXT, "abc123");
        properties.put(TextStylePropertyName.TEXT_ALIGNMENT, TextAlignment.LEFT);
        properties.put(TextStylePropertyName.TEXT_COLOR, Color.fromRgb(0x789abc));
        properties.put(TextStylePropertyName.TEXT_DECORATION, TextDecoration.UNDERLINE);
        properties.put(TextStylePropertyName.TEXT_DECORATION_COLOR, Color.fromRgb(0xabcdef));
        properties.put(TextStylePropertyName.TEXT_DECORATION_STYLE, TextDecorationStyle.DASHED);
        properties.put(TextStylePropertyName.TEXT_DIRECTION, TextDirection.LTR);
        properties.put(TextStylePropertyName.TEXT_INDENT, Length.pixel(40.0));
        properties.put(TextStylePropertyName.TEXT_JUSTIFY, TextJustify.INTER_CHARACTER);
        properties.put(TextStylePropertyName.TEXT_OVERFLOW, TextOverflow.string("abc123"));
        properties.put(TextStylePropertyName.TEXT_TRANSFORM, TextTransform.CAPITALIZE);
        properties.put(TextStylePropertyName.TEXT_WRAPPING, TextWrapping.OVERFLOW);
        properties.put(TextStylePropertyName.VERTICAL_ALIGNMENT, VerticalAlignment.BOTTOM);
        properties.put(TextStylePropertyName.VISIBILITY, Visibility.COLLAPSE);
        properties.put(TextStylePropertyName.WHITE_SPACE, TextWhitespace.PRE);
        properties.put(TextStylePropertyName.WIDTH, Length.pixel(320.0));
        properties.put(TextStylePropertyName.WORD_BREAK, WordBreak.BREAK_WORD);
        properties.put(TextStylePropertyName.WORD_SPACING, WordSpacing.with(Length.normal()));
        properties.put(TextStylePropertyName.WORD_WRAP, WordWrap.BREAK_WORD);
        properties.put(TextStylePropertyName.WRITING_MODE, WritingMode.VERTICAL_LR);

        this.toJsonNodeRoundTripTwiceAndCheck(textStyleNode(
                TextNode.text("text1"),
                TextNode.placeholder(TextPlaceholderName.with("placeholder2")),
                TextNode.style(Lists.of(TextNode.text("text3"))))
                .setAttributes(properties));
    }

    // Visitor .........................................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<TextNode> visited = Lists.array();

        final TextStyleNode properties = textStyleNode(TextNode.text("a1"), TextNode.text("b2"));
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
            protected Visiting startVisit(final TextStyleNode t) {
                assertSame(properties, t);
                b.append("5");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final TextStyleNode t) {
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
        this.toStringAndCheck(textStyleNode(), "[]");
    }

    @Test
    public void testToStringWithChild() {
        this.toStringAndCheck(textStyleNode(text1()), "[\"text-1a\"]");
    }

    @Test
    public void testToStringWithChildren() {
        this.toStringAndCheck(textStyleNode(text1(), text2()), "[\"text-1a\", \"text-2b\"]");
    }

    @Test
    public void testToStringWithAttributesWithoutChildren() {
        this.toStringAndCheck(textStyleNode().setAttributes(Maps.of(TextStylePropertyName.with("abc"), "123")), "{abc: \"123\"}[]");
    }

    @Test
    public void testToStringWithAttributes2() {
        this.toStringAndCheck(textStyleNode().setAttributes(Maps.of(TextStylePropertyName.with("abc"), "123", TextStylePropertyName.with("def"), "456")),
                "{abc: \"123\", def: \"456\"}[]");
    }

    @Test
    public void testToStringWithChildrenAndAttributes() {
        this.toStringAndCheck(textStyleNode(text1()).setAttributes(Maps.of(TextStylePropertyName.with("abc"), "123")), "{abc: \"123\"}[\"text-1a\"]");
    }

    @Test
    public void testToStringWithPropertiesWithChildren() {
        this.toStringAndCheck(textStyleNode(text1(), textStyleNode(text2())), "[\"text-1a\", [\"text-2b\"]]");
    }

    @Override
    TextStyleNode createTextNode() {
        return textStyleNode(text1(), text2());
    }

    private static TextStyleNode textStyleNode(final TextNode... children) {
        return textStyleNode(Lists.of(children));
    }

    private static TextStyleNode textStyleNode(final List<TextNode> children) {
        return TextStyleNode.with(children, TextStyleNode.NO_ATTRIBUTES_MAP);
    }

    @Override
    Class<TextStyleNode> textNodeType() {
        return TextStyleNode.class;
    }

    // JsonNodeTesting...................................................................................................

    @Override
    public final TextStyleNode fromJsonNode(final JsonNode from) {
        return TextStyleNode.fromJsonNode(from);
    }
}
