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
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.TextCursor;

public abstract class ParserTestCase2<P extends Parser<T, FakeParserContext>, T extends ParserToken> extends ParserTestCase<P, T, FakeParserContext> {

    @Test
    public void testEmptyCursorFail() {
        this.parseFailAndCheck("");
    }

    @Test(expected = NullPointerException.class)
    public final void testOptionalNullNameFails() {
        this.createParser().optional(null);
    }

    @Test(expected = NullPointerException.class)
    public final void testOrNullParserFails() {
        this.createParser().or(null);
    }

    @Test
    public final void testRepeating() {
        final Parser<RepeatedParserToken, FakeParserContext> parser = this.createParser().repeating();
        assertEquals("" + parser, RepeatedParser.class, parser.getClass());
    }

    @Test(expected = NullPointerException.class)
    public final void testBuilderWithNull() {
        this.createParser().builder(null);
    }

    @Test
    public void testOr() {
        final P parser = this.createParser();
        final P parser2 = this.createParser();
        assertEquals(Parsers.alternatives(Lists.of(parser.castTC(), parser2.castTC())), parser.or(parser2));
    }

    @Override
    protected FakeParserContext createContext() {
        return new FakeParserContext();
    }

    protected final TextCursor parseFailAndCheck(final Parser <T, FakeParserContext> parser, final TextCursor cursor) {
        return this.parseFailAndCheck(parser, this.createContext(), cursor);
    }
}
