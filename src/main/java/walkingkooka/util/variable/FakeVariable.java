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

package walkingkooka.util.variable;

import walkingkooka.test.Fake;

/**
 * A {@link Variable} that always throws {@link UnsupportedOperationException}
 */
public class FakeVariable<T> implements Variable<T>, Fake {

    /**
     * Creates a new {@link FakeVariable}
     */
    static <T> FakeVariable<T> create() {
        return new FakeVariable<T>();
    }

    /**
     * Protected for sub classing
     */
    protected FakeVariable() {
    }

    @Override
    public void set(final T value) throws VariableException {
        throw new UnsupportedOperationException();
    }

    @Override
    public T get() throws VariableException {
        throw new UnsupportedOperationException();
    }
}
