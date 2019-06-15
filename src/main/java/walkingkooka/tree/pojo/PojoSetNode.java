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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link walkingkooka.tree.Node} where each child is an element in the original {@link Set}.
 * Almost an identical copy of {@link PojoListNode} but uses the adapter {@link PojoSetNodeSetList}.
 */
final class PojoSetNode extends PojoCollectionNode {

    PojoSetNode(final PojoName name,
                final Set<Object> value,
                final int index,
                final PojoNodeContext context) {
        super(name, value, index, context);
    }

    // children ..................................................................................

    private Set<Object> valueAsSet() {
        return Cast.to(this.value());
    }

    @Override
    List<Object> valueAsList() {
        if (null == this.list) {
            this.list = PojoSetNodeSetList.with(this.valueAsSet());
        }
        return this.list;
    }

    private PojoSetNodeSetList list;

    /**
     * Makes a copy of the values in the list {@link List} and creates a new wrapper, and ask its parent to update itself.
     */
    @Override
    final PojoNode replaceChildren(final List<PojoNode> children){
        final Set<Object> copy = children.stream()
                .map(child -> child.value())
                .collect(Collectors.toCollection(() -> this.createSet()));

        return this.replace(copy);
    }

    @Override
    PojoNode replaceChild(final PojoNode newChild) {
        final Set<Object> newChildren = this.createSet();

        final int index = this.index();
        int i = 0;
        for (Object value : this.valueAsSet()) {
            newChildren.add(index == i ?
                    newChild.value() :
                    value);
            i++;
        }

        return this.replace(newChildren);
    }

    @Override
    public final List<Object> childrenValues() {
        return this.valueAsList();
    }

    @Override
    PojoNode replaceChildrenValues(final List<Object> values) {
        final Set<Object> copy = this.createSet();
        copy.addAll(values);

        return this.replace(copy);
    }

    private Set<Object> createSet() {
        return this.context.createSet(this.value.getClass());
    }

    private PojoNode replace(final Set<Object> values) {
        return new PojoSetNode(this.name(),
                values,
                this.index(),
                this.context)
                .replaceChild(this.parent());
    }

    @Override
    final int childrenCount() {
        return this.valueAsSet().size();
    }
}
