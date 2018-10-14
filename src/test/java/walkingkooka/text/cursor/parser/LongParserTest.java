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
package walkingkooka.text.cursor.parser;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursors;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class LongParserTest extends ParserTemplateTestCase<LongParser<ParserContext>, LongParserToken> {

    private final static int RADIX = 10;

    @Test(expected = IllegalArgumentException.class)
    public void testWithNegativeRadixFails() {
        LongParser.with(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithZeroRadixFails() {
        LongParser.with(0);
    }

    @Test
    public void testFailure() {
        this.parseFailAndCheck("a");
    }

    @Test
    public void testFailure2() {
        this.parseFailAndCheck("abc");
    }

    @Test
    public void testPlusSignFails() {
        this.parseFailAndCheck("+");
    }

    @Test
    public void testMinusSignFails() {
        this.parseFailAndCheck("-");
    }

    @Test
    public void testDecimal() {
        this.parseAndCheck2("1", 1, "1", "");
    }

    @Test
    public void testDecimal2() {
        this.parseAndCheck2("123", 123, "123", "");
    }

    @Test
    public void testDecimal3() {
        this.parseAndCheck2("+123", 123, "+123", "");
    }

    @Test
    public void testUntilNonDigit() {
        this.parseAndCheck2("123abc", 123, "123", "abc");
    }

    @Test
    public void testNegativeDecimal() {
        this.parseAndCheck2("-123", -123, "-123", "");
    }

    @Test
    public void testNegativeDecimal2() {
        this.parseAndCheck2("-123//", -123, "-123", "//");
    }

    @Test
    public void testHex() {
        this.parseAndCheck3(16, "1234xyz", 0x1234, "1234", "xyz");
    }

    @Test
    public void testOctal() {
        this.parseAndCheck3(8, "012345678xyz", 01234567, "01234567", "8xyz");
    }

    @Test
    public void testMaxValueHex() {
        this.parseAndCheck3(16, "7fffffffffffffff", Long.MAX_VALUE, "7fffffffffffffff", "");
    }

    @Test
    public void testMaxValueHex2() {
        this.parseAndCheck3(16, "7fffffffffffffff///", Long.MAX_VALUE, "7fffffffffffffff", "///");
    }

    @Test
    public void testLongMaxValue() {
        final BigInteger bigInteger = BigInteger.valueOf(Long.MAX_VALUE);
        final String text = bigInteger.toString();
        this.parseAndCheck3(10, text, Long.MAX_VALUE, text, "");
    }

    @Test
    public void testLongMaxValue2() {
        final BigInteger bigInteger = BigInteger.valueOf(Long.MAX_VALUE);
        final String text = bigInteger.toString();
        final String after = "//";
        this.parseAndCheck3(10, text + after, Long.MAX_VALUE, text, after);
    }

    @Test
    public void testPlusSignLongMaxValue() {
        final BigInteger bigInteger = BigInteger.valueOf(Long.MAX_VALUE);
        final String text = "+" + bigInteger.toString();
        this.parseAndCheck3(10, text, Long.MAX_VALUE, text, "");
    }

    @Test
    public void testPlusSignLongMaxValue2() {
        final BigInteger bigInteger = BigInteger.valueOf(Long.MAX_VALUE);
        final String text = "+" + bigInteger.toString();
        final String after = "//";
        this.parseAndCheck3(10, text + after, Long.MAX_VALUE, text, after);
    }

    @Test
    public void testLongMinValue() {
        final BigInteger bigInteger = BigInteger.valueOf(Long.MIN_VALUE);
        final String text = bigInteger.toString();
        final String after = "";
        this.parseAndCheck3(10, text + after, Long.MIN_VALUE, text, after);
    }

    @Test
    public void testLongMinValue2() {
        final BigInteger bigInteger = BigInteger.valueOf(Long.MIN_VALUE);
        final String text = bigInteger.toString();
        final String after = "//";
        this.parseAndCheck3(10, text + after, Long.MIN_VALUE, text, after);
    }

    @Test(expected = ParserException.class)
    public void testGreaterMaxValueFails() {
        final BigInteger bigInteger = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);

        this.parseFailAndCheck(LongParser.with(10), bigInteger.toString());
    }

    @Test(expected = ParserException.class)
    public void testLessMinValueFails() {
        final BigInteger bigInteger = BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE);

        this.parseFailAndCheck(LongParser.with(10), bigInteger.toString());
    }

    @Test(expected = ParserException.class)
    public void testGreaterMaxValueHexFails() {
        this.parseFailAndCheck(LongParser.with(16), "8fffffffffffffff");
    }

    @Test
    public void testDifferentMinusSign() {
        this.parseAndCheck3("M123", -123);
    }

    @Test
    public void testDifferentPlusSign() {
        this.parseAndCheck3("P123", 123);
    }

    private TextCursor parseAndCheck3(final String text, final long value) {
        return this.parseAndCheck(this.createParser(),
                ParserContexts.basic('!', 'X', 'M', 'P'),
                text,
                ParserTokens.longParserToken(value, text),
                text,
                "");
    }

    @Test
    public void testToString() {
        assertEquals("Long", this.createParser().toString());
    }

    @Test
    public void testToString2() {
        assertEquals("Long(base=8)", LongParser.with(8).toString());
    }

    @Override
    protected LongParser createParser() {
        return LongParser.with(RADIX);
    }

    @Override
    protected ParserContext createContext() {
        return ParserContexts.basic('.', 'E', '-', '+');
    }

    private TextCursor parseAndCheck2(final String in, final long value, final String text, final String textAfter){
        return this.parseAndCheck3(RADIX, in, value, text, textAfter);
    }

    private TextCursor parseAndCheck3(final int radix, final String from, final long value, final String text, final String textAfter){
        return this.parseAndCheck(LongParser.with(radix),
                this.createContext(),
                TextCursors.charSequence(from),
                LongParserToken.with(value, text),
                text,
                textAfter);
    }

    @Override
    protected Class<LongParser<ParserContext>> type() {
        return Cast.to(LongParser.class);
    }
}
