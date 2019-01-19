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

import walkingkooka.net.header.ClientCookie;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 * An {@link Iterator} entry view of all {@link ClientCookie cookies}.
 */
final class RouterHttpRequestParametersMapCookiesEntryIterator implements Iterator<Entry<HttpRequestAttribute<?>, Object>> {

    static RouterHttpRequestParametersMapCookiesEntryIterator with(final List<ClientCookie> cookies) {
        return new RouterHttpRequestParametersMapCookiesEntryIterator(cookies);
    }

    private RouterHttpRequestParametersMapCookiesEntryIterator(final List<ClientCookie> cookies) {
        super();
        this.cookies = cookies;
    }

    @Override
    public boolean hasNext() {
        return this.position < this.cookies.size();
    }

    @Override
    public RouterHttpRequestParametersMapEntry next() {
        final List<ClientCookie> cookies = this.cookies;
        final int position = this.position;
        if (position >= cookies.size()) {
            throw new NoSuchElementException();
        }
        this.position = 1 + position;

        return entry(cookies.get(position));
    }

    private RouterHttpRequestParametersMapEntry entry(final ClientCookie cookie) {
        return RouterHttpRequestParametersMapEntry.with(cookie.name(), cookie);
    }

    private final List<ClientCookie> cookies;

    private int position = 0;

    @Override
    public String toString() {
        final int position = this.position;
        final List<ClientCookie> cookies = this.cookies;

        return position < cookies.size() ?
                cookies.get(position).toString() :
                "";
    }
}
