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

import static org.junit.Assert.assertNotEquals;

final public class ArrayListStackEqualityTest
        extends HashCodeEqualsDefinedEqualityTestCase<ArrayListStack<String>> {

    @Test
    public void testBothEmpty() {
        final ArrayListStack<String> stack1 = ArrayListStack.create();
        final ArrayListStack<String> stack2 = ArrayListStack.create();
        HashCodeEqualsDefinedEqualityTestCase.checkEqualsAndHashCode(stack1, stack2);
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
        HashCodeEqualsDefinedEqualityTestCase.checkEqualsAndHashCode(stack1, stack2);
    }

    @Test
    public void testDifferentItems() {
        final ArrayListStack<String> stack1 = ArrayListStack.create();
        final ArrayListStack<String> stack2 = ArrayListStack.create();
        stack1.push("1");
        stack1.push("2");
        stack2.push("3");
        stack2.push("4");
        assertNotEquals(stack1, stack2);
    }

    @Test
    public void testSameItemsDifferentStackType() {
        final ArrayListStack<String> stack1 = ArrayListStack.create();
        final Stack<String> stack2 = Stacks.jdk();
        stack1.push("1");
        stack1.push("2");
        stack2.push("1");
        stack2.push("2");
        assertEquals(stack1, stack2);
    }

    @Override
    protected ArrayListStack<String> createObject() {
        return ArrayListStack.create();
    }
}
