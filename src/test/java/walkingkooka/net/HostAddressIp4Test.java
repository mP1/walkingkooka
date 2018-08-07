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

import java.util.Arrays;

public final class HostAddressIp4Test extends TestCase {

    @Test
    public void testActuallyIp6Fails() {
        this.parseFails("1234:2:3:4:5:6:7", false, HostAddressProbablyIp6Problem.INSTANCE);
    }

    @Test
    public void testActuallyIp6WithHexDigitCharacterFails() {
        this.parseFails("A234:2:3:4:5:6:7", false, HostAddressProbablyIp6Problem.INSTANCE);
    }

    @Test
    public void testActuallyIp6WithHexDigitCharacter2Fails() {
        this.parseFails("a234:2:3:4:5:6:7", false, HostAddressProbablyIp6Problem.INSTANCE);
    }

    @Test
    public void testIp4WithColonFails() {
        this.parseFails("1:2.3.4", true, HostAddressInvalidCharacterProblem.with(1));
    }

    @Test
    public void testFirstOctetEmptyFails() {
        this.parseFails(".2.3.4", false, HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testSecondOctetEmptyFails() {
        this.parseFails("1..3.4", false, HostAddressInvalidCharacterProblem.with(2));
    }

    @Test
    public void testThirdOctetEmptyFails() {
        this.parseFails("1.2..4", false, HostAddressInvalidCharacterProblem.with(4));
    }

    @Test
    public void testLastOctetEmptyFails() {
        final String address = "1.2.3.";
        this.parseFails(address, false, HostAddressInvalidCharacterProblem.with(address.lastIndexOf('.')));
    }

    @Test
    public void testFirstOctetStartsWithAlphaFails() {
        this.parseFails("a.2.3.4", false, HostAddressProbablyIp6Problem.INSTANCE);
    }

    @Test
    public void testFirstOctetStartsWithInvalidFails() {
        this.parseFails("!.2.3.4", false, HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testFirstOctetIncludesAlphaFails() {
        this.parseFails("1a.2.3.4", false, HostAddressProbablyIp6Problem.INSTANCE);
    }

    @Test
    public void testFirstOctetIsInvalidValueFails() {
        this.parseFails("987.2.3.4", false, HostAddressInvalidValueProblem.with(0));
    }

    @Test
    public void testSecondOctetIsInvalidValueFails() {
        this.parseFails("1.987.3.4", false, HostAddressInvalidValueProblem.with(2));
    }

    @Test
    public void testThirdOctetIsInvalidValueFails() {
        this.parseFails("1.2.987.4", false, HostAddressInvalidValueProblem.with(4));
    }

    @Test
    public void testLastOctetIsInvalidValueFails() {
        this.parseFails("1.2.3.987", false, HostAddressInvalidValueProblem.with(6));
    }

    @Test
    public void testValid() {
        this.passAndVerify("1.2.3.4", 0x01020304, false);
    }

    @Test
    public void testValid2() {
        this.passAndVerify("11.22.33.44", (11 << 24) | (22 << 16) | (33 << 8) | 44, false);
    }

    @Test
    public void testValidFirstOctetIsZero() {
        this.passAndVerify("0.2.3.4", 0x00020304, false);
    }

    @Test
    public void testValidSecondOctetIsZero() {
        this.passAndVerify("1.0.3.4", 0x01000304, false);
    }

    @Test
    public void testValidThirdOctetIsZero() {
        this.passAndVerify("1.2.0.4", 0x01020004, false);
    }

    @Test
    public void testValidLastOctetIsZero() {
        this.passAndVerify("1.2.3.0", 0x01020300, false);
    }

    @Test
    public void testValidFirstOctetIsManyZeros() {
        this.passAndVerify("00000.2.3.4", 0x00020304, false);
    }

    @Test
    public void testValidSecondOctetIsManyZeros() {
        this.passAndVerify("1.00000.3.4", 0x01000304, false);
    }

    @Test
    public void testValidThirdOctetIsManyZeros() {
        this.passAndVerify("1.2.00000.4", 0x01020004, false);
    }

    @Test
    public void testValidLastOctetIsManyZeros() {
        this.passAndVerify("1.2.3.00000", 0x01020300, false);
    }

    @Test
    public void testValidWithStartAndEnd() {
        final String address = "!1.2.3.4!";
        this.passAndVerify(address, 1, address.length() - 1, false, 0x01020304);
    }

    @Test
    public void testOnlyOneOctet() {
        this.parseFails("1", false, HostAddressIncompleteProblem.INSTANCE);
    }

    @Test
    public void testTwoOctets() {
        this.parseFails("1.2", false, HostAddressIncompleteProblem.INSTANCE);
    }

    @Test
    public void testThreeOctets() {
        this.parseFails("1.2.3", false, HostAddressIncompleteProblem.INSTANCE);
    }

    @Test
    public void testFiveOctets() {
        final String address = "1.2.3.4.5";
        this.parseFails(address, false, HostAddressInvalidCharacterProblem.with(address.indexOf('4') + 1));
    }

    @Test
    public void testTooLongOnlyOctet() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, '0');
        final String name = new String(array);
        this.parseFails(name, false, HostAddressInvalidLengthProblem.with(0));
    }

    @Test
    public void testTooLongFirstOctet() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, '0');
        final String name = new String(array);
        this.parseFails(name + ".1", false, HostAddressInvalidLengthProblem.with(0));
    }

    @Test
    public void testTooLongSecondOctet() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, '0');
        this.parseFails("1." + new String(array) + ".3.4", false, HostAddressInvalidLengthProblem.with(2));
    }

    @Test
    public void testTooLongThirdOctet() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, '0');
        this.parseFails("1.2." + new String(array) + ".4", false, HostAddressInvalidLengthProblem.with(4));
    }

    @Test
    public void testTooLongLastOctet() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, '0');
        this.parseFails("1.2.3." + new String(array), false, HostAddressInvalidLengthProblem.with(6));
    }

    // helpers
    private void passAndVerify(final String address, final long value, final boolean insideIp6) {
        this.passAndVerify(address, 0, address.length(), insideIp6, value);
    }

    private void passAndVerify(final String address, final int start, final int end, final boolean insideIp6,
                               final long value) {
        final Object result = HostAddress.tryParseIp4(address, start, end, insideIp6);
        if (result instanceof HostAddressProblem) {
            Assert.fail(((HostAddressProblem) result).message(address));
        }
        assertEquals(HostAddress.toBytes(value), HostAddress.toBytes((Long) result));
    }

    private void parseFails(final String address, final boolean insideIp6, final HostAddressProblem problem) {
        this.parseFails(address, 0, address.length(), insideIp6, problem);
    }

    private void parseFails(final String address, final int start, final int end, final boolean insideIp6,
                            final HostAddressProblem problem) {
        assertEquals("problem", problem, HostAddress.tryParseIp4(address, start, end, insideIp6));
    }
}
