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

package walkingkooka.tree.pointer;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.patch.NodePatchException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class NodePointerTestCase<N extends NodePointer<JsonNode, JsonNodeName>> implements ClassTesting2<N>,
        HashCodeEqualsDefinedTesting<N>,
        ToStringTesting<N>,
        TypeNameTesting<N> {

    NodePointerTestCase() {
        super();
    }

    // add.......................................................................

    @Test
    public final void testAddNullNodeFail() {
        assertThrows(NullPointerException.class, () -> {
            this.createNodePointer().add(null, JsonNode.number(1));
        });
    }

    @Test
    public final void testAddNullAddNodeFail() {
        assertThrows(NullPointerException.class, () -> {
            this.createNodePointer().add(JsonNode.object(), null);
        });
    }

    // remove.......................................................................

    @Test
    public final void testRemoveNullNodeFail() {
        assertThrows(NullPointerException.class, () -> {
            this.createNodePointer().remove(null);
        });
    }

    abstract N createNodePointer();

    @Override
    public final N createObject() {
        return this.createNodePointer();
    }

    final void addAndCheck(final NodePointer<JsonNode, JsonNodeName> pointer,
                           final JsonNode base,
                           final JsonNode add,
                           final JsonNode result) {
        assertEquals(result,
                pointer.add(base, add),
                () -> pointer + " add to " + base + ", " + add);
    }

    final void addAndFail(final NodePointer<JsonNode, JsonNodeName> pointer,
                          final JsonNode base,
                          final JsonNode add) {
        assertThrows(NodePatchException.class, () -> {
            pointer.add(base, add);
        });
    }

    final void removeAndCheck(final NodePointer<JsonNode, JsonNodeName> pointer,
                              final JsonNode node,
                              final JsonNode result) {
        assertEquals(result,
                pointer.remove(node),
                () -> pointer + " remove from " + node);
    }

    final void removeAndFail(final NodePointer<JsonNode, JsonNodeName> pointer,
                             final JsonNode node) {
        assertThrows(NodePatchException.class, () -> {
            pointer.remove(node);
        });
    }

    // equals ....................................................................................

    @Test
    public final void testEqualsDifferentNext() {
        this.checkNotEquals(this.createObject().append(NodePointer.indexed(0, JsonNode.class)),
                this.createObject().append(NodePointer.indexed(99, JsonNode.class)));
    }

    @Test
    public final void testEqualsDifferentNext2() {
        this.checkNotEquals(this.createObject(),
                this.createObject().append(NodePointer.indexed(99, JsonNode.class)));
    }

    @Test
    public final void testEqualsNext2() {
        final NodePointer<JsonNode, JsonNodeName> next = NodePointer.indexed(99, JsonNode.class);

        this.checkEquals(this.createObject().append(next),
                this.createObject().append(next));
    }

    // TypeNameTesting.......................................................................

    @Override
    public final String typeNamePrefix() {
        return "";
    }

    @Override
    public final String typeNameSuffix() {
        return NodePointer.class.getSimpleName();
    }
}
