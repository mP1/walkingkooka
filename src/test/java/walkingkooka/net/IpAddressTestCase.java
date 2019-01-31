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
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.SerializationTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract public class IpAddressTestCase<A extends IpAddress & Comparable<A>> extends ClassTestCase<A>
        implements ComparableTesting<A>,
        SerializationTesting<A> {

    IpAddressTestCase() {
        super();
    }

    @Test
    public final void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createAddress(null);
        });
    }

    @Test
    public final void testWithEmptyComponentsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createAddress(new byte[0]);
        });
    }

    @Test
    public final void testWith() {
        final byte[] components = new byte[this.bitCount() / 8];
        final A address = this.createAddress(components);
        final byte[] value = address.value();
        assertArrayEquals(components, value, "value");
        assertNotSame(components, value, "value");
        assertNotSame(components, address.value(), "value should not be cached.");
    }

    final A createAddress() {
        return this.createAddress(new byte[this.bitCount() / 8]);
    }

    abstract A createAddress(byte[] components);

    abstract int bitCount();

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public final boolean serializableInstanceIsSingleton() {
        return false;
    }
}
