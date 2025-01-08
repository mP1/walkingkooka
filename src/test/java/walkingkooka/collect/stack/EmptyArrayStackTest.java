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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

final public class EmptyArrayStackTest extends StackTestCase<EmptyArrayStack<Object>, Object>
    implements IteratorTesting {

    @Test
    public void testCreate() {
        final Stack<Object> stack = EmptyArrayStack.instance();
        this.checkSize(stack, 0);
    }

    @Test
    public void testPush() {
        final ArrayStack<String> stack = Cast.to(EmptyArrayStack.instance().push("1"));
        assertArrayEquals(new Object[]{"1"}, stack.array, "array");
        this.checkEquals(1, stack.last, "last");
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
        assertArrayEquals(new Object[]{"1", "2", "3"}, stack.array, "array");
        this.checkEquals(3, stack.last, "last");
    }

    @Test
    public void testIterator() {
        this.iterateAndCheck(EmptyArrayStack.instance().iterator());
    }

    @Test
    public void testIteratorWithRemove() {
        this.removeWithoutNextFails(EmptyArrayStack.instance().iterator());
    }

    @Test
    public void testBothEmpty() {
        final EmptyArrayStack<String> stack1 = EmptyArrayStack.instance();
        final Stack<String> stack2 = Stacks.arrayList();
        this.checkEqualsAndHashCode(stack1, stack2);
    }

    @Test
    public void testOtherNotEmpty() {
        final EmptyArrayStack<String> stack1 = EmptyArrayStack.instance();
        final Stack<String> stack2 = Stacks.arrayList();
        stack2.push("*");
        this.checkNotEquals(stack1, stack2);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(EmptyArrayStack.instance(), "[]");
    }

    @Override
    public EmptyArrayStack<Object> createStack() {
        return EmptyArrayStack.instance();
    }

    @Override
    public Class<EmptyArrayStack<Object>> type() {
        return Cast.to(EmptyArrayStack.class);
    }
}
