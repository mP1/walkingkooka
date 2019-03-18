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

package walkingkooka.text.cursor;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Name;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.text.HasText;
import walkingkooka.tree.FakeNode;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

final class NodeTextCursorTestNode extends FakeNode<NodeTextCursorTestNode, StringName, Name, Object> implements HasText {

    NodeTextCursorTestNode(final String name, final String text, final NodeTextCursorTestNode...children) {
        this.name = Names.string(name);
        this.children = Lists.of(children);

        int i = 0;
        for(NodeTextCursorTestNode child : children) {
            child.parent = Optional.of(this);
            child.index = i;
            i++;
        }

        this.children.forEach( n -> n.parent = Optional.of(this));

        this.text = text;
    }

    @Override
    public StringName name() {
        return this.name;
    }

    private final StringName name;

    @Override
    public Optional<NodeTextCursorTestNode> parent() {
        return this.parent;
    }

    private Optional<NodeTextCursorTestNode> parent = Optional.empty();

    @Override
    public int index() {
        return this.index;
    }

    private int index = -1;

    @Override
    public List<NodeTextCursorTestNode> children() {
        return this.children;
    }

    private final List<NodeTextCursorTestNode> children;

    @Override
    public String text() {
        return this.text;
    }

    private final String text;

    @Override
    public int hashCode() {
        return Objects.hash(this.name(), this.children());
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof NodeTextCursorTestNode && this.equals0(Cast.to(other));
    }

    private boolean equals0(final NodeTextCursorTestNode other) {
        return this.name.equals(other.name) && this.children.equals(other.children);
    }

    @Override
    public String toString() {
        return this.text;
    }
}
