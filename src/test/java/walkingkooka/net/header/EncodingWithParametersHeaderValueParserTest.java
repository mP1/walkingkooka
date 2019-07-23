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
import walkingkooka.collect.map.Maps;

public final class EncodingWithParametersHeaderValueParserTest extends HeaderValueParserTestCase<EncodingWithParametersHeaderValueParser, EncodingWithParameters> {

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
                EncodingWithParameters.GZIP);
    }

    @Test
    public void testTokenWhitespace() {
        this.parseAndCheck("gzip ",
                EncodingWithParameters.GZIP);
    }

    @Test
    public void testWhitespaceToken() {
        this.parseAndCheck(" gzip",
                EncodingWithParameters.GZIP);
    }

    @Test
    public void testTokenParameter() {
        this.parseAndCheck("gzip;q=0.5",
                EncodingWithParameters.GZIP.setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f)));
    }

    @Test
    public void testTokenParameterSemiParameter() {
        this.parseAndCheck("gzip;q=0.5;abc=xyz",
                EncodingWithParameters.GZIP.setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f,
                        EncodingParameterName.with("abc"), "xyz")));
    }

    @Test
    public void testWildcard() {
        this.parseAndCheck("*",
                EncodingWithParameters.WILDCARD_ENCODING);
    }

    @Test
    public void testWildcardParameter() {
        this.parseAndCheck("*;q=0.5",
                EncodingWithParameters.WILDCARD_ENCODING.setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f)));
    }

    @Test
    public void testTokenCommaToken() {
        this.parseInvalidCharacterFails("gzip,deflate", ',');
    }

    @Override
    public EncodingWithParameters parse(final String text) {
        return EncodingWithParametersHeaderValueParser.parseEncoding(text);
    }

    @Override
    String valueLabel() {
        return "EncodingWithParameters";
    }

    @Override
    public Class<EncodingWithParametersHeaderValueParser> type() {
        return EncodingWithParametersHeaderValueParser.class;
    }
}
