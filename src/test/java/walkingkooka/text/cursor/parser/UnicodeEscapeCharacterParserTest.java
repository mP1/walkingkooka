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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.text.cursor.TextCursor;

public final class UnicodeEscapeCharacterParserTest extends Parser2TestCase<UnicodeEscapeCharacterParser<ParserContext>, CharacterParserToken> {

    @Test
    public void testFailure() {
        this.parseFailAndCheck("a");
    }

    @Test
    public void testBacklashFails() {
        this.parseFailAndCheck("\\");
    }

    @Test
    public void testBacklashFails2() {
        this.parseFailAndCheck("\\-");
    }

    @Test
    public void testBacklashUFails() {
        this.parseFailAndCheck("\\u");
    }

    @Test
    public void testBacklashUFails2() {
        this.parseFailAndCheck("\\u-");
    }

    @Test
    public void testBacklashUOneDigitFails() {
        this.parseFailAndCheck("\\u1");
    }

    @Test
    public void testBacklashUOneDigitFails2() {
        this.parseFailAndCheck("\\u1-");
    }

    @Test
    public void testBacklashUTwoDigitFails() {
        this.parseFailAndCheck("\\u12");
    }

    @Test
    public void testBacklashUTwoDigitFails2() {
        this.parseFailAndCheck("\\u12-");
    }

    @Test
    public void testBacklashUThreeDigitFails() {
        this.parseFailAndCheck("\\u123");
    }
    @Test
    public void testBacklashUThreeDigitFails2() {
        this.parseFailAndCheck("\\u123-");
    }

    @Test
    public void testComplete() {
        this.parseAndCheck2("\\u1234", '\u1234', "\\u1234");
    }

    @Test
    public void testComplete2() {
        this.parseAndCheck2("\\u12345", '\u1234', "\\u1234", "5");
    }

    @Test
    public void testComplete3() {
        this.parseAndCheck2("\\u1234ABC", '\u1234', "\\u1234", "ABC");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createParser(), "Unicode escape char sequence");
    }

    @Override public UnicodeEscapeCharacterParser<ParserContext> createParser() {
        return UnicodeEscapeCharacterParser.get();
    }

    private TextCursor parseAndCheck2(final String in, final char value, final String text){
        return this.parseAndCheck2(in, value, text, "");
    }

    private TextCursor parseAndCheck2(final String in, final char value, final String text, final String textAfter){
        return this.parseAndCheck(in, CharacterParserToken.with(value, text), text, textAfter);
    }

    @Override
    public Class<UnicodeEscapeCharacterParser<ParserContext>> type() {
        return Cast.to(UnicodeEscapeCharacterParser.class);
    }
}
