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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.header.ClientCookie;
import walkingkooka.net.header.Cookie;
import walkingkooka.net.header.CookieName;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.type.MemberVisibility;

import java.util.Iterator;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class RouterHttpRequestParametersMapCookiesEntryIteratorTest implements ClassTesting2<RouterHttpRequestParametersMapCookiesEntryIterator>,
        IteratorTesting,
        ToStringTesting<RouterHttpRequestParametersMapCookiesEntryIterator>,
        TypeNameTesting<RouterHttpRequestParametersMapCookiesEntryIterator> {

    private final static ClientCookie COOKIE1 = Cookie.client(CookieName.with("a"), "1");
    private final static ClientCookie COOKIE2 = Cookie.client(CookieName.with("b"), "2");

    @Test
    public void testHasNextAndNextConsumeEmpty() {
        this.iterateAndCheck(true);
    }

    @Test
    public void testHasNextAndNextConsumeOneCookie() {
        this.iterateAndCheck(true, COOKIE1);
    }

    @Test
    public void testHasNextAndNextConsume() {
        this.iterateAndCheck(true, COOKIE1, COOKIE2);
    }

    @Test
    public void testHasNextAndNextConsume2() {
        this.iterateAndCheck(true, COOKIE2, COOKIE1);
    }

    @Test
    public void testNextOnlyConsume() {
        this.iterateAndCheck(false, COOKIE2, COOKIE1);
    }

    private void iterateAndCheck(final boolean checkHasNext, final ClientCookie... cookies) {
        final RouterHttpRequestParametersMapCookiesEntryIterator iterator = this.createIterator(cookies);

        for (int i = 0; i < cookies.length; i++) {
            if (checkHasNext) {
                this.hasNextCheckTrue(iterator, "iterator should have " + (cookies.length - i) + " entries left");
            }
            this.checkNext(iterator, cookies[i]);
        }

        this.hasNextCheckFalse(iterator);
        this.nextFails(iterator);
    }

    private void checkNext(final RouterHttpRequestParametersMapCookiesEntryIterator iterator,
                           final ClientCookie cookie) {
        final Entry<HttpRequestAttribute<?>, Object> entry = iterator.next();
        assertEquals(cookie.name(), entry.getKey(), "key");
        assertEquals(cookie, entry.getValue(), "value");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createIterator(COOKIE1), "a=1;");
    }

    @Test
    public void testToStringEmpty() {
        final RouterHttpRequestParametersMapCookiesEntryIterator iterator = this.createIterator(COOKIE1);
        iterator.next();
        this.toStringAndCheck(iterator, "");
    }

    private RouterHttpRequestParametersMapCookiesEntryIterator createIterator(final ClientCookie... cookies) {
        return RouterHttpRequestParametersMapCookiesEntryIterator.with(Lists.of(cookies));
    }

    @Override
    public Class<RouterHttpRequestParametersMapCookiesEntryIterator> type() {
        return RouterHttpRequestParametersMapCookiesEntryIterator.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return RouterHttpRequestParametersMap.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return Iterator.class.getSimpleName();
    }
}
