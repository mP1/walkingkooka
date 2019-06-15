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

/**
 * Readonly list view of {@link PojoObjectNode#childrenValues()}
 */
final class PojoArrayNodeChildrenValueList extends PojoNodeList<PojoArrayNode, Object> {

    static {
        Lists.registerImmutableType(PojoArrayNodeChildrenValueList.class);
    }

    static PojoArrayNodeChildrenValueList with(final PojoArrayNode parent) {
        return new PojoArrayNodeChildrenValueList(parent);
    }

    private PojoArrayNodeChildrenValueList(final PojoArrayNode parent) {
        super(parent);
    }

    @Override
    public Object get(final int index) {
        return this.parent.elementValue(index);
    }

    @Override
    public int size() {
        return this.parent.childrenCount();
    }

    @Override
    boolean isSameType(final Object other) {
        return other instanceof PojoArrayNodeChildrenValueList;
    }

    @Override
    boolean equals0(final PojoNodeList other) {
        return this.parent.equals(other.parent);
    }
}
