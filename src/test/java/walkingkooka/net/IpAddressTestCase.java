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

import static org.junit.Assert.assertNotSame;

abstract public class IpAddressTestCase<A extends IpAddress> extends HashCodeEqualsDefinedTestCase<A> {

    IpAddressTestCase() {
        super();
    }

    @Test(expected = NullPointerException.class)
    public final void testWithNullFails() {
        this.createAddress(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testWithEmptyComponentsFails() {
        this.createAddress(new byte[0]);
    }

    @Test
    public final void testWith() {
        final byte[] components = new byte[this.bitCount() / 8];
        final A address = this.createAddress(components);
        final byte[] value = address.value();
        assertEquals("value", components, value);
        assertNotSame("value", components, value);
        assertNotSame("value should not be cached.", components, address.value());
    }

    final A createAddress() {
        return this.createAddress(new byte[this.bitCount() / 8]);
    }

    abstract A createAddress(byte[] components);

    abstract int bitCount();
}
