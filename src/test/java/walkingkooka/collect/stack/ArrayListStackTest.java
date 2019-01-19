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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

final public class ArrayListStackTest extends StackTestCase<ArrayListStack<String>, String> {

    @Test
    public void testCreate() {
        final Stack<String> stack = ArrayListStack.create();
        assertTrue("isempty", stack.isEmpty());
        assertEquals("size when empty", 0, stack.size());
    }

    @Test
    public void testPush() {
        final ArrayListStack<String> stack = ArrayListStack.create();
        assertSame("this not returned", stack, stack.push("1"));
        assertFalse("isempty", stack.isEmpty());
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

        assertFalse("isempty", stack.isEmpty());
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
        assertFalse("isempty", stack.isEmpty());
        stack.push("2");

        assertEquals("size with 2 items", 2, stack.size());

        assertEquals("peek", "2", stack.peek());
        assertEquals("peek again", "2", stack.peek());

        assertSame("pop", stack, stack.pop());
        assertEquals("peek", "1", stack.peek());

        assertSame("pop last", stack, stack.pop());

        assertTrue("isempty", stack.isEmpty());
        assertEquals("size when empty", 0, stack.size());
    }

    @Test
    public void testPeekWhenEmptyFails() {
        final Stack<String> stack = ArrayListStack.create();
        try {
            stack.peek();
            Assert.fail();
        } catch (final EmptyStackException expected) {
        }
    }

    @Test
    public void testPopWhenEmptyFails() {
        final Stack<String> stack = ArrayListStack.create();
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
        assertFalse("iterator was NOT empty=" + iterator, iterator.hasNext());
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
        assertFalse("iterator was NOT empty=" + iterator, iterator.hasNext());

        assertSame("3", stack.peek());
        stack.pop();
        assertTrue("stack should be empty", stack.isEmpty());
    }

    @Test
    public void testEqualsBothEmpty() {
        final ArrayListStack<String> stack1 = ArrayListStack.create();
        final ArrayListStack<String> stack2 = ArrayListStack.create();
        checkEqualsAndHashCode(stack1, stack2);
    }

    @Test
    public void testEqual() {
        final ArrayListStack<String> stack1 = ArrayListStack.create();
        final ArrayListStack<String> stack2 = ArrayListStack.create();
        stack1.push("1");
        stack1.push("2");
        stack2.push("1");
        stack2.push("*");
        stack2.pop();
        stack2.push("2");
        checkEqualsAndHashCode(stack1, stack2);
    }

    @Test
    public void testDifferentItems() {
        final ArrayListStack<String> stack1 = ArrayListStack.create();
        final ArrayListStack<String> stack2 = ArrayListStack.create();
        stack1.push("1");
        stack1.push("2");
        stack2.push("3");
        stack2.push("4");
        assertNotEquals(stack1, stack2);
    }

    @Test
    public void testSameItemsDifferentStackType() {
        final ArrayListStack<String> stack1 = ArrayListStack.create();
        final Stack<String> stack2 = Stacks.jdk();
        stack1.push("1");
        stack1.push("2");
        stack2.push("1");
        stack2.push("2");
        assertEquals(stack1, stack2);
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
    protected ArrayListStack<String> createStack() {
        return ArrayListStack.create();
    }

    @Override
    public Class<ArrayListStack<String>> type() {
        return Cast.to(ArrayListStack.class);
    }

    @Override
    public ArrayListStack<String> serializableInstance() {
        return ArrayListStack.create();
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }

    @Override
    public ArrayListStack<String> createObject() {
        return ArrayListStack.create();
    }
}
