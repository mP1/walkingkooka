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

package walkingkooka.net;

import junit.framework.Assert;
import org.junit.Test;
import walkingkooka.test.TestCase;
import walkingkooka.text.CharSequences;

import java.util.Arrays;

public final class HostAddressIp6Test extends TestCase {

    @Test
    public void testOnlyColonFails() {
        this.parseFails(":", HostAddressIncompleteProblem.INSTANCE);
    }

    @Test
    public void testStartsWithColonFails() {
        this.parseFails(":2:3:4:5:6:7:8", HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testEndsWithColonFails() {
        this.parseFails("1:2:3:4:5:6:7:", HostAddressIncompleteProblem.INSTANCE);
    }

    @Test
    public void testFirstGroupStartsWithInvalidCharacterFails() {
        this.parseFails("!1:2:3:4:5:6:7:8", HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testFirstGroupIncludesInvalidCharacterFails() {
        this.parseFails("1!:2:3:4:5:6:7:8", HostAddressInvalidCharacterProblem.with(1));
    }

    @Test
    public void testFirstGroupIncludesInvalidValueFails() {
        this.parseFails("12345:2:3:4:5:6:7:8", HostAddressInvalidValueProblem.with(0));
    }

    @Test
    public void testFirstGroupIncludesInvalidValueTooManyZeroesFails() {
        this.parseFails("00000:2:3:4:5:6:7:8", HostAddressInvalidValueProblem.with(0));
    }

    @Test
    public void testSecondGroupStartsWithInvalidCharacterFails() {
        this.parseFails("1:!2:3:4:5:6:7:8", HostAddressInvalidCharacterProblem.with(2));
    }

    @Test
    public void testSecondGroupIncludesInvalidCharacterFails() {
        this.parseFails("1:2!:3:4:5:6:7:8", HostAddressInvalidCharacterProblem.with(3));
    }

    @Test
    public void testSecondGroupIncludesInvalidValueFails() {
        this.parseFails("1:23456:3:4:5:6:7:8", HostAddressInvalidValueProblem.with(2));
    }

    @Test
    public void testSecondGroupIncludesInvalidValueTooManyZeroesFails() {
        this.parseFails("1:00000:3:4:5:6:7:8", HostAddressInvalidValueProblem.with(2));
    }

    @Test
    public void testLastGroupStartsWithInvalidCharacterFails() {
        this.parseFails("1:2:3:4:5:6:7:!", HostAddressInvalidCharacterProblem.with(14));
    }

    @Test
    public void testLastGroupIncludesInvalidCharacterFails() {
        this.parseFails("1:2:3:4:5:6:7:8!", HostAddressInvalidCharacterProblem.with(14 + 1));
    }

    @Test
    public void testLastGroupIncludesInvalidValueFails() {
        this.parseFails("1:2:3:4:5:6:7:89ABC", HostAddressInvalidValueProblem.with(14));
    }

    @Test
    public void testLastGroupIncludesInvalidValueTooManyZeroesFails() {
        this.parseFails("1:2:3:4:5:6:7:00000", HostAddressInvalidValueProblem.with(14));
    }

    @Test
    public void testTooManyGroupsFails() {
        final String address = "1:2:3:4:5:6:7:8:9";
        this.parseFails(address, HostAddressInvalidCharacterProblem.with(address.indexOf('9') - 1));
    }

    @Test
    public void testWithoutEmpty() {
        this.parseAndCheck("1:2:3:4:5:6:7:8", "00010002000300040005000600070008");
    }

    @Test
    public void testWithoutEmpty2() {
        this.parseAndCheck("1122:3344:5566:7788:99aa:bbcc:ddee:ff01", "112233445566778899aabbccddeeff01");
    }

    @Test
    public void testWithoutEmptyAndStartAndEnd() {
        final String address = "!1:2:3:4:5:6:7:8!";
        this.parseAndCheck(address, 1, address.length() - 1, "00010002000300040005000600070008");
    }

    @Test
    public void testMoreThanOneEmptyFails() {
        final String address = "1::3:4::6:7:8";
        this.parseFails(address, HostAddressInvalidCharacterProblem.with(address.indexOf('6') - 1));
    }

    @Test
    public void testStartsWithEmpty() {
        this.parseAndCheck("::2:3:4:5:6:7:8", "00000002000300040005000600070008");
    }

    @Test
    public void testStartsWithEmpty2() {
        this.parseAndCheck("::3:4:5:6:7:8", "00000000000300040005000600070008");
    }

    @Test
    public void testStartsWithEmpty3() {
        this.parseAndCheck("::4:5:6:7:8", "00000000000000040005000600070008");
    }

    @Test
    public void testStartsWithEmpty4() {
        this.parseAndCheck("::8", "00000000000000000000000000000008");
    }

    @Test
    public void testMiddleEmpty() {
        this.parseAndCheck("1:2:3::5:6:7:8", "00010002000300000005000600070008");
    }

    @Test
    public void testMiddleEmpty2() {
        this.parseAndCheck("1:2:3::6:7:8", "00010002000300000000000600070008");
    }

    @Test
    public void testMiddleEmpty3() {
        this.parseAndCheck("1:2:3::7:8", "00010002000300000000000000070008");
    }

    @Test
    public void testEndsWithEmpty() {
        this.parseAndCheck("1:2:3:4:5:6:7::", "00010002000300040005000600070000");
    }

    @Test
    public void testEndsWithEmpty2() {
        this.parseAndCheck("1:2:3:4:5:6::", "00010002000300040005000600000000");
    }

    @Test
    public void testEndsWithEmpty3() {
        this.parseAndCheck("1:2:3:4:5::", "00010002000300040005000000000000");
    }

    @Test
    public void testEndsWithEmpty4() {
        this.parseAndCheck("1::", "00010000000000000000000000000000");
    }

    @Test
    public void testOnlyEmpty() {
        this.parseAndCheck("::", "00000000000000000000000000000000");
    }

    @Test
    public void testIp4WithInvalidValueFails() {
        final String address = "1:2:3:4:5:6:7.8.900.1";
        this.parseFails(address, HostAddressInvalidValueProblem.with(address.indexOf('9')));
    }

    @Test
    public void testIp4WithColonFails() {
        final String address = "1:2:3:4:5:6:7.8:9.0";
        this.parseFails(address, HostAddressInvalidCharacterProblem.with(address.indexOf('8') + 1));
    }

    @Test
    public void testIp4BeforeTooFewGroupsFails() {
        final String address = "1:2:3.4.5.6";
        this.parseFails(address, HostAddressInvalidCharacterProblem.with(address.indexOf('.')));
    }

    @Test
    public void testIp4BeforeTooManyGroupsFails() {
        final String address = "1:2:3:4:5:6:7:1.2.3.4";
        this.parseFails(address, HostAddressInvalidCharacterProblem.with(address.indexOf('.')));
    }

    @Test
    public void testIp4StartsWithEmpty() {
        this.parseAndCheck("::1:2:3:4:5:6.7.8.9", "00000001000200030004000506070809");
    }

    @Test
    public void testIp4StartsWithEmpty2() {
        this.parseAndCheck("::1:2:3:5.6.7.8", "00000000000000010002000305060708");
    }

    @Test
    public void testIp4StartsWithEmpty3() {
        this.parseAndCheck("::1:2:5.6.7.8", "00000000000000000001000205060708");
    }

    @Test
    public void testIp4StartsWithEmpty4() {
        this.parseAndCheck("::1:5.6.7.8", "00000000000000000000000105060708");
    }

    @Test
    public void testIp4StartsWithOnlyEmpty() {
        this.parseAndCheck("::5.6.7.8", "00000000000000000000000005060708");
    }

    @Test
    public void testIp4WithMiddleEmpty1() {
        this.parseAndCheck("1:2:3:4:5::255.1.2.3", "000100020003000400050000ff010203");
    }

    @Test
    public void testIp4WithMiddleEmpty2() {
        this.parseAndCheck("1:2:3:4::255.1.2.3", "000100020003000400000000ff010203");
    }

    @Test
    public void testIp4WithMiddleEmpty3() {
        this.parseAndCheck("1::255.1.2.3", "000100000000000000000000ff010203");
    }

    @Test
    public void testIp4WithoutAnyPreceedingBlocks() {
        this.parseAndCheck("::255.1.2.3", "000000000000000000000000ff010203");
    }

    @Test
    public void testIp4WithoutEmpty() {
        final String address = "1:2:3:4:5:6:255.7.8.9";
        this.parseAndCheck(address, "000100020003000400050006ff070809");
    }

    @Test
    public void testWithStartAndEnd() {
        final String address = "!1:2:3:4:5:6:7:8!";
        this.parseAndCheck(address, 1, address.length() - 1, "00010002000300040005000600070008");
    }

    // helpers

    private void parseAndCheck(final String address, final String value) {
        this.parseAndCheck(address, toByteArray(value));
    }

    private void parseAndCheck(final String address, final byte[] value) {
        this.parseAndCheck(address, 0, address.length(), value);
    }

    private void parseAndCheck(final String address, final int start, final int end, final String value) {
        this.parseAndCheck(address, start, end, toByteArray(value));
    }

    private void parseAndCheck(final String address, final int start, final int end, final byte[] value) {
        final Object result = HostAddress.tryParseIp6(address, start, end);
        if (result instanceof HostAddressProblem) {
            failNotEquals("failed " + CharSequences.quote(address), value,
                    ((HostAddressProblem) result).message(address));
        }
        HostAddressIp6Test.assertEquals("bytes[]", value, (byte[]) result);
    }

    private void parseFails(final String address, final HostAddressProblem problem) {
        this.parseFails(address, 0, address.length(), problem);
    }

    private void parseFails(final String address, final int start, final int end, final HostAddressProblem problem) {
        final Object result = HostAddress.tryParseIp6(address, start, end);
        if (result instanceof byte[]) {
            Assert.fail("Should have failed=" + HostAddressTesting.toHexString((byte[]) result));
        }
        final HostAddressProblem actual = (HostAddressProblem) result;
        if (false == problem.equals(actual)) {
            failNotEquals("wrong problem returned", problem.message(address), actual.message(address));
        }
    }

    static public void assertEquals(final String message, final byte[] expected, final byte[] actual) {
        if (false == Arrays.equals(expected, actual)) {
            failNotEquals(message, HostAddressTesting.toHexString(expected), HostAddressTesting.toHexString(actual));
        }
    }

    /**
     * Parses the {@link String} of hex values assuming that it has hex digits in big endian form.
     */
    private static byte[] toByteArray(final String hexDigits) {
        Assert.assertEquals("hexValues string has wrong number of characters=" + hexDigits,
                HostAddress.IP6_OCTET_COUNT * 2,
                hexDigits.length());
        return CharSequences.bigEndianHexDigits(hexDigits);
    }
}
