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
import walkingkooka.collect.map.EntryTestCase;
import walkingkooka.net.http.HttpTransport;
import walkingkooka.test.HashCodeEqualsDefinedTesting;

import static org.junit.Assert.assertEquals;

public final class RouterHttpRequestParametersMapEntryTest extends EntryTestCase<RouterHttpRequestParametersMapEntry, HttpRequestAttribute<?>, Object>
        implements HashCodeEqualsDefinedTesting<RouterHttpRequestParametersMapEntry> {

    private final static HttpRequestAttribute<?> KEY = HttpRequestAttributes.TRANSPORT;
    private final static Object VALUE = HttpTransport.SECURED;

    @Test
    public void testWith() {
        this.getKeyAndValueAndCheck(this.createEntry(), KEY, VALUE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetValueFails() {
        this.createEntry().setValue(VALUE);
    }

    @Test
    public void testEqualsDifferentKey() {
        this.checkNotEquals(RouterHttpRequestParametersMapEntry.with(HttpRequestAttributes.METHOD, VALUE));
    }

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(RouterHttpRequestParametersMapEntry.with(KEY, "different"));
    }

    @Test
    public void testToString() {
        assertEquals("TRANSPORT=SECURED", this.createEntry().toString());
    }

    @Override
    protected RouterHttpRequestParametersMapEntry createEntry() {
        return RouterHttpRequestParametersMapEntry.with(KEY, VALUE);
    }

    @Override
    protected Class<RouterHttpRequestParametersMapEntry> type() {
        return RouterHttpRequestParametersMapEntry.class;
    }

    @Override
    public RouterHttpRequestParametersMapEntry createObject() {
        return this.createEntry();
    }
}
