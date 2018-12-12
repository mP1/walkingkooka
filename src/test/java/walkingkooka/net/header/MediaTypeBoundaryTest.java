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
import walkingkooka.net.http.HttpHeaderScope;
import walkingkooka.text.CharSequences;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class MediaTypeBoundaryTest extends HeaderValueTestCase<MediaTypeBoundary> {

    // with ................................................................................................

    @Test(expected = NullPointerException.class)
    public void testWithNullFails() {
        MediaTypeBoundary.with(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyFails() {
        MediaTypeBoundary.with("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidCharacterFails() {
        MediaTypeBoundary.with("abc\"def");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTooLongFails() {
        final char[] c = new char[MediaTypeBoundary.MAX_LENGTH];
        Arrays.fill(c, 'a');
        MediaTypeBoundary.with(new String(c));
    }

    @Test
    public void testWithEndsSpaceRemoved() {
        this.checkValue(MediaTypeBoundary.with("abcdef "), "abcdef");
    }

    @Test
    public void testWith() {
        final String text = "abc123";
        checkValue(MediaTypeBoundary.with(text), text);
    }

    @Test
    public void testHeaderScope() {
        assertSame(HttpHeaderScope.REQUEST_RESPONSE,
                MediaTypeBoundary.with("abc123")
                        .httpHeaderScope());
    }

    // parse........................................................................................................

    @Test
    public void testParseUnquoted() {
        this.parseAndCheck("abc",
                MediaTypeBoundary.with("abc"));
    }

    @Test
    public void testParseQuoted() {
        this.parseAndCheck("\"abc\"",
                MediaTypeBoundary.with0("abc",
                        "\"abc\""));
    }

    private void parseAndCheck(final String text, final
    MediaTypeBoundary boundary) {
        assertEquals("parse " + CharSequences.quote(text), boundary, MediaTypeBoundary.parse(text));
    }

    private void checkValue(final MediaTypeBoundary boundary, final String value) {
        assertEquals("value", value, boundary.value());
    }

    // toHeaderText........................................................................................................

    @Test
    public void testToHeaderText() {
        this.toHeaderTextAndCheck(MediaTypeBoundary.with("abc"), "abc");
    }

    @Test
    public void testToHeaderTextQuoted() {
        this.toHeaderTextAndCheck(MediaTypeBoundary.with0("abc", "\"abc\""), "\"abc\"");
    }

    @Test
    public void testToHeaderTextQuotesRequired() {
        this.toHeaderTextAndCheck(MediaTypeBoundary.with("gc0pJq0M:08jU534c0p"),
                "\"gc0pJq0M:08jU534c0p\"");
    }

    // multipartBoundaryText........................................................................................................

    @Test
    public void testMultipartBoundaryDelimiter() {
        this.multipartBoundaryDelimiterAndCheck(MediaTypeBoundary.with("abc"),
                "--abc");
    }

    @Test
    public void testMultipartBoundaryDelimiter2() {
        this.multipartBoundaryDelimiterAndCheck(MediaTypeBoundary.with("--abc"),
                "----abc");
    }

    @Test
    public void testMultipartBoundaryDelimiterQuoted() {
        this.multipartBoundaryDelimiterAndCheck(MediaTypeBoundary.with0("abc", "\"abc\""),
                "--abc");
    }

    private void multipartBoundaryDelimiterAndCheck(final MediaTypeBoundary boundary, final String delimiter) {
        assertEquals(boundary.toString(), delimiter, boundary.multipartBoundaryDelimiter());
    }

    // toString........................................................................................................

    @Test
    public void testToString() {
        assertEquals("abc", MediaTypeBoundary.with("abc").toString());
    }

    @Test
    public void testToString2() {
        assertEquals("--abc", MediaTypeBoundary.with("--abc").toString());
    }

    // helpers........................................................................................................

    @Override
    protected MediaTypeBoundary createHeaderValue() {
        return MediaTypeBoundary.with("abc123");
    }

    @Override
    protected HttpHeaderScope httpHeaderScope() {
        return HttpHeaderScope.REQUEST_RESPONSE;
    }

    @Override
    protected Class<MediaTypeBoundary> type() {
        return MediaTypeBoundary.class;
    }
}
