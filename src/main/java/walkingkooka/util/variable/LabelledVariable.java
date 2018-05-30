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

import walkingkooka.text.Whitespace;

import java.util.Objects;

/**
 * Wraps another {@link Variable} and adds a label to any {@link #toString()} dumps.
 */
final class LabelledVariable<T> implements Variable<T> {

    /**
     * Creates a new {@link LabelledVariable}. If the {@link Variable} is also named then its wrapped {@link Variable} is passed so the original name is
     * lost.
     */
    static <T> LabelledVariable<T> wrap(final String name, final Variable<T> variable) {
        Whitespace.failIfNullOrWhitespace(name, "name");
        Objects.requireNonNull(variable, "variable");

        return new LabelledVariable<T>(name,
                variable instanceof LabelledVariable ? ((LabelledVariable<T>) variable).variable : variable);
    }

    private LabelledVariable(final String name, final Variable<T> variable) {
        super();

        this.name = name;
        this.variable = variable;
    }

    @Override
    public void set(final T value) throws VariableException {
        this.variable.set(value);
    }

    @Override
    public T get() throws VariableException {
        return this.variable.get();
    }

    private final Variable<T> variable;

    private final String name;

    @Override
    public String toString() {
        return this.name + '=' + this.variable;
    }
}
