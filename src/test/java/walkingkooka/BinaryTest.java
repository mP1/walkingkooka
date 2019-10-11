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

package walkingkooka;

import org.junit.jupiter.api.Test;
import walkingkooka.compare.Range;
import walkingkooka.test.HashCodeEqualsDefinedTesting2;
import walkingkooka.test.ToStringTesting;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class BinaryTest implements HashCodeEqualsDefinedTesting2<Binary>,
        ToStringTesting<Binary> {

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> Binary.with(null));
    }

    @Test
    public void testWith() {
        final byte[] value = this.value();
        final Binary binary = Binary.with(value);
        assertArrayEquals(value, binary.value());
        this.checkSize(binary, value.length);
    }

    @Test
    public void testWithCopied() {
        final byte[] value = this.value();
        final Binary binary = Binary.with(value);
        value[0] = (byte) 0xff;

        assertArrayEquals(this.value(), binary.value());
    }

    @Test
    public void testValueCopied() {
        final Binary binary = this.createObject();
        final byte[] value = binary.value();
        value[0] = (byte) 0xff;

        assertArrayEquals(this.value(), binary.value());
    }

    @Test
    public void testWithZeroByteArray() {
        assertSame(Binary.EMPTY, Binary.with(new byte[0]));
    }

    // extract.........................................................................................................

    @Test
    public void testExtractNullRangeFails() {
        assertThrows(NullPointerException.class, () -> this.createObject().extract(null));
    }

    @Test
    public void testExtractInvalidLowerBoundFails() {
        this.extractFails(Range.greaterThan(-2L));
    }

    @Test
    public void testExtractInvalidLowerBoundFails2() {
        this.extractFails(Range.greaterThan(5L));
    }

    @Test
    public void testExtractInvalidUpperBoundFails() {
        this.extractFails(Range.lessThan(7L));
    }

    @Test
    public void testExtractInvalidUpperBoundFails2() {
        this.extractFails(Range.lessThan(8L));
    }

    private void extractFails(final Range<Long> range) {
        this.extractFails(this.createObject(), range);
    }

    private void extractFails(final Binary binary,
                              final Range<Long> range) {
        assertThrows(IllegalArgumentException.class, () -> binary.extract(range));
    }

    @Test
    public void testExtractAll() {
        this.extractAndCheck(this.createObject(), Range.all());
    }

    @Test
    public void testExtractSingleton() {
        this.extractAndCheck(this.createObject(),
                Range.singleton(1L),
                Binary.with(new byte[]{11}));
    }

    @Test
    public void testExtractSame() {
        this.extractAndCheck(this.createObject(),
                Range.greaterThanEquals(0L).and(Range.lessThanEquals(4L)));
    }

    @Test
    public void testExtractSame2() {
        this.extractAndCheck(this.createObject(),
                Range.greaterThanEquals(0L).and(Range.lessThan(5L)));
    }

    @Test
    public void testExtractSame3() {
        this.extractAndCheck(this.createObject(),
                Range.greaterThan(-1L).and(Range.lessThan(5L)));
    }

    @Test
    public void testExtractPartial() {
        this.extractAndCheck(this.createObject(),
                Range.greaterThanEquals(1L).and(Range.lessThanEquals(3L)),
                Binary.with(new byte[]{11, 22, 33}));
    }

    @Test
    public void testExtractPartial2() {
        this.extractAndCheck(this.createObject(),
                Range.greaterThan(1L).and(Range.lessThan(3L)),
                Binary.with(new byte[]{22}));
    }

    @Test
    public void testExtractPartial3() {
        this.extractAndCheck(this.createObject(),
                Range.greaterThan(1L).and(Range.lessThan(4L)),
                Binary.with(new byte[]{22, 33}));
    }

    @Test
    public void testExtractLowerBoundExclusive() {
        this.extractAndCheck(this.createObject(),
                Range.greaterThan(0L),
                Binary.with(new byte[]{11, 22, 33, 44}));
    }

    @Test
    public void testExtractLowerBoundExclusive1() {
        this.extractAndCheck(this.createObject(),
                Range.greaterThan(2L),
                Binary.with(new byte[]{33, 44}));
    }

    @Test
    public void testExtractLowerBoundExclusive2() {
        this.extractAndCheck(this.createObject(),
                Range.greaterThan(-1L));
    }

    @Test
    public void testExtractLowerBoundInclusive() {
        this.extractAndCheck(this.createObject(),
                Range.greaterThanEquals(2L),
                Binary.with(new byte[]{22, 33, 44}));
    }

    @Test
    public void testExtractLowerBoundInclusive2() {
        this.extractAndCheck(this.createObject(),
                Range.greaterThanEquals(0L));
    }

    @Test
    public void testExtractUpperBoundInclusive() {
        this.extractAndCheck(this.createObject(),
                Range.lessThanEquals(1L),
                Binary.with(new byte[]{0, 11}));
    }

    @Test
    public void testExtractUpperBoundInclusive2() {
        this.extractAndCheck(this.createObject(),
                Range.lessThanEquals(3L),
                Binary.with(new byte[]{0, 11, 22, 33}));
    }

    @Test
    public void testExtractUpperBoundInclusive3() {
        this.extractAndCheck(this.createObject(),
                Range.lessThanEquals(this.size() - 1));
    }

    @Test
    public void testExtractUpperBoundExclusive() {
        this.extractAndCheck(this.createObject(),
                Range.lessThan(2L),
                Binary.with(new byte[]{0, 11}));
    }

    @Test
    public void testExtractUpperBoundExclusive2() {
        this.extractAndCheck(this.createObject(),
                Range.lessThan(4L),
                Binary.with(new byte[]{0, 11, 22, 33}));
    }

    @Test
    public void testExtractUpperBoundExclusive3() {
        this.extractAndCheck(this.createObject(),
                Range.lessThan(this.size()));
    }


    private void extractAndCheck(final Binary binary,
                                 final Range<Long> range) {
        assertSame(binary,
                binary.extract(range),
                () -> binary + " extract " + range);
    }

    private void extractAndCheck(final Binary binary,
                                 final Range<Long> range,
                                 final Binary expected) {
        assertEquals(expected,
                binary.extract(range),
                () -> binary + " extract " + range);
    }

    // gzip.........................................................................................................

    @Test
    public void testGzip() throws IOException {
        final int length = 4000;
        final Binary binary = Binary.with(new byte[length]);
        final Binary gzipped = binary.gzip();
        assertNotSame(binary, gzipped);
        assertTrue(gzipped.size() < length, () -> "gzipped " + gzipped.size() + " < " + length);
    }

    // InputStream......................................................................................................

    @Test
    public void testInputStream() throws Exception {
        final byte[] bytes = "ABC123".getBytes(Charset.defaultCharset());

        try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            try (final InputStream input = Binary.with(bytes).inputStream()) {

                for (; ; ) {
                    final int b = input.read();
                    if (-1 == b) {
                        break;
                    }
                    output.write(b);
                }
            }

            assertArrayEquals(bytes, output.toByteArray());
        }
    }

    // equals...........................................................................................................

    @Test
    public void testDifferent() {
        this.checkNotEquals(Binary.with(new byte[]{4, 5, 6}));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createObject(), Arrays.toString(this.value()));
    }

    @Override
    public Binary createObject() {
        return Binary.with(this.value());
    }

    private byte[] value() {
        return new byte[]{0, 11, 22, 33, 44};
    }

    private long size() {
        return this.value().length;
    }

    private void checkSize(final Binary binary, final int size) {
        assertEquals(size, binary.size(), "size");
    }

    @Override
    public Class<Binary> type() {
        return Binary.class;
    }
}
