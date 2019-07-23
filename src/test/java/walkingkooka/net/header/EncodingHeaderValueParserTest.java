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

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;

public final class EncodingHeaderValueParserTest extends HeaderValueParserTestCase<EncodingHeaderValueParser, Encoding> {

    @Test
    public void testWhitespaceFails() {
        this.parseInvalidCharacterFails(" ");
    }

    @Test
    public void testSlashFails() {
        this.parseInvalidCharacterFails("ab/c", '/');
    }

    @Test
    public void testCommentFails() {
        this.parseCommentFails("(abc)", 0);
    }

    @Test
    public void testTokenCommentFails() {
        this.parseCommentFails("gzip(abc)", 4);
    }

    @Test
    public void testQuotedTextFails() {
        this.parseInvalidCharacterFails("\"quoted text 123\"", 0);
    }

    @Test
    public void testToken() {
        this.parseAndCheck("gzip",
                Encoding.GZIP);
    }

    @Test
    public void testTokenParameter() {
        this.parseInvalidCharacterFails("gzip;q=0.5", ';');
    }

    @Test
    public void testWildcard() {
        this.parseInvalidCharacterFails("*");
    }

    @Test
    public void testTokenCommaToken() {
        this.parseInvalidCharacterFails("gzip,deflate", ',');
    }

    @Override
    public Encoding parse(final String text) {
        return EncodingHeaderValueParser.parseEncoding(text);
    }

    @Override
    String valueLabel() {
        return "Encoding";
    }

    @Override
    public Class<EncodingHeaderValueParser> type() {
        return EncodingHeaderValueParser.class;
    }
}
