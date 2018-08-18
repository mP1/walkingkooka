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

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class JsonParentNodeTestCase<N extends JsonParentNode> extends JsonNodeTestCase<N> {

    @Test
    public final void testCreate() {
        final N parent = this.createJsonNode();
        this.checkChildCount(parent, 0);
    }

    @Test
    public final void testSetNameDifferent() {
        final N node = this.createJsonNode();
        final JsonNodeName originalName = node.name();
        final List<JsonNode> value = node.children();

        final JsonNodeName differentName = JsonNodeName.with("different");
        final N different = node.setName(differentName).cast();
        assertEquals("name", differentName, different.name());
        this.checkChildren(different, value);

        assertEquals("original name", originalName, node.name());
        this.checkChildren(node, value);
    }

    @Test
    @Ignore
    public final void testReplaceChildDifferentParent() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Ignore
    public void testSetSameAttributes() {

    }

    final void checkChildren(final N node, final List<JsonNode> children) {
        assertEquals("children", children, node.children());
    }
}
