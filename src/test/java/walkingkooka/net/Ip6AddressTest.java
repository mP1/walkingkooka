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

import static org.junit.Assert.assertEquals;

public final class Ip6AddressTest extends IpAddressTestCase<Ip6Address> {

    @Test
    public void testToStringWith0SignificantOctets() {
        assertEquals("0::", this.createAddress(new byte[Ip6Address.OCTET_COUNT]).toString());
    }

    @Test
    public void testToStringWith4Octets() {
        assertEquals("1:2:3:4::",
                this.createAddress(new byte[]{1, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}).toString());
    }

    @Test
    public void testToStringWith8SignificantOctets() {
        assertEquals("1:2:3:4:5:6:7:8::",
                this.createAddress(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 0, 0, 0, 0, 0, 0, 0, 0}).toString());
    }

    @Test
    public void testToString() {
        assertEquals("FF:2:3:4:5:6:7:8::",
                this.createAddress(new byte[]{(byte) 0xFF, 2, 3, 4, 5, 6, 7, 8, 0, 0, 0, 0, 0, 0, 0, 0})
                        .toString());
    }

    @Test
    public void testToStringFilled() {
        assertEquals("FF:2:3:4:5:6:7:8:9:FF:1:2:3:4:5:6",
                this.createAddress(new byte[]{(byte) 0xFF, 2, 3, 4, 5, 6, 7, 8, 9, (byte) 0xFF, 1, 2, 3, 4, 5, 6})
                        .toString());
    }

    @Override
    Ip6Address createAddress(final byte[] components) {
        return Ip6Address.with(components);
    }

    @Override
    int bitCount() {
        return Ip6Address.BIT_COUNT;
    }

    @Override
    public Class<Ip6Address> type() {
        return Ip6Address.class;
    }

    @Override
    public Ip6Address serializableInstance() {
        return Ip6Address.with(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 0, 0, 0, 0, 0, 0, 0, 0});
    }
}
