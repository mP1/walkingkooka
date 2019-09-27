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

package walkingkooka.tree.json.marshall;

import org.junit.jupiter.api.Test;
import walkingkooka.tree.json.JsonNode;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public final class BasicJsonMarshallerTypedLocalDateTimeTest extends BasicJsonMarshallerTypedTestCase2<BasicJsonMarshallerTypedLocalDateTime, LocalDateTime> {

    @Test
    public void testFromInvalidDateFails() {
        this.unmarshallFailed(JsonNode.string("ABC123"), DateTimeParseException.class);
    }

    @Override
    BasicJsonMarshallerTypedLocalDateTime marshaller() {
        return BasicJsonMarshallerTypedLocalDateTime.instance();
    }

    @Override
    LocalDateTime value() {
        return LocalDateTime.of(2000, 1, 31, 12, 58, 59);
    }

    @Override
    JsonNode node() {
        return JsonNode.string("2000-01-31T12:58:59");
    }

    @Override
    LocalDateTime jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "local-datetime";
    }

    @Override
    Class<LocalDateTime> marshallerType() {
        return LocalDateTime.class;
    }

    @Override
    public Class<BasicJsonMarshallerTypedLocalDateTime> type() {
        return BasicJsonMarshallerTypedLocalDateTime.class;
    }
}
