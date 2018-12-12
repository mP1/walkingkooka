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

import org.junit.Test;

public final class UnquotedStringHeaderValueConverterTest extends StringHeaderValueConverterTestCase<UnquotedStringHeaderValueConverter>{

    @Test(expected = HeaderValueException.class)
    public void testParseOpeningDoubleQuoteFails() {
        this.parse("\"abc");
    }

    @Test(expected = HeaderValueException.class)
    public void testParseBackslashFails() {
        this.parse("a\\bc");
    }

    @Test
    public void testRoundtrip() {
        this.parseAndToTextAndCheck("abc", "abc");
    }

    @Override
    protected String requiredPrefix() {
        return "UnquotedString";
    }

    @Override
    protected String invalidHeaderValue() {
        return "123";
    }

    @Override
    protected UnquotedStringHeaderValueConverter converter() {
        return UnquotedStringHeaderValueConverter.unquoted(this.charPredicate());
    }

    @Override
    protected Class<UnquotedStringHeaderValueConverter> type() {
        return UnquotedStringHeaderValueConverter.class;
    }
}
