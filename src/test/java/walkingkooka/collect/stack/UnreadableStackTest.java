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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class UnreadableStackTest extends StackTestCase<UnreadableStack<Object>, Object> {

    // constants

    private final static Stack<Object> STACK = Stacks.fake();

    // tests

    @Test
    public void testWithNullStackFails() {
        assertThrows(NullPointerException.class, () -> UnreadableStack.wrap(null));
    }

    @Test
    public void testDoesntWrapUnreadableStack() {
        final UnreadableStack<Object> stack = this.createStack();
        assertSame(stack, UnreadableStack.wrap(stack));
    }

    @Test
    public void testPeekFails() {
        final UnreadableStack<Object> stack = this.createStack();
        assertThrows(UnsupportedOperationException.class, stack::peek);
    }

    @Test
    public void testPopFails() {
        final UnreadableStack<Object> stack = this.createStack();
        assertThrows(UnsupportedOperationException.class, stack::pop);
    }

    @Test
    public void testIsEmptyFails() {
        final UnreadableStack<Object> stack = this.createStack();
        assertThrows(UnsupportedOperationException.class, stack::isEmpty);
    }

    @Test
    public void testSizeFails() {
        final UnreadableStack<Object> stack = this.createStack();
        assertThrows(UnsupportedOperationException.class, stack::size);
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

    @Override
    public void testHashCodeAgainstJavaUtilStack() {
    }

    @Test
    public void testHashCode() {
        final UnreadableStack<Object> stack = UnreadableStack.wrap(new FakeStack<>() {

            @Override
            public int hashCode() {
                return 123;
            }
        });
        this.checkEquals(123, stack.hashCode());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createStack(), STACK.toString());
    }

    @Override
    public UnreadableStack<Object> createStack() {
        return UnreadableStack.wrap(STACK);
    }

    @Override
    public Class<UnreadableStack<Object>> type() {
        return Cast.to(UnreadableStack.class);
    }
}
