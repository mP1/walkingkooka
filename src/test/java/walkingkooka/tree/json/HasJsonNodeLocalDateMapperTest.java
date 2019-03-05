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

import java.time.LocalDate;

public final class HasJsonNodeLocalDateMapperTest extends HasJsonNodeMapperTestCase2<HasJsonNodeLocalDateMapper, LocalDate> {

    @Test
    public void testFromEmptyStringFails() {
        this.fromJsonNodeFailed(JsonNode.string(""), JsonNodeException.class);
    }

    @Test
    public void testFromInvalidDateFails() {
        this.fromJsonNodeFailed(JsonNode.string("ABC123"), JsonNodeException.class);
    }

    @Override
    HasJsonNodeLocalDateMapper mapper() {
        return HasJsonNodeLocalDateMapper.instance();
    }

    @Override
    LocalDate value() {
        return LocalDate.of(2000, 1, 31);
    }

    @Override
    boolean requiresTypeName() {
        return true;
    }

    @Override
    JsonNode node() {
        return JsonNode.string("2000-01-31");
    }

    @Override
    LocalDate jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "local-date";
    }

    @Override
    public Class<HasJsonNodeLocalDateMapper> type() {
        return HasJsonNodeLocalDateMapper.class;
    }
}
