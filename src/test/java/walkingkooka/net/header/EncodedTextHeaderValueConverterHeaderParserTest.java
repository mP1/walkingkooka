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

public final class EncodedTextHeaderValueConverterHeaderParserTest extends HeaderParserTestCase<EncodedTextHeaderValueConverterHeaderParser,
        EncodedText> {

    private final static String LABEL = "label123";

    @Test
    public void testParse() {
        this.parseAndCheck("UTF-8''abc%20123", EncodedText.with(CharsetName.UTF_8, EncodedText.NO_LANGUAGE, "abc 123"));
    }

    @Test(expected = HeaderValueException.class)
    public void testKeyValueSeparatorFails() {
        this.createHeaderParser().keyValueSeparator();
    }

    @Test(expected = HeaderValueException.class)
    public void testMissingValueFails() {
        this.createHeaderParser().missingValue();
    }

    @Test(expected = HeaderValueException.class)
    public void testMultiValueSeparatorFails() {
        this.createHeaderParser().multiValueSeparator();
    }

    @Test(expected = HeaderValueException.class)
    public void testQuotedTextSeparatorFails() {
        this.createHeaderParser().quotedText();
    }

    @Test(expected = HeaderValueException.class)
    public void testSlashFails() {
        this.createHeaderParser().slash();
    }

    @Test(expected = HeaderValueException.class)
    public void testTokenSeparatorFails() {
        this.createHeaderParser().tokenSeparator();
    }

    @Test(expected = HeaderValueException.class)
    public void testWhitespaceFails() {
        this.createHeaderParser().whitespace();
    }

    @Test(expected = HeaderValueException.class)
    public void testWildcardFails() {
        this.createHeaderParser().wildcard();
    }

    private EncodedTextHeaderValueConverterHeaderParser createHeaderParser() {
        return new EncodedTextHeaderValueConverterHeaderParser("text", LABEL);
    }

    @Override
    String valueLabel() {
        return LABEL;
    }

    @Override
    EncodedText parse(final String text) {
        return EncodedTextHeaderValueConverterHeaderParser.parseEncodedText(text, LABEL);
    }

    @Override
    protected Class<EncodedTextHeaderValueConverterHeaderParser> type() {
        return EncodedTextHeaderValueConverterHeaderParser.class;
    }
}
