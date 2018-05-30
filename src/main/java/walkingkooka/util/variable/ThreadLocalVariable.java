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
 * A {@link Variable} that uses a {@link ThreadLocal} to hold its values.
 */
final class ThreadLocalVariable<T> implements Variable<T> {

    /**
     * Creates a {@link ThreadLocalVariable} with an empty {@link ThreadLocal}.
     */
    static <T> ThreadLocalVariable<T> createWithThreadLocal() {
        return new ThreadLocalVariable<T>(new ThreadLocal<T>());
    }

    /**
     * Creates a {@link ThreadLocalVariable} with an empty {@link InheritableThreadLocal}.
     */
    static <T> ThreadLocalVariable<T> createWithInheritableThreadLocal() {
        return new ThreadLocalVariable<T>(new InheritableThreadLocal<T>());
    }

    /**
     * Private constructor use factory.
     */
    private ThreadLocalVariable(final ThreadLocal<T> value) {
        super();
        this.value = value;
    }

    /**
     * If null is passed the value is removed from the {@link ThreadLocal} otherwise it is just set.
     */
    @Override
    public void set(final T value) throws VariableException {
        final ThreadLocal<T> threadLocal = this.value;
        if (null == value) {
            threadLocal.remove();
        } else {
            threadLocal.set(value);
        }
    }

    @Override
    public T get() throws VariableException {
        return this.value.get();
    }

    private final ThreadLocal<T> value;

    @Override
    public String toString() {
        return String.valueOf(this.value.get());
    }
}
