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
 */

package walkingkooka.collect.iterator;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

final public class LimitedIteratorTest extends IteratorTestCase<LimitedIterator<String>, String> {

    // constants

    private final static Iterator<String> ITERATOR = Iterators.fake();

    private final static int COUNT = 2;

    // tests

    @Test
    public void testWrapNullIteratorFails() {
        this.wrapFails(null, LimitedIteratorTest.COUNT);
    }

    @Test
    public void testWrapNegativeCountFails() {
        this.wrapFails(LimitedIteratorTest.ITERATOR, -1);
    }

    private void wrapFails(final Iterator<String> iterator, final int count) {
        try {
            LimitedIterator.wrap(iterator, count);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testWrapAnotherLimitedIteratorWithLessCount() {
        final LimitedIterator<String> limited = this.createIterator();
        assertSame(limited, LimitedIterator.wrap(limited, LimitedIteratorTest.COUNT));
    }

    @Test
    public void testWrapAnotherLimitedIteratorWithLessCount2() {
        final LimitedIterator<String> limited = this.createIterator();
        assertSame(limited, LimitedIterator.wrap(limited, LimitedIteratorTest.COUNT + 1));
    }

    @Test
    public void testWrapAnotherLimitedIteratorWithMoreCount2() {
        final LimitedIterator<String> limited = this.createIterator();
        final LimitedIterator<String> limited2 = LimitedIterator.wrap(limited, 1);
        assertSame("iterator", limited.iterator, limited2.iterator);
        assertSame("countdown", 1, limited2.countdown);
    }

    @Test
    public void testExhaustUsingHasNextNext() {
        final LimitedIterator<String> iterator = this.createIterator();
        Assert.assertTrue(iterator.hasNext());
        assertSame("1", iterator.next());

        Assert.assertTrue(iterator.hasNext());
        assertSame("2", iterator.next());

        Assert.assertFalse(iterator.hasNext());
        try {
            iterator.next();
            Assert.fail();
        } catch (final NoSuchElementException expected) {
        }
        try {
            iterator.remove();
            Assert.fail();
        } catch (final UnsupportedOperationException expected) {
        }
    }

    @Test
    public void testExhaustUsingNext() {
        final LimitedIterator<String> iterator = this.createIterator();
        assertSame("1", iterator.next());
        assertSame("2", iterator.next());
        this.checkNextFails(iterator);
        this.checkRemoveFails(iterator);
    }

    @Test
    public void testRemove() {
        final List<String> source = Lists.array();
        source.add("1-removed");
        source.add("2");
        source.add("3");
        final LimitedIterator<String> iterator = LimitedIterator.wrap(source.iterator(),
                LimitedIteratorTest.COUNT);
        iterator.next();
        iterator.remove();
        assertEquals(Lists.of("2", "3"), source);

        assertSame("2", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testToString() {
        assertEquals("at most 2 " + LimitedIteratorTest.ITERATOR,
                LimitedIterator.wrap(LimitedIteratorTest.ITERATOR, LimitedIteratorTest.COUNT)
                        .toString());
    }

    @Override
    protected LimitedIterator<String> createIterator() {
        return LimitedIterator.wrap(Lists.of("1",
                "2",
                "should never be returned1",
                "should never be returned2").iterator(), LimitedIteratorTest.COUNT);
    }

    @Override
    protected Class<LimitedIterator<String>> type() {
        return Cast.to(LimitedIterator.class);
    }
}
