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

import java.util.Locale;

/**
 * A mapper that handles {@link Locale}.
 */
final class HasJsonNodeLocaleMapper extends HasJsonNodeTypedMapper<Locale> {

    static HasJsonNodeLocaleMapper instance() {
        return new HasJsonNodeLocaleMapper();
    }

    private HasJsonNodeLocaleMapper() {
        super();
    }

    @Override
    Class<Locale> type() {
        return Locale.class;
    }

    @Override
    Locale fromJsonNodeNull() {
        return null;
    }

    @Override
    Locale fromJsonNodeNonNull(final JsonNode node) {
        return Locale.forLanguageTag(node.stringValueOrFail());
    }

    @Override
    JsonStringNode typeName() {
        return TYPE_NAME;
    }

    private final JsonStringNode TYPE_NAME = JsonStringNode.with("locale");

    @Override
    JsonNode toJsonNodeNonNull(final Locale value) {
        return JsonNode.string(value.toLanguageTag());
    }
}
