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

package walkingkooka.text.cursor.parser.color;

public final class ColorFunctionSeparatorSymbolParserToken extends ColorFunctionSymbolParserToken<String> {

    static ColorFunctionSeparatorSymbolParserToken with(final String value, final String text) {
        check(value, text);

        return new ColorFunctionSeparatorSymbolParserToken(value, text);
    }

    private ColorFunctionSeparatorSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    // isXXX............................................................................................................

    @Override
    public boolean isDegreesUnitSymbol() {
        return false;
    }

    @Override
    public boolean isParenthesisCloseSymbol() {
        return false;
    }

    @Override
    public boolean isParenthesisOpenSymbol() {
        return false;
    }

    @Override
    public boolean isSeparatorSymbol() {
        return true;
    }

    @Override
    public boolean isWhitespace() {
        return false;
    }

    // ColorFunctionParserTokenVisitor..................................................................................

    @Override
    public void accept(final ColorFunctionParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ColorFunctionSeparatorSymbolParserToken;
    }
}
