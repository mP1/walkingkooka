/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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
 *
 */

package walkingkooka.tree;


import org.junit.jupiter.api.Assertions;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.naming.Names;
import walkingkooka.naming.PathSeparator;
import walkingkooka.naming.StringName;
import walkingkooka.text.CharSequences;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A basic {@link Node} with some simplifications. Note that all node names must be unique and that children have their
 * parent and index properties modified when they get adopted. These considerations should be acocunted for in tests.
 */
public class TestNode implements Node<TestNode, StringName, StringName, Object> {

    /**
     * Should be called before each and every test.
     */
    public static void clear() {
        names.clear();
    }

    /**
     * The {@link PathSeparator} for node selector paths.
     */
    public static final PathSeparator PATH_SEPARATOR = PathSeparator.requiredAtStart('/');

    public final static Optional<TestNode> NO_PARENT = Optional.empty();

    public static TestNode with(final String name, final TestNode... children) {
        if(!names.add(name)) {
            Assertions.fail("Name " + CharSequences.quote(name)+ " must be unique per test");
        }

        return new TestNode(Names.string(name),
                NO_PARENT,
                Lists.of(children),
                Maps.empty());
    }

    /**
     * Used to keep track of all node names, preventing duplicates.
     */
    private static Set<String> names = Sets.sorted();

    private TestNode(final StringName name,
                     final Optional<TestNode> parent,
                     final List<TestNode> children,
                     final Map<StringName, Object> attributes) {
        super();
        this.name = name;
        this.parent = parent;
        this.attributes = attributes;
        this.children = children;

        int i = 0;
        for(TestNode child : children) {
            child.parent = Optional.of(this);
            child.index = i;
            i++;
        }
    }

    public StringName name() {
        return this.name;
    }

    private final StringName name;

    @Override
    public final boolean hasUniqueNameAmongstSiblings() {
        return true;
    }

    @Override
    public Optional<TestNode> parent() {
        return this.parent;
    }

    private Optional<TestNode> parent;

    @Override
    public int index() {
        return this.index;
    }

    private int index = -1;

    @Override
    public List<TestNode> children() {
        return this.children;
    }

    @Override
    public TestNode setChildren(final List<TestNode> children) {
        Objects.requireNonNull(children, "children");

        return this.children.equals(children) ?
                this :
                new TestNode(this.name, NO_PARENT, children, this.attributes);
    }

    public TestNode child(final int i){
        return this.children().get(i);
    }

    private List<TestNode> children;

    @Override
    public Map<StringName, Object> attributes() {
        return this.attributes;
    }

    @Override
    public TestNode setAttributes(final Map<StringName, Object> attributes) {
        Objects.requireNonNull(attributes, "attributes");

        final Map<StringName, Object> copy = Maps.ordered();
        copy.putAll(attributes);
        return this.attributes.equals(copy) ?
                this :
                new TestNode(this.name, NO_PARENT, this.children, copy);
    }

    private final Map<StringName, Object> attributes;

    public int hashCode() {
        return Objects.hash(this.name(), this.attributes());
    }

    public boolean equals(final Object other){
        return this == other || other instanceof TestNode && equals0((TestNode)other);
    }

    private boolean equals0(final TestNode other) {
        return this.name().equals(other.name()) &&
                this.equalsParent(other.parent()) &&
                this.equalsChildren(other.children()) &&
                this.attributes().equals(other.attributes());
    }

    // check all properties, except for children of parent...
    private boolean equalsParent(final Optional<TestNode> other) {
        boolean equals = false;

        final Optional<TestNode> maybeParent = this.parent();
        if(parent.isPresent()) {
            if(other.isPresent()){
                final TestNode parent = maybeParent.get();
                final TestNode otherParent = other.get();

                // skip checking children...
                equals = parent.name().equals(otherParent.name()) &&
                        parent.equalsParent(otherParent.parent()) &&
                        parent.attributes().equals(otherParent.attributes());
            }
        } else {
            // both should have no parent.
            equals = !other.isPresent();
        }

        return equals;
    }

    // check all properties of children except for parent.
    private boolean equalsChildren(final List<TestNode> otherChildren) {
        boolean equals = false;

        final List<TestNode> children = this.children();
        if(children.size() == otherChildren.size()) {
            final Iterator<TestNode> i = children.iterator();
            final Iterator<TestNode> j = otherChildren.iterator();
            equals = true;

            while(equals && i.hasNext()){
                equals &= i.next().equalsChild(j.next());
            }
        }

        return equals;
    }

    // check all properties of children except for parent.
    private boolean equalsChild(final TestNode other) {
        return this.name().equals(other.name()) &&
                this.equalsChildren(other.children()) &&
                this.attributes().equals(other.attributes());
    }

    @Override
    public String toString() {
        final ToStringBuilder b = ToStringBuilder.empty();
        b.value(this.name());
        b.surroundValues("[", "]").value(this.children());
        b.surroundValues("{", "}").value(this.attributes());
        return b.build();
    }
}
