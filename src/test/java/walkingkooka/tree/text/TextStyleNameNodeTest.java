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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStyleNameNodeTest extends TextParentNodeTestCase<TextStyleNameNode> {

    @Test
    public void testWith() {
        final TextStyleName style = TextStyleName.with("abc123");
        final TextStyleNameNode node = TextStyleNameNode.with(style);
        this.checkStyleName(node, style);
        this.childCountCheck(node, 0);
    }

    @Test
    public void testSetSameStyleName() {
        final String name = "abc123";
        final TextStyleNameNode node = TextStyleNameNode.with(TextStyleName.with(name));
        assertSame(node, node.setStyleName(TextStyleName.with(name)));
    }

    @Test
    public void testSetDifferentStyleName() {
        final TextStyleName name = TextStyleName.with( "abc123");
        final TextStyleNameNode node = TextStyleNameNode.with(name);

        final TextStyleName name2 = TextStyleName.with( "different");
        final TextStyleNameNode different = node.setStyleName(name2);
        assertNotSame(node, different);

        this.checkStyleName(different, name2);
        this.checkStyleName(node, name);
    }

    @Test
    public final void testSetAttributesNotEmpty() {
        this.setAttributeNotEmptyAndCheck();
    }

    @Test
    public void testSetDifferentChildren() {
        final TextNode child1 = this.text1();
        final TextStyleNameNode node = this.styleName(STYLE_NAME, child1);

        final TextNode child2 = this.text2();
        final TextStyleNameNode different = node.setChildren(Lists.of(child2));
        assertNotSame(different, node);

        this.childCountCheck(different, child2);
        this.checkStyleName(different);

        this.childCountCheck(node, child1);
        this.checkStyleName(node);
    }

    @Test
    public void testSetDifferentChildrenWithParent() {
        final TextNode child1 = this.text1();

        final TextStyleNameNode parent = this.styleName("parent1", child1);
        final TextStyleNameNode grandParent = this.styleName("grand1", parent);

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
        final TextNode child = this.text1();
        final TextStyleNameNode node = this.createTextNode()
                .appendChild(child);
        final List<TextNode> children = TextNode.NO_CHILDREN;

        final TextStyleNameNode different = node.setChildren(children);
        assertNotSame(different, node);
        this.childCountCheck(different);

        this.childCountCheck(node, child);
    }

    @Override
    public void testParentWithoutChild() {
    }

    @Test
    public void testReplaceChildDifferentParent() {
        assertThrows(IllegalArgumentException.class, () -> {
            final TextStyleNameNode parent1 = this.styleName("parent1", this.text1());
            final TextStyleNameNode parent2 = this.styleName("parent2", this.text2());

            parent1.replaceChild(parent2.children().get(0), this.text3());
        });
    }

    @Test
    public void testReplaceChild() {
        final TextStyleNameNode parent1 = this.styleName("parent1", this.text1());

        final TextNode child = this.text2();
        final TextStyleNameNode replaced = parent1.replaceChild(parent1.children().get(0), child);

        this.childCountCheck(replaced, child);
        this.checkEquals(this.styleName("parent1", child), replaced);
    }

    @Test
    public void testDifferentStyleName() {
        this.checkNotEquals(this.styleName("different"));
    }

    @Test
    public void testDifferentChild() {
        this.checkNotEquals(this.styleName("abc", this.text1()),
                this.styleName("abc", this.different()));
    }

    @Test
    public void testDifferentParent() {
        final TextStyleNameNode child = TextStyleNameNode.with(TextStyleName.with("child-1a"));

        this.checkNotEquals(TextStyleNode.with(Lists.of(child), TextStyleMap.with(Maps.of(TextStylePropertyName.WORD_WRAP, WordWrap.BREAK_WORD))).children().get(0),
                this.styleName("different-parent", child));
    }

    // HasText..........................................................................................................

    @Test
    public void testText() {
        this.textAndCheck(TextNode.styleName(TextStyleName.with("style123")).setChildren(Lists.of(Text.with("a1"), Text.with("b2"))),
                "a1b2");
    }

    // HasTextOffset .....................................................................................................

    @Test
    public void testTextOffsetWithParent() {
        this.textOffsetAndCheck(TextNode.style(Lists.of(Text.with("a1"),
                TextNode.styleName(TextStyleName.with("style123")).setChildren(Lists.of(Text.with("b22"))),
                Text.with("after!")))
                        .children().get(1),
                2);
    }

    // HasJsonNode .....................................................................................................

    @Test
    public void testToJsonNodeWithoutChildren() {
        this.toJsonNodeAndCheck(TextStyleNameNode.with(TextStyleName.with("abc123")),
                "{\"textStyle\": \"abc123\"}");
    }

    @Test
    public void testToJsonNodeWithChildren() {
        this.toJsonNodeAndCheck(TextStyleNameNode.with(TextStyleName.with("abc123"))
                        .setChildren(Lists.of(TextNode.text("text456"))),
                "{\"textStyle\": \"abc123\", \"values\": [{\"type\": \"text\", \"value\": \"text456\"}]}");
    }

    @Test
    public void testFromJsonNodeWithoutChildren() {
        this.fromJsonNodeAndCheck("{\"textStyle\": \"abc123\"}",
                TextStyleNameNode.with(TextStyleName.with("abc123")));
    }

    @Test
    public void testFromJsonNodeWithChildren() {
        this.fromJsonNodeAndCheck("{\"textStyle\": \"abc123\", \"values\": [{\"type\": \"text\", \"value\": \"text456\"}]}",
                TextStyleNameNode.with(TextStyleName.with("abc123"))
                        .setChildren(Lists.of(TextNode.text("text456"))));
    }

    @Test
    public void testJsonRoundtrip() {
        this.toJsonNodeRoundTripTwiceAndCheck(TextStyleNameNode.with(TextStyleName.with("style1"))
                .setChildren(Lists.of(
                        TextNode.text("text1"),
                        TextNode.placeholder(TextPlaceholderName.with("placeholder2")),
                        TextNode.style(Lists.of(TextNode.text("text3"))))));
    }

    // Visitor ........................................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<TextNode> visited = Lists.array();

        final TextStyleNameNode styleName = TextNode.styleName(TextStyleName.with("styleName123"))
                .setChildren(Lists.of(TextNode.text("a1"), TextNode.text("b2")));
        final Text text1 = Cast.to(styleName.children().get(0));
        final Text text2 = Cast.to(styleName.children().get(1));

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
            protected Visiting startVisit(final TextStyleNameNode t) {
                assertSame(styleName, t);
                b.append("5");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final TextStyleNameNode t) {
                assertSame(styleName, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final Text t) {
                b.append("7");
                visited.add(t);
            }
        }.accept(styleName);
        assertEquals("1517217262", b.toString());
        assertEquals(Lists.of(styleName, styleName,
                text1, text1, text1,
                text2, text2, text2,
                styleName, styleName),
                visited,
                "visited");
    }

    // toString........................................................................................................

    @Test
    public void testToStringEmpty() {
        this.toStringAndCheck(styleName("skipped"), "skipped[]");
    }

    @Test
    public void testToStringWithChild() {
        this.toStringAndCheck(this.styleName("abc", text1()), "abc[\"text-1a\"]");
    }

    @Test
    public void testToStringWithChildren() {
        this.toStringAndCheck(this.styleName("def", text1(), text2()), "def[\"text-1a\", \"text-2b\"]");
    }

    @Override
    TextStyleNameNode createTextNode() {
        return this.styleName(this.styleName().value());
    }

    private TextStyleName styleName() {
        return TextStyleName.with(STYLE_NAME);
    }

    private final static String STYLE_NAME = "styleName123";

    private TextStyleNameNode styleName(final String name, final TextNode...children) {
        return TextStyleNameNode.with(TextStyleName.with(name))
                .setChildren(Lists.of(children));
    }

    @Override
    Class<TextStyleNameNode> textNodeType() {
        return TextStyleNameNode.class;
    }

    private void checkStyleName(final TextStyleNameNode node) {
        this.checkStyleName(node, this.styleName());
    }

    private void checkStyleName(final TextStyleNameNode node, final TextStyleName name) {
        assertEquals(name, node.styleName(), "name");
    }

    // JsonNodeTesting...................................................................................................

    @Override
    public final TextStyleNameNode fromJsonNode(final JsonNode from) {
        return TextStyleNameNode.fromJsonNode(from);
    }
}
