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
 */

package walkingkooka.tree.select;


import org.junit.Assert;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.Node;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

final class TestFakeNode implements Node<TestFakeNode, StringName, StringName, Object> {

    final static Optional<TestFakeNode> NO_PARENT = Optional.empty();

    static TestFakeNode node(final String name, final TestFakeNode... nodes) {
        if(!names.add(name)) {
            Assert.fail("Name " + CharSequences.quote(name)+ " must be unique per test");
        }

        return new TestFakeNode(name).setChildren(Lists.of(nodes));
    }

    static Set<String> names = Sets.sorted();

    TestFakeNode(final String name)
    {
        this(Names.string(name), NO_PARENT, Lists.empty(), Maps.empty());
    }

    private TestFakeNode(final StringName name, final Optional<TestFakeNode> parent, final List<TestFakeNode> children, final Map<StringName, Object> attributes) {
        this.name = name;
        this.parent = parent;
        this.attributes = attributes;
        this.children = children;
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
    public Optional<TestFakeNode> parent() {
        return this.parent;
    }

    private Optional<TestFakeNode> parent;

    @Override
    public List<TestFakeNode> children() {
        return this.children;
    }

    @Override public TestFakeNode setChildren(final List<TestFakeNode> children) {
        Objects.requireNonNull(children, "children");

        return this.children.equals(children) ?
                this :
                new TestFakeNode(this.name, NO_PARENT, children, this.attributes).adopt();
    }

    TestFakeNode child(final int i){
        return this.children().get(i);
    }

    private List<TestFakeNode> children;

    @Override
    public Map<StringName, Object> attributes() {
        return this.attributes;
    }

    @Override public TestFakeNode setAttributes(final Map<StringName, Object> attributes) {
        Objects.requireNonNull(attributes, "attributes");

        final Map<StringName, Object> copy = Maps.ordered();
        copy.putAll(attributes);
        return this.attributes.equals(copy) ?
                this :
                new TestFakeNode(this.name, NO_PARENT, this.children, copy).adopt();
    }

    private final Map<StringName, Object> attributes;

    private TestFakeNode adopt() {
        final Optional<TestFakeNode> parentOfChildren = Optional.of(this);

        this.children = this.children().stream()
                .map(c -> new TestFakeNode(c.name, parentOfChildren, c.children, c.attributes).adopt())
                .collect(Collectors.toList());

        return this;
    }

    public int hashCode() {
        return Objects.hash(this.name(), this.attributes());
    }

    public boolean equals(final Object other){
        return this == other || other instanceof TestFakeNode && equals0((TestFakeNode)other);
    }

    private boolean equals0(final TestFakeNode other) {
        return this.name().equals(other.name()) &&
                this.equalsParent(other.parent()) &&
                this.equalsChildren(other.children()) &&
                this.attributes().equals(other.attributes());
    }

    // check all properties, except for children of parent...
    private boolean equalsParent(final Optional<TestFakeNode> other) {
        boolean equals = false;

        final Optional<TestFakeNode> maybeParent = this.parent();
        if(parent.isPresent()) {
            if(other.isPresent()){
                final TestFakeNode parent = maybeParent.get();
                final TestFakeNode otherParent = other.get();

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
    private boolean equalsChildren(final List<TestFakeNode> otherChildren) {
        boolean equals = false;

        final List<TestFakeNode> children = this.children();
        if(children.size() == otherChildren.size()) {
            final Iterator<TestFakeNode> i = children.iterator();
            final Iterator<TestFakeNode> j = otherChildren.iterator();
            equals = true;

            while(equals && i.hasNext()){
                equals &= i.next().equalsChild(j.next());
            }
        }

        return equals;
    }

    // check all properties of children except for parent.
    private boolean equalsChild(final TestFakeNode other) {
        return this.name().equals(other.name()) &&
                this.equalsChildren(other.children()) &&
                this.attributes().equals(other.attributes());
    }

    @Override
    public String toString() {
        final ToStringBuilder b = ToStringBuilder.create();
        b.value(this.name());
        b.surroundValues("[", "]").value(this.children());
        b.surroundValues("{", "}").value(this.attributes());
        return b.build();
    }
}
