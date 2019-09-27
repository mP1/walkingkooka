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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public final class BasicJsonMarshallerTypedLocalDateTest extends BasicJsonMarshallerTypedTestCase2<BasicJsonMarshallerTypedLocalDate, LocalDate> {

    @Test
    public void testFromInvalidDateFails() {
        this.unmarshallFailed(JsonNode.string("ABC123"), DateTimeParseException.class);
    }

    @Override
    BasicJsonMarshallerTypedLocalDate marshaller() {
        return BasicJsonMarshallerTypedLocalDate.instance();
    }

    @Override
    LocalDate value() {
        return LocalDate.of(2000, 1, 31);
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
    Class<LocalDate> marshallerType() {
        return LocalDate.class;
    }

    @Override
    public Class<BasicJsonMarshallerTypedLocalDate> type() {
        return BasicJsonMarshallerTypedLocalDate.class;
    }
}
