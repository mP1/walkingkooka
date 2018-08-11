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

import static org.junit.Assert.assertEquals;

public final class SingleQuotedParserTest extends QuotedParserTestCase<SingleQuotedParser<FakeParserContext>, SingleQuotedParserToken> {

    @Test
    public void testToStringSingleQuoted() {
        assertEquals("single quoted string", this.createParser().toString());
    }

    @Override
    protected SingleQuotedParser createParser() {
        return SingleQuotedParser.instance();
    }

    @Override
    char quoteChar() {
        return '\'';
    }

    @Override
    char otherQuoteChar(){
        return '"';
    }

    @Override
    final SingleQuotedParserToken createToken(final String content, final String text) {
        return SingleQuotedParserToken.with(content, text);
    }

    @Override
    protected Class<SingleQuotedParser<FakeParserContext>> type() {
        return Cast.to(SingleQuotedParser.class);
    }
}
