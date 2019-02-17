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

import walkingkooka.Cast;
import walkingkooka.test.SkipPropertyNeverReturnsNullCheck;
import walkingkooka.tree.search.SearchNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Base type for all the parent json nodes that hold other nodes, such as array and object
 */
abstract class JsonParentNode<C extends List<JsonNode>> extends JsonNode {

    JsonParentNode(final JsonNodeName name, final int index, final C children){
        super(name, index);

        this.children = this.adoptChildren(children);
    }

    /**
     * Called during construction to adopt children.
     */
    abstract C adoptChildren(final C children);

    @Override
    public final List<JsonNode> children() {
        return this.children;
    }

    /**
     * A list holding the children.
     */
    final C children;

    /**
     * If the children are different replaces the children otherwise returns this.
     */
    final JsonNode setChildren0(final C children) {
        return this.childrenEquals(children) ?
                this :
                this.replaceChildren(children);
    }

    /**
     * Allows sub classes to have different strategies to compare children for equality.
     */
    abstract boolean childrenEquals(final List<JsonNode> children);

    /**
     * Returns a new {@Link JsonParentNode} with the given children and also updates the parent/ancestors.
     */
    final JsonParentNode<C> replaceChildren(final C children) {
        return this.create(this.name, this.index, children)
                .replaceChild(this.parent(), this.index)
                .cast();
    }

    @Override
    final JsonNode create(final JsonNodeName name, final int index) {
        return this.create(name, index, this.children);
    }

    /**
     * Factory that creates a {@link JsonParentNode} of the same type as this with the given new properties.
     */
    abstract JsonParentNode<C> create(final JsonNodeName name, final int index, final C children);

    // Value....................................................................................................

    @Override
    @SkipPropertyNeverReturnsNullCheck({JsonArrayNode.class, JsonObjectNode.class})
    public final Object value() {
        throw new UnsupportedOperationException();
    }

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

    /**
     * Only returns true if the descendants of this node and the given children are equal ignoring the parents.
     * A compatibility test between both objects is also done as this is called directly when parents compare their children.
     */
    @Override
    final boolean equalsDescendants(final JsonNode other) {
        boolean equals = this.canBeEqual(other);

        if (equals) {
            final C children = this.children;
            final int count = children.size();

            final C otherChildren = Cast.to(other.children());
            equals = count == other.children().size();

            if (equals) {
                for (int i = 0; equals && i < count; i++) {
                    equals = this.equalsDescendants0(children.get(i), otherChildren, i);
                }
            }
        }

        return equals;
    }

    /**
     * Tests a child for equality. It should ignore the parent.
     */
    abstract boolean equalsDescendants0(final JsonNode child, final C otherChildren, final int i);

    /**
     * Tests if the immediate value belonging to this node for equality.
     */
    @Override
    final boolean equalsValue(final JsonNode other) {
        return true; // no other properties name already tested.
    }
}
