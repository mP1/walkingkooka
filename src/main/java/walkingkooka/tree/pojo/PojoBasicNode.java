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
import walkingkooka.type.Classes;

import java.util.List;
import java.util.Optional;

/**
 * The {@link PojoNode} wrapper around an primitive type. Primitives have no children.
 */
final class PojoBasicNode extends PojoNode {

    static boolean isBasic(final Object value) {
        return null == value || isBasicType(value.getClass());
    }

    // String and Class are considered basic and without properties.
    private static boolean isBasicType(final Class<?> type) {
        return type == Class.class ||
                type == String.class ||
                Classes.isPrimitiveOrWrapper(type);
    }

    PojoBasicNode(final PojoName name,
                  final Object value,
                  final int index,
                  final PojoNodeContext context) {
        super(name, value, index, context);
    }

    // children ..................................................................................

    @Override
    public List<PojoNode> children() {
        return Lists.empty();
    }

    @Override
    public PojoNode setChildren(final List<PojoNode> children) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Object> childrenValues() {
        return Lists.empty();
    }

    @Override
    public PojoNode setChildrenValues(final List<Object> values) {
        throw new UnsupportedOperationException();
    }

    /**
     * Primitive types have no children
     */
    @Override
    PojoNode replaceChild(final PojoNode child) {
        throw new UnsupportedOperationException();
    }

    @Override
    int childrenCount() {
        return 0;
    }

    @Override
    PojoNode clearChildrenListCache() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<PojoNode> firstChild() {
        return NO_CHILD;
    }

    @Override
    public Optional<PojoNode> lastChild() {
        return NO_CHILD;
    }

    private final static Optional<PojoNode> NO_CHILD = Optional.empty();

    @Override
    public PojoNode removeChild(final int child) {
        throw new UnsupportedOperationException();
    }

    @Override
    boolean equals0(final PojoNode other) {
        return this.equals1(other);
    }

    @Override
    public final String toString() {
        return this.toString0();
    }
}
