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
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

/**
 * Base class for testing a {@link Stack}.
 */
abstract public class StackTestCase<S extends Stack<T>, T> extends ClassTestCase<S> {

    protected StackTestCase() {
        super();
    }

    @Test
    public void testNaming() {
        this.checkNaming(Stack.class);
    }

    @Test(expected = NullPointerException.class)
    public void testPushAllNullIteratorFails() {
        final S stack = this.createStack();
        stack.pushAll(null);
    }

    @Test final public void testCheckToStringOverridden() {
        this.checkToStringOverridden(this.type());
    }

    abstract protected S createStack();

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
