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

/**
 * Readonly list view of {@link PojoObjectNode#childrenValues()}
 */
final class PojoObjectNodeChildrenValueList extends PojoNodeList<PojoObjectNode, Object> {

    static {
        Lists.registerImmutableType(PojoObjectNodeChildrenValueList.class);
    }

    static PojoObjectNodeChildrenValueList with(final PojoObjectNode parent) {
        return new PojoObjectNodeChildrenValueList(parent);
    }

    private PojoObjectNodeChildrenValueList(final PojoObjectNode parent) {
        super(parent);
    }

    @Override
    public Object get(int index) {
        return this.properties().get(index).get(this.parent.value);
    }

    @Override
    public int size() {
        return this.properties().size();
    }

    private List<PojoProperty> properties() {
        return this.parent.properties();
    }

    @Override
    boolean isSameType(final Object other) {
        return other instanceof PojoObjectNodeChildrenValueList;
    }

    /**
     * Compare for equality using parent v parent.
     */
    @Override
    boolean equals0(final PojoNodeList other) {
        return this.parent.value.equals(other.parent.value);
    }
}
