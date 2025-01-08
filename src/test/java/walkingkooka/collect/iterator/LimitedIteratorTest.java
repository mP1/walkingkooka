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

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final public class LimitedIteratorTest extends IteratorTestCase<LimitedIterator<String>, String> {

    // constants

    private final static Iterator<String> ITERATOR = Iterators.fake();

    private final static int COUNT = 2;

    // tests

    @Test
    public void testWrapNullIteratorFails() {
        assertThrows(NullPointerException.class, () -> LimitedIterator.wrap((Iterator<String>) null, COUNT));
    }

    @Test
    public void testWrapNegativeCountFails() {
        assertThrows(IllegalArgumentException.class, () -> LimitedIterator.wrap(ITERATOR, -1));
    }

    @Test
    public void testWrapAnotherLimitedIteratorWithLessCount() {
        final LimitedIterator<String> limited = this.createIterator();
        assertSame(limited, LimitedIterator.wrap(limited, COUNT));
    }

    @Test
    public void testWrapAnotherLimitedIteratorWithLessCount2() {
        final LimitedIterator<String> limited = this.createIterator();
        assertSame(limited, LimitedIterator.wrap(limited, COUNT + 1));
    }

    @Test
    public void testWrapAnotherLimitedIteratorWithMoreCount2() {
        final LimitedIterator<String> limited = this.createIterator();
        final LimitedIterator<String> limited2 = LimitedIterator.wrap(limited, 1);
        assertSame(limited.iterator, limited2.iterator, "iterator");
        assertSame(1, limited2.countdown, "countdown");
    }

    @Test
    public void testExhaustUsingHasNextNext() {
        final LimitedIterator<String> iterator = this.createIterator();
        assertTrue(iterator.hasNext());
        assertSame("1", iterator.next());

        assertTrue(iterator.hasNext());
        assertSame("2", iterator.next());

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    public void testExhaustUsingNext() {
        final LimitedIterator<String> iterator = this.createIterator();
        assertSame("1", iterator.next());
        assertSame("2", iterator.next());
        this.nextFails(iterator);
        this.removeUnsupportedFails(iterator);
    }

    @Test
    public void testRemove() {
        final List<String> source = Lists.array();
        source.add("1-removed");
        source.add("2");
        source.add("3");
        final LimitedIterator<String> iterator = LimitedIterator.wrap(source.iterator(),
            COUNT);
        iterator.next();
        iterator.remove();
        this.checkEquals(Lists.of("2", "3"), source);

        assertSame("2", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(LimitedIterator.wrap(ITERATOR, COUNT), "at most 2 " + ITERATOR);
    }

    @Override
    public LimitedIterator<String> createIterator() {
        return LimitedIterator.wrap(Lists.of("1",
            "2",
            "should never be returned1",
            "should never be returned2").iterator(), COUNT);
    }

    @Override
    public Class<LimitedIterator<String>> type() {
        return Cast.to(LimitedIterator.class);
    }
}
