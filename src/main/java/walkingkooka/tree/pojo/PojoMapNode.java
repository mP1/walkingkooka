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
import java.util.Map;
import java.util.Map.Entry;

/**
 * A {@link walkingkooka.tree.Node} where each child is an element in the original {@link Map}.
 * Almost an identical copy of {@link PojoListNode} but uses the adapter {@link PojoMapNodeMapList}.
 */
final class PojoMapNode extends PojoCollectionNode {

    PojoMapNode(final PojoName name,
                final Map<Object, Object> value,
                final int index,
                final PojoNodeContext context) {
        super(name, value, index, context);
    }

    private Map<Object, Object> valueAsMap() {
        return Cast.to(this.value());
    }

    // children ..................................................................................

    @Override
    List<Object> valueAsList() {
        if (null == this.list) {
            this.list = new PojoMapNodeMapList(this.valueAsMap());
        }
        return this.list;
    }

    private PojoMapNodeMapList list;

    /**
     * Makes a copy of the values in the list {@link List} and creates a new wrapper, and ask its parent to update itself.
     */
    @Override
    final PojoNode replaceChildren(final List<PojoNode> children){
        final Map<Object, Object> newChildren = this.createMap();
        for (PojoNode child : children) {
            final Entry<Object, Object> value = Cast.to(child.value());
            newChildren.put(value.getKey(), value.getValue());
        }
        return this.replace(newChildren);
    }

    @Override
    PojoNode replaceChild(final PojoNode newChild) {
        final Map<Object, Object> newChildren = this.createMap();

        // copy all the old children except for $newChild
        final int index = newChild.index();
        int i = 0;
        for (Entry<Object, Object> child : this.valueAsMap().entrySet()) {
            Object key = child.getKey();
            Object value = child.getValue();
            if (i == index) {
                child = Cast.to(newChild.value());
                key = child.getKey();
                value = child.getValue();
            }
            newChildren.put(key, value);
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
        final Map<Object, Object> copy = this.createMap();

        for (Object value : values) {
            final Entry<Object, Object> entry = Cast.to(value);
            copy.put(entry.getKey(), entry.getValue());
        }

        return this.replace(copy);
    }

    private Map<Object, Object> createMap() {
        return this.context.createMap(this.value.getClass());
    }

    private PojoNode replace(final Map<Object, Object> values) {
        return new PojoMapNode(this.name(),
                values,
                this.index(),
                this.context)
                .replaceChild(this.parent());
    }

    @Override
    final int childrenCount() {
        return this.valueAsMap().size();
    }
}
