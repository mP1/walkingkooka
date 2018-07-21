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
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.cursor.TextCursor;

public class StringCharPredicateParserTest extends ParserTemplateTestCase<StringCharPredicateParser<FakeParserContext>, StringParserToken> {

    private final static CharPredicate DIGITS = CharPredicates.digit();
    private final static int MIN_LENGTH = 2;
    private final static int MAX_LENGTH = 4;

    @Test(expected = NullPointerException.class)
    public void testWithNullCharPredicateFails() {
        StringCharPredicateParser.with(null, MIN_LENGTH, MAX_LENGTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidMinLengthFails() {
        StringCharPredicateParser.with(DIGITS, -1, MAX_LENGTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidMinLengthFails2() {
        StringCharPredicateParser.with(DIGITS, 0, MAX_LENGTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidMaxLengthFails2() {
        StringCharPredicateParser.with(DIGITS, MIN_LENGTH, MIN_LENGTH -1);
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
    public void testTooShort() {
        this.parseFailAndCheck("1");
    }

    @Test
    public void testSuccess() {
        this.parseAndCheck2("1", "1", "1");
    }

    @Test
    public void testSuccess2() {
        this.parseAndCheck2("2", "2", "2");
    }

    @Test
    public void testSuccess3() {
        this.parseAndCheck2("2abc", "2", "2", "abc");
    }

    @Test
    public void testSuccess4() {
        this.parseAndCheck3("123abc", "123", "123", "abc");
    }

    @Test
    public void testSuccessTerminatedByMismatch() {
        this.parseAndCheck3("123abc", "123", "123", "abc");
    }

    @Test
    public void testSuccessTerminatedEof() {
        this.parseAndCheck3("123", "123", "123");
    }

    @Test
    public void testMultipleAttempts() {
        final TextCursor cursor = this.parseAndCheck3("123abc", "123", "123", "abc");
        this.parseFailAndCheck(cursor);
    }

    @Test
    public void testToString() {
        assertEquals(DIGITS.toString(), this.createParser().toString());
    }

    @Override
    protected StringCharPredicateParser createParser() {
        return this.createParser(MIN_LENGTH, MAX_LENGTH);
    }

    protected StringCharPredicateParser createParser(final int min, final int max) {
        return StringCharPredicateParser.with(DIGITS, min, max);
    }

    private TextCursor parseAndCheck2(final String in, final String value, final String text){
        return this.parseAndCheck2(in, value, text, "");
    }

    private TextCursor parseAndCheck2(final String in, final String value, final String text, final String textAfter){
        return this.parseAndCheck(this.createParser(1, 4),
                in,
                StringParserToken.with(value, text),
                text,
                textAfter);
    }

    private TextCursor parseAndCheck3(final String in, final String value, final String text){
        return this.parseAndCheck3(in, value, text, "");
    }

    private TextCursor parseAndCheck3(final String in, final String value, final String text, final String textAfter){
        return this.parseAndCheck(in, StringParserToken.with(value, text), text, textAfter);
    }

    @Override
    protected Class<StringCharPredicateParser<FakeParserContext>> type() {
        return Cast.to(StringCharPredicateParser.class);
    }
}
