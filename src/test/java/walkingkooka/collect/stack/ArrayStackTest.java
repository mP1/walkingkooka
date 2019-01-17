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

import java.util.Iterator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class ArrayStackTest extends StackTestCase<ArrayStack<String>, String> {

    @Test
    public void testWithOne() {
        final ArrayStack<String> stack = ArrayStack.with("1");
        assertArrayEquals("array", new Object[]{"1"}, stack.array);
        assertEquals("last", 1, stack.last);
    }

    @Test
    public void testWithArray() {
        final ArrayStack<String> stack = ArrayStack.with(new Object[]{"1", "2", "3"});
        assertArrayEquals("array", new Object[]{"1", "2", "3"}, stack.array);
        assertEquals("last", 3, stack.last);
    }

    @Test
    public void testPush() {
        final ArrayStack<String> stack = ArrayStack.with("1");
        assertArrayEquals("array", new Object[]{"1"}, stack.array);
        assertEquals("last", 1, stack.last);
        Assert.assertFalse("stack should NOT be empty", stack.isEmpty());
        assertEquals("size with 1 items", 1, stack.size());
    }

    @Test
    public void testManyPushes() {
        final ArrayStack<String> stack1 = ArrayStack.with("1");
        final ArrayStack<String> stack2 = stack1.push("2");
        Assert.assertNotSame("shouldnt have returned this", stack1, stack2);

        assertArrayEquals("stack.array", new Object[]{"1", "2"}, stack2.array);
        assertEquals("stack.last", 2, stack2.last);
        Assert.assertFalse("stack should NOT be empty", stack2.isEmpty());
        assertEquals("size with 2 items", 2, stack2.size());

        assertArrayEquals("original stack.array was changed", new Object[]{"1"}, stack1.array);
        assertEquals("original stack.last was changed", 1, stack1.last);
    }

    @Test
    public void testPushAllEmptyIteratorReturnsThis() {
        final ArrayStack<Object> stack = ArrayStack.with(new Object[]{"1", "2"});
        assertSame(stack, stack.pushAll(Iterators.empty()));

        assertArrayEquals("stack.array was changed", new Object[]{"1", "2"}, stack.array);
        assertEquals("stack.last was changed", 2, stack.last);
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
    public void testPushAll2() {
        final ArrayStack<String> stack12 = ArrayStack.with(new Object[]{"1", "2"});
        final ArrayStack<String> stack12345 = stack12.pushAll(Lists.of("3", "4", "5").iterator());
        Assert.assertNotSame("returned the original stack after pushing several items",
                stack12,
                stack12345);

        assertArrayEquals("stack.array", new Object[]{"1", "2", "3", "4", "5"}, stack12345.array);
        assertEquals("stack.last", 5, stack12345.last);
        Assert.assertFalse("stack should NOT be empty", stack12345.isEmpty());
        assertEquals("size", 5, stack12345.size());

        assertArrayEquals("original stack.array was changed", new Object[]{"1", "2"}, stack12.array);
        assertEquals("original stack.last was changed", 2, stack12.last);
    }

    @Test
    public void testPop() {
        final Stack<String> with12 = ArrayStack.with("1").push("2");
        if (false == (with12 instanceof ArrayStack)) {
            Assert.fail("Stack should be a ArrayStack=" + with12);
        }

        final Stack<String> popped = with12.pop();
        assertEquals("peeked", "1", popped.peek());
        assertEquals("size with 1 items", 1, popped.size());
    }

    @Test
    public void testPop2() {
        final Stack<String> stack = ArrayStack.with("1").push("2").push("3");
        if (false == (stack instanceof ArrayStack)) {
            Assert.fail("Stack should be a ArrayStack=" + stack);
        }

        final Stack<String> popped = stack.pop();
        assertEquals("size with 2 items", 2, popped.size());
        assertEquals("peeked", "3", stack.peek());
    }

    @Test
    public void testPopIntoEmpty() {
        final Stack<String> stack = ArrayStack.with("-popped-").pop();
        if (false == (stack instanceof EmptyArrayStack)) {
            Assert.fail("Stack should have been empty=" + stack);
        }
    }

    @Test
    public void testPushPushPopPush() {
        final ArrayStack<String> with1 = ArrayStack.with("1");
        final ArrayStack<String> with12 = with1.push("2");
        final Stack<String> with12x3 = with12.pop().push("2*").push("3");

        assertEquals("3", with12x3.peek());
        final Stack<String> with12x = with12x3.pop();
        assertEquals("peeking stack with 2 elements", "2*", with12x.peek());

        final Stack<String> with123b = with12x.push("3b");
        assertEquals("3b", with123b.peek());
        assertEquals("peeking stack with 2 elements", "2*", with12x.peek());

        final Stack<String> with12again = with123b.pop();
        assertEquals("peeking stack with 2 elements", "2*", with12again.peek());

        final Stack<String> with123b4 = with123b.push("4");
        assertEquals("peeking stack with 4 elements", "4", with123b4.peek());

        final Stack<String> with123b4b = with123b.push("4b");
        assertEquals("peeking stack with 4 elements", "4b", with123b4b.peek());
    }

    @Test
    public void testIterator() {
        final Stack<String> stack = ArrayStack.with("1").push("2").push("3");
        final Iterator<String> iterator = stack.iterator();
        Assert.assertTrue("first pending not empty", iterator.hasNext());
        assertEquals("first", "1", iterator.next());

        Assert.assertTrue("second pending not empty", iterator.hasNext());
        assertEquals("second", "2", iterator.next());

        Assert.assertTrue("last pending not empty", iterator.hasNext());
        assertEquals("last", "3", iterator.next());

        Assert.assertFalse("iterator was NOT empty=" + iterator, iterator.hasNext());
    }

    @Test
    public void testIteratorAfterPop() {
        final Stack<String> stack = ArrayStack.with("1").push("2").push("-popped-").pop();
        final Iterator<String> iterator = stack.iterator();
        Assert.assertTrue("first pending not empty", iterator.hasNext());
        assertEquals("first", "1", iterator.next());

        Assert.assertTrue("second pending not empty", iterator.hasNext());
        assertEquals("second", "2", iterator.next());

        Assert.assertFalse("iterator was NOT empty=" + iterator, iterator.hasNext());
    }

    @Test
    public void testIteratorAfterPopPush() {
        final Stack<String> stack = ArrayStack.with("1").push("2").push("-popped-").pop().push("3");
        final Iterator<String> iterator = stack.iterator();
        Assert.assertTrue("first pending not empty", iterator.hasNext());
        assertEquals("first", "1", iterator.next());

        Assert.assertTrue("second pending not empty", iterator.hasNext());
        assertEquals("second", "2", iterator.next());

        Assert.assertTrue("second pending not empty", iterator.hasNext());
        assertEquals("second", "3", iterator.next());

        Assert.assertFalse("iterator was NOT empty=" + iterator, iterator.hasNext());
    }

    @Test
    public void testIteratorWithRemove() {
        final Stack<String> stack = this.createStack();
        final Iterator<String> iterator = stack.iterator();
        try {
            iterator.remove();
            Assert.fail();
        } catch (final UnsupportedOperationException expected) {
        }
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
        final Stack<String> stack = ArrayStack.with("1");
        assertEquals("[1]", stack.toString());
    }

    @Test
    public void testToStringManyItems() {
        final Stack<String> stack = ArrayStack.with("1").push("2").push("3");
        assertEquals("[1,2,3]", stack.toString());
    }

    @Test
    public void testToStringManyItemsAfterPop() {
        final Stack<String> stack = ArrayStack.with("1").push("2").push("-popped-").pop();
        assertEquals("[1,2]", stack.toString());
    }

    @Override
    protected ArrayStack<String> createStack() {
        return ArrayStack.with("1");
    }

    @Override
    public Class<ArrayStack<String>> type() {
        return Cast.to(ArrayStack.class);
    }

    @Override
    public ArrayStack<String> serializableInstance() {
        return ArrayStack.with("1");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }

    @Override
    public ArrayStack<String> createObject() {
        return ArrayStack.with("1");
    }
}
