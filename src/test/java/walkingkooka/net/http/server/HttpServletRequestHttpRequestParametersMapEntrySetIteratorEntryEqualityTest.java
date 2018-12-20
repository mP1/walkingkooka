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
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

import java.util.Map.Entry;

public final class HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntryEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry> {

    private final static String KEY = "parameter1";
    private final static String VALUE1 = "value1";
    private final static String VALUE2 = "value2";

    @Test
    public void testDifferentKey() {
        this.checkNotEquals(HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry.with(this.entry("different", VALUE1, VALUE2)));
    }

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry.with(this.entry(KEY, "different-value")));
    }

    @Override
    protected HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry createObject() {
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
}
