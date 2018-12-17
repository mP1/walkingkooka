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
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.net.http.HttpHeaderScope;
import walkingkooka.text.CharSequences;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
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
        assertEquals("delimiter", "--" + value, boundary.multipartBoundaryDelimiter());
        assertArrayEquals("mediaTypeBoundaryBytes",
                ("--" + value).getBytes(CharsetName.UTF_8.charset().get()),
                boundary.multipartBoundaryDelimiterBytes());
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

    // generate ....................................................................................................

    @Test(expected = NullPointerException.class)
    public void testGenerate() {
        MediaTypeBoundary.generate(null);
    }

    @Test
    public void testGenerateNoClash() {
        this.generateAndCheck(new byte[20], "ABCDEF", "ABCDEF");
    }

    @Test
    public void testGenerateRetry() {
        this.generateAndCheck("ABCD", "ABCDEF", "DEF");
    }

    @Test
    public void testGenerateRetry2() {
        this.generateAndCheck("1ABC23", "ABCDEF", "DEF");
    }

    @Test
    public void testGenerateRetry3() {
        this.generateAndCheck("1ABC2DEF", "ABCDEFGHI", "GHI");
    }

    private void generateAndCheck(final String body, final String randomSource, final String boundary) {
        this.generateAndCheck(body.getBytes(CharsetName.UTF_8.charset().get()),
                randomSource,
                boundary);
    }

    private void generateAndCheck(final byte[] body, final String randomSource, final String boundary) {
        final Random random = new Random() {

            static final long serialVersionUID = 1L;

            @Override
            public int nextInt(final int bound) {
                return new String(MediaTypeBoundary.BOUNDARY_CHARACTERS)
                        .indexOf(randomSource.charAt(i++));
            }

            int i = 0;
        };

        final MediaTypeBoundary mediaTypeBoundary = MediaTypeBoundary.generate0(body, random, boundary.length());

        assertEquals("Incorrected boundary generated for " + ToStringBuilder.create().value(body).build(),
                MediaTypeBoundary.with(boundary),
                mediaTypeBoundary);
        assertArrayEquals(mediaTypeBoundary.toString(),
                ("--" + boundary).getBytes(CharsetName.UTF_8.charset().get()),
                mediaTypeBoundary.multipartBoundaryDelimiterBytes());
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
