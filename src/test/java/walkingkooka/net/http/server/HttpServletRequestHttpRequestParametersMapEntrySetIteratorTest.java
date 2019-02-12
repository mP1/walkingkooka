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
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.type.MemberVisibility;

import java.util.Iterator;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class HttpServletRequestHttpRequestParametersMapEntrySetIteratorTest extends ClassTestCase<HttpServletRequestHttpRequestParametersMapEntrySetIterator>
        implements IteratorTesting,
        ToStringTesting<HttpServletRequestHttpRequestParametersMapEntrySetIterator>,
        TypeNameTesting<HttpServletRequestHttpRequestParametersMapEntrySetIterator> {

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

    private HttpServletRequestHttpRequestParametersMapEntrySetIterator createIterator() {
        return HttpServletRequestHttpRequestParametersMapEntrySetIterator.with(Iterators.array(
                this.entry(KEY1, VALUE1A, VALUE1B),
                this.entry(KEY2, VALUE2)
        ));
    }

    private Entry<String, String[]> entry(final String key, final String... values) {
        return Maps.entry(key, values.clone());
    }

    @Override
    public Class<HttpServletRequestHttpRequestParametersMapEntrySetIterator> type() {
        return HttpServletRequestHttpRequestParametersMapEntrySetIterator.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return HttpServletRequestHttpRequestParametersMapEntrySet.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return Iterator.class.getSimpleName();
    }
}
