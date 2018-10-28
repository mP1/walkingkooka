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
package walkingkooka.text.cursor.parser.json;

import java.util.Optional;

/**
 * Base class for a leaf token. A leaf has no further breakdown into more detailed tokens.
 */
abstract class JsonNodeValueParserToken<V> extends JsonNodeLeafParserToken<V> {

    JsonNodeValueParserToken(final V value, final String text) {
        super(value, text);
    }

    @Override
    public final Optional<JsonNodeParserToken> withoutSymbolsOrWhitespace() {
        return Optional.of(this);
    }

    @Override
    public final boolean isArrayBeginSymbol() {
        return false;
    }

    @Override
    public final boolean isArrayEndSymbol() {
        return false;
    }

    @Override
    public final boolean isObjectBeginSymbol() {
        return false;
    }

    @Override
    public final boolean isObjectEndSymbol() {
        return false;
    }

    @Override
    public final boolean isObjectAssignmentSymbol() {
        return false;
    }

    @Override
    public final boolean isSeparatorSymbol() {
        return false;
    }

    @Override
    public final boolean isSymbol() {
        return false;
    }
}
