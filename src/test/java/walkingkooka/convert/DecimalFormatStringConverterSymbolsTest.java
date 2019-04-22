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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.MemberVisibility;

public final class DecimalFormatStringConverterSymbolsTest implements ClassTesting2<DecimalFormatStringConverterSymbols>,
        HashCodeEqualsDefinedTesting<DecimalFormatStringConverterSymbols>,
        ToStringTesting<DecimalFormatStringConverterSymbols> {

    private final static String CURRENCY = "C";
    private final static char DECIMAL = 'D';
    private final static char EXPONENT = 'E';
    private final static char GROUPING = 'G';
    private final static char MINUS = 'M';
    private final static char PERCENTAGE = 'R';
    private final static char PLUS = 'P';

    @Test
    public void testEqualsDifferentCurrency() {
        this.checkNotEquals(DecimalFormatStringConverterSymbols.with("!", DECIMAL, EXPONENT, GROUPING, MINUS, PERCENTAGE, PLUS));
    }

    @Test
    public void testEqualsDifferentDecimal() {
        this.checkNotEquals(DecimalFormatStringConverterSymbols.with(CURRENCY, '!', EXPONENT, GROUPING, MINUS, PERCENTAGE, PLUS));
    }

    @Test
    public void testEqualsDifferentExponent() {
        this.checkNotEquals(DecimalFormatStringConverterSymbols.with(CURRENCY, DECIMAL, '!', GROUPING, MINUS, PERCENTAGE, PLUS));
    }

    @Test
    public void testEqualsDifferentGroupingSeparator() {
        this.checkNotEquals(DecimalFormatStringConverterSymbols.with(CURRENCY, DECIMAL, EXPONENT, '!', MINUS, PERCENTAGE, PLUS));
    }

    @Test
    public void testEqualsDifferentMinus() {
        this.checkNotEquals(DecimalFormatStringConverterSymbols.with(CURRENCY, DECIMAL, EXPONENT, GROUPING, '!', PERCENTAGE, PLUS));
    }

    @Test
    public void testEqualsDifferentPercentage() {
        this.checkNotEquals(DecimalFormatStringConverterSymbols.with(CURRENCY, DECIMAL, EXPONENT, GROUPING, MINUS, '!', PLUS));
    }

    @Test
    public void testEqualsDifferentPlus() {
        this.checkNotEquals(DecimalFormatStringConverterSymbols.with(CURRENCY, DECIMAL, EXPONENT, GROUPING, MINUS, PERCENTAGE, '!'));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(DecimalFormatStringConverterSymbols.with("C", 'D', 'X', 'G', 'M', 'R', 'P'),
                "\"C\" 'D' 'X' 'G' 'M' 'R' 'P'");
    }

    @Override
    public Class<DecimalFormatStringConverterSymbols> type() {
        return DecimalFormatStringConverterSymbols.class;
    }

    @Override public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public DecimalFormatStringConverterSymbols createObject() {
        return DecimalFormatStringConverterSymbols.with(CURRENCY, DECIMAL, EXPONENT, GROUPING, MINUS, PERCENTAGE, PLUS);
    }
}
