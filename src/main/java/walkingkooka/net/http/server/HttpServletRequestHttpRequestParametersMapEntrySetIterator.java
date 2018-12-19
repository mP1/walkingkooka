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

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

final class HttpServletRequestHttpRequestParametersMapEntrySetIterator implements Iterator<Entry<HttpRequestParameterName, List<String>>> {

    static HttpServletRequestHttpRequestParametersMapEntrySetIterator with(final Iterator<Entry<String, String[]>> iterator) {
        return new HttpServletRequestHttpRequestParametersMapEntrySetIterator(iterator);
    }

    private HttpServletRequestHttpRequestParametersMapEntrySetIterator(final Iterator<Entry<String, String[]>> iterator) {
        super();
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public Entry<HttpRequestParameterName, List<String>> next() {
        return HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry.with(this.iterator.next());
    }

    private final Iterator<Entry<String, String[]>> iterator;

    @Override
    public String toString() {
        return this.iterator.toString();
    }
}
