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


import walkingkooka.type.PublicStaticHelper;

final public class Variables implements PublicStaticHelper {

    /**
     * {@see AtomicReferenceVariable}
     */
    public static <T> Variable<T> atomicReference() {
        return AtomicReferenceVariable.create();
    }

    /**
     * {@see FakeVariable}.
     */
    public static <T> Variable<T> fake() {
        return FakeVariable.create();
    }

    /**
     * {@see ThreadLocalVariable#createWithInheritableThreadLocal()}.
     */
    public static <T> Variable<T> inheritableThreadLocal() {
        return ThreadLocalVariable.createWithInheritableThreadLocal();
    }

    /**
     * {@see LabelledVariable}.
     */
    public static <T> Variable<T> labelled(final String name, final Variable<T> variable) {
        return LabelledVariable.wrap(name, variable);
    }

    /**
     * {@see NonNullVariable}.
     */
    public static <T> Variable<T> nonNull(final Variable<T> variable) {
        return NonNullVariable.wrap(variable);
    }

    /**
     * {@see ReadOnlyVariable}.
     */
    public static <T> Variable<T> readOnly(final Variable<T> variable) {
        return ReadOnlyVariable.wrap(variable);
    }

    /**
     * {@see SetOnceVariable}.
     */
    public static <T> Variable<T> setOnce(final Variable<T> variable) {
        return SetOnceVariable.wrap(variable);
    }

    /**
     * {@see ThreadLocalVariable#createWithThreadLocal()}.
     */
    public static <T> Variable<T> threadLocal() {
        return ThreadLocalVariable.createWithThreadLocal();
    }

    /**
     * {@see SimpleVariable}.
     */
    public static <T> Variable<T> with(final T value) {
        return SimpleVariable.with(value);
    }

    /**
     * Stop creation
     */
    private Variables() {
        throw new UnsupportedOperationException();
    }
}
