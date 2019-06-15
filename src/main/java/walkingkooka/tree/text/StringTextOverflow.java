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

package walkingkooka.tree.text;

import walkingkooka.Cast;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;

import java.util.Optional;

final class StringTextOverflow extends TextOverflow {

    static StringTextOverflow with(final String value) {
        CharSequences.failIfNullOrEmpty(value, "value");

        return new StringTextOverflow(value);
    }

    /**
     * Private constructor use static factory
     */
    private StringTextOverflow(final String value) {
        super();
        this.value = value;
    }

    /**
     * Getter that returns the weight value as an integer
     */
    @Override
    public Optional<String> value() {
        return Optional.of(this.value);
    }

    private final String value;

    // isXXX............................................................................................................

    @Override
    public boolean isClip() {
        return false;
    }

    @Override
    public boolean isEllipse() {
        return false;
    }

    @Override
    public boolean isString() {
        return true;
    }

    // Object .........................................................................................................

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof StringTextOverflow &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final StringTextOverflow other) {
        return this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return CharSequences.quoteAndEscape(this.value).toString();
    }

    // HasJsonNode......................................................................................................

    @Override
    public JsonNode toJsonNode() {
        return JsonNode.string(STRING_PREFIX + this.value);
    }

    // Serializable ..................................................................................................

    private final static long serialVersionUID = 1L;
}
