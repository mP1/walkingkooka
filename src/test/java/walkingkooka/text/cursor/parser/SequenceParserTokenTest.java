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

public final class SequenceParserTokenTest extends ParserTokenTestCase<SequenceParserToken> {

    @Test(expected = NullPointerException.class)
    public void testWithNullTokensFails() {
        SequenceParserToken.with(null, "tokens");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithZeroTokensFails() {
        SequenceParserToken.with(Lists.of(string("1")), "tokens");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithOneTokenFails() {
        SequenceParserToken.with(Lists.of(string("abc")), "tokens");
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullTextFails() {
        SequenceParserToken.with(Lists.of(string("abc"), string("def")), null);
    }
    
    @Override
    protected SequenceParserToken createToken() {
        return SequenceParserToken.with(Lists.of(string("abc"), string("def")), "abcdef");
    }

    @Override
    protected SequenceParserToken createDifferentToken() {
        return SequenceParserToken.with(Lists.of(string("different"), string("different2")), "differentdifferent2");
    }

    private StringParserToken string(final String s) {
        return ParserTokens.string(s, s);
    }

    @Override
    protected Class<SequenceParserToken> type() {
        return SequenceParserToken.class;
    }
}
