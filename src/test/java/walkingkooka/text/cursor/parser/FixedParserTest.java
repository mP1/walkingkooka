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
import walkingkooka.text.cursor.TextCursors;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FixedParserTest extends ParserTestCase<FixedParser<ParserContext>> {

    private final static StringParserToken RESULT = ParserTokens.string("abc", "");

    @Test
    public void testWithNullParserTokenFails() {
        assertThrows(NullPointerException.class, () -> {
            FixedParser.with(null);
        });
    }

    @Test
    public void testEmptyCursorFail() {
        this.parseAndCheck("", RESULT, "", "");
    }

    @Test
    public void testParse() {
        this.parseAndCheck("", RESULT, "", "");
    }

    @Test
    public void testParse2() {
        this.parseAndCheck("abc", RESULT, "", "abc");
    }

    @Test
    public void testParseEmptyOptionalResult() {
        final Optional<ParserToken> result = Optional.empty();
        this.parseAndCheck(this.createParser(result),
                this.createContext(),
                TextCursors.charSequence(""),
                result,
                "",
                "");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createParser(), Optional.of(RESULT).toString());
    }

    @Override
    public FixedParser<ParserContext> createParser() {
        return this.createParser(Optional.of(RESULT));
    }

    private FixedParser<ParserContext> createParser(final Optional<ParserToken> result) {
        return FixedParser.with(result);
    }

    @Override
    public ParserContext createContext() {
        return ParserContexts.basic(this.decimalNumberContext());
    }

    @Override
    public Class<FixedParser<ParserContext>> type() {
        return Cast.to(FixedParser.class);
    }
}
