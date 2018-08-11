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

final public class IteratorChainTest extends IteratorTestCase<IteratorChain<String>, String> {

    // constants

    private final static Iterator<String> FIRST = Iterators.fake();

    private final static Iterator<String> SECOND = Iterators.fake();

    // tests

    @Override
    @Test
    public void testNaming() {
        this.checkNamingStartAndEnd(Iterator.class, "Chain");
    }

    @Test
    public void testWrapNullFirstFails() {
        this.wrapFails(null, IteratorChainTest.SECOND);
    }

    @Test
    public void testWrapNullSecondFails() {
        this.wrapFails(IteratorChainTest.FIRST, null);
    }

    private void wrapFails(final Iterator<String> first, final Iterator<String> second) {
        try {
            IteratorChain.wrap(first, second);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testDoesntWrapEqualFirstAndSecond() {
        assertSame(IteratorChainTest.FIRST,
                IteratorChain.wrap(IteratorChainTest.FIRST, IteratorChainTest.FIRST));
    }

    @Test
    public void testConsume() {
        final List<String> first = Lists.array();
        first.add("1");
        first.add("2");
        final List<String> second = Lists.array();
        second.add("3");
        second.add("4");

        final IteratorChain<String> iterator = this.createIterator(first.iterator(),
                second.iterator());
        Assert.assertTrue("hasNext from 1st iterator", iterator.hasNext());
        assertSame("next from 1st iterator", "1", iterator.next());

        Assert.assertTrue("hasNext from 1st iterator", iterator.hasNext());
        assertSame("next from 1st iterator", "2", iterator.next());

        Assert.assertTrue("hasNext from last iterator", iterator.hasNext());
        assertSame("next from last iterator", "3", iterator.next());

        Assert.assertTrue("hasNext from last iterator", iterator.hasNext());
        assertSame("next from last iterator", "4", iterator.next());

        Assert.assertFalse("hasNext should be false when empty", iterator.hasNext());
        try {
            iterator.next();
            Assert.fail();
        } catch (final NoSuchElementException expected) {
        }
    }

    @Test
    public void testConsumeWithoutHasNext() {
        final List<String> first = Lists.array();
        first.add("1");
        first.add("2");
        final List<String> second = Lists.array();
        second.add("3");
        second.add("4");

        final IteratorChain<String> iterator = this.createIterator(first.iterator(),
                second.iterator());
        assertSame("next from 1st iterator", "1", iterator.next());
        assertSame("next from 1st iterator", "2", iterator.next());
        assertSame("next from last iterator", "3", iterator.next());
        assertSame("next from last iterator", "4", iterator.next());
    }

    @Test
    public void testNextWhenEmpty() {
        final List<String> first = Lists.array();
        first.add("1");
        first.add("2");
        final List<String> second = Lists.array();
        second.add("3");
        second.add("4");

        final IteratorChain<String> iterator = this.createIterator(first.iterator(),
                second.iterator());
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();

        try {
            iterator.next();
            Assert.fail();
        } catch (final NoSuchElementException expected) {
        }
    }

    @Test
    public void testRemoveWithoutNext() {
        final IteratorChain<String> iterator = this.createIterator(Iterators.fake(),
                Iterators.fake());
        try {
            iterator.remove();
            Assert.fail();
        } catch (final UnsupportedOperationException expected) {
        }
    }

    @Test
    public void testConsumeAndRemoveEverything() {
        final List<String> first = Lists.array();
        first.add("1");
        first.add("2");
        final List<String> second = Lists.array();
        second.add("3");
        second.add("4");

        final IteratorChain<String> iterator = this.createIterator(first.iterator(),
                second.iterator());
        Assert.assertTrue("hasNext from 1st iterator", iterator.hasNext());
        assertSame("next from 1st iterator", "1", iterator.next());
        iterator.remove();
        assertEquals("element not removed from first iterator", Lists.of("2"), first);
        assertEquals("second iterator should remain unmodified", Lists.of("3", "4"), second);

        Assert.assertTrue("hasNext from 1st iterator", iterator.hasNext());
        assertSame("next from 1st iterator", "2", iterator.next());
        iterator.remove();
        assertEquals("element not removed from first iterator", Lists.empty(), first);
        assertEquals("second iterator should remain unmodified", Lists.of("3", "4"), second);

        Assert.assertTrue("hasNext from last iterator", iterator.hasNext());
        assertSame("next from last iterator", "3", iterator.next());
        iterator.remove();
        assertEquals("element not removed from second iterator", Lists.of("4"), second);

        Assert.assertTrue("hasNext from last iterator", iterator.hasNext());
        assertSame("next from last iterator", "4", iterator.next());
        iterator.remove();
        assertEquals("element not removed from second iterator", Lists.empty(), second);
    }

    @Test
    public void testConsumeEverythingFromEmptyIterators() {
        final List<String> first = Lists.empty();
        final List<String> second = Lists.empty();

        final Iterator<String> iterator = IteratorChain.wrap(first.iterator(), second.iterator());
        Assert.assertFalse("hasNext from 1st iterator", iterator.hasNext());
        try {
            iterator.next();
            Assert.fail();
        } catch (final NoSuchElementException expected) {
        }
    }

    @Test
    public void testFirstIteratorEmpty() {
        final List<String> first = Lists.empty();
        final List<String> second = Lists.of("1");

        final IteratorChain<String> iterator = this.createIterator(first.iterator(),
                second.iterator());
        Assert.assertTrue("hasNext from 2nd iterator", iterator.hasNext());
        assertSame("next from 2nd iterator", "1", iterator.next());

        Assert.assertFalse("hasNext from empty 2nd iterator", iterator.hasNext());
        try {
            iterator.next();
            Assert.fail();
        } catch (final NoSuchElementException expected) {
        }
    }

    @Test
    public void testFirstIteratorEmptyWithoutHasNext() {
        final List<String> first = Lists.empty();
        final List<String> second = Lists.of("1");

        final IteratorChain<String> iterator = this.createIterator(first.iterator(),
                second.iterator());
        assertSame("next from 2nd iterator", "1", iterator.next());

        Assert.assertFalse("hasNext from empty 2nd iterator", iterator.hasNext());
        try {
            iterator.next();
            Assert.fail();
        } catch (final NoSuchElementException expected) {
        }
    }

    @Test
    public void testRemoveLastTwiceFails() {
        final List<String> first = Lists.of("1", "2");
        final List<String> second = Lists.array();
        second.add("3");
        second.add("4");

        final IteratorChain<String> iterator = this.createIterator(first.iterator(),
                second.iterator());
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();

        iterator.remove();
        assertEquals("second iterator source was not removed", Lists.of("3"), second);
        try {
            iterator.remove();
            Assert.fail();
        } catch (final UnsupportedOperationException expected) {
        }
    }

    @Test
    public void testRemoveSome() {
        final List<String> first = Lists.array();
        first.add("1");
        first.add("2");
        final List<String> second = Lists.array();
        second.add("3");
        second.add("4");

        final IteratorChain<String> iterator = this.createIterator(first.iterator(),
                second.iterator());
        Assert.assertTrue("hasNext from 1st iterator", iterator.hasNext());
        assertSame("next from 1st iterator", "1", iterator.next());

        Assert.assertTrue("hasNext from 1st iterator", iterator.hasNext());
        assertSame("next from 1st iterator", "2", iterator.next());
        iterator.remove();
        assertEquals("element not removed from first iterator", Lists.of("1"), first);
        assertEquals("second iterator should remain unmodified", Lists.of("3", "4"), second);

        Assert.assertTrue("hasNext from last iterator", iterator.hasNext());
        assertSame("next from last iterator", "3", iterator.next());

        Assert.assertTrue("hasNext from last iterator", iterator.hasNext());
        assertSame("next from last iterator", "4", iterator.next());
        iterator.remove();
        assertEquals("element not removed from second iterator", Lists.of("3"), second);

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
    public void testToString() {
        assertEquals(IteratorChainTest.FIRST + "...", this.createIterator().toString());
    }

    @Test
    public void testToStringAfterConsumingFirstIterator() {
        final List<String> first = Lists.of("1");
        final List<String> second = Lists.array();
        second.add("2");
        second.add("3");

        final Iterator<String> secondIterator = second.iterator();
        final IteratorChain<String> iterator = this.createIterator(first.iterator(),
                secondIterator);
        iterator.next();
        iterator.hasNext();

        assertEquals(secondIterator.toString(), iterator.toString());
    }

    @Test
    public void testToStringWhenEmpty() {
        final List<String> first = Lists.empty();
        final List<String> second = Lists.of("1");
        final IteratorChain<String> iterator = this.createIterator(first.iterator(),
                second.iterator());
        iterator.next();
        iterator.hasNext();

        assertEquals("", iterator.toString());
    }

    @Override
    protected IteratorChain<String> createIterator() {
        return this.createIterator(IteratorChainTest.FIRST, IteratorChainTest.SECOND);
    }

    private IteratorChain<String> createIterator(final Iterator<String> first,
                                                 final Iterator<String> second) {
        return Cast.to(IteratorChain.wrap(first, second));
    }

    @Override
    protected Class<IteratorChain<String>> type() {
        return Cast.to(IteratorChain.class);
    }
}
