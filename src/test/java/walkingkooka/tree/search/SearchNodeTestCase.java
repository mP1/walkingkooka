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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.Node;
import walkingkooka.tree.NodeTestCase2;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

public abstract class SearchNodeTestCase<N extends SearchNode> extends NodeTestCase2<SearchNode, SearchNodeName, SearchNodeAttributeName, String> {

    @Test
    public final void testPublicStaticFactoryMethod()  {
        this.publicStaticFactoryCheck(SearchNode.class, "Search", Node.class);
    }

    @Test
    @Ignore
    public final void testSetSameAttributes() {
        // Ignored
    }

    @Test
    public final void testIsMethods() throws Exception {
        final String prefix = "Search";
        final String suffix = Node.class.getSimpleName();

        final N node = this.createSearchNode();
        final String name = node.getClass().getSimpleName();
        assertEquals(name + " starts with " + prefix, true, name.startsWith(prefix));
        assertEquals(name + " ends with " + suffix, true, name.endsWith(suffix));

        final String isMethodName = "is" + CharSequences.capitalize(name.substring(prefix.length(), name.length() - suffix.length()));

        for(Method method : node.getClass().getMethods()) {
            if(MethodAttributes.STATIC.is(method)) {
                continue;
            }
            final String methodName = method.getName();
            if(methodName.equals("isRoot")){
                continue;
            }
            if(!methodName.startsWith("is")) {
                continue;
            }
            assertEquals(method + " returned",
                    methodName.equals(isMethodName),
                    method.invoke(node));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testReplaceInvalidBeforeOffsetFails() {
        this.createSearchNode().replace(-1, 1, this.replaceNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testReplaceInvalidBeforeOffsetFails2() {
        final N node = this.createSearchNode();
        final int before = node.text().length();
        node.replace(before, before, this.replaceNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testReplaceInvalidBeforeOffsetFails3() {
        final N node = this.createSearchNode();
        final int before = node.text().length();
        node.replace(before + 1, before, this.replaceNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testReplaceInvalidEndOffsetFails() {
        final N node = this.createSearchNode();
        node.replace(1, 0, this.replaceNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testReplaceInvalidEndOffsetFails2() {
        final N node = this.createSearchNode();
        node.replace(0, node.text().length() + 1, this.replaceNode());
    }

    @Test(expected = NullPointerException.class)
    public final void testReplaceNullNodeFails() {
        this.createSearchNode().replace(0, 1, null);
    }

    final SearchNode replaceNode() {
        return text("!REPLACE!");
    }

    @Test(expected = NullPointerException.class)
    public final void testMetaNullAttributesFails() {
        this.createSearchNode().meta(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testMetaEmptyAttributesFails() {
        this.createSearchNode().meta(Maps.empty());
    }

    @Test
    public final void testEqualsNull() {
        assertNotEquals(this.createSearchNode(), null);
    }

    @Test
    public final void testEqualsObject() {
        assertNotEquals(this.createSearchNode(), new Object());
    }

    @Test
    public final void testEqualsDifferentType() {
        assertNotEquals(this.createSearchNode(), this.differentSearchNode());
    }

    @Test
    public final void testEquals() {
        assertEquals(this.createSearchNode(), this.createSearchNode());
    }

    @Override
    protected SearchNode createNode() {
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
        assertNotSame("appendChild must not return the same node", newParent, parent);

        final List<N> children = Cast.to(newParent.children());
        assertNotEquals("children must have at least 1 child", 0, children.size());
        //assertEquals("last child must be the added child", child.attributeName(), children.get(children.size() - 1).attributeName());

        this.checkParentOfChildren(newParent);

        return newParent;
    }

    @Override
    protected final String requiredNamePrefix() {
        return "Search";
    }
}
