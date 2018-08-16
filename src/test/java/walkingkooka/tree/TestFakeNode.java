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

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Name;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

final class TestFakeNode extends FakeNode<TestFakeNode, StringName, Name, Object>{

    TestFakeNode(final String name, final TestFakeNode...children) {
        this.name = Names.string(name);
        this.children = Lists.of(children);

        int i = 0;
        for(TestFakeNode child : children) {
            child.parent = Optional.of(this);
            child.index = i;
            i++;
        }

        this.children.stream().forEach( n -> n.parent = Optional.of(this));
    }

    @Override
    public StringName name() {
        return this.name;
    }

    private final StringName name;

    @Override
    public Optional<TestFakeNode> parent() {
        return this.parent;
    }

    private Optional<TestFakeNode> parent = Optional.empty();

    @Override
    public int index() {
        return this.index;
    }

    private int index = -1;

    @Override
    public List<TestFakeNode> children() {
        return this.children;
    }

    private List<TestFakeNode> children;

    @Override
    public int hashCode() {
        return Objects.hash(this.name(), this.children());
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof TestFakeNode && this.equals0(Cast.to(other));
    }

    private boolean equals0(final TestFakeNode other) {
        return this.name.equals(other.name) && this.children.equals(other.children);
    }

    @Override
    public String toString() {
        return this.name.toString();
    }
}
