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
import walkingkooka.collect.set.Sets;
import walkingkooka.net.header.HeaderValueTestCase;
import walkingkooka.test.ConstantsTesting;
import walkingkooka.type.FieldAttributes;
import walkingkooka.type.MemberVisibility;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;


final public class HttpMethodTest extends HeaderValueTestCase<HttpMethod> implements ConstantsTesting<HttpMethod> {

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
    public void testIsGetOrHead() throws Exception {
        this.constants().stream()
                .forEach(this::isGetOrHeadCheck);
    }

    private void isGetOrHeadCheck(final HttpMethod constant) {
        assertEquals(constant + ".isGetOrHead test",
                HttpMethod.GET == constant || HttpMethod.HEAD == constant,
                constant.isGetOrHead());
    }

    private Set<HttpMethod> constants() throws Exception {
        final Set<HttpMethod> methods = Sets.hash();
        for (final Field constant : HttpMethod.class.getDeclaredFields()) {
            if (false == constant.getType().equals(HttpMethod.class)) {
                continue;
            }
            assertTrue(constant + " is NOT public", MemberVisibility.PUBLIC.is(constant));
            assertTrue(constant + " is NOT static", FieldAttributes.STATIC.is(constant));

            methods.add((HttpMethod) constant.get(null));
        }
        return methods;
    }

    @Test
    public void testHeaderText() {
        this.toHeaderTextAndCheck2("get", "GET");
    }

    @Test
    public void testHeaderText2() {
        this.toHeaderTextAndCheck2("xcustom", "XCUSTOM");
    }

    private void toHeaderTextAndCheck2(final String text,
                                       final String headerText) {
        this.toHeaderTextAndCheck(HttpMethod.with(text), headerText);
    }

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(HttpMethod.POST);
    }

    @Test
    public void testToString() {
        assertEquals("GET", HttpMethod.GET.toString());
    }

    @Override
    protected HttpMethod createHeaderValue() {
        return HttpMethod.GET;
    }

    @Override
    protected boolean isMultipart() {
        return false;
    }

    @Override
    protected boolean isRequest() {
        return true;
    }

    @Override
    protected boolean isResponse() {
        return true;
    }

    @Override
    public Class<HttpMethod> type() {
        return HttpMethod.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public Set<HttpMethod> intentionalDuplicateConstants() {
        return Sets.empty();
    }
}
