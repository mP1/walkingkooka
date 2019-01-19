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
import walkingkooka.net.UrlPathName;

import java.util.Map.Entry;

import static org.junit.Assert.assertEquals;

public final class RouterHttpRequestParametersMapPathComponentEntryIteratorTest extends
        IteratorTestCase<RouterHttpRequestParametersMapPathComponentEntryIterator, Entry<HttpRequestAttribute<?>, Object>> {

    private final static UrlPathName NAME1 = UrlPathName.with("a");
    private final static UrlPathName NAME2 = UrlPathName.with("b");
    private final static UrlPathName NAME3 = UrlPathName.with("c");

    @Test
    public void testHasNextAndNextConsumeEmpty() {
        this.iterateAndCheck(true);
    }

    @Test
    public void testHasNextAndNextConsumeOneCookie() {
        this.iterateAndCheck(true, NAME1);
    }

    @Test
    public void testHasNextAndNextConsume() {
        this.iterateAndCheck(true, NAME1, NAME2);
    }

    @Test
    public void testHasNextAndNextConsume2() {
        this.iterateAndCheck(true, NAME2, NAME1);
    }

    @Test
    public void testHasNextAndNextConsume3() {
        this.iterateAndCheck(true, NAME1, NAME2, NAME3);
    }

    @Test
    public void testNextOnlyConsume() {
        this.iterateAndCheck(false, NAME2, NAME1);
    }

    private void iterateAndCheck(final boolean checkHasNext, final UrlPathName... names) {
        final RouterHttpRequestParametersMapPathComponentEntryIterator iterator = this.createIterator(names);

        for (int i = 0; i < names.length; i++) {
            if (checkHasNext) {
                this.checkHasNextTrue("iterator should have " + (names.length - i) + " entries left", iterator);
            }
            this.checkNext(iterator, i, names[i]);
        }

        this.checkHasNextFalse(iterator);
        this.checkNextFails(iterator);
    }

    private void checkNext(final RouterHttpRequestParametersMapPathComponentEntryIterator iterator,
                           final int position,
                           final UrlPathName name) {
        final RouterHttpRequestParametersMapEntry entry = iterator.next();
        assertEquals("key", HttpRequestAttributes.pathComponent(position), entry.getKey());
        assertEquals("value", name, entry.getValue());
    }

    @Test
    public void testToString() {
        final RouterHttpRequestParametersMapPathComponentEntryIterator iterator = this.createIterator(NAME1);
        assertEquals("path-0=a", iterator.toString());
    }

    @Test
    public void testToStringEmpty() {
        final RouterHttpRequestParametersMapPathComponentEntryIterator iterator = this.createIterator(NAME1);
        iterator.next();
        assertEquals("", iterator.toString());
    }

    @Override
    protected RouterHttpRequestParametersMapPathComponentEntryIterator createIterator() {
        return this.createIterator(NAME1, NAME2);
    }

    private RouterHttpRequestParametersMapPathComponentEntryIterator createIterator(final UrlPathName... names) {
        return RouterHttpRequestParametersMapPathComponentEntryIterator.with(names);
    }

    @Override
    protected Class<RouterHttpRequestParametersMapPathComponentEntryIterator> type() {
        return RouterHttpRequestParametersMapPathComponentEntryIterator.class;
    }
}
