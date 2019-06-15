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

import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.search.SearchNode;

import java.util.Optional;

/**
 * Base class for a non symbol {@link ParserToken}.
 */
abstract class ColorFunctionNonSymbolParserToken<V> extends ColorFunctionLeafParserToken<V> {

    static void check(final Object value, final String text) {
        checkValue(value);
        checkText(text);
    }

    /**
     * Package private ctor to limit subclassing.
     */
    ColorFunctionNonSymbolParserToken(final V value, final String text) {
        super(value, text);
    }

    /**
     * Always returns self.
     */
    @Override
    public Optional<ColorFunctionParserToken> withoutSymbols() {
        return Optional.of(this);
    }

    // isXXX............................................................................................................

    @Override
    public final boolean isDegreesUnitSymbol() {
        return false;
    }

    @Override
    public final boolean isParenthesisCloseSymbol() {
        return false;
    }

    @Override
    public final boolean isParenthesisOpenSymbol() {
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

    @Override
    public final boolean isWhitespace() {
        return false;
    }

    // HasSearchNode....................................................................................................

    @Override
    public final SearchNode toSearchNode() {
        final String text = this.text();
        return SearchNode.text(text, text);
    }
}
