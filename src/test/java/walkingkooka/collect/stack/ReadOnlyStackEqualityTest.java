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

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

final public class ReadOnlyStackEqualityTest
        extends HashCodeEqualsDefinedEqualityTestCase<ReadOnlyStack<Object>> {

    private final static Object PUSHED = "*pushed onto stack*";

    @Test
    public void testDifferentItems() {
        final Stack<Object> stack = Stacks.arrayList();
        stack.push("*different*");
        assertNotEquals(this.createObject(), ReadOnlyStack.wrap(stack));
    }

    @Test
    public void testSameItemsDifferentStackType() {
        final Stack<Object> stack = Stacks.arrayList();
        stack.push(ReadOnlyStackEqualityTest.PUSHED);
        assertEquals(this.createObject(), stack);
    }

    @Override
    protected ReadOnlyStack<Object> createObject() {
        final Stack<Object> stack = Stacks.arrayList();
        stack.push(ReadOnlyStackEqualityTest.PUSHED);
        return ReadOnlyStack.wrap(stack);
    }
}
