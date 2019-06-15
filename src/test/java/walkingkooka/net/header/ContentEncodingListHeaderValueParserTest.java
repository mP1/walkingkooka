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

import java.util.List;

public final class ContentEncodingListHeaderValueParserTest extends HeaderValueParserTestCase<ContentEncodingListHeaderValueParser, List<ContentEncoding>> {

    @Test
    public void testWhitespaceFails() {
        this.parseMissingValueFails("  ");
    }

    @Test
    public void testCommentFails() {
        this.parseCommentFails("(abc)", 0);
    }

    @Test
    public void testParametersFails() {
        this.parseInvalidCharacterFails("gzip;x=1", ';');
    }

    @Test
    public void testQuotedTextFails() {
        this.parseInvalidCharacterFails("\"hello\"", 0);
    }

    @Test
    public void testSlashFails() {
        this.parseInvalidCharacterFails("ab/c", '/');
    }

    @Test
    public void testWildcardFails() {
        this.parseInvalidCharacterFails("*", '*');
    }

    @Test
    public void testTokenCommaWildcardFails() {
        this.parseInvalidCharacterFails("abc, *, def", '*');
    }

    @Test
    public void testTokenCommentFails() {
        this.parseCommentFails("gzip(abc)", 4);
    }

    @Test
    public void testTokenSeparatorFails() {
        this.parseInvalidCharacterFails("abc;", ';');
    }

    @Test
    public void testToken() {
        this.parseAndCheck2("gzip",
                ContentEncoding.GZIP);
    }

    @Test
    public void testTokenWhitespace() {
        this.parseAndCheck2("gzip ",
                ContentEncoding.GZIP);
    }

    @Test
    public void testWhitespaceToken() {
        this.parseAndCheck2(" gzip",
                ContentEncoding.GZIP);
    }

    @Test
    public void testTokenCommaToken() {
        this.parseAndCheck2("gzip,deflate",
                ContentEncoding.GZIP,
                ContentEncoding.with("deflate"));
    }

    @Test
    public void testTokenWhitespaceCommaWhitespaceTokenCommaWhitespaceToken() {
        this.parseAndCheck2("gzip, deflate,  br",
                ContentEncoding.GZIP,
                ContentEncoding.DEFLATE,
                ContentEncoding.BR);
    }

    private void parseAndCheck2(final String text, final ContentEncoding...encodings) {
        this.parseAndCheck(text, Lists.of(encodings));
    }

    @Override
    public List<ContentEncoding> parse(final String text) {
        return ContentEncodingListHeaderValueParser.parseContentEncodingList(text);
    }

    @Override
    String valueLabel() {
        return HttpHeaderName.CONTENT_ENCODING.toString();
    }

    @Override
    public Class<ContentEncodingListHeaderValueParser> type() {
        return ContentEncodingListHeaderValueParser.class;
    }
}
