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

package walkingkooka.tree;

import org.junit.Test;
import walkingkooka.naming.Name;
import walkingkooka.test.HashCodeEqualsDefinedTestCase;

import java.util.Map;

abstract public class NodeEqualityTestCase<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> extends HashCodeEqualsDefinedTestCase<N> {

    @Test
    public void testNoChildren(){
        final N node1 = this.createNode(0);
        final N node2 = this.createNode(0);
        this.checkEquals(node1, node2);
    }

    @Test
    public void testExtraChildDifferent(){
        final N node1 = this.createNode(0);
        final N node2 = this.createNode(0).appendChild(this.createNode(1));
        this.checkNotEquals(node1, node2);
    }

    @Test
    public void testSameChildren() {
        final N child = this.createNode(1);

        final N node1 = this.createNode(0).appendChild(child);
        final N node2 = this.createNode(0).appendChild(child);
        this.checkEquals(node1, node2);
    }

    @Test
    public void testDifferentChildren() {
        final N node1 = this.createNode(0).appendChild(this.createNode(1));
        final N node2 = this.createNode(0).appendChild(this.createNode(99));
        this.checkNotEquals(node1, node2);
    }

    @Test
    public void testDifferentChildrenSameGrandChild() {
        final N node1 = this.createNode(0).appendChild(this.createNode(100).appendChild(this.createNode(200)));
        final N node2 = this.createNode(0).appendChild(this.createNode(199).appendChild(this.createNode(200)));
        this.checkNotEquals(node1, node2);
    }

    @Test
    public void testDifferentChildren2() {
        final N node1 = this.createNode(0).appendChild(this.createNode(100)).appendChild(this.createNode(200));
        final N node2 = this.createNode(0).appendChild(this.createNode(199)).appendChild(this.createNode(200));
        this.checkNotEquals(node1, node2);
    }

    @Test
    public void testDifferentGrandChildren() {
        final N node1 = this.createNode(0).appendChild(this.createNode(100).appendChild(this.createNode(200)));
        final N node2 = this.createNode(0).appendChild(this.createNode(100).appendChild(this.createNode(299)));
        this.checkNotEquals(node1, node2);
    }

    @Test
    public void testSameAttributes() {
        final Map<ANAME, AVALUE> attributes = this.createAttributes(0);
        final N node1 = this.createNode(0).setAttributes(attributes);
        final N node2 = this.createNode(0).setAttributes(attributes);
        this.checkEquals(node1, node2);
    }

    @Test
    public void testDifferentAttributes() {
        final N node1 = this.createNode(0).setAttributes(this.createAttributes(0));
        final N node2 = this.createNode(0).setAttributes(this.createAttributes(1));
        this.checkNotEquals(node1, node2);
    }

    @Test
    public void testChildWithDifferentAttributes() {
        final N node1 = this.createNode(0).appendChild(this.createNode(100)).setAttributes(this.createAttributes(0));
        final N node2 = this.createNode(0).appendChild(this.createNode(100)).setAttributes(this.createAttributes(1));
        this.checkNotEquals(node1, node2);
    }

    protected abstract N createNode(int i);

    protected abstract Map<ANAME, AVALUE> createAttributes(int i);
}
