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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class StringInitialAndPartCharPredicateParserTest extends Parser2TestCase<StringInitialAndPartCharPredicateParser<ParserContext>, StringParserToken> {

    private final static CharPredicate INITIAL = CharPredicates.letter();
    private final static CharPredicate PART = CharPredicates.digit();
    private final static int MIN_LENGTH = 4;
    private final static int MAX_LENGTH = 6;

    @Test
    public void testWithNullInitialCharPredicateFails() {
        assertThrows(NullPointerException.class, () -> {
            StringInitialAndPartCharPredicateParser.with(null, PART, MIN_LENGTH, MAX_LENGTH);
        });
    }

    @Test
    public void testWithNullInitialPartPredicateFails() {
        assertThrows(NullPointerException.class, () -> {
            StringInitialAndPartCharPredicateParser.with(INITIAL, null, MIN_LENGTH, MAX_LENGTH);
        });
    }

    @Test
    public void testWithInvalidMinLengthFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            StringInitialAndPartCharPredicateParser.with(INITIAL, PART, 0, MAX_LENGTH);
        });
    }

    @Test
    public void testWithInvalidMaxLengthFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            StringInitialAndPartCharPredicateParser.with(INITIAL, PART, MIN_LENGTH, MIN_LENGTH);
        });
    }

    @Test
    public void testWithInvalidMaxLengthFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            StringInitialAndPartCharPredicateParser.with(INITIAL, PART, MIN_LENGTH, MIN_LENGTH -1);
        });
    }

    @Test
    public void testInitialFail() {
        this.parseFailAndCheck("1");
    }

    @Test
    public void testInitialOnlyShorterThanMinFails() {
        this.parseFailAndCheck("a");
    }

    @Test
    public void testInitialAndPartShorterThanMinFails() {
        this.parseFailAndCheck("a12");
    }

    @Test
    public void testInitialAndPart() {
        this.parseAndCheck2("a12345", "a12345");
    }

    @Test
    public void testInitialAndPart2() {
        this.parseAndCheck2("a12345-=", "a12345", "-=");
    }

    @Test
    public void testHonourMaxLength() {
        this.parseAndCheck2("a12345678", "a12345", "678");
    }

    @Test
    public void testHonourMaxLength2() {
        this.parseAndCheck(this.createParser(1, 2),"a12345678", this.string("a1"), "a1", "2345678");
    }

    private void parseAndCheck2(final String text, final String expected) {
        this.parseAndCheck2(text, expected, "");
    }

    private void parseAndCheck2(final String text, final String expected, final String textAfter) {
        this.parseAndCheck(text, this.string(expected), expected, textAfter);
    }

    private StringParserToken string(final String text) {
        return ParserTokens.string(text, text);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createParser(), INITIAL + " " + PART);
    }

    @Override public StringInitialAndPartCharPredicateParser<ParserContext> createParser() {
        return this.createParser(MIN_LENGTH, MAX_LENGTH);
    }

    private StringInitialAndPartCharPredicateParser<ParserContext> createParser(final int min, final int max) {
        return StringInitialAndPartCharPredicateParser.with(INITIAL, PART, min, max);
    }

    @Override
    public Class<StringInitialAndPartCharPredicateParser<ParserContext>> type() {
        return Cast.to(StringInitialAndPartCharPredicateParser.class);
    }
}
