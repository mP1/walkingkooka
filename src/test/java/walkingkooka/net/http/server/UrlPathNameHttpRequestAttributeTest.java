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

package walkingkooka.net.http.server;

import org.junit.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public final class UrlPathNameHttpRequestAttributeTest extends ClassTestCase<UrlPathNameHttpRequestAttribute> {

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidIndexFails() {
        UrlPathNameHttpRequestAttribute.with(-1);
    }

    @Test
    public void testCache() {
        assertSame(UrlPathNameHttpRequestAttribute.with(0), UrlPathNameHttpRequestAttribute.with(0));
    }

    @Test
    public void testUncached() {
        final int index = UrlPathNameHttpRequestAttribute.CONSTANT_COUNT;
        assertNotSame(UrlPathNameHttpRequestAttribute.with(index), UrlPathNameHttpRequestAttribute.with(index));
    }

    @Test
    public void testToString() {
        assertEquals("path-0", UrlPathNameHttpRequestAttribute.with(0).toString());
    }

    @Override
    protected Class<UrlPathNameHttpRequestAttribute> type() {
        return UrlPathNameHttpRequestAttribute.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
