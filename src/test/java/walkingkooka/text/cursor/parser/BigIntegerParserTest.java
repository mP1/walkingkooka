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
 */
package walkingkooka.text.cursor.parser;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursors;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class BigIntegerParserTest extends ParserTemplateTestCase<BigIntegerParser<ParserContext>, BigIntegerParserToken> {

    private final static int RADIX = 10;

    @Test(expected = IllegalArgumentException.class)
    public void testWithNegativeRadixFails() {
        BigIntegerParser.with(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithZeroRadixFails() {
        BigIntegerParser.with(0);
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
    public void testPlusSignFail() {
        this.parseFailAndCheck("+");
    }

    @Test
    public void testMinusSignFail() {
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
    public void testPlusSignDecimal() {
        this.parseAndCheck2("+1", 1, "+1", "");
    }

    @Test
    public void testPlusSignDecimal2() {
        this.parseAndCheck2("+0", 0, "+0", "");
    }

    @Test
    public void testPlusSignDecimal3() {
        this.parseAndCheck2("+123", 123, "+123", "");
    }

    @Test
    public void testMinusSignDecimal() {
        this.parseAndCheck2("-1", -1, "-1", "");
    }

    @Test
    public void testMinusSignDecimal2() {
        this.parseAndCheck2("-123", -123, "-123", "");
    }

    @Test
    public void testUntilNonDigit() {
        this.parseAndCheck2("123abc", 123, "123", "abc");
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
    public void testDifferentMinusSign() {
        this.parseAndCheck3("M123", -123);
    }

    @Test
    public void testDifferentPlusSign() {
        this.parseAndCheck3("P123", 123);
    }

    private TextCursor parseAndCheck3(final String text, final int value) {
        return this.parseAndCheck(this.createParser(),
                ParserContexts.basic('!', 'X', 'M', 'P'),
                text,
                ParserTokens.bigInteger(BigInteger.valueOf(value), text),
                text,
                "");
    }

    @Test
    public void testToString() {
        assertEquals("BigInteger", this.createParser().toString());
    }

    @Test
    public void testToString2() {
        assertEquals("BigInteger(base=8)", BigIntegerParser.with(8).toString());
    }

    @Override
    protected BigIntegerParser createParser() {
        return BigIntegerParser.with(RADIX);
    }

    @Override
    protected ParserContext createContext() {
        return ParserContexts.basic('.', 'E', '-', '+');
    }

    private TextCursor parseAndCheck2(final String in, final long value, final String text, final String textAfter){
        return this.parseAndCheck2(in, BigInteger.valueOf(value), text, textAfter);
    }

    private TextCursor parseAndCheck2(final String in, final BigInteger value, final String text, final String textAfter){
        return this.parseAndCheck(in, BigIntegerParserToken.with(value, text), text, textAfter);
    }

    private TextCursor parseAndCheck3(final int radix, final String from, final long value, final String text, final String textAfter){
        return this.parseAndCheck(BigIntegerParser.with(radix),
                this.createContext(),
                TextCursors.charSequence(from),
                BigIntegerParserToken.with(BigInteger.valueOf(value), text),
                text,
                textAfter);
    }

    @Override
    protected Class<BigIntegerParser<ParserContext>> type() {
        return Cast.to(BigIntegerParser.class);
    }
}
