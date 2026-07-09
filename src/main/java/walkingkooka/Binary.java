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

import javaemul.internal.annotations.GwtIncompatible;
import walkingkooka.collect.Range;
import walkingkooka.text.Ascii;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;

/**
 * A {@link HasValue} that holds a byte array.
 */
public final class Binary implements HasValue<byte[]>,
    CanBeEmpty,
    HasBinary {

    /**
     * A {@link Binary} with zero bytes.
     */
    public final static Binary EMPTY = new Binary(new byte[0]);

    public static Binary with(final byte[] value) {
        Objects.requireNonNull(value, "value");

        return value.length == 0 ?
            EMPTY :
            new Binary(
                Arrays.copyOf(
                    value,
                    value.length
                )
            );
    }

    private Binary(final byte[] value) {
        super();
        this.value = value;
    }

    @Override
    public byte[] value() {
        return Arrays.copyOf(
            this.value,
            this.value.length
        );
    }

    // BinaryRangeVisitor
    private final byte[] value;

    /**
     * The size or number of bytes in this {@link Binary}
     */
    public int size() {
        return this.value.length;
    }

    /**
     * Attempts to find the starting index of the given bytes within this binary, returning the index of any match with -1 meaning it was not found.
     * This is useful for operations such as finding a multi-part boundary with a Binary.
     */
    public int indexOf(final byte[] bytes,
                       final int start,
                       final int end) {
        Objects.requireNonNull(bytes, "bytes");

        final byte[] binary = this.value;
        final int binaryLength = binary.length;
        if (start < 0 || start > binaryLength) {
            throw new IllegalArgumentException("Got " + start + " not within 0 and " + binaryLength);
        }
        if (end < start || end > binaryLength) {
            throw new IllegalArgumentException("Got " + end + " not within " + start + " and " + binaryLength);
        }

        int found = -1;

        final int findLength = bytes.length;

        // $find cannot be found if its longer than value.length
        if (findLength > 0 && findLength <= binary.length) {
            final byte[] copy = Arrays.copyOf(
                bytes,
                findLength
            );

            final int last = end - (findLength - 1);
            final byte firstByte = copy[0];

            for (int i = start; i < last; i++) {
                if (firstByte == binary[i]) {
                    found = i;

                    for (int j = 1; j < findLength; j++) {
                        if (copy[j] != binary[i + j]) {
                            found = -1;
                            break;
                        }
                    }

                    // found!
                    if (-1 != found) {
                        break;
                    }
                }
            }
        }

        return found;
    }

    /**
     * Extracts a {@link Binary} that matches the given {@link Range}.
     */
    public Binary extract(final Range<Long> range) {
        return BinaryRangeVisitor.extract(
            this,
            range
        );
    }

    /**
     * Extracts and returns a {@link Binary} that matches the given bounds, returning this if possible.
     */
    // BinaryRangeVisitor
    Binary extract0(final int lower,
                    final int upper,
                    final Range<Long> range) {
        final int size = this.size();
        if (lower < 0 || upper > size) {
            throw new IllegalArgumentException("Range out of bounds " + range + " for binary with size: " + size);
        }
        return 0 == lower && size == upper ?
            this :
            new Binary(Arrays.copyOfRange(
                this.value,
                lower,
                upper
            )
            );
    }

    /**
     * Returns a {@link Binary} with its contents gzipped.
     */
    @GwtIncompatible
    public Binary gzip() throws IOException {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            try (final GZIPOutputStream gzip = new GZIPOutputStream(outputStream)) {
                gzip.write(this.value);
                gzip.flush();
            }
            return new Binary(outputStream.toByteArray());
        }
    }

    /**
     * Returns a {@link InputStream} which may be used to read the bytes inside this {@link Binary}
     */
    public InputStream inputStream() {
        return new ByteArrayInputStream(this.value);
    }

    /**
     * Builds a {@link String} using the given {@link Charset}.
     */
    public String text(final Charset charset) {
        return new String(
            this.value,
            Objects.requireNonNull(charset, "charset")
        );
    }

    // CanBeEmpty.......................................................................................................

    @Override
    public boolean isEmpty() {
        return 0 == this.size();
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.value);
    }

    public boolean equals(final Object other) {
        return this == other ||
            other instanceof Binary &&
                this.equals0((Binary) other);
    }

    private boolean equals0(final Binary other) {
        return Arrays.equals(
            this.value,
            other.value
        );
    }

    /**
     * Dumps two bytes in a row, the left holding the hex bytes separated by spaces and the right holding the ascii characters.
     * <pre>
     * 41 42 43 44 45 46 47 48 49 4a 4b 4c 4d 4e 4f 50 51 52 53 54 ABCDEFGHIJKLMNOPQRST
     * </pre>
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();

        final byte[] bytes = this.value;
        final int length = bytes.length;

        int i = 0;
        while (i < length) {
            final int rowCount = 20;

            // hex bytes
            // 12 SPACE 34 SPACE
            for (int j = 0; j < rowCount; j++) {
                final int k = i + j;

                if (k < length) {
                    final int b = 0xff & bytes[k];

                    buffer.append(
                        b < 0x10 ?
                            ' ' :
                            TO_HEX[b >> 4]
                    ).append(
                        TO_HEX[0xf & b]
                    );
                } else {
                    buffer.append("  ");
                }
                buffer.append(' ');
            }

            for (int j = 0; j < rowCount; j++) {
                final int k = i + j;

                if (k < length) {
                    final char c = (char)(0xff & bytes[k]);
                    if(Ascii.isPrintable(c)) {
                        buffer.append(c);
                    } else {
                        buffer.append('?');
                    }
                } else {
                    buffer.append(' ');
                }
            }

            i = i + rowCount;
            buffer.append('\n');
        }

        return buffer.toString();
    }

    private final static char[] TO_HEX = "0123456789abcdef".toCharArray();

    // HasBinary........................................................................................................

    @Override
    public Binary binary() {
        return this;
    }
}
