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
import walkingkooka.Cast;
import walkingkooka.test.ClassTestCase;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class NodeChildNamedNodePointerTest extends ClassTestCase<NodeChildNamedNodePointer<?, ?, ?, ?>> {

    @Test
    public void testWithSlash() {
        this.checkToString(NodeChildNamedNodePointer.with(JsonNodeName.with("slash/")), "/slash~1");
    }

    @Test
    public void testWithTilde() {
        this.checkToString(NodeChildNamedNodePointer.with(JsonNodeName.with("tilde~")), "/tilde~0");
    }

    private void checkToString(final NodePointer<?, ?, ?, ?> pointer, final String toString) {
        assertEquals(toString, pointer.toString());
    }

    @Override
    protected Class<NodeChildNamedNodePointer<?, ?, ?, ?>> type() {
        return Cast.to(NodeChildNamedNodePointer.class);
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
