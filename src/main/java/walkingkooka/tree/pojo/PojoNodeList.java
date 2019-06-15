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

import java.util.AbstractList;
import java.util.List;

/**
 * Base for all {@link List} of children.
 */
abstract class PojoNodeList<P extends PojoNode, E> extends AbstractList<E> {

    PojoNodeList(final P parent) {
        this.parent = parent;
    }

    final P parent;

    // Object .......................................................................................................

    @Override
    public final int hashCode() {
        return this.parent.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this.isSameType(other) ?
                equals0(Cast.to(other)) :
                super.equals(other);
    }

    abstract boolean isSameType(final Object other);

    abstract boolean equals0(final PojoNodeList other);

    @Override
    public final String toString() {
        return this.parent.toString();
    }
}
