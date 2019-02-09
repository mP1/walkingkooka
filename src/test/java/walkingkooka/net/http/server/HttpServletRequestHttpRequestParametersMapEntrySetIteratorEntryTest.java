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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.EntryTesting;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.type.MemberVisibility;

import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntryTest
        implements ClassTesting2<HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry>,
        EntryTesting<HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry, HttpRequestParameterName, List<String>>,
        HashCodeEqualsDefinedTesting<HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry> {

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

    @Test
    public void testSetValueFails() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createEntry().setValue(Lists.empty());
        });
    }

    @Test
    public void testEqualsDifferentKey() {
        this.checkNotEquals(HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry.with(this.entry("different", VALUE1, VALUE2)));
    }

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry.with(this.entry(KEY, "different-value")));
    }

    @Override public HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry createEntry() {
        return HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry.with(this.entry(KEY, VALUE1, VALUE2));
    }

    private Entry<String, String[]> entry(final String key, final String... values) {
        return Maps.entry(key, values.clone());
    }

    @Override
    public Class<HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry> type() {
        return HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry.class;
    }

    @Override
    public HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry createObject() {
        return HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry.with(this.entry(KEY, VALUE1, VALUE2));
    }

    @Override
    public String typeNamePrefix() {
        return HttpServletRequestHttpRequestParametersMapEntrySet.class.getSimpleName();
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
