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

package walkingkooka.convert;

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

public final class DecimalFormatStringConverterSymbolsEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<DecimalFormatStringConverterSymbols> {

    private final static char DECIMAL = 'D';
    private final static char EXPONENT = 'E';
    private final static char MINUS = 'M';
    private final static char PLUS = 'P';

    @Test
    public void testDifferentDecimal() {
        this.checkNotEquals(DecimalFormatStringConverterSymbols.with('!', EXPONENT, MINUS, PLUS));
    }

    @Test
    public void testDifferentExponent() {
        this.checkNotEquals(DecimalFormatStringConverterSymbols.with(DECIMAL, '!', MINUS, PLUS));
    }

    @Test
    public void testDifferentMinus() {
        this.checkNotEquals(DecimalFormatStringConverterSymbols.with(DECIMAL, EXPONENT, '!', PLUS));
    }

    @Test
    public void testDifferentPlus() {
        this.checkNotEquals(DecimalFormatStringConverterSymbols.with(DECIMAL, EXPONENT, MINUS, '!'));
    }

    @Override
    protected DecimalFormatStringConverterSymbols createObject() {
        return DecimalFormatStringConverterSymbols.with(DECIMAL, EXPONENT, MINUS, PLUS);
    }
}
