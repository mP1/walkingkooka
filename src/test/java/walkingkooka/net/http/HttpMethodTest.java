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

package walkingkooka.net.http;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.compare.ComparableTesting;
import walkingkooka.net.header.HeaderValueTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ConstantsTesting;
import walkingkooka.type.FieldAttributes;
import walkingkooka.type.JavaVisibility;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


final public class HttpMethodTest implements ClassTesting2<HttpMethod>,
        HeaderValueTesting<HttpMethod>,
        ComparableTesting<HttpMethod>,
        ConstantsTesting<HttpMethod> {

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            HttpMethod.with(null);
        });
    }

    @Test
    public void testWithEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            HttpMethod.with("");
        });
    }

    @Test
    public void testWithWhitespaceFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            HttpMethod.with("   ");
        });
    }

    @Test
    public void testWithNonLetterFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            HttpMethod.with("METH1");

        });
    }

    @Test
    public void testWith() {
        final String value = HttpMethod.GET.value();
        final HttpMethod method = HttpMethod.with(value);
        assertSame(value, method.value(), "value");
        assertSame(HttpMethod.GET, method);
    }

    @Test
    public void testCustom() {
        final String value = "XYZ";
        final HttpMethod method = HttpMethod.with(value);
        assertSame(value, method.value(), "value");
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
        assertSame(method,
                HttpMethod.with(method.value()),
                "Expected singleton rather than new instance when invoking HttpMethod.with(HttpMethod.value())");
    }

    @Test
    public void testIsGetOrHead() throws Exception {
        this.constants().forEach(this::isGetOrHeadCheck);
    }

    private void isGetOrHeadCheck(final HttpMethod constant) {
        assertEquals(HttpMethod.GET == constant || HttpMethod.HEAD == constant,
                constant.isGetOrHead(),
                constant + ".isGetOrHead test");
    }

    private Set<HttpMethod> constants() throws Exception {
        final Set<HttpMethod> methods = Sets.hash();
        for (final Field constant : HttpMethod.class.getDeclaredFields()) {
            if (false == constant.getType().equals(HttpMethod.class)) {
                continue;
            }
            assertTrue(JavaVisibility.PUBLIC.is(constant), constant + " is NOT public");
            assertTrue(FieldAttributes.STATIC.is(constant), constant + " is NOT static");

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
        this.toStringAndCheck(HttpMethod.GET, "GET");
    }

    // Comparable..........................................................................................

    @Test
    public void testCompareBefore() {
        this.compareToAndCheckLess(HttpMethod.POST); // GET < POST
    }

    @Test
    public void testCompareAfter() {
        this.compareToAndCheckMore(HttpMethod.DELETE); // GET > DELETE
    }

    @Override
    public HttpMethod createHeaderValue() {
        return HttpMethod.GET;
    }

    @Override
    public boolean isMultipart() {
        return false;
    }

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    // ClassTesting.................................................................................

    @Override
    public Class<HttpMethod> type() {
        return HttpMethod.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // ConstantsTesting.................................................................................

    @Override
    public Set<HttpMethod> intentionalDuplicateConstants() {
        return Sets.empty();
    }

    // ComparableTesting.................................................................................

    @Override
    public HttpMethod createObject() {
        return this.createComparable();
    }

    @Override
    public HttpMethod createComparable() {
        return HttpMethod.GET;
    }
}
