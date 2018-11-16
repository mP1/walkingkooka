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
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CaseSensitivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public final class CustomToStringParserTest extends ParserTestCase2<CustomToStringParser<StringParserToken, ParserContext>, StringParserToken>{

    private final static String STRING = "abc";
    private final static Parser<StringParserToken, ParserContext> WRAPPED = CaseSensitivity.SENSITIVE.parser(STRING);
    private final static String CUSTOM_TO_STRING = "!!abc!!";

    @Test(expected = NullPointerException.class)
    public void testWrapNullParserFails() {
        CustomToStringParser.wrap(null, CUSTOM_TO_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void testWrapNullToStringFails() {
        CustomToStringParser.wrap(WRAPPED, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapEmptyToStringFails() {
        CustomToStringParser.wrap(WRAPPED, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapWhitespaceToStringFails() {
        CustomToStringParser.wrap(WRAPPED, " \t");
    }

    @Test
    public void testDoesntWrapEquivalentToString() {
        assertSame(WRAPPED, CustomToStringParser.wrap(WRAPPED, WRAPPED.toString()));
    }

    @Test
    public void testUnwrapOtherCustomToStringParser() {
        final Parser<StringParserToken, ParserContext> first = CustomToStringParser.wrap(WRAPPED, "different");
        final CustomToStringParser<StringParserToken, ParserContext> wrapped = Cast.to(CustomToStringParser.wrap(first, CUSTOM_TO_STRING));
        assertNotSame(first, wrapped);
        assertSame("wrapped parser", WRAPPED, wrapped.parser);
        assertSame("wrapped toString", CUSTOM_TO_STRING, wrapped.toString);
    }

    @Test
    public void testSetToStringSame() {
        assertSame(WRAPPED, WRAPPED.setToString(WRAPPED.toString()));
    }

    @Test
    public void testDefaultMethodSetToString() {
        final Parser<?, ?> parser = WRAPPED.setToString(CUSTOM_TO_STRING);
        assertNotSame(WRAPPED, parser);
        assertEquals(CUSTOM_TO_STRING, parser.toString());
    }

    @Test
    public void testDefaultMethodSetToStringCustomToString() {
        assertSame(CUSTOM_TO_STRING,  this.createParser().setToString(CUSTOM_TO_STRING).toString());
    }

    @Test
    public void testDefaultMethodSetToStringCustomToStringDifferent() {
        final String different = "2";
        final Parser<?, ?> parser = this.createParser();
        final Parser<?, ?> parser2 = parser.setToString(different);
        assertNotSame(parser, parser2);
        assertEquals(different, parser2.toString());
    }

    @Test
    @Override
    public void testOr() {
        final CustomToStringParser<ParserToken, ParserContext> parser1 = this.createParser().cast();
        final CustomToStringParser<ParserToken, ParserContext> parser2 = this.createParser().cast();
        assertEquals(Parsers.alternatives(Lists.<Parser<ParserToken, ParserContext>>of(parser1, parser2)), parser1.or(parser2));
    }

    @Test
    public void testToString() {
        assertEquals(CUSTOM_TO_STRING, this.createParser().toString());
    }

    @Override
    protected CustomToStringParser<StringParserToken, ParserContext> createParser() {
        return CustomToStringParser.wrap(WRAPPED, CUSTOM_TO_STRING).cast();
    }

    @Override
    protected Class<CustomToStringParser<StringParserToken, ParserContext>> type() {
        return Cast.to(CustomToStringParser.class);
    }
}
