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

import walkingkooka.Cast;
import walkingkooka.Value;

import java.util.Objects;

/**
 * A leaf node with a single value.
 */
abstract class ExpressionLeafValueNode<V> extends ExpressionLeafNode implements Value<V> {

    ExpressionLeafValueNode(final int index, final V value){
        super(index);
        this.value = value;
    }

    @Override
    public final V value() {
        return this.value;
    }

    final V value;

    //abstract ExpressionLeafNode<V> setValue(final V value);

    final ExpressionLeafValueNode<V> setValue0(final V value) {
        return Objects.equals(this.value(), value) ?
                this :
                this.replaceValue(value);
    }

    final ExpressionLeafValueNode<V> replaceValue(final V value) {
        return this.wrap1(this.index, value)
                .replaceChild(this.parent())
                .cast();
    }

    final ExpressionLeafValueNode wrap0(final int index) {
        return this.wrap1(index, this.value);
    }

    abstract ExpressionLeafValueNode wrap1(final int index, final V value);

    // Object ......................................................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    final boolean equalsDescendants0(final ExpressionNode other) {
        return true;
    }

    @Override
    final boolean equalsIgnoringParentAndChildren(final ExpressionNode other) {
        return other instanceof ExpressionLeafValueNode &&
               equalsIgnoringParentAndChildren0(Cast.to(other));

    }

    private boolean equalsIgnoringParentAndChildren0(final ExpressionLeafValueNode other) {
        return this.value.equals(other.value);

    }
}
