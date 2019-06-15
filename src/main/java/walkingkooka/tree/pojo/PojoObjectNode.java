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

import walkingkooka.collect.map.Maps;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Map;

/**
 * The {@link PojoNode} wrapper around an object (including primitive types) value.
 */
final class PojoObjectNode extends PojoNode2 {

    PojoObjectNode(final PojoName name,
                   final Object value,
                   final int index,
                   final PojoNodeContext context) {
        super(name, value, index, context);
    }

    // children ..................................................................................

    @Override
    PojoObjectNodeChildrenList createChildrenList() {
        return PojoObjectNodeChildrenList.with(this);
    }

    /**
     * Accepts all children, updating the properties of the value, support setters that return a new value.
     * If the final value is different from the original a new {@link PojoObjectNode} is created and wrapped.
     */
    @Override
    final PojoNode replaceChildren(final List<PojoNode> children) {
        final Object before = this.value();
        Object current = before;

        int i = 0;
        final Map<PojoName, PojoProperty> properties = this.nameToProperties();
        for (PojoNode child : children) {
            if (null == child) {
                throw new NullPointerException("Child at " + i + " has null " + PojoNode.class.getSimpleName());
            }
            final PojoProperty property = properties.get(child.name());
            if (null == property) {
                throw new IllegalArgumentException("Unknown property " + CharSequences.quoteIfChars(child.name()) + " = " + child);
            }
            //if(!property.isReadOnly()){
            // try and set property
            final Object after = property.set(current, child.value());
            if (null != after) {
                current = after;
            }
            //}
            i++;
            if (i > properties.size()) {
                throw new IndexOutOfBoundsException("Too many children " + i + "=" + child);
            }
        }

        return this.wrapIfDifferent(before, current);
    }

    /**
     * Uses the new given child and updates only that property and if the instance is different returns a new node.
     */
    @Override
    PojoNode replaceChild(final PojoNode child) {
        final Object before = this.value();
        Object current = before;

        final PojoProperty property = this.properties().get(child.index());
        child.name().check(property);
        if (property.isReadOnly()) {
            throw new UnsupportedOperationException("Property " + CharSequences.quoteIfChars(property.name()) + " is read only");
        }

        final Object after = property.set(before, child.value());
        if (null != after) {
            current = after;
        }

        return this.wrapIfDifferent(before, current);
    }

    /**
     * Accepts the value after setting all properties, and if its different from the original, replace it,
     * and also notify any present parent.
     */
    private PojoNode wrapIfDifferent(final Object before, final Object after) {
        return before.equals(after) ?
                this.clearChildrenListCache() :
                wrap0(this.name(), after, this.index(), this.context)
                        .replaceChild(this.parent());
    }

    @Override
    public List<Object> childrenValues() {
        if (null == this.childrenValueList) {
            this.childrenValueList = PojoObjectNodeChildrenValueList.with(this);
        }
        return this.childrenValueList;
    }

    private PojoObjectNodeChildrenValueList childrenValueList;

    @Override
    PojoNode replaceChildrenValues(final List<Object> values) {
        final Object before = this.value();
        Object current = before;

        int i = 0;
        for (PojoProperty property : this.nameToProperties().values()) {
            if (property.isReadOnly()) {
                continue;
            }
            final Object childValue = values.get(i);
            final Object after = property.set(current, childValue);
            if (null != after) {
                current = after;
            }
            i++;
        }

        if (i > values.size()) {
            throw new IndexOutOfBoundsException("Several child values unset=" + values.subList(i, values.size()));
        }

        return this.wrapIfDifferent(before, current);
    }

    @Override
    int childrenCount() {
        return this.properties().size();
    }

    @Override
    final public PojoObjectNode removeChild(final int child) {
        throw new UnsupportedOperationException();
    }

    /**
     * Lazily fetches and caches all the pojo properties for this value.
     */
    final Map<PojoName, PojoProperty> nameToProperties() {
        if (null == this.nameToProperties) {
            final Map<PojoName, PojoProperty> nameToProperties = Maps.ordered();
            for (PojoProperty property : this.properties()) {
                nameToProperties.put(property.name(), property);
            }
            this.nameToProperties = nameToProperties;
        }
        return this.nameToProperties;
    }

    /**
     * Cached copy of the properties for the current value instance.
     */
    private Map<PojoName, PojoProperty> nameToProperties;

    final List<PojoProperty> properties() {
        if (null == this.properties) {
            this.properties = context.properties(this.value.getClass());
        }
        return this.properties;
    }

    private List<PojoProperty> properties;

    @Override
    boolean equals0(final PojoNode other) {
        return this.equals1(other);
    }

    @Override
    public final String toString() {
        return this.toString0();
    }
}
