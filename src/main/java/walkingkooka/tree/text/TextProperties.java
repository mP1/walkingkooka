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

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.test.HashCodeEqualsDefined;

import java.util.Map;

/**
 * A {@link TextProperties} holds a {@link Map} of {@link TextPropertyName} and values.
 */
public final class TextProperties implements HashCodeEqualsDefined,
        Value<Map<TextPropertyName<?>, Object>> {

    /**
     * Factory that creates a {@link TextProperties} from a {@link Map}.
     */
    public static TextProperties with(final Map<TextPropertyName<?>, Object> value) {
        return new TextProperties(TextPropertiesMap.with(value));
    }

    private TextProperties(final Map<TextPropertyName<?>, Object> value) {
        super();
        this.value = value;
    }

    @Override
    public Map<TextPropertyName<?>, Object> value() {
        return this.value;
    }

    final Map<TextPropertyName<?>, Object> value;

    // Object..........................................................................................................

    @Override
    public final int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                other instanceof TextProperties &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final TextProperties other) {
        return this.value.equals(other.value);
    }

    @Override
    public final String toString() {
        return this.value.toString();
    }
}
