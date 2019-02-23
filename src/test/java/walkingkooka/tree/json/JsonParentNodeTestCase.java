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

import org.junit.jupiter.api.Test;
import walkingkooka.test.BeanPropertiesTesting;
import walkingkooka.tree.search.SearchNode;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class JsonParentNodeTestCase<N extends JsonParentNode<C>, C extends List<JsonNode>>
        extends JsonNodeTestCase<N> {

    JsonParentNodeTestCase() {
        super();
    }

    @Test
    public final void testCreate() {
        final N parent = this.createJsonNode();
        this.childCountCheck(parent, 0);
    }

    @Test
    public final void testSetNameDifferent() {
        final N node = this.createJsonNode();
        final JsonNodeName originalName = node.name();
        final List<JsonNode> value = node.children();

        final JsonNodeName differentName = JsonNodeName.with("different");
        final N different = node.setName(differentName).cast();
        assertEquals(differentName, different.name(), "name");
        this.checkChildren(different, value);

        assertEquals(originalName, node.name(), "original name");
        this.checkChildren(node, value);
    }

    @Override
    public final void testReplaceChildDifferentParent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void testSetSameAttributes() {
        throw new UnsupportedOperationException();
    }

    public final void testValueFails() {
        assertThrows(UnsupportedOperationException.class, ()-> {
            this.createJsonNode().value();
        });
    }

    @Test
    public final void testPropertiesNeverReturnNull() throws Exception {
        BeanPropertiesTesting.allPropertiesNeverReturnNullCheck(this.createJsonNode(),
                (m) -> m.getName().equals("value"));
    }

    @Test
    public final void testToSearchNodeEmpty() {
        this.toSearchNodeAndCheck(this.createJsonNode(), SearchNode.text("", ""));
    }

    final void checkChildren(final N node, final List<JsonNode> children) {
        assertEquals(children, node.children(), "children");
    }
}
