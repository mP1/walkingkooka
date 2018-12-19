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
import walkingkooka.collect.iterator.Iterators;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.Assert.assertEquals;

public final class HttpServletRequestHttpRequestParametersMapEntrySetIteratorTest extends
        IteratorTestCase<HttpServletRequestHttpRequestParametersMapEntrySetIterator,
                Entry<HttpRequestParameterName, List<String>>> {

    private final static String KEY1 = "parameter1";
    private final static String VALUE1A = "value1a";
    private final static String VALUE1B = "value1b";

    private final static String KEY2 = "parameter2";
    private final static String VALUE2 = "value2";

    @Test
    public void testIterate() {
        this.iterateAndCheck(this.createIterator(),
                HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry.with(this.entry(KEY1, VALUE1A, VALUE1B)),
                HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry.with(this.entry(KEY2, VALUE2)));
    }

    @Test
    public void testToString() {
        assertEquals("[parameter1=[value1a, value1b], parameter2=[value2]]",
                this.createIterator().toString());
    }

    @Override
    protected HttpServletRequestHttpRequestParametersMapEntrySetIterator createIterator() {
        return HttpServletRequestHttpRequestParametersMapEntrySetIterator.with(Iterators.array(
                this.entry(KEY1, VALUE1A, VALUE1B),
                this.entry(KEY2, VALUE2)
        ));
    }

    private Entry entry(final String key, final String... values) {
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

            @Override
            public String toString() {
                return key + "=" + Arrays.toString(values);
            }
        };
    }

    @Override
    protected Class<HttpServletRequestHttpRequestParametersMapEntrySetIterator> type() {
        return HttpServletRequestHttpRequestParametersMapEntrySetIterator.class;
    }
}
