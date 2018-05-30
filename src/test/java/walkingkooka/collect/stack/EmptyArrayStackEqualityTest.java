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

final public class EmptyArrayStackEqualityTest
        extends HashCodeEqualsDefinedEqualityTestCase<EmptyArrayStack<String>> {

    @Test
    public void testBothEmpty() {
        final EmptyArrayStack<String> stack1 = EmptyArrayStack.instance();
        final Stack<String> stack2 = Stacks.arrayList();
        HashCodeEqualsDefinedEqualityTestCase.checkEqualsAndHashCode(stack1, stack2);
    }

    @Test
    public void testOtherNotEmpty() {
        final EmptyArrayStack<String> stack1 = EmptyArrayStack.instance();
        final Stack<String> stack2 = Stacks.arrayList();
        stack2.push("*");
        HashCodeEqualsDefinedEqualityTestCase.checkNotEquals(stack1, stack2);
    }

    @Override
    protected EmptyArrayStack<String> createObject() {
        return EmptyArrayStack.instance();
    }
}
