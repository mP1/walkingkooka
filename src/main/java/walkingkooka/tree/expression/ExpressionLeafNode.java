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

import walkingkooka.ShouldNeverHappenError;
import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;

/**
 * A leaf node, where a leaf has no children.
 */
abstract class ExpressionLeafNode extends ExpressionNode {

    ExpressionLeafNode(final int index){
        super(index);
    }

    @Override
    final ExpressionNode wrap(final int index) {
        return this.wrap0(index);
    }

    abstract ExpressionLeafValueNode wrap0(final int index);

    @Override
    public final List<ExpressionNode> children() {
        return Lists.empty();
    }

    /**
     * By definition a leaf node has no children.
     */
    @Override
    public final ExpressionNode setChildren(final List<ExpressionNode> children) {
        Objects.requireNonNull(children, "chidren");
        throw new UnsupportedOperationException();
    }

    @Override
    final ExpressionNode setChild(final ExpressionNode newChild) {
        throw new ShouldNeverHappenError(this.getClass().getSimpleName() + ".setChild");
    }

    // is ................................................................................................................

    @Override
    public final boolean isAddition() {
        return false;
    }

    @Override
    public final boolean isAnd() {
        return false;
    }

    @Override
    public final boolean isDivision() {
        return false;
    }

    @Override
    public final boolean isEquals() {
        return false;
    }

    @Override
    public final boolean isFunction() {
        return false;
    }

    @Override
    public final boolean isGreaterThan() {
        return false;
    }
    
    @Override
    public final boolean isGreaterThanEquals() {
        return false;
    }

    @Override
    public final boolean isLessThan() {
        return false;
    }

    @Override
    public final boolean isLessThanEquals() {
        return false;
    }

    @Override
    public final boolean isModulo() {
        return false;
    }

    @Override
    public final boolean isMultiplication() {
        return false;
    }

    @Override
    public final boolean isNegative() {
        return false;
    }

    @Override
    public final boolean isNot() {
        return false;
    }

    @Override
    public final boolean isNotEquals() {
        return false;
    }

    @Override
    public final boolean isOr() {
        return false;
    }

    @Override
    public final boolean isPower() {
        return false;
    }

    @Override
    public final boolean isSubtraction() {
        return false;
    }

    @Override
    public final boolean isXor() {
        return false;
    }
}
