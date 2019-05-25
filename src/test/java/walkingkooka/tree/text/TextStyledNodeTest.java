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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStyledNodeTest extends TextParentNodeTestCase<TextStyledNode> {

    @Test
    public void testWith() {
        final TextStyleName style = TextStyleName.with("abc123");
        final TextStyledNode node = TextStyledNode.with(style);
        this.checkStyleName(node, style);
        this.childCountCheck(node, 0);
    }

    @Test
    public void testSetSameStyleName() {
        final String name = "abc123";
        final TextStyledNode node = TextStyledNode.with(TextStyleName.with(name));
        assertSame(node, node.setStyleName(TextStyleName.with(name)));
    }

    @Test
    public void testSetDifferentStyleName() {
        final TextStyleName name = TextStyleName.with( "abc123");
        final TextStyledNode node = TextStyledNode.with(name);

        final TextStyleName name2 = TextStyleName.with( "different");
        final TextStyledNode different = node.setStyleName(name2);
        assertNotSame(node, different);

        this.checkStyleName(different, name2);
        this.checkStyleName(node, name);
    }

    @Override
    public void testSetSameAttributes() {
    }

    @Test
    public void testSetAttributes() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createTextNode().setAttributes(TextStyledNode.NO_ATTRIBUTES);
        });
    }

    @Test
    public void testSetDifferentChildren() {
        final TextNode child1 = this.text1();
        final TextStyledNode node = this.styled(STYLE_NAME, child1);

        final TextNode child2 = this.text2();
        final TextStyledNode different = node.setChildren(Lists.of(child2));
        assertNotSame(different, node);

        this.childCountCheck(different, child2);
        this.checkStyleName(different);

        this.childCountCheck(node, child1);
        this.checkStyleName(node);
    }

    @Test
    public void testSetDifferentChildrenWithParent() {
        final TextNode child1 = this.text1();

        final TextStyledNode parent = this.styled("parent1", child1);
        final TextStyledNode grandParent = this.styled("grand1", parent);

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
        final TextStyledNode node = this.createTextNode()
                .appendChild(child);
        final List<TextNode> children = TextNode.NO_CHILDREN;

        final TextStyledNode different = node.setChildren(children);
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
            final TextStyledNode parent1 = this.styled("parent1", this.text1());
            final TextStyledNode parent2 = this.styled("parent2", this.text2());

            parent1.replaceChild(parent2.children().get(0), this.text3());
        });
    }

    @Test
    public void testReplaceChild() {
        final TextStyledNode parent1 = this.styled("parent1", this.text1());

        final TextNode child = this.text2();
        final TextStyledNode replaced = parent1.replaceChild(parent1.children().get(0), child);

        this.childCountCheck(replaced, child);
        this.checkEquals(this.styled("parent1", child), replaced);
    }

    @Test
    public void testDifferentStyleName() {
        this.checkNotEquals(this.styled("different"));
    }

    @Test
    public void testDifferentChild() {
        this.checkNotEquals(this.styled("abc", this.text1()),
                this.styled("abc", this.different()));
    }

    @Test
    public void testDifferentParent() {
        final TextStyledNode child = TextStyledNode.with(TextStyleName.with("child"));

        this.checkNotEquals(TextPropertiesNode.with(Lists.of(child)).children().get(0),
                this.styled("different-parent", child));
    }

    @Test
    public void testToStringEmpty() {
        this.toStringAndCheck(styled("skipped"), "skipped[]");
    }

    @Test
    public void testToStringWithChild() {
        this.toStringAndCheck(this.styled("abc", text1()), "abc[\"text-1a\"]");
    }

    @Test
    public void testToStringWithChildren() {
        this.toStringAndCheck(this.styled("def", text1(), text2()), "def[\"text-1a\", \"text-2b\"]");
    }

    @Override
    TextStyledNode createTextNode() {
        return this.styled(this.styleName().value());
    }

    private TextStyleName styleName() {
        return TextStyleName.with(STYLE_NAME);
    }

    private final static String STYLE_NAME = "styled123";

    private TextStyledNode styled(final String name, final TextNode...children) {
        return TextStyledNode.with(TextStyleName.with(name))
                .setChildren(Lists.of(children));
    }

    @Override
    Class<TextStyledNode> textNodeType() {
        return TextStyledNode.class;
    }

    private void checkStyleName(final TextStyledNode node) {
        this.checkStyleName(node, this.styleName());
    }

    private void checkStyleName(final TextStyledNode node, final TextStyleName name) {
        assertEquals(name, node.styleName(), "name");
    }
}
