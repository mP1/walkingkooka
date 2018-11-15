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

package walkingkooka.net.http;


import org.junit.Test;
import walkingkooka.test.PublicClassTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class HttpProtocolTest extends PublicClassTestCase<HttpProtocol> {

    @Test
    public void testOneZero() {
        assertEquals("HTTP/1.0", HttpProtocol.VERSION_1_0.value());
    }

    @Test
    public void testOneOne() {
        assertEquals("HTTP/1.1", HttpProtocol.VERSION_1_1.value());
    }

    @Test(expected = NullPointerException.class)
    public void testFromNullHeaderFails() {
        HttpProtocol.fromHeader(null);
    }

    @Test
    public void testFromOneZero() {
        assertSame(HttpProtocol.VERSION_1_0, HttpProtocol.fromHeader("HTTP/1.0"));
    }

    @Test
    public void testFromOneOne() {
        assertSame(HttpProtocol.VERSION_1_1, HttpProtocol.fromHeader("HTTP/1.1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromUnknownFails() {
        HttpProtocol.fromHeader("unknown/???");
    }

    @Test
    public void testToStringVersion0() {
        assertEquals("HTTP/1.0", HttpProtocol.VERSION_1_0.toString());
    }

    @Test
    public void testToStringVersion1() {
        assertEquals("HTTP/1.1", HttpProtocol.VERSION_1_1.toString());
    }

    @Override
    protected Class<HttpProtocol> type() {
        return HttpProtocol.class;
    }
}
