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
 */

package walkingkooka.text;

import walkingkooka.type.PackagePrivateStaticHelper;

import java.util.Objects;

/**
 * Contains utilities that reads and returns an array of bytes from a {@link CharSequence}.
 */
final class BigEndianHexByteReader implements PackagePrivateStaticHelper {

    /**
     * Reads a {@link CharSequence} that contains pairs of hex digits representing individual bytes
     * which are returned in a byte[].
     */
    static byte[] read(final CharSequence hexDigits) {
        Objects.requireNonNull(hexDigits, "hexDigits");

        final int length = hexDigits.length();
        final int halfLength = length / 2;
        if (length != (halfLength + halfLength)) {
            throw new IllegalArgumentException(BigEndianHexByteReader.oddNumberOfDigits(length));
        }

        final byte[] bytes = new byte[halfLength];

        int value = 0;
        boolean lo = true;
        int index = 0;

        for (int i = 0; i < length; i++) {
            final char c = hexDigits.charAt(i);
            final int digit = Character.digit(c, 16);
            if (digit < 0) {
                throw new IllegalArgumentException(BigEndianHexByteReader.nonHexDigit(hexDigits,
                        c,
                        i));
            }
            if (lo) {
                value = digit;
                lo = false;
                continue;
            }

            bytes[index] = (byte) ((value * 16) + digit);
            index++;
            lo = true;
        }
        return bytes;
    }

    static String oddNumberOfDigits(final int length) {
        return "Expected even number of hex digits=" + length;
    }

    static String nonHexDigit(final CharSequence hexDigits, final char c, final int at) {
        return "Non hex digit '" + CharSequences.escape(String.valueOf(c)) + "' at " + at;
    }

    /**
     * Stop creation
     */
    private BigEndianHexByteReader() {
        throw new UnsupportedOperationException();
    }
}
