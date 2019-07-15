/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.tree.json;

import org.junit.jupiter.api.Test;

public abstract class JsonLeafNonNullNodeTestCase<N extends JsonLeafNonNullNode<V>, V> extends JsonLeafNodeTestCase<N, V> {

    JsonLeafNonNullNodeTestCase() {
        super();
    }

    @Test
    public final void testFromJsonNodeListFails() {
        this.fromJsonNodeListAndFail(Void.class, JsonNodeException.class);
    }

    @Test
    public final void testFromJsonNodeSetFails() {
        this.fromJsonNodeSetAndFail(Void.class, JsonNodeException.class);
    }

    @Test
    public final void testFromJsonNodeMapFails() {
        this.fromJsonNodeMapAndFail(Void.class, Void.class, JsonNodeException.class);
    }

    @Test
    public final void testFromJsonNodeWithTypeListFails() {
        this.fromJsonNodeWithTypeListAndFail(JsonNodeException.class);
    }

    @Test
    public final void testFromJsonNodeWithTypeSetFails() {
        this.fromJsonNodeWithTypeSetAndFail(JsonNodeException.class);
    }

    @Test
    public final void testFromJsonNodeWithTypeMapFails() {
        this.fromJsonNodeWithTypeMapAndFail(JsonNodeException.class);
    }
}
