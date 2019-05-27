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

import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;

/**
 * A {@link TextPropertyValueConverter} for non empty {@link String} parameter values.
 */
final class StringTextPropertyValueConverter extends TextPropertyValueConverter<String> {

    /**
     * Singleton
     */
    final static StringTextPropertyValueConverter INSTANCE = new StringTextPropertyValueConverter();

    /**
     * Private ctor
     */
    private StringTextPropertyValueConverter() {
        super();
    }

    @Override
    void check0(final Object value, final TextPropertyName<?> name) {
        final String string = this.checkType(value, String.class, name);
        if (string.isEmpty()) {
            throw new TextPropertyValueException("Property " + name.inQuotes() + " contains an empty/whitespace value " + CharSequences.quoteAndEscape(string));
        }
    }

    // fromJsonNode ....................................................................................................

    @Override
    String fromJsonNode(final JsonNode node) {
        return node.stringValueOrFail();
    }

    @Override
    JsonNode toJsonNode(final String value) {
        return JsonNode.string(value);
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return String.class.getSimpleName();
    }
}
