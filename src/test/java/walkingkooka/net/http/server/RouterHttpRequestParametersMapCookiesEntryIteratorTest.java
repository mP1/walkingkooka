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
import walkingkooka.collect.iterator.IteratorTestCase;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.header.ClientCookie;
import walkingkooka.net.header.Cookie;
import walkingkooka.net.header.CookieName;

import java.util.Map.Entry;

import static org.junit.Assert.assertEquals;

public final class RouterHttpRequestParametersMapCookiesEntryIteratorTest extends
        IteratorTestCase<RouterHttpRequestParametersMapCookiesEntryIterator, Entry<HttpRequestAttribute, Object>> {

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
                this.checkHasNextTrue("iterator should have " + (cookies.length - i) + " entries left", iterator);
            }
            this.checkNext(iterator, cookies[i]);
        }

        this.checkHasNextFalse(iterator);
        this.checkNextFails(iterator);
    }

    private void checkNext(final RouterHttpRequestParametersMapCookiesEntryIterator iterator,
                           final ClientCookie cookie) {
        final RouterHttpRequestParametersMapEntry entry = iterator.next();
        assertEquals("key", cookie.name(), entry.getKey());
        assertEquals("value", cookie, entry.getValue());
    }

    @Test
    public void testToString() {
        final RouterHttpRequestParametersMapCookiesEntryIterator iterator = this.createIterator(COOKIE1);
        assertEquals("a=1;", iterator.toString());
    }

    @Test
    public void testToStringEmpty() {
        final RouterHttpRequestParametersMapCookiesEntryIterator iterator = this.createIterator(COOKIE1);
        iterator.next();
        assertEquals("", iterator.toString());
    }

    @Override
    protected RouterHttpRequestParametersMapCookiesEntryIterator createIterator() {
        return this.createIterator(COOKIE1, COOKIE2);
    }

    private RouterHttpRequestParametersMapCookiesEntryIterator createIterator(final ClientCookie... cookies) {
        return RouterHttpRequestParametersMapCookiesEntryIterator.with(Lists.of(cookies));
    }

    @Override
    protected Class<RouterHttpRequestParametersMapCookiesEntryIterator> type() {
        return RouterHttpRequestParametersMapCookiesEntryIterator.class;
    }
}
