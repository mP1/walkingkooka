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

final public class ArrayStackEqualityTest
        extends HashCodeEqualsDefinedEqualityTestCase<ArrayStack<String>> {

    @Test
    public void testAgainstEmpty() {
        this.checkNotEquals(Stacks.arrayList());
    }

    @Test
    public void testDifferentItemCount() {
        final Stack<String> stack1 = this.createObject();
        final Stack<String> stack2 = this.createObject().push("2");
        checkNotEquals(stack1, stack2);
    }

    @Test
    public void testDifferentItems() {
        this.checkNotEquals(ArrayStack.with("different"));
    }

    @Test
    public void testDifferentItems2() {
        checkNotEquals(this.createObject().push("2"), ArrayStack.with("different").push("2"));
    }

    @Test
    public void testDifferentItems3() {
        checkNotEquals(this.createObject().push("2"), ArrayStack.with("1").push("different"));
    }

    @Test
    public void testDifferentItemsAndDifferentStackType() {
        this.checkNotEquals(Stacks.arrayList().push("different"));
    }

    @Test
    public void testDifferentItemsAndDifferentStackType2() {
        checkNotEquals(this.createObject().push("2"),
                Stacks.arrayList().push("1").push("different"));
    }

    @Test
    public void testSameItemsDifferentStackType() {
        final Stack<String> stack1 = this.createObject().push("2");
        final Stack<String> stack2 = Stacks.<String>arrayList().push("1").push("2");
        checkEqualsAndHashCode(stack1, stack2);
    }

    @Override
    protected ArrayStack<String> createObject() {
        return ArrayStack.with("1");
    }
}
