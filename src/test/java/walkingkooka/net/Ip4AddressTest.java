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

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public final class Ip4AddressTest extends IpAddressTestCase<Ip4Address> {

    @Test(expected = IllegalArgumentException.class)
    public void testMaskNegativeSignificantBitsFails() {
        this.createAddress().subnet(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaskTooManySignificantBitsFails() {
        this.createAddress().subnet(Ip4Address.BIT_COUNT + 1);
    }

    @Test
    public void testSubnet0() {
        this.subnetAndCheck(0, 0);
    }

    @Test
    public void testSubnet1() {
        this.subnetAndCheck(0xFFFFFFFF, 1, 0x80000000);
    }

    @Test
    public void testSubnet2() {
        this.subnetAndCheck(0xFFFFFFFF, 2, 0xC0000000);
    }

    @Test
    public void testSubnet3() {
        this.subnetAndCheck(0xFFFFFFFF, 3, 0xE0000000);
    }

    @Test
    public void testSubnet4() {
        this.subnetAndCheck(0xFFFFFFFF, 4, 0xF0000000);
    }

    @Test
    public void testSubnet5() {
        this.subnetAndCheck(0xFFFFFFFF, 5, 0xF8000000);
    }

    @Test
    public void testSubnet6() {
        this.subnetAndCheck(0xFFFFFFFF, 6, 0xFC000000);
    }

    @Test
    public void testSubnet31() {
        this.subnetAndCheck(0xFFFFFFFF, 31, 0xFFFFFFFE);
    }

    @Test
    public void testSubnet32() {
        final Ip4Address address = Ip4Address.with(Ip4AddressTest.toBytes(0xFFFFFFFF));
        assertSame(address, address.subnet(32));
    }

    @Test
    public void testSubnet12() {
        final Ip4Address address = Ip4Address.with(Ip4AddressTest.toBytes(0xFFF00000));
        assertSame(address, address.subnet(12));
    }

    @Test
    public void testSubnet12b() {
        final Ip4Address address = Ip4Address.with(Ip4AddressTest.toBytes(0x0FF00000));
        assertSame(address, address.subnet(12));
    }

    private void subnetAndCheck(final int significantBits, final int octets) {
        this.subnetAndCheck(this.createAddress(), significantBits, octets);
    }

    private void subnetAndCheck(final int address, final int significantBits, final int octets) {
        this.subnetAndCheck(this.createAddress(Ip4AddressTest.toBytes(address)), significantBits, octets);
    }

    private void subnetAndCheck(final Ip4Address address, final int significantBits, final int octets) {
        final IpAddress masked = address.subnet(significantBits);
        if (false == Arrays.equals(Ip4AddressTest.toBytes(octets), masked.value())) {
            fail(address + " subnet(" + significantBits + ") gave " + masked);
        }
    }

    private static byte[] toBytes(final int value) {
        return Ip4Address.toBytes(value);
    }

    @Test
    public void testDifferent() {
        this.checkNotEquals(Ip4Address.with(new byte[]{5, 6, 7, 8}));
    }

    @Test
    public void testEqualsIp6() {
        this.checkNotEquals(Ip6Address.with(new byte[]{1, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    }

    @Test
    public void testLess() {
        this.compareToAndCheckLess(Ip4Address.with(new byte[]{(byte) 255, 2, 3, 4}));
    }

    @Override
    public Ip4Address createComparable() {
        return Ip4Address.with(new byte[]{1, 2, 3, 4});
    }

    @Test
    public void testToString() {
        assertEquals("1.2.3.4", this.createAddress(new byte[]{1, 2, 3, 4}).toString());
    }

    @Test
    public void testToString2() {
        assertEquals("255.254.253.252",
                this.createAddress(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0xFD, (byte) 0xFC}).toString());
    }

    @Override
    Ip4Address createAddress(final byte[] components) {
        return Ip4Address.with(components);
    }

    @Override
    int bitCount() {
        return Ip4Address.BIT_COUNT;
    }

    @Override
    public Class<Ip4Address> type() {
        return Ip4Address.class;
    }

    @Override
    public Ip4Address serializableInstance() {
        return Ip4Address.with(new byte[]{1, 2, 3, 4});
    }
}
