/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
package walkingkooka.tree.pojo;

import java.lang.reflect.Method;

/**
 * Base class for all reflection based {@link PojoProperty} properties.
 */
abstract class ReflectionPojoProperty implements PojoProperty {

    static PojoProperty with(final PojoName name, final Method getter, final Method setter) {
        return null != setter ?
                ReflectionWritablePojoProperty.with(name, getter, setter) :
                new ReflectionReadOnlyPojoProperty(name, getter);
    }

    /**
     * package private to limit sub classing, use factory.
     */
    ReflectionPojoProperty(final PojoName name, final Method getter) {
        this.name = name;
        this.getter = this.illegalAccessExceptionFix(getter);
    }

    final Method illegalAccessExceptionFix(final Method method) {
        method.setAccessible(true);
        return method;
    }

    // PojoProperty ...................................................................................

    @Override
    public PojoName name() {
        return this.name;
    }

    private final PojoName name;

    @Override
    public final Object get(final Object instance) {
        try {
            return this.getter.invoke(instance);
        } catch (final Exception cause) {
            throw new ReflectionPojoException("Unable to get property " + this.name().inQuotes() + ", " + cause.getMessage(), cause);
        }
    }

    private final Method getter;

    // Object ...................................................................................

    @Override
    public final String toString() {
        return this.name.toString();
    }
}
