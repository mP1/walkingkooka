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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.list.Lists;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

final public class ArrayStackTest extends StackTestCase<ArrayStack<String>, String> implements IteratorTesting {

    @Test
    public void testWithOne() {
        this.check(ArrayStack.with("1"), 1, "1");
    }

    @Test
    public void testWithArray() {
        this.check(ArrayStack.with(new Object[]{"1", "2", "3"}), 3, "1", "2", "3");
    }

    @Test
    public void testPush() {
        final ArrayStack<String> stack = ArrayStack.with("1");
        this.check(stack, 1, "1");
    }

    @Test
    public void testManyPushes() {
        final ArrayStack<String> stack1 = ArrayStack.with("1");
        final ArrayStack<String> stack2 = stack1.push("2");
        assertNotSame(stack1, stack2, "shouldnt have returned this");

        this.check(stack2, 2, "1", "2");
        this.check(stack1, 1, "1");
    }

    @Test
    public void testPushAllEmptyIteratorReturnsThis() {
        final ArrayStack<String> stack = ArrayStack.with(new Object[]{"1", "2"});
        assertSame(stack, stack.pushAll(Iterators.empty()));

        this.check(stack, 2, "1", "2");
    }

    @Test
    public void testPushAll() {
        final ArrayStack<String> stack
            = Cast.to(EmptyArrayStack.<String>instance().pushAll(Lists.of("1", "2", "3")
            .iterator()));
        this.check(stack, 3, "1", "2", "3");
    }

    @Test
    public void testPushAll2() {
        final ArrayStack<String> stack12 = ArrayStack.with(new Object[]{"1", "2"});
        final ArrayStack<String> stack12345 = stack12.pushAll(Lists.of("3", "4", "5").iterator());
        assertNotSame(stack12,
            stack12345,
            "returned the original stack after pushing several items");

        this.check(stack12345, 5, "1", "2", "3", "4", "5");
        this.check(stack12, 2, "1", "2");
    }

    @Test
    public void testPop() {
        final Stack<String> with12 = ArrayStack.with("1").push("2");
        this.checkEquals(ArrayStack.class, with12.getClass(), () -> "Stack should be a ArrayStack=" + with12);

        final Stack<String> popped = with12.pop();
        this.checkEquals("1", popped.peek(), "peeked");
        this.checkEquals(1, popped.size(), "size with 1 items");
    }

    @Test
    public void testPop2() {
        final Stack<String> stack = ArrayStack.with("1").push("2").push("3");
        this.checkEquals(ArrayStack.class, stack.getClass(), () -> "Stack should be a ArrayStack=" + stack);

        final Stack<String> popped = stack.pop();
        this.checkSize(popped, 2);
        this.checkEquals("3", stack.peek(), "peeked");
    }

    @Test
    public void testPopIntoEmpty() {
        final Stack<String> stack = ArrayStack.with("-popped-").pop();
        if (false == (stack instanceof EmptyArrayStack)) {
            Assertions.fail("Stack should have been empty=" + stack);
        }
    }

    @Test
    public void testPushPushPopPush() {
        final ArrayStack<String> with1 = ArrayStack.with("1");
        final ArrayStack<String> with12 = with1.push("2");
        final Stack<String> with12x3 = with12.pop().push("2*").push("3");

        this.checkEquals("3", with12x3.peek());
        final Stack<String> with12x = with12x3.pop();
        this.checkEquals("2*", with12x.peek(), "peeking stack with 2 elements");

        final Stack<String> with123b = with12x.push("3b");
        this.checkEquals("3b", with123b.peek());
        this.checkEquals("2*", with12x.peek(), "peeking stack with 2 elements");

        final Stack<String> with12again = with123b.pop();
        this.checkEquals("2*", with12again.peek(), "peeking stack with 2 elements");

        final Stack<String> with123b4 = with123b.push("4");
        this.checkEquals("4", with123b4.peek(), "peeking stack with 4 elements");

        final Stack<String> with123b4b = with123b.push("4b");
        this.checkEquals("4b", with123b4b.peek(), "peeking stack with 4 elements");
    }

