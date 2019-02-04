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
import walkingkooka.naming.PathSeparator;
import walkingkooka.naming.PathTestCase;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.test.SerializationTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class UrlPathTest extends PathTestCase<UrlPath, UrlPathName>
        implements ParseStringTesting<UrlPath>,
        SerializationTesting<UrlPath> {

    @Test
    public void testParseEmpty() {
        this.parseAndCheck("", UrlPath.EMPTY);
    }

    @Override
    public void testParseEmptyFails() {
        // ignored
    }

    @Test
    public void testParseRoot() {
        assertSame(UrlPath.ROOT, this.parseAndCheck("/", UrlPath.ROOT));
    }

    @Test
    public void testParseWithoutLeadingSlash() {
        final UrlPath path = UrlPath.parse("without-leading-slash");
        this.checkValue(path, "/without-leading-slash");
    }

    @Test
    public void testParseWithoutLeadingSlash2() {
        final UrlPath path = UrlPath.parse("without/leading/slash");
        this.checkValue(path, "/without/leading/slash");
        this.checkParent(path, "/without/leading");
    }

    @Test
    public void testParseDeoesntNormalize() {
        final UrlPath path = UrlPath.parse("/a1/removed/../x/y");
        this.checkValue(path, "/a1/removed/../x/y");
        this.checkParent(path, "/a1/removed/../x");
    }

    @Test
    public void testAppendSlash() {
        final UrlPath path = UrlPath.parse("/path");
        final UrlPath pathSlash = path.append(UrlPathName.ROOT);
        this.checkValue(pathSlash, "/path/");
        this.checkParent(pathSlash, "/path");
    }

    @Test
    public void testAppendEmptyPath() {
        final UrlPath path = UrlPath.parse("/path");
        assertEquals(path, path.append(UrlPath.EMPTY));
    }

    @Test
    public void testAppendToEmptyPath() {
        final UrlPath path = UrlPath.parse("/path");
        assertEquals(path, UrlPath.EMPTY.append(path));
    }

    @Test
    public void testAppendToEmptyPath2() {
        final UrlPath path = UrlPath.parse("/");
        assertEquals(path, UrlPath.EMPTY.append(path));
    }

    @Test
    public void testAppendPathOneComponent() {
        final UrlPath a1 = UrlPath.parse("/a1");
        final UrlPath b2 = UrlPath.parse("/b2");
        final UrlPath appended = a1.append(b2);
        this.checkValue(appended, "/a1/b2");
        this.checkParent(appended, a1);
    }

    @Test
    public void testAppendPathTwoComponent() {
        final UrlPath a1 = UrlPath.parse("/a1");
        final UrlPath b2 = UrlPath.parse("/b2/c3");
        final UrlPath appended = a1.append(b2);
        this.checkValue(appended, "/a1/b2/c3");

        this.checkParent(appended, "/a1/b2");
    }

    // addQueryString..................................................................................................

    @Test
    public void testAddQueryStringNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createPath().addQueryString(null);
        });
    }

    @Test
    public void testAddQueryString() {
        final UrlPath path = this.createPath();
        final UrlQueryString queryString = UrlQueryString.with("a=b");
        final RelativeUrl url = path.addQueryString(queryString);
        assertSame(path, url.path(),"path");
        assertSame(queryString, url.query(),"queryString");
        assertEquals(UrlFragment.EMPTY, url.fragment(),"fragment");
    }

    @Test
    public void testConstantsAreSingleton() throws Exception {
        this.constantsAreSingletons();
    }

    @Test
    public void testEqualsDifferentComponent() {
        this.checkNotEquals(UrlPath.parse("/different/path"));
    }

    @Test
    public void testEqualsDifferentName() {
        this.checkNotEquals(UrlPath.parse("/path/different"));
    }

    @Test
    public void testLess() {
        this.compareToAndCheckLess(UrlPath.parse("/path/to/zzzzz"));
    }

    // helpers..................................................................................................

    @Override
    protected UrlPath createPath() {
        return UrlPath.parse("/a1");
    }

    @Override
    protected UrlPathName createName(final int n) {
        final char c = (char)('a' + n);
        return UrlPathName.with( c + "" + n);
    }

    @Override
    protected UrlPath root() {
        return UrlPath.ROOT;
    }

    @Override
    protected UrlPath parsePath(final String name){
        return UrlPath.parse(name);
    }

    @Override
    protected PathSeparator separator() {
        return UrlPath.SEPARATOR;
    }

    @Override
    public Class<UrlPath> type() {
        return UrlPath.class;
    }

    @Override
    public UrlPath createComparable() {
        return UrlPath.parse("/path/to/resource");
    }

    // ParseStringTesting ........................................................................................

    @Override
    public UrlPath parse(final String text) {
        return UrlPath.parse(text);
    }

    @Override
    public RuntimeException parseFailedExpected(final RuntimeException expected) {
        return expected;
    }

    @Override
    public Class<? extends RuntimeException> parseFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

    // SerializationTesting ........................................................................................

    @Override
    public UrlPath serializableInstance() {
        return UrlPath.parse("/path/to");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
