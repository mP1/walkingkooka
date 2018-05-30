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

final public class UnreadableStackTest extends StackTestCase<UnreadableStack<Object>, Object> {

    // constants

    private final static Stack<Object> STACK = Stacks.fake();

    // tests

    @Test
    public void testWithNullStackFails() {
        try {
            UnreadableStack.wrap(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testDoesntWrapUnreadableStack() {
        final UnreadableStack<Object> stack = this.createStack();
        assertSame(stack, UnreadableStack.wrap(stack));
    }

    @Test
    public void testPeekFails() {
        final UnreadableStack<Object> stack = this.createStack();
        try {
            stack.peek();
            Assert.fail();
        } catch (final UnsupportedOperationException expected) {
        }
    }

    @Test
    public void testPopFails() {
        final UnreadableStack<Object> stack = this.createStack();
        try {
            stack.pop();
            Assert.fail();
        } catch (final UnsupportedOperationException expected) {
        }
    }

    @Test
    public void testIsEmptyFails() {
        final UnreadableStack<Object> stack = this.createStack();
        try {
            stack.isEmpty();
            Assert.fail();
        } catch (final UnsupportedOperationException expected) {
        }
    }

    @Test
    public void testSizeFails() {
        final UnreadableStack<Object> stack = this.createStack();
        try {
            stack.size();
            Assert.fail();
        } catch (final UnsupportedOperationException expected) {
        }
    }

    @Test
    public void testPush() {
        final Stack<Object> stack = Stacks.arrayList();
        final Object pushed = new Object();
        final UnreadableStack<Object> unreadable = UnreadableStack.wrap(stack);
        final UnreadableStack<Object> unreadable2 = unreadable.push(pushed);
        assertSame(unreadable, unreadable2);
        assertSame(pushed, stack.peek());
    }

    @Test
    public void testToString() {
        Assert.assertEquals(UnreadableStackTest.STACK.toString(), this.createStack().toString());
    }

    @Override
    protected UnreadableStack<Object> createStack() {
        return UnreadableStack.wrap(UnreadableStackTest.STACK);
    }

    @Override
    protected Class<UnreadableStack<Object>> type() {
        return Cast.to(UnreadableStack.class);
    }
}
