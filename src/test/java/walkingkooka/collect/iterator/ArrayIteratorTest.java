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

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class ArrayIteratorTest extends IteratorTestCase<ArrayIterator<String>, String> {

    @Test
    public void testWithNullArrayFails() {
        assertThrows(NullPointerException.class, () -> {
            ArrayIterator.with((Object[]) null);
        });
    }

    @Test
    public void testConsumedUsingHasNextNext() {
        this.iterateUsingHasNextAndCheck(this.createIterator(), "A", "B", "C");
    }

    @Test
    public void testConsumedUsingNext() {
        this.iterateAndCheck(this.createIterator(), "A", "B", "C");
    }

    @Test
    public void testArrayCopied() {
        final String[] items = new String[]{"A", "B", "C"};
        final Iterator<String> iterator = ArrayIterator.with(items);
        items[0] = "D";
        items[1] = "E";
        items[2] = "F";

        this.iterateUsingHasNextAndCheck(iterator, "A", "B", "C");
    }

    @Test
    public void testRemoveWithoutNext() {
        this.removeUnsupportedFails(this.createIterator());
    }

    @Test
    public void testRemove() {
        final ArrayIterator<String> iterator = this.createIterator();
        iterator.next();
        this.removeUnsupportedFails(iterator);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createIterator(), Lists.of("A", "B", "C").toString());
    }

    @Test
    public void testToStringPartiallyConsumed() {
        final ArrayIterator<String> iterator = this.createIterator();
        iterator.next();

        this.toStringAndCheck(iterator, Lists.of("B", "C").toString());
    }

    @Test
    public void testToStringWhenEmpty() {
        final ArrayIterator<String> iterator = this.createIterator();
        iterator.next();
        iterator.next();
        iterator.next();

        this.toStringAndCheck(iterator, Lists.of().toString());
    }

    @Override public ArrayIterator<String> createIterator() {
        return ArrayIterator.with("A", "B", "C");
    }

    @Override
    public Class<ArrayIterator<String>> type() {
        return Cast.to(ArrayIterator.class);
    }
}
