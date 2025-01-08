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

import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class JdkStackStackTest extends StackTestCase<JdkStackStack<Object>, Object>
    implements IteratorTesting {

    @Test
    public void testCreate() {
        final Stack<Object> stack = JdkStackStack.create();
        this.checkSize(stack, 0);
    }

    @Test
    public void testPush() {
        final JdkStackStack<String> stack = JdkStackStack.create();

        assertSame(stack, stack.push("1"), "this not returned");
        this.checkSize(stack, 1);

        assertSame(stack, stack.push("2"), "this not returned");
        this.checkSize(stack, 2);

        final java.util.Stack<String> jdkStack = new java.util.Stack<>();
        jdkStack.push("1");
        jdkStack.push("2");
        this.checkEquals(jdkStack, stack.stack, "items on wrapped jdk stack");
    }

    @Test
    public void testPushAllEmptyIterator() {
        final JdkStackStack<String> stack = JdkStackStack.create();
        assertSame(stack, stack.pushAll(Iterators.empty()), "this not returned");
        this.checkEquals(new java.util.Stack<String>(), stack.stack, "wrapped jdk stack items");
    }

    @Test
    public void testPushAll() {
        final JdkStackStack<String> stack = JdkStackStack.create();

        assertSame(stack, stack.pushAll(Lists.of("1", "2").iterator()), "this was not returned");
        this.checkSize(stack, 2);

        final java.util.Stack<String> jdkStack = new java.util.Stack<>();
        jdkStack.push("1");
        jdkStack.push("2");
        this.checkEquals(jdkStack, stack.stack, "items on wrapped jdk stack");
    }

    @Test
    public void testPopAndPeek() {
        final JdkStackStack<String> stack = JdkStackStack.create();

        stack.push("1");
        this.checkSize(stack, 1);

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
    public void testPeekWhenEmptyFails() {
        final Stack<Object> stack = JdkStackStack.create();
        assertThrows(EmptyStackException.class, stack::peek);
    }

    @Test
    public void testPopWhenEmptyFails() {
        final Stack<Object> stack = JdkStackStack.create();
        assertThrows(EmptyStackException.class, stack::pop);
    }

    @Test
    public void testIterator() {
        final JdkStackStack<String> stack = JdkStackStack.create();
        stack.push("1");
        stack.push("2");
        stack.push("3");

        this.iterateAndCheck(stack.iterator(), "1", "2", "3");
    }

    @Test
    public void testIteratorWithRemove() {
        final JdkStackStack<String> stack = JdkStackStack.create();
        stack.push("1");
        stack.push("2");
        stack.push("3");

        this.iterateAndCheck(stack.iterator(), "1", "2", "3");

        assertSame("3", stack.peek());
        stack.pop();
        this.checkSize(stack, 2);
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testEqual() {
        final JdkStackStack<String> stack1 = JdkStackStack.create();
        final JdkStackStack<String> stack2 = JdkStackStack.create();
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
        final JdkStackStack<String> stack1 = JdkStackStack.create();
        final JdkStackStack<String> stack2 = JdkStackStack.create();
        stack1.push("1");
        stack1.push("2");
        stack2.push("3");
        stack2.push("4");
        this.checkNotEquals(stack1, stack2);
    }

    @Test
    public void testEqualsSameItemsDifferentStackType() {
        final JdkStackStack<String> stack1 = JdkStackStack.create();
        final Stack<String> stack2 = Stacks.arrayList();
        stack1.push("1");
        stack1.push("2");
        stack2.push("1");
        stack2.push("2");
        this.checkEquals(stack1, stack2);
    }

    @Test
    public void testToString() {
        final JdkStackStack<String> stack = JdkStackStack.create();
        stack.push("1");
        stack.push("2");
        stack.push("3");
        this.toStringAndCheck(stack.toString(), Lists.of("1", "2", "3").toString());
    }

    @Override
    public JdkStackStack<Object> createStack() {
        return JdkStackStack.create();
    }

    @Override
    public Class<JdkStackStack<Object>> type() {
        return Cast.to(JdkStackStack.class);
    }
}
