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

public final class RepeatedParserTokenTest extends ParserTokenTestCase<RepeatedParserToken> {

    @Test(expected = NullPointerException.class)
    public void testWithNullContentFails() {
        RepeatedParserToken.with(null, "tokens");
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullTextFails() {
        RepeatedParserToken.with(Lists.of(string("abc")), null);
    }
    
    @Override
    protected RepeatedParserToken createToken() {
        return RepeatedParserToken.with(Lists.of(string("abc")), "abc");
    }

    @Override
    protected RepeatedParserToken createDifferentToken() {
        return RepeatedParserToken.with(Lists.of(string("different")), "different");
    }

    private StringParserToken string(final String s) {
        return ParserTokens.string(s, s);
    }

    @Override
    protected Class<RepeatedParserToken> type() {
        return RepeatedParserToken.class;
    }
}
