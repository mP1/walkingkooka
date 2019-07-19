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

import walkingkooka.text.CharSequences;

final class HasJsonNodeCharacterMapper extends HasJsonNodeTypedMapper<Character> {

    static HasJsonNodeCharacterMapper instance() {
        return new HasJsonNodeCharacterMapper();
    }

    private HasJsonNodeCharacterMapper() {
        super();
    }

    @Override
    Class<Character> type() {
        return Character.class;
    }

    @Override
    Character fromJsonNodeNonNull(final JsonNode node) {
        final String string = node.stringValueOrFail();
        final int length = string.length();
        if (1 != length) {
            throw new IllegalArgumentException("Character string must have length of 1 not " + length + "=" + CharSequences.quoteAndEscape(string));
        }
        return string.charAt(0);
    }

    @Override
    Character fromJsonNodeNull() {
        throw new NullPointerException();
    }

    @Override
    JsonStringNode typeName() {
        return TYPE_NAME;
    }

    private final JsonStringNode TYPE_NAME = JsonStringNode.with("character");

    @Override
    JsonNode toJsonNodeNonNull(final Character value) {
        return JsonNode.string(value.toString());
    }
}
