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
import walkingkooka.collect.list.Lists;

import java.util.EmptyStackException;
import java.util.Iterator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class EmptyArrayStackTest extends StackTestCase<EmptyArrayStack<Object>, Object> {

    @Test
    public void testCreate() {
        final Stack<Object> stack = EmptyArrayStack.instance();
        Assert.assertTrue("isempty", stack.isEmpty());
        assertEquals("size when empty", 0, stack.size());
    }

    @Test
    public void testPush() {
        final ArrayStack<String> stack = Cast.to(EmptyArrayStack.instance().push("1"));
        assertArrayEquals("array", new Object[]{"1"}, stack.array);
        assertEquals("last", 1, stack.last);
    }

    @Test
    public void testPushAllEmptyIteratorReturnsThis() {
        final Stack<Object> stack = EmptyArrayStack.instance();
        assertSame(stack, stack.pushAll(Iterators.empty()));
    }

    @Test
    public void testPushAll() {
        final ArrayStack<String> stack
                = Cast.to(EmptyArrayStack.<String>instance().pushAll(Lists.of("1", "2", "3")
                .iterator()));
        assertArrayEquals("array", new Object[]{"1", "2", "3"}, stack.array);
        assertEquals("last", 3, stack.last);
    }

    @Test
    public void testPeekFails() {
        final EmptyArrayStack<String> stack = EmptyArrayStack.instance();
        try {
            stack.peek();
            Assert.fail();
        } catch (final EmptyStackException expected) {

        }
    }

    @Test
    public void testPopFails() {
        final Stack<Object> stack = EmptyArrayStack.instance();
        try {
            stack.pop();
            Assert.fail();
        } catch (final EmptyStackException expected) {
        }
    }

    @Test
    public void testIterator() {
        final EmptyArrayStack<String> stack = EmptyArrayStack.instance();

        final Iterator<String> iterator = stack.iterator();
        Assert.assertFalse("iterator must be empty=" + iterator, iterator.hasNext());
    }

    @Test
    public void testIteratorWithRemove() {
        final EmptyArrayStack<String> stack = EmptyArrayStack.instance();
        final Iterator<String> iterator = stack.iterator();
        try {
            iterator.remove();
            Assert.fail();
        } catch (final IllegalStateException expected) {
        }
    }

    @Test
    public void testToString() {
        assertEquals("[]", EmptyArrayStack.instance().toString());
    }

    @Override
    protected EmptyArrayStack<Object> createStack() {
        return EmptyArrayStack.instance();
    }

    @Override
    public Class<EmptyArrayStack<Object>> type() {
        return Cast.to(EmptyArrayStack.class);
    }

    @Override
    public EmptyArrayStack<Object> serializableInstance() {
        return EmptyArrayStack.instance();
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return true;
    }

}
