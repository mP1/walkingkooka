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
package walkingkooka.text.cursor.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.cursor.TextCursor;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class StringCharPredicateParserTest extends Parser2TestCase<StringCharPredicateParser<ParserContext>, StringParserToken> {

    private final static CharPredicate DIGITS = CharPredicates.digit();
    private final static int MIN_LENGTH = 2;
    private final static int MAX_LENGTH = 4;

    @Test
    public void testWithNullCharPredicateFails() {
        assertThrows(NullPointerException.class, () -> {
            StringCharPredicateParser.with(null, MIN_LENGTH, MAX_LENGTH);
        });
    }

    @Test
    public void testWithInvalidMinLengthFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            StringCharPredicateParser.with(DIGITS, -1, MAX_LENGTH);
        });
    }

    @Test
    public void testWithInvalidMinLengthFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            StringCharPredicateParser.with(DIGITS, 0, MAX_LENGTH);
        });
    }

    @Test
    public void testWithInvalidMaxLengthFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            StringCharPredicateParser.with(DIGITS, MIN_LENGTH, MIN_LENGTH - 1);
        });
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
        this.toStringAndCheck(this.createParser(), DIGITS.toString());
    }

    @Override
    public StringCharPredicateParser<ParserContext> createParser() {
        return this.createParser(MIN_LENGTH, MAX_LENGTH);
    }

    protected StringCharPredicateParser<ParserContext> createParser(final int min, final int max) {
        return StringCharPredicateParser.with(DIGITS, min, max);
    }

    private TextCursor parseAndCheck2(final String in, final String value, final String text) {
        return this.parseAndCheck2(in, value, text, "");
    }

    private TextCursor parseAndCheck2(final String in, final String value, final String text, final String textAfter) {
        return this.parseAndCheck(this.createParser(1, 4),
                in,
                StringParserToken.with(value, text),
                text,
                textAfter);
    }

    private TextCursor parseAndCheck3(final String in, final String value, final String text) {
        return this.parseAndCheck3(in, value, text, "");
    }

    private TextCursor parseAndCheck3(final String in, final String value, final String text, final String textAfter) {
        return this.parseAndCheck(in, StringParserToken.with(value, text), text, textAfter);
    }

    @Override
    public Class<StringCharPredicateParser<ParserContext>> type() {
        return Cast.to(StringCharPredicateParser.class);
    }
}
