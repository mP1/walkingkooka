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

import org.junit.jupiter.api.Test;
import walkingkooka.build.BuilderTesting;
import walkingkooka.naming.PathSeparator;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class NodeSelectorToStringBuilderTest extends ClassTestCase<NodeSelectorToStringBuilder>
        implements BuilderTesting<NodeSelectorToStringBuilder, String> {

    @Test
    public void testAxis() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.axis("child1");
        this.buildAndCheck(b, "child1::*");
    }

    @Test
    public void testAxisNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.axis("child1");
        b.node("abc2");
        this.buildAndCheck(b, "child1::*/abc2");
    }

    @Test
    public void testNodeAxis() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.node("abc1");
        b.axis("child2");
        this.buildAndCheck(b, "child2::abc1");
    }

    @Test
    public void testAxisPredicate() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.axis("child1");
        b.predicate("i>abc2");
        this.buildAndCheck(b, "child1::*/*[i>abc2]");
    }

    @Test
    public void testPredicateAxis() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.predicate("i>1");
        b.axis("child1");
        this.buildAndCheck(b, "child1::*[i>1]");
    }

    @Test
    public void testAxisNodePredicate() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.axis("child1");
        b.node("abc2");
        b.predicate("i>abc2");
        this.buildAndCheck(b, "child1::*/abc2[i>abc2]");
    }

    @Test
    public void testAxisPredicateNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.axis("child1");
        b.predicate("i>abc2");
        b.node("abc2");
        this.buildAndCheck(b, "child1::*/abc2[i>abc2]");
    }

    @Test
    public void testNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.node("abc1");
        this.buildAndCheck(b, "abc1");
    }

    @Test
    public void testNodePredicate() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.node("abc1");
        b.predicate("i>1");
        this.buildAndCheck(b, "abc1[i>1]");
    }

    @Test
    public void testPredicateNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.predicate("i>1");
        b.node("abc1");
        this.buildAndCheck(b, "abc1[i>1]");
    }

    @Test
    public void testPredicateNodePredicateNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.predicate("i>1");
        b.node("abc1");
        b.predicate("i>2");
        b.node("def2");
        this.buildAndCheck(b, "abc1[i>1]/def2[i>2]");
    }

    @Test
    public void testPredicate() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.predicate("i>1");
        this.buildAndCheck(b, "*[i>1]");
    }

    @Test
    public void testSelf() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.self();
        this.buildAndCheck(b, ".");
    }

    @Test
    public void testParent() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.parent();
        this.buildAndCheck(b, "..");
    }

    @Test
    public void testNodeNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.node("abc1");
        b.node("def2");
        this.buildAndCheck(b, "abc1/def2");
    }

    @Test
    public void testNodePredicateNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.node("abc1");
        b.predicate("i>1");
        b.node("def2");
        this.buildAndCheck(b, "abc1[i>1]/def2");
    }

    @Test
    public void testNodeSelfNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.node("abc1");
        b.self();
        b.node("def3");
        this.buildAndCheck(b, "abc1/./def3");
    }

    @Test
    public void testNodeParentOfNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.node("abc1");
        b.parent();
        b.node("def3");
        this.buildAndCheck(b, "abc1/../def3");
    }

    @Test
    public void testNode3() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.node("abc1");
        b.node("def2");
        b.node("ghi3");
        this.buildAndCheck(b, "abc1/def2/ghi3");
    }

    @Test
    public void testNode3DifferentPathRequiredAtStart() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.separator(PathSeparator.requiredAtStart('\\'));
        b.node("abc1");
        b.node("def2");
        b.node("ghi3");
        this.buildAndCheck(b, "abc1\\def2\\ghi3");
    }

    @Test
    public void testNode3DifferentPathRequiredAtStartAbsolute() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.absolute(PathSeparator.requiredAtStart('\\'));
        b.node("abc1");
        b.node("def2");
        b.node("ghi3");
        this.buildAndCheck(b, "\\abc1\\def2\\ghi3");
    }

    @Test
    public void testNode3DifferentPath() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.separator(PathSeparator.notRequiredAtStart('\\'));
        b.node("abc1");
        b.node("def2");
        b.node("ghi3");
        this.buildAndCheck(b, "abc1\\def2\\ghi3");
    }

    @Test
    public void testNodeNodeAxis() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.node("abc1");
        b.node("def2");
        b.axis("child2");
        this.buildAndCheck(b, "abc1/child2::def2");
    }

    @Test
    public void testNodeNodeAxisNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.node("abc1");
        b.node("def2");
        b.axis("child2");
        b.axis("ghi3");
        this.buildAndCheck(b, "abc1/child2::def2/ghi3::*");
    }

    @Test
    public void testNodeAxisNodeAxis() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.node("abc1");
        b.axis("child1");
        b.node("abc2");
        b.axis("child2");
        this.buildAndCheck(b, "child1::abc1/child2::abc2");
    }

    @Test
    public void tesAxistNodeNodeAxis2() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.axis("child1");
        b.node("def2");
        b.axis("child2");
        b.node("ghi3");
        this.buildAndCheck(b, "child1::*/child2::def2/ghi3");
    }

    @Test
    public void testNodeSelf() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.node("child1");
        b.self();
        this.buildAndCheck(b, "child1/.");
    }

    @Test
    public void testNodeParent() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.node("child1");
        b.parent();
        this.buildAndCheck(b, "child1/..");
    }

    @Test
    public void testAbsolute() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.absolute(PathSeparator.requiredAtStart('/'));
        this.buildAndCheck(b, "/");
    }

    @Test
    public void testAbsoluteNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.absolute(PathSeparator.requiredAtStart('/'));
        b.node("abc1");
        this.buildAndCheck(b, "/abc1");
    }

    @Test
    public void testAbsoluteNode2() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.absolute(PathSeparator.requiredAtStart('/'));
        b.node("abc1");
        b.node("def2");
        this.buildAndCheck(b, "/abc1/def2");
    }

    @Test
    public void testAbsoluteNodePredicateNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.absolute(PathSeparator.requiredAtStart('/'));
        b.node("abc1");
        b.predicate("i>1");
        b.node("def2");
        this.buildAndCheck(b, "/abc1[i>1]/def2");
    }

    @Test
    public void testDescendantOrSelf() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.descendantOrSelf(PathSeparator.requiredAtStart('/'));
        this.buildAndCheck(b, "//");
    }

    @Test
    public void testDescendantOrSelfNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.descendantOrSelf(PathSeparator.requiredAtStart('/'));
        b.node("abc1");
        this.buildAndCheck(b, "//abc1");
    }

    @Test
    public void testDescendantOrSelfNode2() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.descendantOrSelf(PathSeparator.requiredAtStart('/'));
        b.node("abc1");
        b.node("def2");
        this.buildAndCheck(b, "//abc1/def2");
    }

    @Test
    public void testDescendantOrSelfNodePredicateNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.descendantOrSelf(PathSeparator.requiredAtStart('/'));
        b.node("abc1");
        b.predicate("i>1");
        b.node("def2");
        this.buildAndCheck(b, "//abc1[i>1]/def2");
    }

    @Test
    public void testNodePredicateAxis() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.descendantOrSelf(PathSeparator.requiredAtStart('/'));
        b.node("abc1");
        b.predicate("i>1");
        b.axis("child1");
        this.buildAndCheck(b, "//child1::abc1[i>1]");
    }

    @Test
    public void testNodePredicateAxisNode() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.descendantOrSelf(PathSeparator.requiredAtStart('/'));
        b.node("abc1");
        b.predicate("i>1");
        b.axis("child1");
        b.node("def2");
        this.buildAndCheck(b, "//child1::abc1[i>1]/def2");
    }

    @Test
    public void testToString() {
        assertEquals("", this.createBuilder().toString());
    }

    @Test
    public void testToString2() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.axis("child1");
        b.node("abc2");
        b.predicate("i>2");

        assertEquals("\"abc2\" \"i>2\"", b.toString());
    }

    @Override
    public NodeSelectorToStringBuilder createBuilder() {
        return NodeSelectorToStringBuilder.empty();
    }

    @Override
    public Class<String> builderProductType() {
        return String.class;
    }

    @Override
    public Class<NodeSelectorToStringBuilder> type() {
        return NodeSelectorToStringBuilder.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
