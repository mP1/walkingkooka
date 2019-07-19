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

public final class HasJsonNodeCharacterMapperTest extends HasJsonNodeTypedMapperTestCase2<HasJsonNodeCharacterMapper, Character> {

    @Test
    @Override
    public void testFromJsonNodeJsonNullNode() {
        this.fromJsonNodeFailed(JsonNode.nullNode(), NullPointerException.class);
    }

    @Test
    public void testFromMoreThanOneLengthStringFails() {
        this.fromJsonNodeFailed(JsonNode.string("abc"), IllegalArgumentException.class);
    }

    @Test
    public void testFrom() {
        this.fromJsonNodeAndCheck(JsonNode.string("A"), 'A');
    }

    @Test
    public void testFrom2() {
        this.fromJsonNodeAndCheck(JsonNode.string("\t"), '\t');
    }

    @Test
    public void testTo() {
        this.toJsonNodeAndCheck('Z', JsonNode.string("Z"));
    }

    @Override
    HasJsonNodeCharacterMapper mapper() {
        return HasJsonNodeCharacterMapper.instance();
    }

    @Override
    Character value() {
        return 'Q';
    }

    @Override
    JsonNode node() {
        return JsonNode.string(this.value().toString());
    }

    @Override
    Character jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "character";
    }

    @Override
    Class<Character> mapperType() {
        return Character.class;
    }

    @Override
    Class<? extends RuntimeException> fromFailsCauseType() {
        return IllegalArgumentException.class;
    }

    @Override
    public Class<HasJsonNodeCharacterMapper> type() {
        return HasJsonNodeCharacterMapper.class;
    }
}
