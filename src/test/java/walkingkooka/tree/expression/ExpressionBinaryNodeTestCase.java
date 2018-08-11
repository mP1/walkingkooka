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

package walkingkooka.tree.expression;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;

import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

public abstract class ExpressionBinaryNodeTestCase<N extends ExpressionBinaryNode> extends ExpressionParentFixedNodeTestCase<N> {

    final static String LEFT = "left-123";
    final static String RIGHT = "right-456";

    final static CharSequence LEFT_TO_STRING = CharSequences.quoteAndEscape(LEFT);
    final static CharSequence RIGHT_TO_STRING = CharSequences.quoteAndEscape(RIGHT);

    @Test(expected = NullPointerException.class)
    public final void testWithNullLeftFails() {
        this.createExpressionNode(null, this.right());
    }

    @Test(expected = NullPointerException.class)
    public final void testWithNullRightFails() {
        this.createExpressionNode(this.left(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetChildrenZeroFails() {
        this.createExpressionNode().setChildren(ExpressionNode.NO_CHILDREN);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetChildrenOneFails() {
        this.createExpressionNode().setChildren(Lists.of(left()));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetChildrenThreeFails() {
        this.createExpressionNode().setChildren(Lists.of(left(), right(), text("third")));
    }

    @Test
    public void testSetDifferentChildren() {
        final N expression = this.createExpressionNode();
        final List<ExpressionNode> children = expression.children();

        final List<ExpressionNode> differentChildren = Lists.of(differentLeft(), differentRight());
        final N different = expression.setChildren(differentChildren).cast();
        assertNotSame(expression, different);

        this.checkChildren(different, differentChildren);
        this.checkChildren(expression, children);

        assertSame("original children", children, expression.children());
        assertSame("updated children", differentChildren, different.children());
    }

    @Test
    public void testSetDifferentChildrenListCopied() {
        final N expression = this.createExpressionNode();

        final List<ExpressionNode> differentChildren = Lists.array();
        differentChildren.add(differentLeft());
        differentChildren.add(differentRight());

        final N different = expression.setChildren(differentChildren).cast();
        assertNotSame(expression, different);

        this.checkChildren(different, differentChildren);
        checkChildren(expression, this.children());
    }

    @Test
    public void testToString() {
        assertEquals(this.expectedToString(), this.createExpressionNode().toString());
    }

    abstract String expectedToString();

    @Test
    public final void testEqualsDifferentChildren() {
        assertNotEquals(this.createExpressionNode(), this.createExpressionNode(differentLeft(), differentRight()));
    }

    @Override
    N createExpressionNode() {
        return this.createExpressionNode(this.left(), this.right());
    }

    abstract N createExpressionNode(final ExpressionNode left, final ExpressionNode right);

    @Override
    List<ExpressionNode> children(){
        return Lists.of(this.left(), this.right());
    }

    final ExpressionNode left() {
        return text(LEFT);
    }

    final ExpressionNode right() {
        return text(RIGHT);
    }

    final ExpressionNode differentLeft() {
        return text("different-left123");
    }

    final ExpressionNode differentRight() {
        return text("different-right456");
    }
}
