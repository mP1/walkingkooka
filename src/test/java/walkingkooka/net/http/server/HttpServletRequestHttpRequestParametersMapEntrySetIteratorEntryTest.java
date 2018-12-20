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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.EntryTestCase;

import java.util.List;
import java.util.Map.Entry;

import static org.junit.Assert.assertEquals;

public final class HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntryTest extends EntryTestCase
        <HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry,
                HttpRequestParameterName,
                List<String>> {
    private final static String KEY = "parameter1";
    private final static String VALUE1 = "value1";
    private final static String VALUE2 = "value2";

    @Test
    public void testGetKey() {
        this.getKeyAndCheck(this.createEntry(), HttpRequestParameterName.with(KEY));
    }

    @Test
    public void testGetValue() {
        this.getValueAndCheck(this.createEntry(), Lists.of(VALUE1, VALUE2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetValueFails() {
        this.createEntry().setValue(Lists.empty());
    }

    @Test
    public void testToString() {
        assertEquals("parameter1=\"value1\", \"value2\"", this.createEntry().toString());
    }

    @Override
    protected HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry createEntry() {
        return HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry.with(this.entry(KEY, VALUE1, VALUE2));
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

    @Override
    protected Class<HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry> type() {
        return HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry.class;
    }
}
