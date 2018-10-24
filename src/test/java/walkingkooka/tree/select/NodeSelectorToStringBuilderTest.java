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

package walkingkooka.tree.select;

import org.junit.Test;
import walkingkooka.build.BuilderTestCase;
import walkingkooka.naming.PathSeparator;

import static org.junit.Assert.assertEquals;

public final class NodeSelectorToStringBuilderTest extends BuilderTestCase<NodeSelectorToStringBuilder, String> {

    @Test
    public void testAxis() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.axis("child");
        this.buildAndCheck(b, "child::*");
    }

    @Test
    public void testAxisNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.axis("child");
        b.node("abc");
        this.buildAndCheck(b, "child::abc");
    }

    @Test
    public void testAxisNode2() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.node("abc");
        b.axis("child");
        this.buildAndCheck(b, "child::abc");
    }

    @Test
    public void testAxisPredicate() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.axis("child");
        b.predicate("i>0");
        this.buildAndCheck(b, "child::*[i>0]");
    }

    @Test
    public void testAxisPredicate2() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.predicate("i>0");
        b.axis("child");
        this.buildAndCheck(b, "child::*[i>0]");
    }

    @Test
    public void testAxisNodePredicate() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.axis("child");
        b.node("abc");
        b.predicate("i>0");
        this.buildAndCheck(b, "child::abc[i>0]");
    }

    @Test
    public void testAxisNodePredicate2() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.axis("child");
        b.predicate("i>0");
        b.node("abc");
        this.buildAndCheck(b, "child::abc[i>0]");
    }

    @Test
    public void testNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.node("abc");
        this.buildAndCheck(b, "abc");
    }

    @Test
    public void testNodePredicate() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.node("abc");
        b.predicate("i>0");
        this.buildAndCheck(b, "abc[i>0]");
    }

    @Test
    public void testNodePredicate2() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.predicate("i>0");
        b.node("abc");
        this.buildAndCheck(b, "abc[i>0]");
    }

    @Test
    public void testPredicate() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.predicate("i>0");
        this.buildAndCheck(b, "*[i>0]");
    }

    @Test
    public void testSelf() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.self();
        this.buildAndCheck(b, ".");
    }

    @Test
    public void testParent() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.parent();
        this.buildAndCheck(b, "..");
    }

    @Test
    public void testNode2() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.node("abc");
        b.node("def");
        this.buildAndCheck(b, "abc/def");
    }

    @Test
    public void testNodePredicateNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.node("abc");
        b.predicate("i>0");
        b.node("def");
        this.buildAndCheck(b, "abc[i>0]/def");
    }

    @Test
    public void testNodeSelfNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.node("abc");
        b.self();
        b.node("def");
        this.buildAndCheck(b, "abc/./def");
    }

    @Test
    public void testNodeParentOfNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.node("abc");
        b.parent();
        b.node("def");
        this.buildAndCheck(b, "abc/../def");
    }

    @Test
    public void testNode3() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.node("abc");
        b.node("def");
        b.node("ghi");
        this.buildAndCheck(b, "abc/def/ghi");
    }

    @Test
    public void testNode3DifferentPathRequiredAtStart() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.separator(PathSeparator.requiredAtStart('\\'));
        b.node("abc");
        b.node("def");
        b.node("ghi");
        this.buildAndCheck(b, "abc\\def\\ghi");
    }

    @Test
    public void testNode3DifferentPathRequiredAtStartAbsolute() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.absolute(PathSeparator.requiredAtStart('\\'));
        b.node("abc");
        b.node("def");
        b.node("ghi");
        this.buildAndCheck(b, "\\abc\\def\\ghi");
    }

    @Test
    public void testNode3DifferentPath() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.separator(PathSeparator.notRequiredAtStart('\\'));
        b.node("abc");
        b.node("def");
        b.node("ghi");
        this.buildAndCheck(b, "abc\\def\\ghi");
    }

    @Test
    public void testNode2Axis() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.node("abc");
        b.node("def");
        b.axis("child");
        this.buildAndCheck(b, "abc/child::def");
    }

    @Test
    public void testNodeAxisNodeAxis() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.node("abc");
        b.axis("child");
        b.node("abc2");
        b.axis("child2");
        this.buildAndCheck(b, "child::abc/child2::abc2");
    }

    @Test
    public void testNodeAxisNodeAxis2() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.axis("child");
        b.node("abc");
        b.axis("child2");
        b.node("abc2");
        this.buildAndCheck(b, "child::abc/child2::abc2");
    }

    @Test
    public void testNodeSelf() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.node("child");
        b.self();
        this.buildAndCheck(b, "child/.");
    }

    @Test
    public void testNodeParent() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.node("child");
        b.parent();
        this.buildAndCheck(b, "child/..");
    }

    @Test
    public void testAbsolute() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.absolute(PathSeparator.requiredAtStart('/'));
        this.buildAndCheck(b, "/");
    }

    @Test
    public void testAbsoluteNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.absolute(PathSeparator.requiredAtStart('/'));
        b.node("abc");
        this.buildAndCheck(b, "/abc");
    }

    @Test
    public void testAbsoluteNode2() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.absolute(PathSeparator.requiredAtStart('/'));
        b.node("abc");
        b.node("def");
        this.buildAndCheck(b, "/abc/def");
    }

    @Test
    public void testAbsoluteNodePredicateNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.absolute(PathSeparator.requiredAtStart('/'));
        b.node("abc");
        b.predicate("i>0");
        b.node("def");
        this.buildAndCheck(b, "/abc[i>0]/def");
    }

    @Test
    public void testDescendant() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.descendant(PathSeparator.requiredAtStart('/'));
        this.buildAndCheck(b, "//");
    }

    @Test
    public void testDescendantNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.descendant(PathSeparator.requiredAtStart('/'));
        b.node("abc");
        this.buildAndCheck(b, "//abc");
    }

    @Test
    public void testDescendantNode2() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.descendant(PathSeparator.requiredAtStart('/'));
        b.node("abc");
        b.node("def");
        this.buildAndCheck(b, "//abc/def");
    }

    @Test
    public void testDescendantNodePredicateNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.descendant(PathSeparator.requiredAtStart('/'));
        b.node("abc");
        b.predicate("i>0");
        b.node("def");
        this.buildAndCheck(b, "//abc[i>0]/def");
    }

    @Test
    public void testToString() {
        assertEquals("", this.createBuilder().toString());
    }

    @Test
    public void testToString2() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.create();
        b.axis("child");
        b.node("abc");
        b.predicate("i>0");

        assertEquals("\"child\" \"abc\" \"i>0\"", b.toString());
    }

    @Override
    protected NodeSelectorToStringBuilder createBuilder() {
        return NodeSelectorToStringBuilder.create();
    }

    @Override
    protected Class<String> builderProductType() {
        return String.class;
    }

    @Override
    protected Class<NodeSelectorToStringBuilder> type() {
        return NodeSelectorToStringBuilder.class;
    }

    @Override
    protected boolean typeMustBePublic() {
        return false;
    }
}
