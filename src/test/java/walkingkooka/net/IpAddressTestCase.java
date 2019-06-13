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
import walkingkooka.test.SerializationTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract public class IpAddressTestCase<A extends IpAddress & Comparable<A>> implements ClassTesting2<A>,
        ComparableTesting<A>,
        SerializationTesting<A>,
        ToStringTesting<A>,
        TypeNameTesting<A> {

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
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public final boolean serializableInstanceIsSingleton() {
        return false;
    }

    // TypeNameTesting .........................................................................................

    @Override
    public final String typeNamePrefix() {
        return "Ip";
    }

    @Override
    public final String typeNameSuffix() {
        return "Address";
    }
}
