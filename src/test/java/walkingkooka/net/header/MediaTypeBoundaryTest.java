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
import walkingkooka.InvalidCharacterException;
import walkingkooka.InvalidTextLengthException;
import walkingkooka.ToStringBuilder;
import walkingkooka.compare.ComparableTesting;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.JavaVisibility;

import java.util.Arrays;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class MediaTypeBoundaryTest extends HeaderValueTestCase<MediaTypeBoundary>
        implements ComparableTesting<MediaTypeBoundary>,
        ParseStringTesting<MediaTypeBoundary> {

    // with ................................................................................................

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            MediaTypeBoundary.with(null);
        });
    }

    @Test
    public void testWithEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaTypeBoundary.with("");
        });
    }

    @Test
    public void testWithInvalidCharacterFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            MediaTypeBoundary.with("abc\"def");
        });
    }

    @Test
    public void testWithTooLongFails() {
        final char[] c = new char[MediaTypeBoundary.MAX_LENGTH];
        Arrays.fill(c, 'a');
        assertThrows(InvalidTextLengthException.class, () -> {
            MediaTypeBoundary.with(new String(c));
        });
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

    private void checkValue(final MediaTypeBoundary boundary, final String value) {
        assertEquals(value, boundary.value(), "value");
        assertEquals("--" + value, boundary.multipartBoundaryDelimiter(), "delimiter");
    }

    @Test
    public void testLess() {
        this.compareToAndCheckLess(MediaTypeBoundary.with("qrst"));
    }

    // Comparable ..................................................................................................

    @Test
    public void testDifferent() {
        this.checkNotEquals(MediaTypeBoundary.with("different"));
    }

    @Test
    public void testDifferentCase() {
        this.checkNotEquals(MediaTypeBoundary.with("ABC123"));
    }

    // parse........................................................................................................

    @Test
    public void testParseUnquoted() {
        this.parseAndCheck("abc", MediaTypeBoundary.with("abc"));
    }

    @Test
    public void testParseQuoted() {
        this.parseAndCheck("\"abc\"", MediaTypeBoundary.with("abc"));
    }

    // toHeaderText........................................................................................................

    @Test
    public void testToHeaderText() {
        this.toHeaderTextAndCheck(MediaTypeBoundary.with("abc"), "abc");
    }

    @Test
    public void testToHeaderTextQuotesRequired() {
        this.toHeaderTextAndCheck(MediaTypeBoundary.with("gc0pJq0M:08jU534c0p"),
                "\"gc0pJq0M:08jU534c0p\"");
    }

    @Test
    public void testIsWildcard() {
        this.isWildcardAndCheck(false);
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
        this.multipartBoundaryDelimiterAndCheck(MediaTypeBoundary.with("abc+def"),
                "--abc+def");
    }

    private void multipartBoundaryDelimiterAndCheck(final MediaTypeBoundary boundary, final String delimiter) {
        assertEquals(delimiter, boundary.multipartBoundaryDelimiter(), () -> boundary.toString());
    }

    // generate ....................................................................................................

    @Test
    public void testGenerateNullBodyFails() {
        assertThrows(NullPointerException.class, () -> {
            MediaTypeBoundary.generate(null, () -> Byte.MAX_VALUE);
        });
    }

    @Test
    public void testGenerateNullBoundaryCharacterFails() {
        assertThrows(NullPointerException.class, () -> {
            MediaTypeBoundary.generate(new byte[0], null);
        });
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
        final Supplier<Byte> boundaryCharcters = new Supplier<Byte>() {

            @Override
            public Byte get() {
                return (byte) new String(MediaTypeBoundary.BOUNDARY_CHARACTERS)
                        .indexOf(randomSource.charAt(i++));
            }

            int i = 0;
        };

        final MediaTypeBoundary mediaTypeBoundary = MediaTypeBoundary.generate0(body,
                boundaryCharcters,
                boundary.length());

        assertEquals(MediaTypeBoundary.with(boundary),
                mediaTypeBoundary,
                "Incorrect boundary generated for " + ToStringBuilder.empty().value(body).build());
    }

    // multipartByteRanges........................................................................................................

    @Test
    public void testMultipartByteRanges() {
        final MediaTypeBoundary boundary = MediaTypeBoundary.with("abc");
        final MediaType mediaType = boundary.multipartByteRanges();
        assertEquals("multipart/byteranges; boundary=abc", mediaType.toString());
    }

    // toString........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(MediaTypeBoundary.with("abc"), "abc");
    }

    @Test
    public void testToString2() {
        this.toStringAndCheck(MediaTypeBoundary.with("--abc"), "--abc");
    }

    // helpers........................................................................................................

    @Override
    public MediaTypeBoundary createHeaderValue() {
        return MediaTypeBoundary.with("abc123");
    }

    @Override
    public boolean isMultipart() {
        return true;
    }

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    @Override
    public Class<MediaTypeBoundary> type() {
        return MediaTypeBoundary.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public MediaTypeBoundary createComparable() {
        return this.createHeaderValue();
    }

    @Override
    public MediaTypeBoundary createObject() {
        return this.createHeaderValue();
    }

    // ParseStringTesting ........................................................................................

    @Override
    public MediaTypeBoundary parse(final String text) {
        return MediaTypeBoundary.parse(text);
    }
}
