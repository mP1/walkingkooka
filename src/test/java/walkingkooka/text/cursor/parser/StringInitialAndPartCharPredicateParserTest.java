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
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

public class StringInitialAndPartCharPredicateParserTest extends ParserTemplateTestCase<StringInitialAndPartCharPredicateParser<FakeParserContext>, StringParserToken> {

    private final static CharPredicate INITIAL = CharPredicates.letter();
    private final static CharPredicate PART = CharPredicates.digit();
    private final static int MIN_LENGTH = 1;
    private final static int MAX_LENGTH = 6;

    @Test(expected = NullPointerException.class)
    public void testWithNullInitialCharPredicateFails() {
        StringInitialAndPartCharPredicateParser.with(null, PART, MIN_LENGTH, MAX_LENGTH);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullInitialPartPredicateFails() {
        StringInitialAndPartCharPredicateParser.with(INITIAL, null, MIN_LENGTH, MAX_LENGTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidMinLengthFails() {
        StringInitialAndPartCharPredicateParser.with(INITIAL, PART, 0, MAX_LENGTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidMaxLengthFails() {
        StringInitialAndPartCharPredicateParser.with(INITIAL, PART, MIN_LENGTH, MIN_LENGTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidMaxLengthFails2() {
        StringInitialAndPartCharPredicateParser.with(INITIAL, PART, MIN_LENGTH, MIN_LENGTH -1);
    }

    @Test
    public void testInitialFail() {
        this.parseFailAndCheck("1");
    }

    @Test
    public void testInitial() {
        this.parseAndCheck2("a", "a", "");
    }

    @Test
    public void testInitial2() {
        final String textAfter = "xyz";
        this.parseAndCheck2("a" + textAfter, "a", textAfter);
    }

    @Test
    public void testInitialAndPart() {
        this.parseAndCheck2("a1", "a1", "");
    }

    @Test
    public void testInitialAndPart2() {
        final String textAfter = "...";
        this.parseAndCheck2("a1" + textAfter, "a1", textAfter);
    }

    @Test
    public void testInitialAndPart3() {
        this.parseAndCheck2("a12345", "a12345", "");
    }

    @Test
    public void testInitialAndPart4() {
        final String textAfter = "...";
        this.parseAndCheck2("a12345" + textAfter, "a12345", textAfter);
    }

    @Test
    public void testTooLongFail() {
        this.parseFailAndCheck("a12345678");
    }

    private void parseAndCheck2(final String text, final String expected) {
        this.parseAndCheck2(text, expected, expected);
    }

    private void parseAndCheck2(final String text, final String expected, final String textAfter) {
        this.parseAndCheck(text, ParserTokens.string(expected, expected), expected, textAfter);
    }

    @Test
    public void testToString() {
        assertEquals(INITIAL + " " + PART, this.createParser().toString());
    }

    @Override
    protected StringInitialAndPartCharPredicateParser createParser() {
        return StringInitialAndPartCharPredicateParser.with(INITIAL, PART, MIN_LENGTH, MAX_LENGTH);
    }

    @Override
    protected Class<StringInitialAndPartCharPredicateParser<FakeParserContext>> type() {
        return Cast.to(StringInitialAndPartCharPredicateParser.class);
    }
}
