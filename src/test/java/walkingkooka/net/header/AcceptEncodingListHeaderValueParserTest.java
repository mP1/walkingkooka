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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;

import java.util.List;

public final class AcceptEncodingListHeaderValueParserTest extends HeaderValueParserTestCase<AcceptEncodingListHeaderValueParser, List<AcceptEncoding>> {

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
                AcceptEncoding.GZIP);
    }

    @Test
    public void testTokenWhitespace() {
        this.parseAndCheck2("gzip ",
                AcceptEncoding.GZIP);
    }

    @Test
    public void testWhitespaceToken() {
        this.parseAndCheck2(" gzip",
                AcceptEncoding.GZIP);
    }

    @Test
    public void testTokenParameter() {
        this.parseAndCheck2("gzip;q=0.5",
                AcceptEncoding.GZIP.setParameters(Maps.of(AcceptEncodingParameterName.Q_FACTOR, 0.5f)));
    }

    @Test
    public void testTokenParameterSemiParameter() {
        this.parseAndCheck2("gzip;q=0.5;abc=xyz",
                AcceptEncoding.GZIP.setParameters(Maps.of(AcceptEncodingParameterName.Q_FACTOR, 0.5f,
                        AcceptEncodingParameterName.with("abc"), "xyz")));
    }

    @Test
    public void testWildcard() {
        this.parseAndCheck2("*",
                AcceptEncoding.WILDCARD_ACCEPT_ENCODING);
    }

    @Test
    public void testWildcardParameter() {
        this.parseAndCheck2("*;q=0.5",
                AcceptEncoding.WILDCARD_ACCEPT_ENCODING.setParameters(Maps.of(AcceptEncodingParameterName.Q_FACTOR, 0.5f)));
    }

    @Test
    public void testTokenCommaToken() {
        this.parseAndCheck2("gzip,deflate",
                AcceptEncoding.GZIP,
                AcceptEncoding.DEFLATE);
    }

    @Test
    public void testTokenCommaWildcard() {
        this.parseAndCheck2("gzip,*",
                AcceptEncoding.GZIP,
                AcceptEncoding.WILDCARD_ACCEPT_ENCODING);
    }

    @Test
    public void testWildcardCommaToken() {
        this.parseAndCheck2("*,gzip",
                AcceptEncoding.WILDCARD_ACCEPT_ENCODING,
                AcceptEncoding.GZIP);
    }

    @Test
    public void testTokenWhitespaceCommaWhitespaceTokenCommaWhitespaceToken() {
        this.parseAndCheck2("gzip, deflate,  br",
                AcceptEncoding.GZIP,
                AcceptEncoding.DEFLATE,
                AcceptEncoding.BR);
    }

    private void parseAndCheck2(final String text, final AcceptEncoding... encodings) {
        this.parseAndCheck(text, Lists.of(encodings));
    }

    @Override
    public List<AcceptEncoding> parse(final String text) {
        return AcceptEncodingListHeaderValueParser.parseAcceptEncodingList(text);
    }

    @Override
    String valueLabel() {
        return HttpHeaderName.ACCEPT_ENCODING.toString();
    }

    @Override
    public Class<AcceptEncodingListHeaderValueParser> type() {
        return AcceptEncodingListHeaderValueParser.class;
    }
}
