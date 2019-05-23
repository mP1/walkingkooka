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
import walkingkooka.collect.map.Maps;

public final class EncodingHeaderValueParserTest extends HeaderValueParserTestCase<EncodingHeaderValueParser, Encoding> {

    @Test
    public void testWhitespaceFails() {
        this.parseMissingValueFails("  ");
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
    public void testTokenQuotedTextFails() {
        this.parseInvalidCharacterFails("abc \"quoted text 123\"", '"');
    }

    @Test
    public void testToken() {
        this.parseAndCheck("gzip",
                Encoding.GZIP);
    }

    @Test
    public void testTokenWhitespace() {
        this.parseAndCheck("gzip ",
                Encoding.GZIP);
    }

    @Test
    public void testWhitespaceToken() {
        this.parseAndCheck(" gzip",
                Encoding.GZIP);
    }

    @Test
    public void testTokenParameter() {
        this.parseAndCheck("gzip;q=0.5",
                Encoding.GZIP.setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f)));
    }

    @Test
    public void testTokenParameterSemiParameter() {
        this.parseAndCheck("gzip;q=0.5;abc=xyz",
                Encoding.GZIP.setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f,
                        EncodingParameterName.with("abc"), "xyz")));
    }

    @Test
    public void testWildcard() {
        this.parseAndCheck("*",
                Encoding.WILDCARD_ENCODING);
    }

    @Test
    public void testWildcardParameter() {
        this.parseAndCheck("*;q=0.5",
                Encoding.WILDCARD_ENCODING.setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f)));
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
