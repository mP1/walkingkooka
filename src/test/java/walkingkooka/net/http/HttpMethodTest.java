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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.PublicClassTestCase;
import walkingkooka.type.FieldAttributes;
import walkingkooka.type.MemberVisibility;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;


final public class HttpMethodTest extends PublicClassTestCase<HttpMethod> {

    @Test(expected = NullPointerException.class)
    public void testWithNullFails() {
        HttpMethod.with(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyFails() {
        HttpMethod.with("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithWhitespaceFails() {
        HttpMethod.with("   ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNonLetterFails() {
        HttpMethod.with("METH1");
    }

    @Test
    public void testWith() {
        final String value = HttpMethod.GET.value();
        final HttpMethod method = HttpMethod.with(value);
        assertSame("value", value, method.value());
        assertSame(HttpMethod.GET, method);
    }

    @Test
    public void testCustom() {
        final String value = "XYZ";
        final HttpMethod method = HttpMethod.with(value);
        assertSame("value", value, method.value());
        assertNotSame(method, HttpMethod.with(value));
    }

    @Test
    public void testDifferentCaseGet() {
        assertSame(HttpMethod.GET, HttpMethod.with("GeT"));
    }

    @Test
    public void testHead() {
        this.singleton(HttpMethod.HEAD);
    }

    @Test
    public void testGet() {
        this.singleton(HttpMethod.GET);
    }

    @Test
    public void testPost() {
        this.singleton(HttpMethod.POST);
    }

    @Test
    public void testPut() {
        this.singleton(HttpMethod.PUT);
    }

    @Test
    public void testDelete() {
        this.singleton(HttpMethod.DELETE);
    }

    @Test
    public void testTrace() {
        this.singleton(HttpMethod.TRACE);
    }

    @Test
    public void testOptions() {
        this.singleton(HttpMethod.OPTIONS);
    }

    public void testPatch() {
        this.singleton(HttpMethod.PATCH);
    }

    @Test
    public void testConnect() {
        this.singleton(HttpMethod.CONNECT);
    }

    private void singleton(final HttpMethod method) {
        assertSame(
                "Expected singleton rather than new instance when invoking HttpMethod.with(HttpMethod.value())",
                method,
                HttpMethod.with(method.value()));
    }

    @Test
    public void testConstantsAllUnique() throws Exception {
        final Set<HttpMethod> methods = Sets.hash();
        for (final Field constant : HttpMethod.class.getDeclaredFields()) {
            if (false == constant.getType().equals(HttpMethod.class)) {
                continue;
            }
            assertTrue(constant + " is NOT public", MemberVisibility.PUBLIC.is(constant));
            assertTrue(constant + " is NOT static", FieldAttributes.STATIC.is(constant));

            final HttpMethod value = (HttpMethod) constant.get(null);
            if (false == methods.add(value)) {
                Assert.fail("Duplicate header constant=" + value);
            }
        }
    }

    @Test
    public void testToString() {
        assertEquals("GET", HttpMethod.GET.toString());
    }

    @Override
    protected Class<HttpMethod> type() {
        return HttpMethod.class;
    }
}
