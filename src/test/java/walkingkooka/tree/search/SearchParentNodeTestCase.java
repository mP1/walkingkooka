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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SearchParentNodeTestCase<N extends SearchParentNode> extends SearchNodeTestCase<N> {

    SearchParentNodeTestCase() {
        super();
    }

    @Test
    public final void testCreate() {
        final N parent = this.createSearchNode();
        this.checkChildCount(parent, this.children().size());
    }

    @Test
    public final void testSetChildrenSame() {
        final N expression = this.createSearchNode();
        assertSame(expression, expression.setChildren(expression.children()));
    }

    @Test
    public final void testSetChildrenEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createSearchNode().setChildren(Lists.empty());
        });
    }

    @Test
    public final void testSetChildrenEquivalent() {
        final N expression = this.createSearchNode();
        assertSame(expression, expression.setChildren(this.children()));
    }

    final void setChildrenDifferent(final List<SearchNode> differentChildren) {
        final N node = this.createSearchNode();
        final N different = node.setChildren(differentChildren).cast();
        assertNotSame(node, different);
        this.checkChildren(different, differentChildren);
    }

    abstract List<SearchNode> children();

    final void checkChildren(final N node, final List<SearchNode> children) {
        // horrible if equals is used comparison will fail because children have different parents.
        assertEquals(children.toString(), node.children().toString(), "children");
    }

    @Override
    final SearchNode differentSearchNode() {
        return this.text("different");
    }
}
