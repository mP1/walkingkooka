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

import java.util.concurrent.atomic.AtomicReference;

/**
 * A {@link Variable} that uses a {@link AtomicReference} to hold the variable itself.
 */
final class AtomicReferenceVariable<T> implements Variable<T> {

    static <T> AtomicReferenceVariable<T> create() {
        return new AtomicReferenceVariable<T>();
    }

    private AtomicReferenceVariable() {
        super();
    }

    @Override
    public T get() throws VariableException {
        return this.reference.get();
    }

    @Override
    public void set(final T value) throws VariableException {
        this.reference.set(value);
    }

    private final AtomicReference<T> reference = new AtomicReference<T>();

    @Override
    public String toString() {
        return String.valueOf(this.reference.get());
    }
}
