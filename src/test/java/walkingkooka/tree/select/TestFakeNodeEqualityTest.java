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
 */

package walkingkooka.tree.select;

import org.junit.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.tree.NodeEqualityTestCase;

import java.util.Map;

public class TestFakeNodeEqualityTest extends NodeEqualityTestCase<TestFakeNode, StringName, StringName, Object> {

    @Test
    public void testNoChildren() {
        final TestFakeNode node1 = this.createNode(0);
        final TestFakeNode node2 = this.createNode(0);
        this.checkEquals(node1, node2);
    }

    @Test
    public void testExtraChildDifferent() {
        final TestFakeNode node1 = this.createNode(0);
        final TestFakeNode node2 = this.createNode(0).appendChild(this.createNode(1));
        this.checkNotEquals(node1, node2);
    }

    @Test
    public void testSameChildren() {
        final TestFakeNode child = this.createNode(1);

        final TestFakeNode node1 = this.createNode(0).appendChild(child);
        final TestFakeNode node2 = this.createNode(0).appendChild(child);
        this.checkEquals(node1, node2);
    }

    @Test
    public void testDifferentChildren() {
        final TestFakeNode node1 = this.createNode(0).appendChild(this.createNode(1));
        final TestFakeNode node2 = this.createNode(0).appendChild(this.createNode(99));
        this.checkNotEquals(node1, node2);
    }

    @Test
    public void testDifferentChildrenSameGrandChild() {
        final TestFakeNode node1 = this.createNode(0).appendChild(this.createNode(100).appendChild(this.createNode(200)));
        final TestFakeNode node2 = this.createNode(0).appendChild(this.createNode(199).appendChild(this.createNode(200)));
        this.checkNotEquals(node1, node2);
    }

    @Test
    public void testDifferentChildren2() {
        final TestFakeNode node1 = this.createNode(0).appendChild(this.createNode(100)).appendChild(this.createNode(200));
        final TestFakeNode node2 = this.createNode(0).appendChild(this.createNode(199)).appendChild(this.createNode(200));
        this.checkNotEquals(node1, node2);
    }

    @Test
    public void testDifferentGrandChildren() {
        final TestFakeNode node1 = this.createNode(0).appendChild(this.createNode(100).appendChild(this.createNode(200)));
        final TestFakeNode node2 = this.createNode(0).appendChild(this.createNode(100).appendChild(this.createNode(299)));
        this.checkNotEquals(node1, node2);
    }

    @Test
    public void testSameAttributes() {
        final Map<StringName, Object> attributes = this.createAttributes(0);
        final TestFakeNode node1 = this.createNode(0).setAttributes(attributes);
        final TestFakeNode node2 = this.createNode(0).setAttributes(attributes);
        this.checkEquals(node1, node2);
    }

    @Test
    public void testDifferentAttributes() {
        final TestFakeNode node1 = this.createNode(0).setAttributes(this.createAttributes(0));
        final TestFakeNode node2 = this.createNode(0).setAttributes(this.createAttributes(1));
        this.checkNotEquals(node1, node2);
    }

    @Test
    public void testChildWithDifferentAttributes() {
        final TestFakeNode node1 = this.createNode(0).appendChild(this.createNode(100)).setAttributes(this.createAttributes(0));
        final TestFakeNode node2 = this.createNode(0).appendChild(this.createNode(100)).setAttributes(this.createAttributes(1));
        this.checkNotEquals(node1, node2);
    }

    @Override
    protected TestFakeNode createObject() {
        return this.createNode(0);
    }

    private TestFakeNode createNode(final int i) {
        return new TestFakeNode("test-" + i);
    }

    private Map<StringName, Object> createAttributes(int i) {
        final Map<StringName, Object> attributes = Maps.ordered();
        for (int j = 0; j < i; j++) {
            attributes.put(Names.string("attribute-" + i), i);
        }
        return attributes;
    }
}
