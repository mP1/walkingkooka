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
import walkingkooka.text.CharSequences;

import static org.junit.Assert.assertEquals;

public class StringParserTest extends ParserTemplateTestCase<StringParser<FakeParserContext>, StringParserToken> {

    private final static String STRING = "abc";

    @Test(expected = NullPointerException.class)
    public void testWithNullStringFails() {
        StringParser.with(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyStringFails() {
        StringParser.with("");
    }

    @Test
    public void testIncomplete() {
        this.parseFailAndCheck("a");
    }

    @Test
    public void testIncomplete2() {
        this.parseFailAndCheck("ab");
    }

    @Test
    public void testStringEoc() {
        this.parseAndCheck(STRING, StringParserToken.with(STRING, STRING), STRING, "");
    }

    @Test
    public void testStringIgnoresRemainder() {
        this.parseAndCheck(STRING + "xyz", StringParserToken.with(STRING, STRING), STRING, "xyz");
    }

    @Test
    public void testToString() {
        assertEquals(CharSequences.quoteAndEscape(STRING).toString(), this.createParser().toString());
    }

    @Override
    protected StringParser createParser() {
        return StringParser.with(STRING);
    }

    @Override
    protected Class<StringParser<FakeParserContext>> type() {
        return Cast.to(StringParser.class);
    }
}
