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

public final class SingleQuotedParserTest extends QuotedParserTestCase<SingleQuotedParser<ParserContext>, SingleQuotedParserToken> {

    @Test
    public void testToStringSingleQuoted() {
        this.toStringAndCheck(this.createParser(), "single quoted string");
    }

    @Override public SingleQuotedParser<ParserContext> createParser() {
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
    public Class<SingleQuotedParser<ParserContext>> type() {
        return Cast.to(SingleQuotedParser.class);
    }
}
