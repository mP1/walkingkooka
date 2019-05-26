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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

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
}
