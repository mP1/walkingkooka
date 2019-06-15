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
import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link walkingkooka.tree.Node} where each child is an element in the original {@link List}.
 */
final class PojoListNode extends PojoCollectionNode {

    PojoListNode(final PojoName name,
                 final List<Object> value,
                 final int index,
                 final PojoNodeContext context) {
        super(name, value, index, context);
    }

    // children ..................................................................................

    /**
     * The value is already a list, just cast.
     */
    @Override
    List<Object> valueAsList() {
        return Cast.to(this.value);
    }

    /**
     * Makes a copy of the values in the list {@link List} and creates a new wrapper, and ask its parent to update itself.
     */
    @Override
    final PojoNode replaceChildren(final List<PojoNode> children) {
        final List<Object> copy = children.stream()
                .map(child -> child.value())
                .collect(Collectors.toCollection(() -> this.createList()));

        return this.replace(copy);
    }

    @Override
    PojoNode replaceChild(final PojoNode newChild) {
        final List<Object> newChildren = this.createList();

        // copy all the old children except for $newChild
        final int index = newChild.index();
        int i = 0;
        for (Object child : this.valueAsList()) {
            newChildren.add(i == index ? newChild.value() : child);
            i++;
        }
        return this.replace(newChildren);
    }

    @Override
    public final List<Object> childrenValues() {
        return Lists.readOnly(this.valueAsList());
    }

    @Override
    PojoNode replaceChildrenValues(final List<Object> values) {
        final List<Object> copy = this.createList();
        copy.addAll(values);

        return this.replace(values);
    }

    /**
     * Factory that creates a new {@link List} when accepting new elements.
     */
    private List<Object> createList() {
        return this.context.createList(this.value.getClass());
    }

    private PojoNode replace(final List<Object> values) {
        return new PojoListNode(this.name(),
                values,
                this.index(),
                this.context)
                .replaceChild(this.parent());
    }

    @Override
    final int childrenCount() {
        return this.valueAsList().size();
    }
}
