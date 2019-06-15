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

import java.time.LocalDate;

final class HasJsonNodeLocalDateMapper extends HasJsonNodeMapper2<LocalDate> {

    static HasJsonNodeLocalDateMapper instance() {
        return new HasJsonNodeLocalDateMapper();
    }

    private HasJsonNodeLocalDateMapper() {
        super();
    }

    @Override
    Class<LocalDate> type() {
        return LocalDate.class;
    }

    @Override
    LocalDate fromJsonNodeNull() {
        return null;
    }

    @Override
    LocalDate fromJsonNode0(final JsonNode node) {
        return LocalDate.parse(node.stringValueOrFail());
    }

    @Override
    JsonStringNode typeName() {
        return TYPE_NAME;
    }

    private final JsonStringNode TYPE_NAME = JsonStringNode.with("local-date");

    @Override
    JsonNode toJsonNode0(final LocalDate value) {
        return JsonNode.string(value.toString());
    }
}
