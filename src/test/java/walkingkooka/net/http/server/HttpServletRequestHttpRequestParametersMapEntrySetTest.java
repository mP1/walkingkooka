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
import walkingkooka.collect.set.SetTestCase;
import walkingkooka.collect.set.Sets;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class HttpServletRequestHttpRequestParametersMapEntrySetTest extends SetTestCase<HttpServletRequestHttpRequestParametersMapEntrySet,
        Entry<HttpRequestParameterName, List<String>>> {

    private final static String KEY1 = "parameter1";
    private final static String VALUE1A = "value1a";
    private final static String VALUE1B = "value1b";

    private final static String KEY2 = "parameter2";
    private final static String VALUE2 = "value2";

    @Test
    public void testContains() {
        this.containsAndCheck(this.createSet(),
                this.entry(HttpRequestParameterName.with(KEY1), VALUE1A, VALUE1B));
    }

    @Test
    public void testContains2() {
        this.containsAndCheck(this.createSet(),
                this.entry(HttpRequestParameterName.with(KEY2), VALUE2));
    }

    @Test
    public void testToString() {
        assertEquals("[parameter1=\"value1a\", \"value1b\", parameter2=\"value2\"]",
                this.createSet().toString());
    }

    @Override
    protected HttpServletRequestHttpRequestParametersMapEntrySet createSet() {
        final Set<Entry<String, String[]>> parameters = Sets.ordered();
        parameters.add(this.entry(KEY1, VALUE1A, VALUE1B));
        parameters.add(this.entry(KEY2, VALUE2));

        return HttpServletRequestHttpRequestParametersMapEntrySet.with(parameters);
    }

    private Entry<String, String[]> entry(final String key, final String... values) {
        return new Entry<String, String[]>() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public String[] getValue() {
                return values.clone();
            }

            @Override
            public String[] setValue(final String[] value) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry entry(final HttpRequestParameterName key,
                                                                                  final String... values) {
        return HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry.with(this.entry(key.value(), values));
    }

    @Override
    protected Class<HttpServletRequestHttpRequestParametersMapEntrySet> type() {
        return HttpServletRequestHttpRequestParametersMapEntrySet.class;
    }
}
