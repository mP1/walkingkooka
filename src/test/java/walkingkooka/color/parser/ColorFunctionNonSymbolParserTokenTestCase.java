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

import org.junit.jupiter.api.Test;
import walkingkooka.text.cursor.parser.ParserTokenTesting;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ColorFunctionNonSymbolParserTokenTestCase<T extends ColorFunctionNonSymbolParserToken, V> extends ColorFunctionLeafParserTokenTestCase<T>
        implements ParserTokenTesting<T> {

    ColorFunctionNonSymbolParserTokenTestCase() {
        super();
    }

    @Test
    public void testWithoutSymbols() {
        final T token = this.createToken();
        assertEquals(Optional.of(token), token.withoutSymbols());
    }

    @Test
    public final void testIsSymbol() {
        assertEquals(false, this.createToken().isSymbol());
    }

    @Test
    public final void testDifferentValue() {
        this.checkNotEquals(this.createToken(this.text(), this.differentValue()));
    }

    @Override
    public final T createToken(final String text) {
        return this.createToken(text, this.value());
    }

    abstract T createToken(final String text, final V value);

    abstract V value();

    abstract V differentValue();
}
