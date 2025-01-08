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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class ReverseIteratorTest extends IteratorTestCase<ReverseIterator<Object>, Object> {

    // tests

    @Test
    public void testWithNullIteratorFails() {
        assertThrows(NullPointerException.class, () -> ReverseIterator.with(null));
    }

    @Test
    public void testHasNextWhenEmpty() {
        assertFalse(this.iterator().hasNext());
    }

    @Test
    public void testNextWhenEmpty() {
        this.nextFails(this.iterator());
    }

    @Test
    public void testIterateOverNonEmptyHasNext() {
        this.iterateUsingHasNextAndCheck(this.iterator("1", "2", "3"), "3", "2", "1");
    }

    @Test
    public void testIterateOverNonEmpty() {
        this.iterateAndCheck(this.iterator("1", "2", "3"), "3", "2", "1");
    }

    @Test
    public void testReverseReversedIterator() {
        final Iterator<String> iterator = ReverseIterator.with(this.iterator("1", "2", "3"));
        assertFalse(iterator instanceof ReverseIterator,
            "Iterator should not be a ReverseIterator=" + iterator.getClass().getName());
        this.iterateAndCheck(iterator, "1", "2", "3");
    }

    @Test
    public void testRemove() {
        this.removeUnsupportedFails(ReverseIterator.with(this.iterator("1")));
    }

    private ReverseIterator<String> iterator(final String... strings) {
        return Cast.to(ReverseIterator.with(new ArrayList<>(Lists.of(strings)).iterator()));
    }

    @Override
    public ReverseIterator<Object> createIterator() {
        return Cast.to(ReverseIterator.with(Iterators.fake()));
    }

    @Override
    public Class<ReverseIterator<Object>> type() {
        return Cast.to(ReverseIterator.class);
    }
}
