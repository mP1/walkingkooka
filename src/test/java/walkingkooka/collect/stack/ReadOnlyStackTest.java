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

package walkingkooka.collect.stack;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.iterator.Iterators;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class ReadOnlyStackTest extends StackTestCase<ReadOnlyStack<Object>, Object> {

    // constants

    private final static Object ITEM = "*item*";

    // tests

    @Test
    public void testWithNullStackFails() {
        assertThrows(NullPointerException.class, () -> ReadOnlyStack.wrap(null));
    }

    @Test
    public void testDoesntWrapReadOnlyStack() {
        final ReadOnlyStack<Object> stack = this.createStack();
        assertSame(stack, ReadOnlyStack.wrap(stack));
    }

    @Test
    public void testPeek() {
        final ReadOnlyStack<Object> stack = this.createStack();
        assertSame(ITEM, stack.peek());
    }

    @Test
    public void testPop() {
        final ReadOnlyStack<Object> stack = this.createStack();
        assertSame(stack, stack.pop());
    }

    @Test
    public void testIsEmpty() {
        final ReadOnlyStack<Object> stack = this.createStack();
        this.checkSize(stack, 1);

        stack.pop();
        this.checkSize(stack, 0);

    }

    @Test
    public void testPushFails() {
        final ReadOnlyStack<Object> readOnly = ReadOnlyStack.wrap(Stacks.fake());
        assertThrows(UnsupportedOperationException.class, () -> readOnly.push(ITEM));
    }

    @Test
    public void testPushAllFails() {
        final ReadOnlyStack<Object> readOnly = ReadOnlyStack.wrap(Stacks.fake());
        assertThrows(UnsupportedOperationException.class, () -> readOnly.pushAll(Iterators.one(ITEM)));
    }

    @Test
    public void testIterator() {
        final Iterator<Object> iterator = Iterators.fake();
        final Stack<Object> stack = ReadOnlyStack.wrap(//
            new FakeStack<>() {

                @Override
                public Iterator<Object> iterator() {
                    return iterator;
                }
            });
        assertSame(iterator, stack.iterator());
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testEqualsDifferentItems() {
        final Stack<Object> stack = Stacks.arrayList();
        stack.push("*different*");
        this.checkNotEquals(ReadOnlyStack.wrap(stack));
    }

    @Test
    public void testSameItemsDifferentStackType() {
        final Stack<Object> stack = Stacks.arrayList();
        stack.push(ITEM);
        this.checkEquals(stack);
    }

    // toString ..................................................................................................

    @Test
    public void testToString() {
        final Stack<Object> stack = Stacks.fake();
        this.toStringAndCheck(ReadOnlyStack.wrap(stack), stack.toString());
    }

    @Override
    public ReadOnlyStack<Object> createStack() {
        final Stack<Object> stack = Stacks.arrayList();
        stack.push(ITEM);
        return ReadOnlyStack.wrap(stack);
    }

    @Override
    public Class<ReadOnlyStack<Object>> type() {
        return Cast.to(ReadOnlyStack.class);
    }
}
