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

public abstract class HasJsonNodeTypedMapperTestCase2<M extends HasJsonNodeTypedMapper<T>, T> extends HasJsonNodeTypedMapperTestCase<M, T> {

    HasJsonNodeTypedMapperTestCase2() {
        super();
    }

    @Test
    public final void testFromArrayFails() {
        this.fromJsonNodeFailed(JsonNode.array(), JsonNodeException.class);
    }

    @Test
    public final void testFromNumberFails() {
        this.fromJsonNodeFailed(JsonNode.number(123), JsonNodeException.class);
    }

    @Test
    public final void testFromEmptyStringFails() {
        this.fromJsonNodeFailed(JsonNode.string(""), JsonNodeException.class);
    }

    @Test
    public final void testFromStringFails() {
        this.fromJsonNodeFailed(JsonNode.string("1A"), JsonNodeException.class);
    }
}
