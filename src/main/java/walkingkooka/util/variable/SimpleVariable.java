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

/**
 * Holds a single mutable value or reference including null.
 */
final class SimpleVariable<T> implements Variable<T> {

    /**
     * Creates a new {@link SimpleVariable} set to the given value.
     */
    static <T> SimpleVariable<T> with(final T value) {
        return new SimpleVariable<T>(value);
    }

    private SimpleVariable(final T value) {
        this.value = value;
    }

    @Override
    public void set(final T value) throws VariableException {
        this.value = value;
    }

    @Override
    public T get() throws VariableException {
        return this.value;
    }

    private T value;

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
