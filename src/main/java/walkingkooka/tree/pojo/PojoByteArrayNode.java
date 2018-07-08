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

package walkingkooka.tree.pojo;

import walkingkooka.Cast;

import java.util.Arrays;
import java.util.List;

/**
 * A {@link walkingkooka.tree.Node} where each child is an element in the original {@link byte[]}.
 */
final class PojoByteArrayNode extends PojoArrayNode {

    PojoByteArrayNode(final PojoName name,
                      final byte[] value,
                      final int index,
                      final PojoNodeContext context) {
        super(name, value, index, context);
    }

    private byte[] valueAsByteArray() {
        return Cast.to(this.value);
    }

    // children ..................................................................................

    @Override
    final PojoNode replaceChildren(final List<PojoNode> children){
        final byte[] newChildren = new byte[children.size()];

        int i = 0;
        for(PojoNode child : children){
            newChildren[i] = (byte)child.value();
            i++;
        }

        return this.wrap(newChildren);
    }

    @Override
    final PojoNode replaceChild(final PojoNode newChild) {
        final byte[] newChildren = new byte[this.childrenCount()];

        newChildren[newChild.index()]=(byte)newChild.value();

        return this.wrap(newChildren);
    }

    @Override
    PojoNode replaceChildrenValues(final List<Object> values) {
        final byte[] newChildren = new byte[values.size()];

        int i = 0;
        for(Object child : values){
            newChildren[i] = (byte)child;
            i++;
        }

        return this.wrap(newChildren);
    }

    private PojoNode wrap(final byte[] values){
        return new PojoByteArrayNode(this.name(),
                values,
                this.index(),
                this.context)
                .replaceChild(this.parent());
    }

    @Override
    Object elementValue(final int index) {
        return this.valueAsByteArray()[index];
    }

    @Override
    final int childrenCount() {
        return this.valueAsByteArray().length;
    }

    @Override
    boolean equals0(final PojoNode other){
        final PojoByteArrayNode otherArray = Cast.to(other);
        return Arrays.equals(this.valueAsByteArray(), otherArray.valueAsByteArray());
    }

    @Override
    final public String toString() {
        return Arrays.toString(this.valueAsByteArray());
    }
}
