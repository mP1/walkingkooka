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

package walkingkooka.tree.search;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SearchLeafNodeTestCase<N extends SearchLeafNode<V>, V> extends SearchNodeTestCase<N> {

    SearchLeafNodeTestCase() {
        super();
    }

    @Test
    public final void testCreate() {
        final N node = this.createSearchNode();
        assertEquals(Lists.empty(), node.children(), "children");
        this.parentMissingCheck(node);
        this.checkValue(node, this.value());
        this.textAndCheck(node, this.text());
    }

    @Test
    public final void testSetValueNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createSearchNode().setValue(null);
        });
    }

    @Test
    public final void testSetSameValue() {
        final N node = this.createSearchNode();
        assertSame(node, node.setValue(node.value()));
    }

    @Override
    public void testParentWithoutChild() {
    }

    @Test
    public void testSetDifferentValue() {
        final N node = this.createSearchNode();

        final V differentValue = this.differentValue();
        final N different = node.setValue(differentValue).cast();
        assertNotSame(node, different);
        this.checkValue(different, differentValue);
        this.parentMissingCheck(different);

        this.checkValue(node, this.value());
    }

    @Test
    public final void testRemoveParentWithParent() {
        final N node = this.createSearchNode();

        final SearchIgnoredNode parent = node.ignored();
        assertEquals(node, parent.child().removeParent());
    }

    @Test
    public final void testReplaceAll() {
        final N node = this.createSearchNode();
        final SearchNode replace = this.replaceNode();
        assertSame(replace, node.replace(0, node.text().length(), replace));
    }

    @Test
    public final void testReplacePartBegin() {
        final N node = this.createSearchNode();
        final String text = node.text();

        this.replacePartBeginAndCheck(node, text, 1);
    }

    @Test
    public final void testReplacePartBegin2() {
        final N node = this.createSearchNode();
        final String text = node.text();

        this.replacePartBeginAndCheck(node, text, text.length() - 1);
    }

    private void replacePartBeginAndCheck(final N node, final String text, final int beginOffset) {
        final String before = text.substring(0, beginOffset);

        final SearchNode replacing = this.replaceNode();
        final SearchNode replaced = node.replace(beginOffset, text.length(), replacing);

        assertEquals(this.sequence(this.text(before), replacing), replaced);
    }

    @Test
    public final void testReplacePartBeginAndEnd() {
        final N node = this.createSearchNode();
        final String text = node.text();

        this.replacePartBeginAndEndAndCheck(node, text, 1, text.length() - 1);
    }

    @Test
    public final void testReplacePartBeginAndEnd2() {
        final N node = this.createSearchNode();
        final String text = node.text();

        this.replacePartBeginAndEndAndCheck(node, text, 2, text.length() - 2);
    }

    private void replacePartBeginAndEndAndCheck(final N node, final String text, final int beginOffset, final int endOffset) {
        final String before = text.substring(0, beginOffset);
        final String after = text.substring(endOffset);

        final SearchNode replacing = this.replaceNode();
        final SearchNode replaced = node.replace(beginOffset, endOffset, replacing);
        assertEquals(this.sequence(this.text(before), replacing, this.text(after)), replaced);
    }

    @Test
    public final void testReplacePartEnd() {
        final N node = this.createSearchNode();
        final String text = node.text();

        this.replacePartEndAndCheck(node, text, 1);
    }

    @Test
    public final void testReplacePartEnd2() {
        final N node = this.createSearchNode();
        final String text = node.text();

        this.replacePartEndAndCheck(node, text, text.length() - 1);
    }

    private void replacePartEndAndCheck(final N node, final String text, final int endOffset) {
        final String after = text.substring(endOffset);

        final SearchNode replacing = this.replaceNode();
        final SearchNode replaced = node.replace(0, endOffset, replacing);
        assertEquals(this.sequence(replacing, this.text(after)), replaced);
    }

    @Test
    public final void testIgnored() {
        final N node = this.createSearchNode();
        final SearchIgnoredNode ignored = node.ignored();
        assertEquals(SearchNode.ignored(node), ignored, "ignored with child");
    }

    @Test
    public final void testSelected() {
        final N node = this.createSearchNode();
        final SearchSelectNode selected = node.selected();
        assertEquals(SearchNode.select(node), selected, "ignored with child");
    }

    @Test
    public final void testReplaceSelected() {
        this.replaceSelectedWithoutSelectedAndCheck(this.createSearchNode());
    }

    @Test
    public void testEqualsDifferentValue() {
        assertNotEquals(this.createSearchNode(), this.createSearchNode(this.differentText(), this.differentValue()));
    }

    @Test
    public final void testText() {
        this.textAndCheck(this.createSearchNode(), this.text());
    }

    @Test
    public final void testTextOffset() {
        this.textOffsetAndCheck(SearchNode.sequence(
                Lists.of(
                        SearchNode.text("abc123", "abc123"),
                        this.createSearchNode())
                ).children().get(1),
                6);
    }

    @Override
    final N createSearchNode() {
        return this.createSearchNode(this.text(), this.value());
    }

    final N createSearchNode(final V value) {
        return this.createSearchNode(String.valueOf(value), value);
    }

    abstract N createSearchNode(final String text, final V value);

    abstract String text();

    abstract V value();

    abstract String differentText();

    abstract V differentValue();

    @Override
    final SearchNode differentSearchNode() {
        return this.createSearchNode(this.differentText(), this.differentValue());
    }

    final void checkValue(final N node, final V value) {
        assertEquals(value, node.value(), "value");
    }
}
