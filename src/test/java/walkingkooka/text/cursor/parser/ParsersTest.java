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
import walkingkooka.test.PublicStaticHelperTestCase;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Optional;

public final class ParsersTest extends PublicStaticHelperTestCase<Parsers> {

    @Test
    public void testSurroundAndMerge() {
        final Parser<StringParserToken, FakeParserContext> before = Parsers.string("before");
        final Parser<NumberParserToken, FakeParserContext> middle = Parsers.number(10);
        final Parser<StringParserToken, FakeParserContext> after = Parsers.string("after");

        final Parser<NumberParserToken, FakeParserContext> parser = Parsers.surroundAndMerge(before,
                StringParserToken.NAME,
                middle,
                NumberParserToken.NAME,
                NumberParserToken.class,
                after,
                StringParserToken.NAME);

        final String textAfter = "123";
        final String text = "before10after";
        final TextCursor cursor = TextCursors.charSequence(text + textAfter);
        assertEquals(Optional.of(ParserTokens.number(BigInteger.valueOf(10), text)),
                parser.parse(cursor, new FakeParserContext()));

        final TextCursorSavePoint save = cursor.save();
        cursor.end();
        assertEquals("text after", textAfter, save.textBetween().toString());
    }

    @Override
    protected Class<Parsers> type() {
        return Parsers.class;
    }

    @Override
    protected boolean canHavePublicTypes(final Method method) {
        return false;
    }
}
