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
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.list.Lists;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

final public class ArrayListStackTest extends StackTestCase<ArrayListStack<String>, String>
    implements IteratorTesting {

    @Test
    public void testCreate() {
        final Stack<String> stack = ArrayListStack.create();
        this.checkSize(stack, 0);
    }

    @Test
    public void testPush() {
        final ArrayListStack<String> stack = ArrayListStack.create();
        assertSame(stack, stack.push("1"), "this not returned");
        this.checkSize(stack, 1);

        assertSame(stack, stack.push("2"), "this not returned");
        this.checkSize(stack, 2);

        final List<String> list = Lists.array();
        list.add("1");
        list.add("2");
        this.checkEquals(list, stack.items, "items on wrapped list");
    }

    @Test
    public void testPushAllEmptyIterator() {
        final ArrayListStack<String> stack = ArrayListStack.create();
        assertSame(stack, stack.pushAll(Iterators.empty()), "this not returned");
        this.checkEquals(Lists.empty(), stack.items, "wrapped List items");
    }

    @Test
    public void testPushAll() {
        final ArrayListStack<String> stack = ArrayListStack.create();
        assertSame(stack, stack.pushAll(Lists.of("1", "2").iterator()), "this was not returned");

        this.checkSize(stack, 2);

        final List<String> list = Lists.array();
        list.add("1");
        list.add("2");
        this.checkEquals(list, stack.items, "items on wrapped list");
    }

    @Test
    public void testPopAndPeek() {
        final ArrayListStack<String> stack = ArrayListStack.create();
        stack.push("1");
        assertFalse(stack.isEmpty(), "isempty");
        stack.push("2");

        this.checkSize(stack, 2);

        this.checkEquals("2", stack.peek(), "peek");
        this.checkEquals("2", stack.peek(), "peek again");

        assertSame(stack, stack.pop(), "pop");
        this.checkEquals("1", stack.peek(), "peek");

        assertSame(stack, stack.pop(), "pop last");

        this.checkSize(stack, 0);
    }

    @Test
    public void testIterator() {
        final ArrayListStack<String> stack = ArrayListStack.create();
        stack.push("1");
        stack.push("2");
        stack.push("3");

        this.iterateAndCheck(stack.iterator(), "1", "2", "3");
    }

    @Test
    public void testIteratorWithRemove() {
        final ArrayListStack<String> stack = ArrayListStack.create();
        stack.push("1");
        stack.push("2");
        stack.push("3");

        final Iterator<String> iterator = stack.iterator();
        this.checkEquals("1", iterator.next());
        iterator.remove();
        this.checkEquals("2", iterator.next());
        iterator.remove();
        this.checkEquals("3", iterator.next());
        assertFalse(iterator.hasNext(), "iterator was NOT empty=" + iterator);

        assertSame("3", stack.peek());
        stack.pop();
        assertTrue(stack.isEmpty(), "stack should be empty");
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
    public void testEqualsDifferentItems() {
        final ArrayListStack<String> stack1 = ArrayListStack.create();
        final ArrayListStack<String> stack2 = ArrayListStack.create();
        stack1.push("1");
        stack1.push("2");
        stack2.push("3");
        stack2.push("4");
        this.checkNotEquals(stack1, stack2);
    }

    @Test
    public void testSameItemsDifferentStackType() {
        final ArrayListStack<String> stack1 = ArrayListStack.create();
        final Stack<String> stack2 = Stacks.jdk();
        stack1.push("1");
        stack1.push("2");
        stack2.push("1");
        stack2.push("2");
        this.checkEquals(stack1, stack2);
    }

    @Test
    public void testToString() {
        final ArrayListStack<String> stack = ArrayListStack.create();
        stack.push("1");
        stack.push("2");
        stack.push("3");
        this.toStringAndCheck(stack, Lists.of("1", "2", "3").toString());
    }

    @Override
    public ArrayListStack<String> createStack() {
        return ArrayListStack.create();
    }

    @Override
    public Class<ArrayListStack<String>> type() {
        return Cast.to(ArrayListStack.class);
    }

    @Override
    public ArrayListStack<String> createObject() {
        return ArrayListStack.create();
    }
}
