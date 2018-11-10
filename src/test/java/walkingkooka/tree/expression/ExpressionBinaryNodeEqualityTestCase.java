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

public abstract class ExpressionBinaryNodeEqualityTestCase<N extends ExpressionBinaryNode> extends
        ExpressionParentFixedNodeEqualityTestCase<N> {

    ExpressionBinaryNodeEqualityTestCase() {
        super();
    }

    @Test
    public final void testDifferentLeft() {
        this.checkNotEquals(this.createObject(this.different(), this.right()));
    }

    @Test
    public final void testDifferentRight() {
        this.checkNotEquals(this.createObject(this.left(), this.different()));
    }

    @Override
    public final N createObject() {
        return this.createObject(this.left(), this.right());
    }

    private ExpressionNode left() {
        return ExpressionNode.longNode(1);
    }

    private ExpressionNode right() {
        return ExpressionNode.longNode(2);
    }

    private ExpressionNode different() {
        return ExpressionNode.longNode(99);
    }

    abstract N createObject(final ExpressionNode left, final ExpressionNode right);
}
