/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.collect.iterator;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class ReadOnlyIteratorTest extends IteratorTestCase<ReadOnlyIterator<Object>, Object> {

    // constants

    private final static Object ELEMENT = "element";

    // tests

    @Test
    public void testWrapNullIteratorFails() {
        assertThrows(NullPointerException.class, () -> ReadOnlyIterator.wrap(null));
    }

    @Test
    public void testWrap() {
        final Iterator<Void> iterator = Iterators.fake();
        final ReadOnlyIterator<Void> readOnly = ReadOnlyIterator.wrap(iterator);
        assertSame(iterator, readOnly.iterator, "iterator");
    }

    @Test
    public void testDoesntWrapReadOnlyIterator() {
        final Iterator<Void> iterator = Iterators.fake();
        final ReadOnlyIterator<Void> readOnly
            = ReadOnlyIterator.wrap(ReadOnlyIterator.wrap(iterator));
        assertSame(iterator, readOnly.iterator, "iterator");
    }

    @Test
    public void testHasNextWhenEmpty() {
        this.hasNextCheckFalse(this.iterator());
    }

    @Test
    public void testHasNext() {
        this.hasNextCheckTrue(this.iterator(ELEMENT));
    }

    @Test
    public void testNext() {
        assertSame(ELEMENT,
            this.iterator(ELEMENT).next());
    }

    @Test
    public void testRemove() {
        this.removeUnsupportedFails(ReadOnlyIterator.wrap(this.iterator(ELEMENT)));
    }

    @Test
    public void testToString() {
        final Iterator<Object> iterator
            = new ArrayList<>(Lists.of(ELEMENT)).iterator();
        this.toStringAndCheck(ReadOnlyIterator.wrap(iterator), iterator.toString());
    }

    private ReadOnlyIterator<Object> iterator(final Object... strings) {
        return ReadOnlyIterator.wrap(new ArrayList<>(Lists.of(strings)).iterator());
    }

    @Override
    public ReadOnlyIterator<Object> createIterator() {
        return ReadOnlyIterator.wrap(Iterators.fake());
    }

    @Override
    public Class<ReadOnlyIterator<Object>> type() {
        return Cast.to(ReadOnlyIterator.class);
    }
}
