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
import java.util.Map.Entry;

/**
 * An {@link Iterator} over all {@link HttpRequestAttributes} values as {@link Entry}.
 */
final class RouterHttpRequestParametersMapHttpRequestAttributeEntryIterator implements Iterator<Entry<HttpRequestAttribute, Object>> {

    /**
     * Factory
     */
    static RouterHttpRequestParametersMapHttpRequestAttributeEntryIterator with(final HttpRequest request) {
        return new RouterHttpRequestParametersMapHttpRequestAttributeEntryIterator(request);
    }

    /**
     * Private ctor use factory
     */
    private RouterHttpRequestParametersMapHttpRequestAttributeEntryIterator(final HttpRequest request) {
        super();
        this.request = request;
    }

    @Override
    public boolean hasNext() {
        return this.position < HttpRequestAttributes.size();
    }

    @Override
    public RouterHttpRequestParametersMapEntry next() {
        return HttpRequestAttributes.entry(this.position++, this.request);
    }

    private int position = 0;

    private final HttpRequest request;

    @Override
    public String toString() {
        return HttpRequestAttributes.iteratorEntryToString(this.position, this.request).toString();
    }
}
