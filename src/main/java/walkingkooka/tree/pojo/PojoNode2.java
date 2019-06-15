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

import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;

/**
 * Base class for non primitive/wrapper types, which may also have children.
 */
abstract class PojoNode2 extends PojoNode {

    /**
     * Package private ctor to limit subclassing.
     */
    PojoNode2(final PojoName name,
              final Object value,
              final int index,
              final PojoNodeContext context) {
        super(name, value, index, context);
    }

    // children ....................................................................................

    /**
     * Getter that returns the children for this node.
     */
    @Override
    public final List<PojoNode> children() {
        if (null == this.children) {
            this.children = this.createChildrenList();
        }
        return this.children;
    }

    PojoNodeChildrenList<?> children;

    /**
     * Factory that creates the list of children.
     */
    abstract PojoNodeChildrenList<?> createChildrenList();

    @Override
    public final PojoNode setChildren(final List<PojoNode> children) {
        Objects.requireNonNull(children, "children");

        final List<PojoNode> copy = Lists.array();
        copy.addAll(children);

        return this.children().equals(copy) ?
                this :
                this.replaceChildren(copy);
    }

    abstract PojoNode replaceChildren(final List<PojoNode> children);

    @Override
    final public PojoNode setChildrenValues(final List<Object> values){
        Objects.requireNonNull(values, "values");

        final List<Object> copy = Lists.array();
        copy.addAll(values);

        return this.childrenValues().equals(copy) ? this : this.replaceChildrenValues(copy);
    }

    abstract PojoNode replaceChildrenValues(final List<Object> values);

    @Override
    PojoNode clearChildrenListCache() {
        final PojoNodeChildrenList<?> children = this.children;
        if (null != children) {
            children.clearChildrenNodeCache();
        }
        return this;
    }
}
