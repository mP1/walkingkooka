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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EncodedTextHeaderValueConverterHeaderParserTest extends HeaderParserTestCase<EncodedTextHeaderValueConverterHeaderParser,
        EncodedText> {

    private final static String LABEL = "label123";

    @Test
    public void testParse() {
        this.parseAndCheck("UTF-8''abc%20123", EncodedText.with(CharsetName.UTF_8, EncodedText.NO_LANGUAGE, "abc 123"));
    }

    @Test
    public void testKeyValueSeparatorFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.createHeaderParser().keyValueSeparator();
        });
    }

    @Test
    public void testMissingValueFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.createHeaderParser().missingValue();
        });
    }

    @Test
    public void testMultiValueSeparatorFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.createHeaderParser().multiValueSeparator();
        });
    }

    @Test
    public void testQuotedTextSeparatorFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.createHeaderParser().quotedText();
        });
    }

    @Test
    public void testSlashFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.createHeaderParser().slash();
        });
    }

    @Test
    public void testTokenSeparatorFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.createHeaderParser().tokenSeparator();
        });
    }

    @Test
    public void testWhitespaceFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.createHeaderParser().whitespace();
        });
    }

    @Test
    public void testWildcardFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.createHeaderParser().wildcard();
        });
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
