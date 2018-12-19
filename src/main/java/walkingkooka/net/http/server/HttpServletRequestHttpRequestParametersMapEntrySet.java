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

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The {@link Set} view of all entries in a parameters {@link Map}.
 */
final class HttpServletRequestHttpRequestParametersMapEntrySet extends AbstractSet<Entry<HttpRequestParameterName, List<String>>> {

    static HttpServletRequestHttpRequestParametersMapEntrySet with(final Set<Entry<String, String[]>> parameters) {
        return new HttpServletRequestHttpRequestParametersMapEntrySet(parameters);
    }

    private HttpServletRequestHttpRequestParametersMapEntrySet(final Set<Entry<String, String[]>> parameters) {
        super();
        this.parameters = parameters;
    }

    @Override
    public Iterator<Entry<HttpRequestParameterName, List<String>>> iterator() {
        return HttpServletRequestHttpRequestParametersMapEntrySetIterator.with(this.parameters.iterator());
    }

    @Override
    public int size() {
        return this.parameters.size();
    }

    private final Set<Entry<String, String[]>> parameters;
}
