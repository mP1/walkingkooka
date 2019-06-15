/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.net;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.compare.ComparableTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ConstantsTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.JavaVisibility;

import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public final class IpPortTest implements ClassTesting2<IpPort>,
        ConstantsTesting<IpPort>,
        ComparableTesting<IpPort>,
        SerializationTesting<IpPort>,
        ToStringTesting<IpPort> {

    @Test
    public void testIsPort() {
        assertTrue(IpPort.isPort(0));
        assertTrue(IpPort.isPort(80));
        assertTrue(IpPort.isPort(65535));
        assertFalse(IpPort.isPort(-1));
        assertFalse(IpPort.isPort(65536));
    }

    @Test
    public void testPortAboveRangeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            IpPort.with(65536);
        });
    }

    @Test
    public void testPortBelowRangeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            IpPort.with(-1);
        });
    }

    @Test
    public void testWith() {
        final int port = 65000;
        final IpPort ipPort = IpPort.with(port);
        assertEquals(port, ipPort.value(), "value");
    }

    @Test
    public void testNonConstantsNotSingletons() {
        final int port = 65000;
        assertNotSame(IpPort.with(port), IpPort.with(port));
    }

    @Test
    public void testFree() {
        assertNotNull(IpPort.free());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(IpPort.HTTP, "80");
    }

    @Override
    public Class<IpPort> type() {
        return IpPort.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public Set<IpPort> intentionalDuplicateConstants() {
        return Sets.empty();
    }

    @Test
    public void testHttpSingleton() throws Exception {
        this.serializeSingletonAndCheck(IpPort.HTTP);
    }

    @Test
    public void testHttpsSingleton() throws Exception {
        this.serializeSingletonAndCheck(IpPort.HTTPS);
    }

    @Override
    public IpPort serializableInstance() {
        return IpPort.with(65000);
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }

    private final static int PORT = 80;

    @Test
    public void testDifferentPort() {
        this.checkNotEquals(IpPort.HTTPS);
    }

    @Test
    public void testEqual() {
        this.checkEqualsAndHashCode(IpPort.with(PORT));
    }

    @Test
    public void testLess() {
        this.compareToAndCheckLess(IpPort.HTTPS);
    }

    @Test
    public void testInSortedSet() {
        final IpPort one = IpPort.with(1);
        final IpPort two = IpPort.with(2);
        final IpPort three = IpPort.with(3);

        final Set<IpPort> set = Sets.sorted();
        set.add(one);
        set.add(two);
        set.add(three);

        final Iterator<IpPort> values = set.iterator();
        assertEquals(one, values.next());
        assertEquals(two, values.next());
        assertEquals(three, values.next());
    }

    @Override
    public IpPort createComparable() {
        return IpPort.HTTP;
    }
}
