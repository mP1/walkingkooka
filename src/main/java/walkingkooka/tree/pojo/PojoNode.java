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
import walkingkooka.Value;
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.HasChildrenValues;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.select.parser.NodeSelectorExpressionParserToken;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A {@link Node} wrapper around regular java POJOs, supporting read-only and write operations including
 * POJO's that support would be setters, that is setters that return a new often immutable instance if the new value
 * is different from the old. In addition to {@link Node} methods additional methods are available to get and set
 * unwrapped child values and to get and set the actual wrapped java POJO. To avoid weird things happening it is
 * best not to update the POJOs when using them via their {@link Node} wrappers.
 * <br>
 * Note that some types such as primitive and their wrappers, String and Class are considered basic types without any children.
 */
public abstract class PojoNode implements Node<PojoNode, PojoName, PojoNodeAttributeName, Object>,
        HasChildrenValues<Object, PojoNode>,
        Value<Object>,
        Comparable<PojoNode> {

    private final static Optional<PojoNode> NO_PARENT = Optional.empty();

    /**
     * Factory that creates a pojo that will act as the root.
     */
    public static PojoNode wrap(final PojoName name,
                                final Object value,
                                final PojoNodeContext context) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(context, "context");

        return wrap0(name, value, NO_INDEX, context);
    }

    /**
     * Creates a wrapper for a node that isnt the root within a graph.
     */
    static PojoNode wrap0(final PojoName name,
                          final Object value,
                          final int index,
                          final PojoNodeContext context) {
        PojoNode wrapped;
        for (; ; ) {
            if (PojoBasicNode.isBasic(value)) {
                wrapped = new PojoBasicNode(name, value, index, context);
                break;
            }
            if (value instanceof boolean[]) {
                wrapped = new PojoBooleanArrayNode(name, Cast.to(value), index, context);
                break;
            }
            if (value instanceof byte[]) {
                wrapped = new PojoByteArrayNode(name, Cast.to(value), index, context);
                break;
            }
            if (value instanceof char[]) {
                wrapped = new PojoCharArrayNode(name, Cast.to(value), index, context);
                break;
            }
            if (value instanceof double[]) {
                wrapped = new PojoDoubleArrayNode(name, Cast.to(value), index, context);
                break;
            }
            if (value instanceof float[]) {
                wrapped = new PojoFloatArrayNode(name, Cast.to(value), index, context);
                break;
            }
            if (value instanceof int[]) {
                wrapped = new PojoIntArrayNode(name, Cast.to(value), index, context);
                break;
            }
            if (value instanceof List) {
                wrapped = new PojoListNode(name, Cast.to(value), index, context);
                break;
            }
            if (value instanceof long[]) {
                wrapped = new PojoLongArrayNode(name, Cast.to(value), index, context);
                break;
            }
            if (value instanceof Map) {
                wrapped = new PojoMapNode(name, Cast.to(value), index, context);
                break;
            }
            if (value instanceof Object[]) {
                wrapped = new PojoObjectArrayNode(name, Cast.to(value), index, context);
                break;
            }
            if (value instanceof Set) {
                wrapped = new PojoSetNode(name, Cast.to(value), index, context);
                break;
            }
            if (value instanceof short[]) {
                wrapped = new PojoShortArrayNode(name, Cast.to(value), index, context);
                break;
            }
            wrapped = new PojoObjectNode(name, value, index, context);
            break;
        }

        return wrapped;
    }

    /**
     * Package private ctor to limit subclassing.
     */
    PojoNode(final PojoName name,
             final Object value,
             final int index,
             final PojoNodeContext context) {
        this.name = name;
        this.value = value;
        this.parent = NO_PARENT;
        this.index = index;
        this.context = context;
    }

    /**
     * Creates a new node with the identified name/index.
     */
    public PojoNode createNode(final PojoName name, final Object value) {
        Objects.requireNonNull(name, "name");

        return wrap(name, value, this.context);
    }

    // Name ..............................................................................................

    @Override
    public final PojoName name() {
        return this.name;
    }

    private final PojoName name;

    @Override
    public final boolean hasUniqueNameAmongstSiblings() {
        return true;
    }

    // parent ..................................................................................................

    @Override
    public final Optional<PojoNode> parent() {
        return this.parent;
    }

    @Override
    public PojoNode removeParent() {
        throw new UnsupportedOperationException();
    }

    /**
     * This setter is used to recreate the entire graph including parents of parents receiving new children.
     */
    final PojoNode setParent(final Optional<PojoNode> parent) {
        this.parent = parent;
        return this;
    }

    private Optional<PojoNode> parent;

    // children ..........................................................................................................

    /**
     * Finds the first child with the given name.
     */
    final Optional<PojoNode> child(final PojoName name) {
        return this.children().stream()
                .filter(p -> p.name().equals(name))
                .findFirst();
    }

    /**
     * Sub classes must create a new copy of the parent and replace the identified child using its index or similar,
     * and also sets its parent after creation, returning the equivalent child at the same index.
     */
    abstract PojoNode replaceChild(final PojoNode newChild);

    /**
     * Only ever called after during the completion of a setChildren, basically used to recreate the parent graph
     * containing this child.
     */
    final PojoNode replaceChild(final Optional<PojoNode> previousParent) {
        return previousParent.isPresent() ?
                previousParent.get()
                        .replaceChild(this)
                        .children()
                        .get(this.index()) :
                this;
    }

    /**
     * Getter that returns the child values without the wrappers.
     */
    abstract public List<Object> childrenValues();

    /**
     * Setter that accepts raw values without the wrappers.
     */
    abstract public PojoNode setChildrenValues(final List<Object> values);

    /**
     * Returns the children or property count.
     */
    abstract int childrenCount();

    /**
     * Because {@link #setChildren(List)} and {@link #setChildrenValues(List)} may mutate an instance,
     * it is necessary to clear the accompanying child list, which may/will be stale.
     */
    abstract PojoNode clearChildrenListCache();

    // index ..................................................................................................

    @Override
    public final int index() {
        return this.index;
    }

    private final int index;

    // attributes ...............................................................................................

    @Override
    public final Map<PojoNodeAttributeName, Object> attributes() {
        if (null == this.attributes) {
            final Object value = this.value();
            this.attributes = null != value ?
                    Maps.of(PojoNodeAttributeName.CLASS, value.getClass().getName()) :
                    Maps.empty();
        }
        return this.attributes;
    }

    private Map<PojoNodeAttributeName, Object> attributes;

    @Override
    public final PojoNode setAttributes(final Map<PojoNodeAttributeName, Object> attributes) {
        throw new UnsupportedOperationException();
    }

    final PojoNodeContext context;

    // Value .........................................................................................................

    @Override
    public final Object value() {
        return this.value;
    }

    final Object value;

    /**
     * Would setter that returns a {@link PojoNode} with the new value creating a new node if necessary.
     */
    public final PojoNode setValue(final Object value) {
        return Objects.deepEquals(this.value(), value) ?
                this :
                PojoNode.wrap0(this.name(),
                        value,
                        index(),
                        this.context)
                        .replaceChild(this.parent());
    }

    // Comparable ......................................................................................................

    /**
     * Should only ever be used by {@link Set} and {@link Map} that require {@link Comparable}s
     */
    @Override
    public int compareTo(final PojoNode other) {
        return this.comparableValue().compareTo(other.comparableValue());
    }

    private Comparable<Object> comparableValue() {
        return Cast.to(this.value());
    }

    // Object .........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof PojoNode && equals0(Cast.to(other));
    }

    abstract boolean equals0(final PojoNode other);

    /**
     * The default equals for all sub classes except for wrappers around arrays, sub classes of {@link PojoArrayNode}
     */
    final boolean equals1(final PojoNode other) {
        return Objects.equals(this.value, other.value);
    }

    @Override
    abstract public String toString();

    /**
     * Default toString for the value, note does not handle arrays.
     */
    final String toString0() {
        return String.valueOf(this.value);
    }

    // NodeSelector .......................................................................................................

    /**
     * {@see NodeSelector#absolute}
     */
    public static NodeSelector<PojoNode, PojoName, PojoNodeAttributeName, Object> absoluteNodeSelector() {
        return NodeSelector.absolute();
    }

    /**
     * {@see NodeSelector#relative}
     */
    public static NodeSelector<PojoNode, PojoName, PojoNodeAttributeName, Object> relativeNodeSelector() {
        return NodeSelector.relative();
    }

    /**
     * Creates a {@link NodeSelector} for {@link PojoNode} from a {@link NodeSelectorExpressionParserToken}.
     */
    public static NodeSelector<PojoNode, PojoName, PojoNodeAttributeName, Object> nodeSelectorExpressionParserToken(final NodeSelectorExpressionParserToken token,
                                                                                                                    final Predicate<ExpressionNodeName> functions) {
        return NodeSelector.parserToken(token,
                n -> PojoName.property(n.value()),
                functions,
                PojoNode.class);
    }
}
