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

import java.util.Objects;

/**
 * Wraps another {@link Variable} only allowing a single {@link #set(Object)}. Subsequent sets result in a {@link UnsupportedOperationException} being
 * thrown.
 */
final class SetOnceVariable<T> implements Variable<T> {

    /**
     * Creates a new {@link SetOnceVariable} which wraps another {@link Variable}. Wrapping an already {@link SetOnceVariable} returns this.
     */
    static <T> SetOnceVariable<T> wrap(final Variable<T> variable) {
        Objects.requireNonNull(variable, "variable");

        return variable instanceof SetOnceVariable ? (SetOnceVariable<T>) variable : new SetOnceVariable<T>(variable);
    }

    private SetOnceVariable(final Variable<T> variable) {
        super();
        this.variable = variable;
    }

    /**
     * Allows the first set but throws {@link UnsupportedOperationException} if tried again.
     */
    @Override
    public void set(final T value) throws VariableException {
        if (this.set) {
            throw new UnsupportedOperationException();
        }
        this.set = true;
        this.variable.set(value);
    }

    private volatile boolean set = false;

    /**
     * Invokes {@link Variable#get()} on the wrapped variable.
     */
    @Override
    public T get() throws VariableException {
        return this.variable.get();
    }

    /**
     * The wrapped {@link Variable}
     */
    private final Variable<T> variable;

    /**
     * Returns the wrapped {@link Variable#toString()}.
     */
    @Override
    public String toString() {
        return this.variable.toString();
    }
}
