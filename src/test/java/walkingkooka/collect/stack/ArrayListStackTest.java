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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class ArrayListStackTest extends StackTestCase<ArrayListStack<Object>, Object> {

    @Test
    public void testCreate() {
        final Stack<Object> stack = ArrayListStack.create();
        Assert.assertTrue("isempty", stack.isEmpty());
        assertEquals("size when empty", 0, stack.size());
    }

    @Test
    public void testPush() {
        final ArrayListStack<String> stack = ArrayListStack.create();
        assertSame("this not returned", stack, stack.push("1"));
        Assert.assertFalse("isempty", stack.isEmpty());
        assertSame("this not returned", stack, stack.push("2"));
        assertEquals("size with 2 items", 2, stack.size());

        final List<String> list = Lists.array();
        list.add("1");
        list.add("2");
        assertEquals("items on wrapped list", list, stack.items);
    }

    @Test
    public void testPushAllEmptyIterator() {
        final ArrayListStack<String> stack = ArrayListStack.create();
        assertSame("this not returned", stack, stack.pushAll(Iterators.empty()));
        assertEquals("wrapped List items", Lists.empty(), stack.items);
    }

    @Test
    public void testPushAll() {
        final ArrayListStack<String> stack = ArrayListStack.create();
        assertSame("this was not returned", stack, stack.pushAll(Lists.of("1", "2").iterator()));

        Assert.assertFalse("isempty", stack.isEmpty());
        assertEquals("size with 2 items", 2, stack.size());

        final List<String> list = Lists.array();
        list.add("1");
        list.add("2");
        assertEquals("items on wrapped list", list, stack.items);
    }

    @Test
    public void testPopAndPeek() {
        final ArrayListStack<String> stack = ArrayListStack.create();
        stack.push("1");
        Assert.assertFalse("isempty", stack.isEmpty());
        stack.push("2");

        assertEquals("size with 2 items", 2, stack.size());

        assertEquals("peek", "2", stack.peek());
        assertEquals("peek again", "2", stack.peek());

        assertSame("pop", stack, stack.pop());
        assertEquals("peek", "1", stack.peek());

        assertSame("pop last", stack, stack.pop());

        Assert.assertTrue("isempty", stack.isEmpty());
        assertEquals("size when empty", 0, stack.size());
    }

    @Test
    public void testPeekWhenEmptyFails() {
        final Stack<Object> stack = ArrayListStack.create();
        try {
            stack.peek();
            Assert.fail();
        } catch (final EmptyStackException expected) {
        }
    }

    @Test
    public void testPopWhenEmptyFails() {
        final Stack<Object> stack = ArrayListStack.create();
        try {
            stack.pop();
            Assert.fail();
        } catch (final EmptyStackException expected) {
        }
    }

    @Test
    public void testIterator() {
        final ArrayListStack<String> stack = ArrayListStack.create();
        stack.push("1");
        stack.push("2");
        stack.push("3");

        final Iterator<String> iterator = stack.iterator();
        assertEquals("first", "1", iterator.next());
        assertEquals("second", "2", iterator.next());
        assertEquals("last", "3", iterator.next());
        Assert.assertFalse("iterator was NOT empty=" + iterator, iterator.hasNext());
    }

    @Test
    public void testIteratorWithRemove() {
        final ArrayListStack<String> stack = ArrayListStack.create();
        stack.push("1");
        stack.push("2");
        stack.push("3");

        final Iterator<String> iterator = stack.iterator();
        assertEquals("first", "1", iterator.next());
        iterator.remove();
        assertEquals("second", "2", iterator.next());
        iterator.remove();
        assertEquals("last", "3", iterator.next());
        Assert.assertFalse("iterator was NOT empty=" + iterator, iterator.hasNext());

        assertSame("3", stack.peek());
        stack.pop();
        Assert.assertTrue("stack should be empty", stack.isEmpty());
    }

    @Test
    public void testToString() {
        final ArrayListStack<String> stack = ArrayListStack.create();
        stack.push("1");
        stack.push("2");
        stack.push("3");
        assertEquals(Lists.of("1", "2", "3").toString(), stack.toString());
    }

    @Override
    protected ArrayListStack<Object> createStack() {
        return ArrayListStack.create();
    }

    @Override
    public Class<ArrayListStack<Object>> type() {
        return Cast.to(ArrayListStack.class);
    }

    @Override
    public ArrayListStack<Object> serializableInstance() {
        return ArrayListStack.create();
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
