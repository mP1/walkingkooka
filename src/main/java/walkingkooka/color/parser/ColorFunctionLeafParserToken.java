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

package walkingkooka.color.parser;

import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.LeafParserToken;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.Objects;

/**
 * Base class for a color function leaf {@link ParserToken}.
 */
abstract class ColorFunctionLeafParserToken<V> extends ColorFunctionParserToken implements LeafParserToken<V> {

    static String checkValue(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");
        return text;
    }

    static void checkValue(final Object value) {
        Objects.requireNonNull(value, "value");
    }

    /**
     * Package private ctor to limit subclassing.
     */
    ColorFunctionLeafParserToken(final V value, final String text) {
        super(text);
        this.value = value;
    }

    @Override
    public final V value() {
        return this.value;
    }

    private final V value;

    // isXXX............................................................................................................

    @Override
    public final boolean isFunction() {
        return false;
    }
}
