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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.test.PackagePrivateClassTestCase;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;

import static org.junit.Assert.assertEquals;

public final class NoneNodePointerTest extends PackagePrivateClassTestCase<NoneNodePointer<?, ?, ?, ?>> {

    @Test
    public void testElementAppendToString() {
        final NodePointer<?, ?, ?, ?> element = NodePointer.named(JsonNodeName.with("abc"), JsonNode.class);
        final NodePointer<?, ?, ?, ?> none = element.none();
        assertEquals("/abc/-", none.toString());
    }

    @Test
    public void testArrayAppendToString() {
        final NodePointer<?, ?, ?, ?> array = NodePointer.index(123, JsonNode.class);
        final NodePointer<?, ?, ?, ?> none = array.none();
        assertEquals("/123/-", none.toString());
    }

    @Test
    public void testToString() {
        assertEquals("-", NoneNodePointer.with("-").toString());
    }

    @Override
    protected Class<NoneNodePointer<?, ?, ?, ?>> type() {
        return Cast.to(NoneNodePointer.class);
    }
}
