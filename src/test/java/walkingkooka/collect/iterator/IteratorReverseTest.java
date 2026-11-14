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

final public class IteratorReverseTest extends IteratorTestCase<IteratorReverse<Object>, Object> {

    // tests

    @Test
    public void testWithNullIteratorFails() {
        assertThrows(NullPointerException.class, () -> IteratorReverse.with(null));
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
        final Iterator<String> iterator = IteratorReverse.with(this.iterator("1", "2", "3"));
        assertFalse(iterator instanceof IteratorReverse,
            "Iterator should not be a IteratorReverse=" + iterator.getClass().getName());
        this.iterateAndCheck(iterator, "1", "2", "3");
    }

    @Test
    public void testRemove() {
        this.removeUnsupportedFails(IteratorReverse.with(this.iterator("1")));
    }

    private IteratorReverse<String> iterator(final String... strings) {
        return Cast.to(IteratorReverse.with(new ArrayList<>(Lists.of(strings)).iterator()));
    }

    @Override
    public IteratorReverse<Object> createIterator() {
        return Cast.to(IteratorReverse.with(Iterators.fake()));
    }

    @Override
    public Class<IteratorReverse<Object>> type() {
        return Cast.to(IteratorReverse.class);
    }
}
