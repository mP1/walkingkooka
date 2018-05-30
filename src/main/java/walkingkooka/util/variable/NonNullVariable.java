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
 * Wraps another {@link Variable} and adds a check to {@link #set(Object)} that complains if null is passed.
 */
final class NonNullVariable<T> implements Variable<T> {

    /**
     * Creates a new empty {@link NonNullVariable} with a check to verify that a double wrap does not occur.
     */
    static <T> NonNullVariable<T> wrap(final Variable<T> variable) {
        Objects.requireNonNull(variable, "variable");

        return variable instanceof NonNullVariable ? (NonNullVariable<T>) variable : new NonNullVariable<T>(variable);
    }

    private NonNullVariable(final Variable<T> variable) {
        super();
        this.variable = variable;
    }

    /**
     * Before forwarding the setter verify that the value is not null.
     */
    @Override
    public void set(final T value) throws VariableException {
        Objects.requireNonNull(value, "value");
        this.variable.set(value);
    }

    @Override
    public T get() throws VariableException {
        return this.variable.get();
    }

    private final Variable<T> variable;

    @Override
    public String toString() {
        return "non null " + this.variable;
    }
}
