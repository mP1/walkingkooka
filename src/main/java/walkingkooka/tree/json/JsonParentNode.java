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

package walkingkooka.tree.json;

import walkingkooka.collect.list.Lists;
import walkingkooka.tree.search.SearchNode;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Base type for all the parent json nodes that hold other nodes, such as array and object
 */
abstract class JsonParentNode extends JsonNode {

    JsonParentNode(final JsonNodeName name, final int index, final List<JsonNode> children){
        super(name, index);

        final Optional<JsonNode> p = Optional.of(this);

        final List<JsonNode> copy = Lists.array();
        int i = 0;
        for(JsonNode child : children) {
            copy.add(child.setParent(p, i));
            i++;
        }

        this.children = copy;
    }

    @Override
    public final List<JsonNode> children() {
        return this.children;
    }

    final List<JsonNode> copyChildren() {
        final List<JsonNode> copy = Lists.array();
        copy.addAll(this.children);
        return copy;
    }

    final List<JsonNode> children;

    final JsonNode setChildren1(final List<JsonNode> children) {
        return Lists.equals(this.children, children, (first, other) -> first.equalsIgnoringParentAndChildren0(other) && first.equalsDescendants(other)) ?
                this :
                this.replaceChildren(children);
    }

    @Override
    final JsonNode setChild(final JsonNode newChild) {
        final int index = newChild.index();
        final JsonNode previous = this.children.get(index);
        return previous.equalsIgnoringParentAndChildren0(newChild) && previous.equalsDescendants(newChild) ?
              this :
               this.replaceChild0(newChild, index);
    }

    private JsonNode replaceChild0(final JsonNode newChild, final int index) {
        final List<JsonNode> newChildren = Lists.array();
        newChildren.addAll(this.children);
        newChildren.set(index, newChild);

        return this.replaceChildren(newChildren);
    }

    final JsonParentNode replaceChildren(final List<JsonNode> children) {
        return this.wrap0(this.name, this.index, children)
                .replaceChild(this.parent())
                .cast();
    }

    @Override
    final JsonNode wrap(final JsonNodeName name, final int index) {
        return this.wrap0(name, index, this.children)
                .replaceChild(this.parent());
    }

    abstract JsonParentNode wrap0(final JsonNodeName name, final int index, final List<JsonNode> children);

    // HasSearchNode...............................................................................................

    @Override
    public final SearchNode toSearchNode() {
        return this.children.isEmpty() ?
               SearchNode.text("", "") :
               this.toSearchNode0();
    }

    abstract SearchNode toSearchNode0();

    // isXXX...............................................................................................

    @Override
    public final boolean isBoolean() {
        return false;
    }

    @Override
    public final boolean isNull() {
        return false;
    }

    @Override
    public final boolean isNumber() {
        return false;
    }

    @Override
    public final boolean isString() {
        return false;
    }

    final void acceptValues(final JsonNodeVisitor visitor){
        for(JsonNode node: this.children()){
            visitor.accept(node);
        }
    }

    // HasText......................................................................................................

    /**
     * Combine the text of all children(descendants). Note property names and indices will not be included.
     */
    @Override
    public String text() {
        return this.children().stream()
                .map(c -> c.text())
                .collect(Collectors.joining());
    }

    // Object.....................................................................................................

    @Override
    public final int hashCode() {
        return this.children.hashCode();
    }

    final boolean equalsDescendants0(final JsonNode other) {
        return this.equalsDescendants1(other.children());
    }

    /**
     * Only returns true if the descendants of this node and the given children are equal ignoring the parents.
     */
    final boolean equalsDescendants1(final List<JsonNode> otherChildren) {
        final List<JsonNode> children = this.children();
        final int count = children.size();
        boolean equals = count == otherChildren.size();

        if (equals) {
            for (int i = 0; equals && i < count; i++) {
                equals = children.get(i).equalsDescendants(otherChildren.get(i));
            }
        }

        return equals;
    }

    @Override
    final boolean equalsIgnoringParentAndChildren0(final JsonNode other) {
        return true; // no other properties name already tested.
    }
}
