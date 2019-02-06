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

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;

public final class TokenHeaderValueOneHeaderParserTest extends TokenHeaderValueHeaderParserTestCase<TokenHeaderValueOneHeaderParser,
        TokenHeaderValue> {

    @Test
    public void testParameterSeparatorFails() {
        this.parseMissingValueFails(";", 0);
    }

    @Test
    public void testValueSpaceSeparatorFails() {
        this.parseInvalidCharacterFails("A ,");
    }

    @Test
    public void testValueTabSeparatorFails() {
        this.parseInvalidCharacterFails("A\t,");
    }

    @Test
    public void testValueParameterSeparatorFails() {
        this.parseInvalidCharacterFails("A;b=c,");
    }

    @Test
    public final void testValueValueSeparatorFails() {
        this.parseInvalidCharacterFails("A;,");
    }

    @Override
    public TokenHeaderValue parse(final String text) {
        return TokenHeaderValueOneHeaderParser.parseTokenHeaderValue(text);
    }

    @Override
    void parseAndCheck2(final String headerValue, final TokenHeaderValue token) {
        this.parseAndCheck3(headerValue, token);
    }

    @Override
    public Class<TokenHeaderValueOneHeaderParser> type() {
        return TokenHeaderValueOneHeaderParser.class;
    }
}
