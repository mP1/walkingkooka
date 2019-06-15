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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;

public final class AcceptEncodingHeaderValueParserTest extends HeaderValueParserTestCase<AcceptEncodingHeaderValueParser, AcceptEncoding> {

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
        this.parseAndCheck2("gzip",
                Encoding.GZIP);
    }

    @Test
    public void testTokenWhitespace() {
        this.parseAndCheck2("gzip ",
                Encoding.GZIP);
    }

    @Test
    public void testWhitespaceToken() {
        this.parseAndCheck2(" gzip",
                Encoding.GZIP);
    }

    @Test
    public void testTokenParameter() {
        this.parseAndCheck2("gzip;q=0.5",
                Encoding.GZIP.setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f)));
    }

    @Test
    public void testTokenParameterSemiParameter() {
        this.parseAndCheck2("gzip;q=0.5;abc=xyz",
                Encoding.GZIP.setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f,
                        EncodingParameterName.with("abc"), "xyz")));
    }

    @Test
    public void testWildcard() {
        this.parseAndCheck2("*",
                Encoding.WILDCARD_ENCODING);
    }

    @Test
    public void testWildcardParameter() {
        this.parseAndCheck2("*;q=0.5",
                Encoding.WILDCARD_ENCODING.setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f)));
    }

    @Test
    public void testTokenCommaToken() {
        this.parseAndCheck2("gzip,deflate",
                Encoding.GZIP,
                Encoding.DEFLATE);
    }

    @Test
    public void testTokenCommaWildcard() {
        this.parseAndCheck2("gzip,*",
                Encoding.GZIP,
                Encoding.WILDCARD_ENCODING);
    }

    @Test
    public void testWildcardCommaToken() {
        this.parseAndCheck2("*,gzip",
                Encoding.WILDCARD_ENCODING,
                Encoding.GZIP);
    }

    @Test
    public void testTokenWhitespaceCommaWhitespaceTokenCommaWhitespaceToken() {
        this.parseAndCheck2("gzip, deflate,  br",
                Encoding.GZIP,
                Encoding.DEFLATE,
                Encoding.BR);
    }

    @Test
    public void testSortedByQFactor() {
        this.parseAndCheck2("gzip;q=0.8, deflate, br;q=0.9",
                Encoding.DEFLATE,
                Encoding.BR.setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.9f)),
                Encoding.GZIP.setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.8f)));
    }

    @Test
    public void testSortedByQFactor2() {
        this.parseAndCheck2("gzip;q=0.8, deflate;q=1.0",
                Encoding.DEFLATE.setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 1.0f)),
                Encoding.GZIP.setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.8f)));
    }

    private void parseAndCheck2(final String text,
                                final Encoding... encodings) {
        this.parseAndCheck(text, AcceptEncoding.with(Lists.of(encodings)));
    }

    @Override
    public AcceptEncoding parse(final String text) {
        return AcceptEncodingHeaderValueParser.parseAcceptEncoding(text);
    }

    @Override
    String valueLabel() {
        return HttpHeaderName.ACCEPT_ENCODING.toString();
    }

    @Override
    public Class<AcceptEncodingHeaderValueParser> type() {
        return AcceptEncodingHeaderValueParser.class;
    }
}
