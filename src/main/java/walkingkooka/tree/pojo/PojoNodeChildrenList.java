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

import java.util.Optional;

/**
 * Base for all {@link java.util.List} of children.
 */
abstract class PojoNodeChildrenList<P extends PojoNode> extends PojoNodeList<P, PojoNode> {

    PojoNodeChildrenList(final P parent) {
        super(parent);
        this.parentOptional = Optional.of(parent);
    }

    @Override
    public final int size() {
        return this.parent.childrenCount();
    }

    abstract Object elementValue(final int index);

    abstract PojoNode replace(final int index);

    final PojoNode replace0(final PojoName name, final Object instance, final int index) {
        return PojoNode.wrap0(
                name,
                instance,
                index,
                this.parent.context).setParent(this.parentOptional);
    }

    final Optional<PojoNode> parentOptional;

    abstract void clearChildrenNodeCache();

    /**
     * Since the children list is just a view of the parent, test for equality using the parents.
     */
    @Override
    final boolean equals0(final PojoNodeList other) {
        return this.parent.equals(other.parent);
    }
}
