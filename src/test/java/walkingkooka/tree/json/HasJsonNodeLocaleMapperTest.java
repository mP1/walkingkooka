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

import java.util.Locale;

public final class HasJsonNodeLocaleMapperTest extends HasJsonNodeTypedMapperTestCase<HasJsonNodeLocaleMapper, Locale> {

    @Test
    public final void testFromBooleanFails() {
        this.fromJsonNodeFailed(JsonNode.booleanNode(true), JsonNodeException.class);
    }

    @Test
    public final void testFromNumberFails() {
        this.fromJsonNodeFailed(JsonNode.number(1.5), JsonNodeException.class);
    }

    @Test
    public final void testFromObjectFails() {
        this.fromJsonNodeFailed(JsonNode.object(), JsonNodeException.class);
    }

    @Test
    public final void testFromArrayFails() {
        this.fromJsonNodeFailed(JsonNode.array(), JsonNodeException.class);
    }

    @Override
    HasJsonNodeLocaleMapper mapper() {
        return HasJsonNodeLocaleMapper.instance();
    }

    @Override
    Locale value() {
        return Locale.ENGLISH;
    }

    @Override
    JsonNode node() {
        return JsonNode.string(this.value().toLanguageTag());
    }

    @Override
    Locale jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "locale";
    }

    @Override
    Class<Locale> mapperType() {
        return Locale.class;
    }

    @Override
    public Class<HasJsonNodeLocaleMapper> type() {
        return HasJsonNodeLocaleMapper.class;
    }
}
