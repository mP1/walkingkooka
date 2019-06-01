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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.json.JsonNode;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class EmptyTextPropertiesTest extends TextPropertiesTestCase<EmptyTextProperties> {

    @Test
    public void testEmpty() {
        assertSame(EmptyTextProperties.INSTANCE, TextProperties.with(Maps.empty()));
    }

    @Test
    public void testValue() {
        assertSame(EmptyTextProperties.INSTANCE.value(), EmptyTextProperties.INSTANCE.value());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(EmptyTextProperties.INSTANCE, "{}");
    }

    @Test
    public void testFromEmptyJsonObject() {
        assertSame(TextProperties.EMPTY, TextProperties.fromJsonNode(JsonNode.object()));
    }

    @Override
    public EmptyTextProperties createObject() {
        return EmptyTextProperties.INSTANCE;
    }

    @Override
    Class<EmptyTextProperties> textPropertiesType() {
        return EmptyTextProperties.class;
    }
}
