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
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;


public final class IpPortTest extends ClassTestCase<IpPort> {

    @Test
    public void testIsPort() {
        assertTrue(IpPort.isPort(0));
        assertTrue(IpPort.isPort(80));
        assertTrue(IpPort.isPort(65535));
        assertFalse(IpPort.isPort(-1));
        assertFalse(IpPort.isPort(65536));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPortAboveRangeFails() {
        IpPort.with(65536);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPortBelowRangeFails() {
        IpPort.with(-1);
    }

    @Test
    public void testWith() {
        final int port = 65000;
        final IpPort ipPort = IpPort.with(port);
        assertEquals("value", port, ipPort.value());
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
        assertEquals("80", IpPort.HTTP.toString());
    }

    @Override
    protected Class<IpPort> type() {
        return IpPort.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
