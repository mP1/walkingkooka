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

package walkingkooka.tree.search;

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.collect.list.Lists;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public abstract class SearchLeafNodeTestCase<N extends SearchLeafNode, V> extends SearchNodeTestCase<N> {

    @Test
    public final void testCreate() {
        final N node = this.createSearchNode();
        assertEquals("children", Lists.empty(), node.children());
        this.checkWithoutParent(node);
        this.checkValue(node, this.value());
    }

    @Test
    public final void testSetSameValue() {
        final N node = this.createSearchNode();
        assertSame(node, node.setValue0(node.value()));
    }

    @Test
    public void testSetDifferentValue() {
        final N node = this.createSearchNode();

        final V differentValue = this.differentValue();
        final N different = node.setValue0(differentValue).cast();
        assertNotSame(node, different);
        this.checkValue(different, differentValue);
        this.checkWithoutParent(different);

        this.checkValue(node, this.value());
    }

    @Test
    public void testEqualsDifferentValue() {
        assertNotEquals(this.createSearchNode(), this.createSearchNode(this.differentText(), this.differentValue()));
    }

    @Test
    @Ignore
    public void testAppendChild() {
        // Ignored
    }

    @Test
    @Ignore
    public void testAppendChild2() {
        // Ignored
    }

    @Test
    @Ignore
    public void testRemoveChildWithoutParent() {
        // Ignored
    }

    @Test
    @Ignore
    public void testRemoveChildDifferentParent() {
        // Ignored
    }

    @Test
    @Ignore
    public void testRemoveChild() {
        // Ignored
    }

    @Test
    @Ignore
    public void testRemoveChildFirst() {
        // Ignored
    }

    @Test
    @Ignore
    public void testRemoveChildLast() {
        // Ignored
    }

    @Test
    @Ignore
    public void testReplaceChildWithoutParent() {
        // Ignored
    }

    @Test
    @Ignore
    public void testReplaceChildDifferentParent() {
        // Ignored
    }

    @Test
    @Ignore
    public void testReplaceChild() {
        // Ignored
    }

    @Test
    @Ignore
    public void testSameChildren() {
        // Ignored
    }

    @Test
    @Ignore
    public void testSetDifferentChildren() {
        // Ignored
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
        assertEquals("value", value, node.value());
    }
}
