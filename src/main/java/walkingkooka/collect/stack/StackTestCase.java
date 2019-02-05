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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.type.MemberVisibility;

import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Base class for testing a {@link Stack}.
 */
abstract public class StackTestCase<S extends Stack<T> & HashCodeEqualsDefined, T> extends ClassTestCase<S>
        implements HashCodeEqualsDefinedTesting<S>,
        SerializationTesting<S> {

    protected StackTestCase() {
        super();
    }

    @Test
    public void testNaming() {
        this.checkNaming(Stack.class);
    }

    @Test
    public void testPeekWhenEmptyFails() {
        final Stack<String> stack = ArrayListStack.create();
        assertThrows(EmptyStackException.class, () -> {
            stack.peek();
        });
    }

    @Test
    public void testPopWhenEmptyFails() {
        final Stack<String> stack = ArrayListStack.create();
        assertThrows(EmptyStackException.class, () -> {
            stack.pop();
        });
    }

    @Test
    public void testPushAllNullIteratorFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createStack().pushAll(null);
        });
    }

    @Test
    final public void testCheckToStringOverridden() {
        this.checkToStringOverridden(this.type());
    }

    abstract protected S createStack();

    protected void checkSize(final Stack<?> stack, final int size) {
        assertEquals(0 == size, stack.isEmpty(), () -> stack + " isEmpty");
        assertEquals(size, stack.size(), () -> stack + " size");
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public S createObject() {
        return this.createStack();
    }
}
