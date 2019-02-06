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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertThrows(NullPointerException.class, () -> {
            IteratorChain.wrap(null, SECOND);
        });
    }

    @Test
    public void testWrapNullSecondFails() {
        assertThrows(NullPointerException.class, () -> {
            IteratorChain.wrap(FIRST, null);
        });
    }

    @Test
    public void testDoesntWrapEqualFirstAndSecond() {
        assertSame(FIRST, IteratorChain.wrap(FIRST, FIRST));
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
        assertTrue(iterator.hasNext(), "hasNext from 1st iterator");
        assertSame("1", iterator.next(), "next from 1st iterator");

        assertTrue(iterator.hasNext(), "hasNext from 1st iterator");
        assertSame("2", iterator.next(), "next from 1st iterator");

        assertTrue(iterator.hasNext(), "hasNext from last iterator");
        assertSame("3", iterator.next());

        assertTrue(iterator.hasNext(), "hasNext from last iterator");
        assertSame("4", iterator.next(), "next from last iterator");

        assertFalse(iterator.hasNext(), "hasNext should be false when empty");

        this.checkNextFails(iterator);
    }

    @Test
    public void testConsumeWithoutHasNext() {
        final List<String> first = Lists.array();
        first.add("1");
        first.add("2");
        final List<String> second = Lists.array();
        second.add("3");
        second.add("4");

        final IteratorChain<String> iterator = this.createIterator(first.iterator(), second.iterator());
        assertSame("1", iterator.next(), "next from 1st iterator");
        assertSame("2", iterator.next(), "next from 1st iterator");
        assertSame( "3", iterator.next(), "next from last iterator");
        assertSame("4", iterator.next(), "next from last iterator");
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

        this.checkNextFails(iterator);
    }

    @Test
    public void testRemoveWithoutNext() {
        this.checkRemoveUnsupportedFails(this.createIterator());
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
        assertTrue(iterator.hasNext(), "hasNext from 1st iterator");
        assertSame("1", iterator.next(), "next from 1st iterator");
        iterator.remove();
        assertEquals(Lists.of("2"), first, "element not removed from first iterator");
        assertEquals(Lists.of("3", "4"), second, "second iterator should remain unmodified");

        assertTrue(iterator.hasNext(), "hasNext from 1st iterator");
        assertSame("2", iterator.next(), "next from 1st iterator");
        iterator.remove();
        assertEquals(Lists.empty(), first, "element not removed from first iterator");
        assertEquals(Lists.of("3", "4"), second, "second iterator should remain unmodified");

        assertTrue(iterator.hasNext(), "hasNext from last iterator");
        assertSame("3", iterator.next(), "next from last iterator");
        iterator.remove();
        assertEquals(Lists.of("4"), second, "element not removed from second iterator");

        assertTrue(iterator.hasNext(), "hasNext from last iterator");
        assertSame("4", iterator.next(), "next from last iterator");
        iterator.remove();
        assertEquals(Lists.empty(), second, "element not removed from second iterator");
    }

    @Test
    public void testConsumeEverythingFromEmptyIterators() {
        final List<String> first = Lists.empty();
        final List<String> second = Lists.empty();

        final Iterator<String> iterator = IteratorChain.wrap(first.iterator(), second.iterator());
        this.checkHasNextFalse(iterator);
        this.checkNextFails(iterator);
    }

    @Test
    public void testFirstIteratorEmpty() {
        final List<String> first = Lists.empty();
        final List<String> second = Lists.of("1");

        final IteratorChain<String> iterator = this.createIterator(first.iterator(),
                second.iterator());
        assertTrue(iterator.hasNext(), "hasNext from 2nd iterator");
        assertSame("1", iterator.next(), "next from 2nd iterator");

        assertFalse(iterator.hasNext(), "hasNext from empty 2nd iterator");
        this.checkNextFails(iterator);
    }

    @Test
    public void testFirstIteratorEmptyWithoutHasNext() {
        final List<String> first = Lists.empty();
        final List<String> second = Lists.of("1");

        final IteratorChain<String> iterator = this.createIterator(first.iterator(),
                second.iterator());
        assertSame("1", iterator.next(), "next from 2nd iterator");

        assertFalse(iterator.hasNext(), "hasNext from empty 2nd iterator");
        this.checkNextFails(iterator);
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
        assertEquals(Lists.of("3"), second, "second iterator source was not removed");
        this.checkRemoveUnsupportedFails(iterator);
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
        assertTrue(iterator.hasNext(), "hasNext from 1st iterator");
        assertSame("1", iterator.next(), "next from 1st iterator");

        assertTrue(iterator.hasNext(), "hasNext from 1st iterator");
        assertSame("2", iterator.next(), "next from 1st iterator");
        iterator.remove();
        assertEquals(Lists.of("1"), first, "element not removed from first iterator");
        assertEquals(Lists.of("3", "4"), second, "second iterator should remain unmodified");

        assertTrue(iterator.hasNext(), "hasNext from last iterator");
        assertSame("3", iterator.next(), "next from last iterator");

        assertTrue(iterator.hasNext(), "hasNext from last iterator");
        assertSame( "4", iterator.next(), "next from last iterator");
        iterator.remove();
        assertEquals(Lists.of("3"), second, "element not removed from second iterator");

        this.checkNextFails(iterator);
        this.checkRemoveUnsupportedFails(iterator);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createIterator(), FIRST + "...");
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

        this.toStringAndCheck(iterator, secondIterator.toString());
    }

    @Test
    public void testToStringWhenEmpty() {
        final List<String> first = Lists.empty();
        final List<String> second = Lists.of("1");
        final IteratorChain<String> iterator = this.createIterator(first.iterator(),
                second.iterator());
        iterator.next();
        iterator.hasNext();

        this.toStringAndCheck(iterator, "");
    }

    @Override public IteratorChain<String> createIterator() {
        return this.createIterator(FIRST, SECOND);
    }

    private IteratorChain<String> createIterator(final Iterator<String> first,
                                                 final Iterator<String> second) {
        return Cast.to(IteratorChain.wrap(first, second));
    }

    @Override
    public Class<IteratorChain<String>> type() {
        return Cast.to(IteratorChain.class);
    }
}
