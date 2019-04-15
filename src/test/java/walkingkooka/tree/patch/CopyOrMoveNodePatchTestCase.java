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

package walkingkooka.tree.patch;

import org.junit.jupiter.api.Test;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.pointer.NodePointer;

public abstract class CopyOrMoveNodePatchTestCase<P extends CopyOrMoveNodePatch<JsonNode, JsonNodeName>> extends NonEmptyNodePatchTestCase<P> {

    CopyOrMoveNodePatchTestCase(){
        super();
    }

    @Test
    public final void testFromPathUnknownFails() {
        this.applyFails(this.createPatch(),
                JsonNode.object());
    }

    @Test
    public final void testFromPathUnknownFails2() {
        this.applyFails(this.createPatch(),
                JsonNode.object().set(this.property3(), this.value3()));
    }

    @Test
    public final void testTargetPathUnknownFails() {
        this.applyFails(this.createPatch("/a1", "/b2/c3"),
                JsonNode.object().set(this.property3(), this.value3()));
    }


    @Test
    public final void testEqualsDifferentFrom() {
        this.checkNotEquals(this.createPatch(this.path3(), this.path1()));
    }

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.createPatch(), this.operation() + " from=\"/b2\" path=\"/a1\"");
    }

    abstract String operation();

    @Override
    final P createPatch(final NodePointer<JsonNode, JsonNodeName> path) {
        return this.createPatch(this.path2(), path);
    }

    final P createPatch(final String from,
                        final String path) {
        return this.createPatch(this.pointer(from), this.pointer(path));
    }

    abstract P createPatch(final NodePointer<JsonNode, JsonNodeName> from,
                           final NodePointer<JsonNode, JsonNodeName> path);
}
