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

import walkingkooka.text.CharSequences;

final class HasJsonNodeLongMapper extends HasJsonNodeMapper2<Long> {

    static HasJsonNodeLongMapper instance() {
        return new HasJsonNodeLongMapper();
    }

    private HasJsonNodeLongMapper() {
        super();
    }

    @Override
    Long fromJsonNode0(final JsonNode node) {
        try {
            final String text = node.stringValueOrFail();
            if (!text.startsWith(LONG_PREFIX)) {
                throw new IllegalArgumentException("Long string missing prefix " + CharSequences.quote(LONG_PREFIX) + "=" + node);
            }
            return Long.parseLong(text.substring(2), 16);
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    @Override
    JsonStringNode typeName() {
        return TYPE_NAME;
    }

    private final JsonStringNode TYPE_NAME = JsonStringNode.with("long");

    @Override
    JsonNode toJsonNode0(final Long value) {
        return JsonNode.string(LONG_PREFIX + Long.toHexString(value).toLowerCase());
    }

    /**
     * Prefix included at the start of all longs encoded as a string.
     */
    private final static String LONG_PREFIX = "0x";
}
