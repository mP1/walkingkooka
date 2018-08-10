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

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedTestCase;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public final class HostAddressTest extends HashCodeEqualsDefinedTestCase<HostAddress> {

    // tests

    @Test
    public void testNullFails() {
        this.withFails(null);
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
        try {
            HostAddress.with(address);
            fail();
        } catch (final RuntimeException expected) {
            if (null != message) {
                assertEquals("message", message, expected.getMessage());
            }
        }
    }

    @Test
    public void testReallyShort() {
        final String address = "a";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        HostAddressTest.assertEquals("values", new byte[0], hostAddress.values());
        this.checkName(hostAddress);
        this.checkNotIpAddress(hostAddress);
    }

    @Test
    public void testName() {
        final String address = "address";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        HostAddressTest.assertEquals("values", new byte[0], hostAddress.values());
        this.checkName(hostAddress);
        this.checkNotIpAddress(hostAddress);
    }

    @Test
    public void testNameWithDigits() {
        final String address = "address123";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        HostAddressTest.assertEquals("values", new byte[0], hostAddress.values());
        this.checkName(hostAddress);
        this.checkNotIpAddress(hostAddress);
    }

    @Test
    public void testNameWithDash() {
        final String address = "address-after";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        HostAddressTest.assertEquals("values", new byte[0], hostAddress.values());
        this.checkName(hostAddress);
        this.checkNotIpAddress(hostAddress);
    }

    @Test
    public void testNameWithSubDomains() {
        final String address = "sub.address";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        HostAddressTest.assertEquals("values", new byte[0], hostAddress.values());
        this.checkName(hostAddress);
        this.checkNotIpAddress(hostAddress);
    }

    @Test
    public void testNameThatsStartsOfLikeIp4() {
        final String address = "1.2.3.4.5.address";
        final HostAddress hostAddress = HostAddress.with(address);
        this.checkValue(hostAddress, address);
        HostAddressTest.assertEquals("values", new byte[0], hostAddress.values());
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
        assertEquals("address", address, hostAddress.value());
    }

    private void checkValues(final HostAddress hostAddress, final String expected) {
        this.checkValues(hostAddress, HostAddressTesting.toByteArray(expected));
    }

    private void checkValues(final HostAddress hostAddress, final byte[] expected) {
        HostAddressTest.assertEquals("values", hostAddress.values(), expected);
    }

    private void checkName(final HostAddress hostAddress) {
        assertTrue("is name=" + hostAddress, hostAddress.isName());
    }

    private void checkNotIpAddress(final HostAddress hostAddress) {
        this.checkName(hostAddress);
        assertNull(hostAddress + " should have an IpAddress", hostAddress.isIpAddress());
    }

    private void checkIp4Address(final HostAddress address) {
        assertFalse(address + " should not be a name", address.isName());
        assertNotNull(address + " should have an Ip4Address", address.isIpAddress().isIp4());
    }

    private void checkIp6Address(final HostAddress address) {
        assertFalse(address + " should not be a name", address.isName());
        assertNotNull(address + " should have an Ip6Address", address.isIpAddress().isIp6());
    }

    public void testToString() {
        final String address = "address";
        assertEquals(address, HostAddress.with(address).toString());
    }

    @Override
    protected Class<HostAddress> type() {
        return HostAddress.class;
    }

    static public void assertEquals(final String message, final byte[] expected, final byte[] actual) {
        if (false == Arrays.equals(expected, actual)) {
            failNotEquals(message, HostAddressTest.toHex(expected), HostAddressTest.toHex(actual));
        }
    }

    static private String toHex(final byte[] bytes) {
        return HostAddressTesting.toHexString(bytes);
    }
}
