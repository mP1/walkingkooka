/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.tree.pojo;

import walkingkooka.Cast;

import java.util.Arrays;
import java.util.List;

/**
 * A {@link walkingkooka.tree.Node} where each child is an element in the original {@link boolean[]}.
 */
final class PojoBooleanArrayNode extends PojoArrayNode {

    PojoBooleanArrayNode(final PojoName name,
                         final boolean[] value,
                         final int index,
                         final PojoNodeContext context) {
        super(name, value, index, context);
    }

    private boolean[] valueAsBooleanArray() {
        return Cast.to(this.value);
    }

    // children ..................................................................................

    @Override
    final PojoNode replaceChildren(final List<PojoNode> children){
        final boolean[] newChildren = new boolean[children.size()];

        int i = 0;
        for (PojoNode child : children) {
            newChildren[i] = (boolean) child.value();
            i++;
        }

        return this.replace(newChildren);
    }

    @Override
    final PojoNode replaceChild(final PojoNode newChild) {
        final boolean[] newChildren = new boolean[this.childrenCount()];

        newChildren[newChild.index()] = (boolean) newChild.value();

        return this.replace(newChildren);
    }

    @Override
    PojoNode replaceChildrenValues(final List<Object> values) {
        final boolean[] newChildren = new boolean[values.size()];

        int i = 0;
        for (Object child : values) {
            newChildren[i] = (boolean) child;
            i++;
        }

        return this.replace(newChildren);
    }

    private PojoNode replace(final boolean[] values) {
        return new PojoBooleanArrayNode(this.name(),
                values,
                this.index(),
                this.context)
                .replaceChild(this.parent());
    }

    @Override
    Object elementValue(final int index) {
        return this.valueAsBooleanArray()[index];
    }

    @Override
    final int childrenCount() {
        return this.valueAsBooleanArray().length;
    }

    @Override
    boolean equals0(final PojoNode other){
        final PojoBooleanArrayNode otherArray = Cast.to(other);
        return Arrays.equals(this.valueAsBooleanArray(), otherArray.valueAsBooleanArray());
    }

    @Override
    final public String toString() {
        return Arrays.toString(this.valueAsBooleanArray());
    }
}
