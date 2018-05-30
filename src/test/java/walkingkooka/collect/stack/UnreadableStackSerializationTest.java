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

import walkingkooka.Cast;
import walkingkooka.test.SerializationTestCase;

final public class UnreadableStackSerializationTest
        extends SerializationTestCase<UnreadableStack<Object>> {

    @Override
    protected Class<UnreadableStack<Object>> type() {
        return Cast.to(UnreadableStack.class);
    }

    @Override
    protected UnreadableStack<Object> create() {
        final Stack<Object> stack = Stacks.arrayList();
        stack.push("*pushed onto stack*");
        return UnreadableStack.wrap(stack);
    }

    @Override
    protected boolean isSingleton() {
        return false;
    }
}