    @Test
    public void testIterator() {
        final Stack<String> stack = ArrayStack.with("1").push("2").push("3");
        final Iterator<String> iterator = stack.iterator();
        assertTrue(iterator.hasNext(), "first pending not empty");
        this.checkEquals("1", iterator.next(), "first");

        assertTrue(iterator.hasNext(), "second pending not empty");
        this.checkEquals("2", iterator.next(), "second");

        assertTrue(iterator.hasNext(), "last pending not empty");
        this.checkEquals("3", iterator.next(), "last");

        assertFalse(iterator.hasNext(), "iterator was NOT empty=" + iterator);
    }

    @Test
    public void testIteratorAfterPop() {
        final Stack<String> stack = ArrayStack.with("1").push("2").push("-popped-").pop();
        final Iterator<String> iterator = stack.iterator();
        assertTrue(iterator.hasNext(), "first pending not empty");
        this.checkEquals("1", iterator.next(), "first");

        assertTrue(iterator.hasNext(), "second pending not empty");
        this.checkEquals("2", iterator.next(), "second");

        assertFalse(iterator.hasNext(), "iterator was NOT empty=" + iterator);
    }

    @Test
    public void testIteratorAfterPopPush() {
        final Stack<String> stack = ArrayStack.with("1").push("2").push("-popped-").pop().push("3");
        this.iterateAndCheck(stack.iterator(), "1", "2", "3");
    }

    @Test
    public void testIteratorWithRemove() {
        this.removeUnsupportedFails(this.createStack().iterator());
    }

    @Test
    public void testEqualsAgainstEmpty() {
        this.checkNotEquals(Stacks.arrayList());
    }

    @Test
    public void testEqualsDifferentItemCount() {
        final Stack<String> stack1 = this.createObject();
        final Stack<String> stack2 = this.createObject().push("2");
        checkNotEquals(stack1, stack2);
    }

    @Test
    public void testEqualsDifferentItems() {
        this.checkNotEquals(ArrayStack.with("different"));
    }

    @Test
    public void testEqualsDifferentItems2() {
        checkNotEquals(this.createObject().push("2"), ArrayStack.with("different").push("2"));
    }

    @Test
    public void testEqualsDifferentItems3() {
        checkNotEquals(this.createObject().push("2"), ArrayStack.with("1").push("different"));
    }

    @Test
    public void testEqualsDifferentItemsAndDifferentStackType() {
        this.checkNotEquals(Stacks.arrayList().push("different"));
    }

    @Test
    public void testEqualsDifferentItemsAndDifferentStackType2() {
        checkNotEquals(this.createObject().push("2"),
            Stacks.arrayList().push("1").push("different"));
    }

    @Test
    public void testSameItemsDifferentStackType() {
        final Stack<String> stack1 = this.createObject().push("2");
        final Stack<String> stack2 = Stacks.<String>arrayList().push("1").push("2");
        checkEqualsAndHashCode(stack1, stack2);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(ArrayStack.with("1"), "[1]");
    }

    @Test
    public void testToStringManyItems() {
        this.toStringAndCheck(ArrayStack.with("1").push("2").push("3"), "[1,2,3]");
    }

    @Test
    public void testToStringManyItemsAfterPop() {
        this.toStringAndCheck(ArrayStack.with("1").push("2").push("-popped-").pop(), "[1,2]");
    }

    @Override
    public ArrayStack<String> createStack() {
        return ArrayStack.with("1");
    }

    @Override
    public Class<ArrayStack<String>> type() {
        return Cast.to(ArrayStack.class);
    }

    @Override
    public ArrayStack<String> createObject() {
        return ArrayStack.with("1");
    }

    private void check(final ArrayStack<String> stack, final int last, final String... array) {
        assertArrayEquals(array, stack.array, "array");
        this.checkEquals(last, stack.last, "last");

        this.checkSize(stack, array.length);
    }
}
