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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.tree.Node;
import walkingkooka.tree.NodeTestCase2;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SearchNodeTestCase<N extends SearchNode> extends NodeTestCase2<SearchNode,
        SearchNodeName,
        SearchNodeAttributeName,
        String>
        implements IsMethodTesting<N> {

    @Test
    public final void testPublicStaticFactoryMethod()  {
        this.publicStaticFactoryCheck(SearchNode.class, "Search", Node.class);
    }

    @Test
    public final void testSetNameNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createSearchNode().setName(null);
        });
    }

    @Test
    public final void testSetNameSame() {
        final N node = this.createSearchNode();
        assertSame(node, node.setName(node.name()));
    }

    @Test
    public final void testSetNameDifferent() {
        final N node = this.createSearchNode();
        final SearchNodeName name = SearchNodeName.with("different");
        final N different = node.setName(name).cast();
        assertNotSame(node, different);
        assertEquals(name, different.name(), "name");
    }

    @Test
    public final void testSetNameReturnType() throws Exception {
        final Class<N> type = this.searchNodeType();
        final Method method = type.getMethod("setName", SearchNodeName.class);
        assertEquals(type, method.getReturnType(), ()-> method.toGenericString());
    }

    @Test
    public final void testSetAttributesEmpty() {
        final N node = this.createSearchNode();
        assertSame(node, node.setAttributes(Maps.empty()));
    }

    @Test
    public final void testReplaceInvalidBeforeOffsetFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createSearchNode().replace(-1, 1, this.replaceNode());
        });
    }

    @Test
    public final void testReplaceInvalidBeforeOffsetFails2() {
        final N node = this.createSearchNode();
        final int before = node.text().length();

        assertThrows(IllegalArgumentException.class, () -> {
            node.replace(before, before, this.replaceNode());
        });
    }

    @Test
    public final void testReplaceInvalidBeforeOffsetFails3() {
        final N node = this.createSearchNode();
        final int before = node.text().length();

        assertThrows(IllegalArgumentException.class, () -> {
            node.replace(before + 1, before, this.replaceNode());
        });

    }

    @Test
    public final void testReplaceInvalidEndOffsetFails() {
        final N node = this.createSearchNode();

        assertThrows(IllegalArgumentException.class, () -> {
            node.replace(1, 0, this.replaceNode());
        });
    }

    @Test
    public final void testReplaceInvalidEndOffsetFails2() {
        final N node = this.createSearchNode();

        assertThrows(IllegalArgumentException.class, () -> {
            node.replace(0, node.text().length() + 1, this.replaceNode());
        });
    }

    @Test
    public final void testReplaceNullNodeFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createSearchNode().replace(0, 1, null);
        });
    }

    final SearchNode replaceNode() {
        return text("!REPLACE!");
    }

    @Test
    public void testReplaceSelectedWithNullFunctionFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createSearchNode().replaceSelected(null);
        });

    }

    @Override
    public SearchNode createNode() {
        return this.createSearchNode();
    }

    abstract N createSearchNode();

    abstract SearchNode differentSearchNode();

    @Override
    protected Class<SearchNode> type() {
        return Cast.to(this.searchNodeType());
    }

    abstract Class<N> searchNodeType();

    final SearchTextNode text(final String text) {
        return SearchNode.text(text, text);
    }

    final SearchSequenceNode sequence(final SearchNode...children) {
        return SearchNode.sequence(Lists.of(children));
    }

    @Override
    protected SearchNode appendChildAndCheck(final SearchNode parent, final SearchNode child) {
        final N newParent = parent.appendChild(child).cast();
        assertNotSame(newParent, parent, "appendChild must not return the same node");

        final List<N> children = Cast.to(newParent.children());
        assertNotEquals(0, children.size(), "children must have at least 1 child");
        //assertEquals("last child must be the added child", child.attributeName(), children.get(children.size() - 1).attributeName());

        this.checkParentOfChildren(newParent);

        return newParent;
    }

    protected final void replaceSelectedWithoutSelectedAndCheck(final SearchNode node) {
        assertSame(node, node.replaceSelected((s) -> {throw new UnsupportedOperationException(); }), node.toString());
    }

    protected final void replaceSelectedAndCheck(final SearchNode node,
                                                 final Function<SearchSelectNode, SearchNode> replacer,
                                                 final SearchNode expected) {
        assertEquals(expected, node.replaceSelected(replacer), node.toString());
    }

    protected final void replaceSelectedNothingAndCheck(final SearchNode node) {
        this.replaceSelectedNothingAndCheck(node, (n) -> n);
    }

    protected final void replaceSelectedNothingAndCheck(final SearchNode node,
                                                        final Function<SearchSelectNode, SearchNode> replacer) {
        assertSame(node, node.replaceSelected(replacer), node.toString());
    }

    @Override
    protected final String requiredNamePrefix() {
        return "Search";
    }

    // IsMethodTesting.................................................................................................

    @Override
    public final N createIsMethodObject() {
        return Cast.to(this.createNode());
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return "Search";
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return Node.class.getSimpleName();
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> m.equals("isRoot");
    }
}
