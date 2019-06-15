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
 * A {@link walkingkooka.tree.Node} where each child is an element in the original {@link long[]}.
 */
final class PojoLongArrayNode extends PojoArrayNode {

    PojoLongArrayNode(final PojoName name,
                      final long[] value,
                      final int index,
                      final PojoNodeContext context) {
        super(name, value, index, context);
    }

    private long[] valueAsLongArray() {
        return Cast.to(this.value);
    }

    // children ..................................................................................

    @Override
    final PojoNode replaceChildren(final List<PojoNode> children){
        final long[] newChildren = new long[children.size()];

        int i = 0;
        for (PojoNode child : children) {
            newChildren[i] = (long) child.value();
            i++;
        }

        return this.replace(newChildren);
    }

    @Override
    final PojoNode replaceChild(final PojoNode newChild) {
        final long[] newChildren = new long[this.childrenCount()];

        newChildren[newChild.index()] = (long) newChild.value();

        return this.replace(newChildren);
    }

    @Override
    PojoNode replaceChildrenValues(final List<Object> values) {
        final long[] newChildren = new long[values.size()];

        int i = 0;
        for (Object child : values) {
            newChildren[i] = (long) child;
            i++;
        }

        return this.replace(newChildren);
    }

    private PojoNode replace(final long[] values) {
        return new PojoLongArrayNode(this.name(),
                values,
                this.index(),
                this.context)
                .replaceChild(this.parent());
    }

    @Override
    Object elementValue(final int index) {
        return this.valueAsLongArray()[index];
    }

    @Override
    final int childrenCount() {
        return this.valueAsLongArray().length;
    }

    @Override
    boolean equals0(final PojoNode other) {
        final PojoLongArrayNode otherArray = Cast.to(other);
        return Arrays.equals(this.valueAsLongArray(), otherArray.valueAsLongArray());
    }

    @Override
    final public String toString() {
        return Arrays.toString(this.valueAsLongArray());
    }
}
