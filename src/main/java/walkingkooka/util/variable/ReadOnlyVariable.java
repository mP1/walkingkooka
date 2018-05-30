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
 * Wraps another {@link Variable} but prevents any attempts to set.
 */
final class ReadOnlyVariable<T> implements Variable<T> {

    /**
     * Creates a new {@link ReadOnlyVariable} which wraps another {@link Variable}. Wrapping an already {@link ReadOnlyVariable} returns this.
     */
    static <T> ReadOnlyVariable<T> wrap(final Variable<T> variable) {
        Objects.requireNonNull(variable, "variable");

        return variable instanceof ReadOnlyVariable ? (ReadOnlyVariable<T>) variable : new ReadOnlyVariable<T>(variable);
    }

    private ReadOnlyVariable(final Variable<T> variable) {
        super();
        this.variable = variable;
    }

    /**
     * Throws {@link UnsupportedOperationException}.
     */
    @Override
    public void set(final T value) throws VariableException {
        throw new UnsupportedOperationException();
    }

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
     * Dumps <code>read only</code> followed by the wrapped {@link Variable#toString()}.
     */
    @Override
    public String toString() {
        return "read only " + this.variable;
    }
}
