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

import org.junit.jupiter.api.Test;
import walkingkooka.compare.ComparableTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public final class HostAddressTest implements ClassTesting2<HostAddress>,
        ComparableTesting<HostAddress>,
        HashCodeEqualsDefinedTesting<HostAddress>,
        SerializationTesting<HostAddress>,
        ToStringTesting<HostAddress> {

    private final static String HOST = "example.com";

    // tests

    @Test
    public void testNullFails() {
        assertThrows(NullPointerException.class, () -> {
            HostAddress.with(null);
        });
    }

    @Test
    public void testEmptyFails() {
        this.withFails("");
    }

    @Test
    public void testTooLongFails() {
        final char[] array = new char[HostAddress.MAX_LENGTH];
        Arrays.fill(array, 'a');
        final String address = new String(array);
        this.withFails(address, HostAddress.tooLong(array.length));
    }

    @Test
    public void testInvalidNameStartsWithDashFails() {
        final String address = "-invalid.last";
        this.withFails(address, HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testInvalidNameStartsWithDash2Fails() {
        final String address = "first.-invalid.last";
        this.withFails(address, HostAddressInvalidCharacterProblem.with(address.indexOf('-')));
    }

    @Test
    public void testInvalidNameStartsWithDotFails() {
        final String address = ".invalid";
        this.withFails(address, HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testIp4WithInvalidValue() {
        final String address = "1.2.345.6";
        this.withFails(address, HostAddressInvalidValueProblem.with(address.indexOf('3')));
    }

    @Test
    public void testIp6WithInvalidValueFails() {
        final String address = "1:2:34567:4:5:6:7:8";
        this.withFails(address, HostAddressInvalidValueProblem.with(address.indexOf('3')));
    }

    private void withFails(final String address) {
        this.withFails(address, (String) null);
    }

    private void withFails(final String address, final HostAddressProblem problem) {
        this.withFails(address, problem.message(address));
    }

    private void withFails(final String address, final String message) {
        final IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            HostAddress.with(address);
        });
        if(null!=message){
            assertEquals(message, expected.getMessage(), "message");
        }
    }

    @Test
    public void testReallyShort() {
        final String address = "a";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        checkEquals("values", new byte[0], hostAddress.values());
        this.checkName(hostAddress);
        this.checkNotIpAddress(hostAddress);
    }

    @Test
    public void testName() {
        final String address = "address";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        checkEquals("values", new byte[0], hostAddress.values());
        this.checkName(hostAddress);
        this.checkNotIpAddress(hostAddress);
    }

    @Test
    public void testNameWithDigits() {
        final String address = "address123";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        checkEquals("values", new byte[0], hostAddress.values());
        this.checkName(hostAddress);
        this.checkNotIpAddress(hostAddress);
    }

    @Test
    public void testNameWithDash() {
        final String address = "address-after";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        checkEquals("values", new byte[0], hostAddress.values());
        this.checkName(hostAddress);
        this.checkNotIpAddress(hostAddress);
    }

    @Test
    public void testNameWithSubDomains() {
        final String address = "sub.address";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        checkEquals("values", new byte[0], hostAddress.values());
        this.checkName(hostAddress);
        this.checkNotIpAddress(hostAddress);
    }

    @Test
    public void testNameThatsStartsOfLikeIp4() {
        final String address = "1.2.3.4.5.address";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        checkEquals("values", new byte[0], hostAddress.values());
        this.checkName(hostAddress);
        this.checkNotIpAddress(hostAddress);
    }

    @Test
    public void testNameWithInvalidOctetValue() {
        final String address = "1234.address";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        this.checkValues(hostAddress, new byte[0]);
        this.checkName(hostAddress);
        this.checkNotIpAddress(hostAddress);
    }

    @Test
    public void testNameWithInvalidOctetValue2() {
        final String address = "1.2.3456.address";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        this.checkValues(hostAddress, new byte[0]);
        this.checkName(hostAddress);
        this.checkNotIpAddress(hostAddress);
    }

    @Test
    public void testIp4Address() {
        final String address = "1.2.3.4";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        this.checkValues(hostAddress, new byte[]{1, 2, 3, 4});
        this.checkIp4Address(hostAddress);
    }

    @Test
    public void testIp4Address2() {
        final String address = "123.45.67.255";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        this.checkValues(hostAddress, new byte[]{123, 45, 67, (byte) 255});
        this.checkIp4Address(hostAddress);
    }

    @Test
    public void testIp6Address() {
        final String address = "1:2:3:4:5:6:7:8";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        this.checkValues(hostAddress, "00010002000300040005000600070008");
        this.checkIp6Address(hostAddress);
    }

    @Test
    public void testIp6Address2() {
        final String address = "1111:2222:3333:4444:5555:6666:7777:8888";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        this.checkValues(hostAddress, "11112222333344445555666677778888");
        this.checkIp6Address(hostAddress);
    }

    @Test
    public void testIp6AddressWithEmpty() {
        final String address = "1111::4444:5555:6666:7777:8888";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        this.checkValues(hostAddress, "11110000000044445555666677778888");
        this.checkIp6Address(hostAddress);
    }

    @Test
    public void testIp6AddressWithIp4() {
        final String address = "1:2:3:4:5:6:255.7.8.9";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        this.checkValues(hostAddress, "000100020003000400050006ff070809");
        this.checkIp6Address(hostAddress);
    }

    private void checkValue(final HostAddress hostAddress, final String address) {
        assertEquals(address, hostAddress.value(), "address");
    }

    private void checkValues(final HostAddress hostAddress, final String expected) {
        this.checkValues(hostAddress, toByteArray(expected));
    }

    private void checkValues(final HostAddress hostAddress, final byte[] expected) {
        checkEquals("values", hostAddress.values(), expected);
    }

    private void checkName(final HostAddress hostAddress) {
        assertTrue(hostAddress.isName(), "is name=" + hostAddress);
    }

    private void checkNotIpAddress(final HostAddress hostAddress) {
        this.checkName(hostAddress);
        assertNull(hostAddress.isIpAddress(), hostAddress + " should have an IpAddress");
    }

    private void checkIp4Address(final HostAddress address) {
        assertFalse(address.isName(), address + " should not be a name");
        assertNotNull(address.isIpAddress().isIp4(), address + " should have an Ip4Address");
    }

    private void checkIp6Address(final HostAddress address) {
        assertFalse(address.isName(), address + " should not be a name");
        assertNotNull(address.isIpAddress().isIp6(), address + " should have an Ip6Address");
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testCaseUnimportant() {
        this.checkEqualsAndHashCode(HostAddress.with(HOST.toUpperCase()));
    }

    @Test
    public void testEqualsDifferentName() {
        this.checkNotEquals(HostAddress.with("different"));
    }

    // parse .........................................................

    @Test
    public void testParseProbablyIp6Fails() {
        this.parseFails("a.b:c", HostAddressProbablyIp6Problem.INSTANCE);
    }

    @Test
    public void testParseDotAtStartFails() {
        this.parseFails(".n", HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testParseDotAtEndFails() {
        this.parseFails("n.", HostAddressIncompleteProblem.INSTANCE);
    }

    @Test
    public void testParseDashAtStartFails() {
        this.parseFails("-n", HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testParseDashAtEndFails() {
        final String name = "n-";
        this.parseFails(name, HostAddressInvalidCharacterProblem.with(name.indexOf('-')));
    }

    @Test
    public void testParseOneLabelInvalidAtStartFails() {
        this.parseFails("!b", HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testParseOneLabelInvalidAtStarts2Fails() {
        this.parseFails("!bc", HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testParseOneLabelInvalidFails() {
        this.parseFails("n!ame", HostAddressInvalidCharacterProblem.with(1));
    }

    @Test
    public void testParseOneLabelEndsWithInvalidFails() {
        this.parseFails("a!", HostAddressInvalidCharacterProblem.with(1));
    }

    @Test
    public void testParseOneLabelEndsWithInvalid2Fails() {
        this.parseFails("ab!", HostAddressInvalidCharacterProblem.with(2));
    }

    @Test
    public void testParseDashThenDotFails() {
        final String address = "exampl-.com";
        this.parseFails(address, HostAddressInvalidCharacterProblem.with(address.indexOf('-')));
    }

    @Test
    public void testParseSecondLabelInvalidAtStartFails() {
        final String name = "a.!b";
        this.parseFails(name, HostAddressInvalidCharacterProblem.with(name.indexOf('!')));
    }

    @Test
    public void testParseSecondLabelInvalidFails() {
        final String name = "a.b!c.d";
        this.parseFails(name, HostAddressInvalidCharacterProblem.with(name.indexOf('!')));
    }

    @Test
    public void testParseSecondLabelEndsWithInvalidFails() {
        final String name = "a.b!.c";
        this.parseFails(name, HostAddressInvalidCharacterProblem.with(name.indexOf('!')));
    }

    @Test
    public void testParseSecondLabelEndsWithDashFails() {
        final String name = "a.b-.c";
        this.parseFails(name, HostAddressInvalidCharacterProblem.with(name.indexOf('-')));
    }

    @Test
    public void testParseLastLabelEndWithInvalidFails() {
        final String name = "a.b!";
        this.parseFails(name, HostAddressInvalidCharacterProblem.with(name.indexOf('!')));
    }

    @Test
    public void testParseLastLabelEndsWithDashFails() {
        final String name = "a.b-";
        this.parseFails(name, HostAddressInvalidCharacterProblem.with(name.indexOf('-')));
    }

    @Test
    public void testParseIp4PartialFails() {
        final String name = "1.2";
        this.parseFails(name, HostAddressProbablyIp4Problem.INSTANCE);
    }

    @Test
    public void testParseIp4Fails() {
        final String name = "1.2.3.4";
        this.parseFails(name, HostAddressProbablyIp4Problem.INSTANCE);
    }

    @Test
    public void testParseIp4WithExtraOctetsFails() {
        final String name = "1.2.3.4.5.6";
        this.parseFails(name, HostAddressProbablyIp4Problem.INSTANCE);
    }

    @Test
    public void testParseIp6Fails() {
        final String name = "1:2:3:4:5:6:7:8";
        this.parseFails(name, HostAddressProbablyIp6Problem.INSTANCE);
    }

    @Test
    public void testParseLabelTooLongFails() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, 'x');
        final String name = new String(array);
        this.parseFails(name, HostAddressInvalidLengthProblem.with(0));
    }

    @Test
    public void testParseLabelTooLongFirstFails() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, 'x');
        final String name = new String(array);
        this.parseFails(name + ".second", HostAddressInvalidLengthProblem.with(0));
    }

    public void testParseLabelTooLongSecondFails() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, 'x');
        this.parseFails("first." + new String(array) + ".last", HostAddressInvalidLengthProblem.with("first.".length()));
    }

    @Test
    public void testParseLabelTooLongLastFails() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, 'x');
        this.parseFails("first." + new String(array), HostAddressInvalidLengthProblem.with("first.".length()));
    }

    @Test
    public void testParseOneLetter() {
        this.parseAndCheck("A");
    }

    @Test
    public void testParseOneLetterWithStartAndEnd() {
        this.parseAndCheck("!B!", 1, 2);
    }

    @Test
    public void testParseOneLabel() {
        this.parseAndCheck("one");
    }

    @Test
    public void testParseOneLabelWithStartAndEnd() {
        this.parseAndCheck("!one!", 1, 4);
    }

    @Test
    public void testParseManyLabels() {
        this.parseAndCheck("first.second");
    }

    @Test
    public void testParseManyLabelsWithStartAndEnd() {
        final String address = "!first.second!";
        this.parseAndCheck(address, 1, address.length() - 1);
    }

    @Test
    public void testParseManyLabels2() {
        this.parseAndCheck("first.second.third");
    }

    @Test
    public void testParseManyLabels2WithStartAndEnd() {
        final String address = "!first.second.third!";
        this.parseAndCheck(address, 1, address.length() - 1);
    }

    private void parseAndCheck(final String address) {
        this.parseAndCheck(address, 0, address.length());
    }

    private void parseAndCheck(final String address, final int start, final int end) {
        assertNull(HostAddress.tryParseName(address, start, end));
    }

    private void parseFails(final String address, final HostAddressProblem problem) {
        this.parseFails(address, 0, address.length(), problem);
    }

    private void parseFails(final String address, final int start, final int end, final HostAddressProblem problem) {
        assertEquals(problem, HostAddress.tryParseName(address, start, end), "problem");
    }

    // parseIp4...................................................................................................

    @Test
    public void testParseIp4ActuallyIp6Fails() {
        this.parseIp4Fails("1234:2:3:4:5:6:7", false, HostAddressProbablyIp6Problem.INSTANCE);
    }

    @Test
    public void testParseIp4ActuallyIp6WithHexDigitCharacterFails() {
        this.parseIp4Fails("A234:2:3:4:5:6:7", false, HostAddressProbablyIp6Problem.INSTANCE);
    }

    @Test
    public void testParseIp4ActuallyIp6WithHexDigitCharacter2Fails() {
        this.parseIp4Fails("a234:2:3:4:5:6:7", false, HostAddressProbablyIp6Problem.INSTANCE);
    }

    @Test
    public void testParseIp4Ip4WithColonFails() {
        this.parseIp4Fails("1:2.3.4", true, HostAddressInvalidCharacterProblem.with(1));
    }

    @Test
    public void testParseIp4FirstOctetEmptyFails() {
        this.parseIp4Fails(".2.3.4", false, HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testParseIp4SecondOctetEmptyFails() {
        this.parseIp4Fails("1..3.4", false, HostAddressInvalidCharacterProblem.with(2));
    }

    @Test
    public void testParseIp4ThirdOctetEmptyFails() {
        this.parseIp4Fails("1.2..4", false, HostAddressInvalidCharacterProblem.with(4));
    }

    @Test
    public void testParseIp4LastOctetEmptyFails() {
        final String address = "1.2.3.";
        this.parseIp4Fails(address, false, HostAddressInvalidCharacterProblem.with(address.lastIndexOf('.')));
    }

    @Test
    public void testParseIp4FirstOctetStartsWithAlphaFails() {
        this.parseIp4Fails("a.2.3.4", false, HostAddressProbablyIp6Problem.INSTANCE);
    }

    @Test
    public void testParseIp4FirstOctetStartsWithInvalidFails() {
        this.parseIp4Fails("!.2.3.4", false, HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testParseIp4FirstOctetIncludesAlphaFails() {
        this.parseIp4Fails("1a.2.3.4", false, HostAddressProbablyIp6Problem.INSTANCE);
    }

    @Test
    public void testParseIp4FirstOctetIsInvalidValueFails() {
        this.parseIp4Fails("987.2.3.4", false, HostAddressInvalidValueProblem.with(0));
    }

    @Test
    public void testParseIp4SecondOctetIsInvalidValueFails() {
        this.parseIp4Fails("1.987.3.4", false, HostAddressInvalidValueProblem.with(2));
    }

    @Test
    public void testParseIp4ThirdOctetIsInvalidValueFails() {
        this.parseIp4Fails("1.2.987.4", false, HostAddressInvalidValueProblem.with(4));
    }

    @Test
    public void testParseIp4LastOctetIsInvalidValueFails() {
        this.parseIp4Fails("1.2.3.987", false, HostAddressInvalidValueProblem.with(6));
    }

    @Test
    public void testParseIp4Valid() {
        this.parseIp4AndVerify("1.2.3.4", 0x01020304, false);
    }

    @Test
    public void testParseIp4Valid2() {
        this.parseIp4AndVerify("11.22.33.44", (11 << 24) | (22 << 16) | (33 << 8) | 44, false);
    }

    @Test
    public void testParseIp4ValidFirstOctetIsZero() {
        this.parseIp4AndVerify("0.2.3.4", 0x00020304, false);
    }

    @Test
    public void testParseIp4ValidSecondOctetIsZero() {
        this.parseIp4AndVerify("1.0.3.4", 0x01000304, false);
    }

    @Test
    public void testParseIp4ValidThirdOctetIsZero() {
        this.parseIp4AndVerify("1.2.0.4", 0x01020004, false);
    }

    @Test
    public void testParseIp4ValidLastOctetIsZero() {
        this.parseIp4AndVerify("1.2.3.0", 0x01020300, false);
    }

    @Test
    public void testParseIp4ValidFirstOctetIsManyZeros() {
        this.parseIp4AndVerify("00000.2.3.4", 0x00020304, false);
    }

    @Test
    public void testParseIp4ValidSecondOctetIsManyZeros() {
        this.parseIp4AndVerify("1.00000.3.4", 0x01000304, false);
    }

    @Test
    public void testParseIp4ValidThirdOctetIsManyZeros() {
        this.parseIp4AndVerify("1.2.00000.4", 0x01020004, false);
    }

    @Test
    public void testParseIp4ValidLastOctetIsManyZeros() {
        this.parseIp4AndVerify("1.2.3.00000", 0x01020300, false);
    }

    @Test
    public void testParseIp4ValidWithStartAndEnd() {
        final String address = "!1.2.3.4!";
        this.parseIp4AndVerify(address, 1, address.length() - 1, false, 0x01020304);
    }

    @Test
    public void testParseIp4OnlyOneOctet() {
        this.parseIp4Fails("1", false, HostAddressIncompleteProblem.INSTANCE);
    }

    @Test
    public void testParseIp4TwoOctets() {
        this.parseIp4Fails("1.2", false, HostAddressIncompleteProblem.INSTANCE);
    }

    @Test
    public void testParseIp4ThreeOctets() {
        this.parseIp4Fails("1.2.3", false, HostAddressIncompleteProblem.INSTANCE);
    }

    @Test
    public void testParseIp4FiveOctets() {
        final String address = "1.2.3.4.5";
        this.parseIp4Fails(address, false, HostAddressInvalidCharacterProblem.with(address.indexOf('4') + 1));
    }

    @Test
    public void testParseIp4TooLongOnlyOctet() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, '0');
        final String name = new String(array);
        this.parseIp4Fails(name, false, HostAddressInvalidLengthProblem.with(0));
    }

    @Test
    public void testParseIp4TooLongFirstOctet() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, '0');
        final String name = new String(array);
        this.parseIp4Fails(name + ".1", false, HostAddressInvalidLengthProblem.with(0));
    }

    @Test
    public void testParseIp4TooLongSecondOctet() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, '0');
        this.parseIp4Fails("1." + new String(array) + ".3.4", false, HostAddressInvalidLengthProblem.with(2));
    }

    @Test
    public void testParseIp4TooLongThirdOctet() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, '0');
        this.parseIp4Fails("1.2." + new String(array) + ".4", false, HostAddressInvalidLengthProblem.with(4));
    }

    @Test
    public void testParseIp4TooLongLastOctet() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, '0');
        this.parseIp4Fails("1.2.3." + new String(array), false, HostAddressInvalidLengthProblem.with(6));
    }

    // helpers
    private void parseIp4AndVerify(final String address, final long value, final boolean insideIp6) {
        this.parseIp4AndVerify(address, 0, address.length(), insideIp6, value);
    }

    private void parseIp4AndVerify(final String address, final int start, final int end, final boolean insideIp6,
                                   final long value) {
        final Object result = HostAddress.tryParseIp4(address, start, end, insideIp6);
        if (result instanceof HostAddressProblem) {
            fail(((HostAddressProblem) result).message(address));
        }
        assertArrayEquals(HostAddress.toBytes(value), HostAddress.toBytes((Long) result));
    }

    private void parseIp4Fails(final String address, final boolean insideIp6, final HostAddressProblem problem) {
        this.parseIp4Fails(address, 0, address.length(), insideIp6, problem);
    }

    private void parseIp4Fails(final String address, final int start, final int end, final boolean insideIp6,
                               final HostAddressProblem problem) {
        assertEquals(problem, HostAddress.tryParseIp4(address, start, end, insideIp6),"problem");
    }

    // parseIp6 ...................................................................................................

    @Test
    public void testParseIp6OnlyColonFails() {
        this.parseIp6Fails(":", HostAddressIncompleteProblem.INSTANCE);
    }

    @Test
    public void testParseIp6StartsWithColonFails() {
        this.parseIp6Fails(":2:3:4:5:6:7:8", HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testParseIp6EndsWithColonFails() {
        this.parseIp6Fails("1:2:3:4:5:6:7:", HostAddressIncompleteProblem.INSTANCE);
    }

    @Test
    public void testParseIp6FirstGroupStartsWithInvalidCharacterFails() {
        this.parseIp6Fails("!1:2:3:4:5:6:7:8", HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testParseIp6FirstGroupIncludesInvalidCharacterFails() {
        this.parseIp6Fails("1!:2:3:4:5:6:7:8", HostAddressInvalidCharacterProblem.with(1));
    }

    @Test
    public void testParseIp6FirstGroupIncludesInvalidValueFails() {
        this.parseIp6Fails("12345:2:3:4:5:6:7:8", HostAddressInvalidValueProblem.with(0));
    }

    @Test
    public void testParseIp6FirstGroupIncludesInvalidValueTooManyZeroesFails() {
        this.parseIp6Fails("00000:2:3:4:5:6:7:8", HostAddressInvalidValueProblem.with(0));
    }

    @Test
    public void testParseIp6SecondGroupStartsWithInvalidCharacterFails() {
        this.parseIp6Fails("1:!2:3:4:5:6:7:8", HostAddressInvalidCharacterProblem.with(2));
    }

    @Test
    public void testParseIp6SecondGroupIncludesInvalidCharacterFails() {
        this.parseIp6Fails("1:2!:3:4:5:6:7:8", HostAddressInvalidCharacterProblem.with(3));
    }

    @Test
    public void testParseIp6SecondGroupIncludesInvalidValueFails() {
        this.parseIp6Fails("1:23456:3:4:5:6:7:8", HostAddressInvalidValueProblem.with(2));
    }

    @Test
    public void testParseIp6SecondGroupIncludesInvalidValueTooManyZeroesFails() {
        this.parseIp6Fails("1:00000:3:4:5:6:7:8", HostAddressInvalidValueProblem.with(2));
    }

    @Test
    public void testParseIp6LastGroupStartsWithInvalidCharacterFails() {
        this.parseIp6Fails("1:2:3:4:5:6:7:!", HostAddressInvalidCharacterProblem.with(14));
    }

    @Test
    public void testParseIp6LastGroupIncludesInvalidCharacterFails() {
        this.parseIp6Fails("1:2:3:4:5:6:7:8!", HostAddressInvalidCharacterProblem.with(14 + 1));
    }

    @Test
    public void testParseIp6LastGroupIncludesInvalidValueFails() {
        this.parseIp6Fails("1:2:3:4:5:6:7:89ABC", HostAddressInvalidValueProblem.with(14));
    }

    @Test
    public void testParseIp6LastGroupIncludesInvalidValueTooManyZeroesFails() {
        this.parseIp6Fails("1:2:3:4:5:6:7:00000", HostAddressInvalidValueProblem.with(14));
    }

    @Test
    public void testParseIp6TooManyGroupsFails() {
        final String address = "1:2:3:4:5:6:7:8:9";
        this.parseIp6Fails(address, HostAddressInvalidCharacterProblem.with(address.indexOf('9') - 1));
    }

    @Test
    public void testParseIp6WithoutEmpty() {
        this.parseIp6AndCheck("1:2:3:4:5:6:7:8", "00010002000300040005000600070008");
    }

    @Test
    public void testParseIp6WithoutEmpty2() {
        this.parseIp6AndCheck("1122:3344:5566:7788:99aa:bbcc:ddee:ff01", "112233445566778899aabbccddeeff01");
    }

    @Test
    public void testParseIp6WithoutEmptyAndStartAndEnd() {
        final String address = "!1:2:3:4:5:6:7:8!";
        this.parseIp6AndCheck(address, 1, address.length() - 1, "00010002000300040005000600070008");
    }

    @Test
    public void testParseIp6MoreThanOneEmptyFails() {
        final String address = "1::3:4::6:7:8";
        this.parseIp6Fails(address, HostAddressInvalidCharacterProblem.with(address.indexOf('6') - 1));
    }

    @Test
    public void testParseIp6StartsWithEmpty() {
        this.parseIp6AndCheck("::2:3:4:5:6:7:8", "00000002000300040005000600070008");
    }

    @Test
    public void testParseIp6StartsWithEmpty2() {
        this.parseIp6AndCheck("::3:4:5:6:7:8", "00000000000300040005000600070008");
    }

    @Test
    public void testParseIp6StartsWithEmpty3() {
        this.parseIp6AndCheck("::4:5:6:7:8", "00000000000000040005000600070008");
    }

    @Test
    public void testParseIp6StartsWithEmpty4() {
        this.parseIp6AndCheck("::8", "00000000000000000000000000000008");
    }

    @Test
    public void testParseIp6MiddleEmpty() {
        this.parseIp6AndCheck("1:2:3::5:6:7:8", "00010002000300000005000600070008");
    }

    @Test
    public void testParseIp6MiddleEmpty2() {
        this.parseIp6AndCheck("1:2:3::6:7:8", "00010002000300000000000600070008");
    }

    @Test
    public void testParseIp6MiddleEmpty3() {
        this.parseIp6AndCheck("1:2:3::7:8", "00010002000300000000000000070008");
    }

    @Test
    public void testParseIp6EndsWithEmpty() {
        this.parseIp6AndCheck("1:2:3:4:5:6:7::", "00010002000300040005000600070000");
    }

    @Test
    public void testParseIp6EndsWithEmpty2() {
        this.parseIp6AndCheck("1:2:3:4:5:6::", "00010002000300040005000600000000");
    }

    @Test
    public void testParseIp6EndsWithEmpty3() {
        this.parseIp6AndCheck("1:2:3:4:5::", "00010002000300040005000000000000");
    }

    @Test
    public void testParseIp6EndsWithEmpty4() {
        this.parseIp6AndCheck("1::", "00010000000000000000000000000000");
    }

    @Test
    public void testParseIp6OnlyEmpty() {
        this.parseIp6AndCheck("::", "00000000000000000000000000000000");
    }

    @Test
    public void testParseIp6Ip4WithInvalidValueFails() {
        final String address = "1:2:3:4:5:6:7.8.900.1";
        this.parseIp6Fails(address, HostAddressInvalidValueProblem.with(address.indexOf('9')));
    }

    @Test
    public void testParseIp6Ip4WithColonFails() {
        final String address = "1:2:3:4:5:6:7.8:9.0";
        this.parseIp6Fails(address, HostAddressInvalidCharacterProblem.with(address.indexOf('8') + 1));
    }

    @Test
    public void testParseIp6Ip4BeforeTooFewGroupsFails() {
        final String address = "1:2:3.4.5.6";
        this.parseIp6Fails(address, HostAddressInvalidCharacterProblem.with(address.indexOf('.')));
    }

    @Test
    public void testParseIp6Ip4BeforeTooManyGroupsFails() {
        final String address = "1:2:3:4:5:6:7:1.2.3.4";
        this.parseIp6Fails(address, HostAddressInvalidCharacterProblem.with(address.indexOf('.')));
    }

    @Test
    public void testParseIp6Ip4StartsWithEmpty() {
        this.parseIp6AndCheck("::1:2:3:4:5:6.7.8.9", "00000001000200030004000506070809");
    }

    @Test
    public void testParseIp6Ip4StartsWithEmpty2() {
        this.parseIp6AndCheck("::1:2:3:5.6.7.8", "00000000000000010002000305060708");
    }

    @Test
    public void testParseIp6Ip4StartsWithEmpty3() {
        this.parseIp6AndCheck("::1:2:5.6.7.8", "00000000000000000001000205060708");
    }

    @Test
    public void testParseIp6Ip4StartsWithEmpty4() {
        this.parseIp6AndCheck("::1:5.6.7.8", "00000000000000000000000105060708");
    }

    @Test
    public void testParseIp6Ip4StartsWithOnlyEmpty() {
        this.parseIp6AndCheck("::5.6.7.8", "00000000000000000000000005060708");
    }

    @Test
    public void testParseIp6Ip4WithMiddleEmpty1() {
        this.parseIp6AndCheck("1:2:3:4:5::255.1.2.3", "000100020003000400050000ff010203");
    }

    @Test
    public void testParseIp6Ip4WithMiddleEmpty2() {
        this.parseIp6AndCheck("1:2:3:4::255.1.2.3", "000100020003000400000000ff010203");
    }

    @Test
    public void testParseIp6Ip4WithMiddleEmpty3() {
        this.parseIp6AndCheck("1::255.1.2.3", "000100000000000000000000ff010203");
    }

    @Test
    public void testParseIp6Ip4WithoutAnyPreceedingBlocks() {
        this.parseIp6AndCheck("::255.1.2.3", "000000000000000000000000ff010203");
    }

    @Test
    public void testParseIp6Ip4WithoutEmpty() {
        final String address = "1:2:3:4:5:6:255.7.8.9";
        this.parseIp6AndCheck(address, "000100020003000400050006ff070809");
    }

    @Test
    public void testParseIp6WithStartAndEnd() {
        final String address = "!1:2:3:4:5:6:7:8!";
        this.parseIp6AndCheck(address, 1, address.length() - 1, "00010002000300040005000600070008");
    }

    // helpers

    private void parseIp6AndCheck(final String address, final String value) {
        this.parseIp6AndCheck(address, toByteArray(value));
    }

    private void parseIp6AndCheck(final String address, final byte[] value) {
        this.parseIp6AndCheck(address, 0, address.length(), value);
    }

    private void parseIp6AndCheck(final String address, final int start, final int end, final String value) {
        this.parseIp6AndCheck(address, start, end, toByteArray(value));
    }

    private void parseIp6AndCheck(final String address, final int start, final int end, final byte[] value) {
        final Object result = HostAddress.tryParseIp6(address, start, end);
        if (result instanceof HostAddressProblem) {
            assertEquals(value,
                    ((HostAddressProblem) result).message(address),
                    "failed " + CharSequences.quote(address));
        }
        checkEquals("bytes[]", value, (byte[]) result);
    }

    private void parseIp6Fails(final String address, final HostAddressProblem problem) {
        this.parseIp6Fails(address, 0, address.length(), problem);
    }

    private void parseIp6Fails(final String address, final int start, final int end, final HostAddressProblem problem) {
        final Object result = HostAddress.tryParseIp6(address, start, end);
        if (result instanceof byte[]) {
            fail("Should have failed=" + toHexString((byte[]) result));
        }
        final HostAddressProblem actual = (HostAddressProblem) result;
        if (false == problem.equals(actual)) {
            assertEquals(problem.message(address),
                    actual.message(address),
                    "wrong problem returned");
        }
    }

    /**
     * Parses the {@link String} of hex values assuming that it has hex digits in big endian form.
     */
    private static byte[] toByteArray(final String hexDigits) {
        assertEquals(HostAddress.IP6_OCTET_COUNT * 2,
                hexDigits.length(),
                "hexValues string has wrong number of characters=" + hexDigits);
        return CharSequences.bigEndianHexDigits(hexDigits);
    }

    // Comparable...................................................................................................

    @Test
    public void testCompareLess() {
        this.compareToAndCheckLess(HostAddress.with("zebra.com"));
    }

    @Test
    public void testCompareLessCaseInsignificant() {
        this.compareToAndCheckLess(HostAddress.with("ZEBRA.com"));
    }

    @Test
    public void testCompareEqualsCaseInsignificant() {
        this.compareToAndCheckEqual(HostAddress.with(HOST.toUpperCase()));
    }

    // toString...................................................................................................

    @Test
    public void testToString() {
        final String address = "address";
        this.toStringAndCheck(HostAddress.with(address), address);
    }

    @Override public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    static public void checkEquals(final String message, final byte[] expected, final byte[] actual) {
        if (false == Arrays.equals(expected, actual)) {
            assertEquals(message, toHexString(expected), toHexString(actual));
        }
    }

    /**
     * Accepts some bytes in big endian form and builds a hex string representation.
     */
    static private String toHexString(final byte[] bytes) {
        final StringBuilder builder = new StringBuilder();
        for (final byte value : bytes) {
            builder.append(CharSequences.padLeft(Integer.toHexString(0xff & value), 2, '0'));
        }
        return builder.toString();
    }

    @Override
    public HostAddress createObject() {
        return HostAddress.with(HOST.toLowerCase());
    }

    @Override
    public Class<HostAddress> type() {
        return HostAddress.class;
    }

    // ComparableTesting..........................................................................................

    @Override
    public HostAddress createComparable() {
        return this.createObject();
    }

    // SerializableTesting..........................................................................................

    @Override
    public HostAddress serializableInstance() {
        return HostAddress.with("example.com");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
