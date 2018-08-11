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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.test.PackagePrivateStaticHelperTestCase;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

final public class BigEndianHexByteReaderTest
        extends PackagePrivateStaticHelperTestCase<BigEndianHexByteReader> {

    @Test
    public void testNullFails() {
        this.withFails(null);
    }

    @Test
    public void testOddNumberOfDigitsFails() {
        this.withFails("1", BigEndianHexByteReader.oddNumberOfDigits(1));
    }

    @Test
    public void testOddNumberOfDigits2Fails() {
        this.withFails("12345", BigEndianHexByteReader.oddNumberOfDigits(5));
    }

    @Test
    public void testIllegalCharacterFails() {
        final String hexDigits = "123X56";
        this.withFails(hexDigits,
                BigEndianHexByteReader.nonHexDigit(hexDigits, 'X', hexDigits.indexOf('X')));
    }

    private void withFails(final String hexDigits) {
        this.withFails(hexDigits, null);
    }

    private void withFails(final String hexDigits, final String message) {
        try {
            BigEndianHexByteReader.read(hexDigits);
            Assert.fail();
        } catch (final RuntimeException expected) {
            if (null != message) {
                assertEquals("message", message, expected.getMessage());
            }
        }
    }

    @Test
    public void testEmpty() {
        this.read("", new byte[0]);
    }

    @Test
    public void testTwoDigits() {
        this.read("1F", new byte[]{0x1f});
    }

    @Test
    public void testTwoDigits2() {
        this.read("1f", new byte[]{0x1f});
    }

    @Test
    public void testTwoDigitsLeadingZero() {
        this.read("01", new byte[]{0x01});
    }

    @Test
    public void testManyDigits() {
        this.read("010203", new byte[]{1, 2, 3});
    }

    @Test
    public void testManyDigits2() {
        this.read("1234567890abcdef",
                new byte[]{0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xab, (byte) 0xcd,
                        (byte) 0xef});
    }

    private void read(final String hexDigits, final byte[] expected) {
        final byte[] bytes = BigEndianHexByteReader.read(hexDigits);
        if (false == Arrays.equals(expected, bytes)) {
            assertEquals("from " + CharSequences.quote(hexDigits),
                    Arrays.toString(expected),
                    Arrays.toString(bytes));
        }
    }

    @Override
    protected Class<BigEndianHexByteReader> type() {
        return BigEndianHexByteReader.class;
    }
}
