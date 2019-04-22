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
 *
 */

package walkingkooka.collect.stack;

import org.junit.jupiter.api.Test;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;

import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin interface for testing a {@link Stack}.
 */
public interface StackTesting<S extends Stack<T> & HashCodeEqualsDefined, T> extends HashCodeEqualsDefinedTesting<S>,
        SerializationTesting<S>,
        ToStringTesting<S>,
        TypeNameTesting<S> {

    @Test
    default void testPeekWhenEmptyFails() {
        final Stack<String> stack = ArrayListStack.create();
        assertThrows(EmptyStackException.class, () -> {
            stack.peek();
        });
    }

    @Test
    default void testPopWhenEmptyFails() {
        final Stack<String> stack = ArrayListStack.create();
        assertThrows(EmptyStackException.class, () -> {
            stack.pop();
        });
    }

    @Test
    default void testPushAllNullIteratorFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createStack().pushAll(null);
        });
    }

    S createStack();

    default void checkSize(final Stack<?> stack, final int size) {
        assertEquals(0 == size, stack.isEmpty(), () -> stack + " isEmpty");
        assertEquals(size, stack.size(), () -> stack + " size");
    }

    @Override
    default S createObject() {
        return this.createStack();
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return Stack.class.getSimpleName();
    }
}
