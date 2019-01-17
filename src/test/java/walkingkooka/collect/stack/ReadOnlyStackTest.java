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

package walkingkooka.collect.stack;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.iterator.Iterators;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class ReadOnlyStackTest extends StackTestCase<ReadOnlyStack<Object>, Object> {

    // constants

    private final static Object ITEM = "*item*";

    // tests

    @Test
    public void testWithNullStackFails() {
        try {
            ReadOnlyStack.wrap(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testDoesntWrapReadOnlyStack() {
        final ReadOnlyStack<Object> stack = this.createStack();
        assertSame(stack, ReadOnlyStack.wrap(stack));
    }

    @Test
    public void testPeek() {
        final ReadOnlyStack<Object> stack = this.createStack();
        assertSame(ReadOnlyStackTest.ITEM, stack.peek());
    }

    @Test
    public void testPop() {
        final ReadOnlyStack<Object> stack = this.createStack();
        assertSame(stack, stack.pop());
    }

    @Test
    public void testIsEmpty() {
        final ReadOnlyStack<Object> stack = this.createStack();
        Assert.assertFalse("stack should NOT be empty it has of item", stack.isEmpty());
        assertEquals("size when with of item", 1, stack.size());

        stack.pop();
        Assert.assertTrue("stack should be empty after pop", stack.isEmpty());
        assertEquals("size when empty", 0, stack.size());
    }

    @Test
    public void testPushFails() {
        final ReadOnlyStack<Object> readOnly = ReadOnlyStack.wrap(Stacks.fake());
        try {
            readOnly.push(ReadOnlyStackTest.ITEM);
            Assert.fail();
        } catch (final UnsupportedOperationException expected) {
        }
    }

    @Test
    public void testPushAllFails() {
        final ReadOnlyStack<Object> readOnly = ReadOnlyStack.wrap(Stacks.fake());

        try {
            readOnly.pushAll(Iterators.one(ReadOnlyStackTest.ITEM));
            Assert.fail();
        } catch (final UnsupportedOperationException expected) {
        }
    }

    @Test
    public void testIterator() {
        final Iterator<Object> iterator = Iterators.fake();
        final Stack<Object> stack = ReadOnlyStack.wrap(//
                new FakeStack<Object>() {

                    private static final long serialVersionUID = 381918503618696270L;

                    @Override
                    public Iterator<Object> iterator() {
                        return iterator;
                    }
                });
        assertSame(iterator, stack.iterator());
    }

    @Test
    public void testToString() {
        final Stack<Object> stack = Stacks.fake();
        assertEquals(stack.toString(), ReadOnlyStack.wrap(stack).toString());
    }

    @Override
    protected ReadOnlyStack<Object> createStack() {
        final Stack<Object> stack = Stacks.arrayList();
        stack.push(ReadOnlyStackTest.ITEM);
        return ReadOnlyStack.wrap(stack);
    }

    @Override
    public Class<ReadOnlyStack<Object>> type() {
        return Cast.to(ReadOnlyStack.class);
    }

    @Override
    public ReadOnlyStack<Object> serializableInstance() {
        final Stack<Object> stack = Stacks.arrayList();
        stack.push("*pushed onto stack*");
        return ReadOnlyStack.wrap(stack);
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
