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

import walkingkooka.text.CharSequences;

import java.lang.reflect.Method;

/**
 * A {@link PojoProperty} that uses two {@link Method} to get and set a property.
 */
abstract class ReflectionWritablePojoProperty extends ReflectionPojoProperty {

    static ReflectionWritablePojoProperty with(final PojoName name, final Method getter, final Method setter) {
        return isMutable(setter) ?
                new ReflectionMutableWritablePojoProperty(name, getter, setter) :
                new ReflectionImmutableWritablePojoProperty(name, getter, setter);
    }

    /**
     * This methods true for setters that return void or Object, with the later included to match
     * {@link java.util.Map.Entry#setValue(Object)}
     */
    private static boolean isMutable(final Method setter) {
        final Class<?> returnType = setter.getReturnType();
        return Void.TYPE == returnType || Object.class == returnType;
    }

    /**
     * Ctor only called by {@link ReflectionPojoNodeContext}
     */
    ReflectionWritablePojoProperty(final PojoName name, final Method getter, final Method setter) {
        super(name, getter);
        this.setter = this.illegalAccessExceptionFix(setter);
    }

    @Override
    public Object set(final Object instance, final Object value) {
        try {
            return this.set0(instance, value);
        } catch (final Exception cause) {
            throw new ReflectionPojoException("Unable to set property " + this.name().inQuotes() + " (" + this.setter + ")" +
                    " with " + (null == value ? null : value.getClass() + "=" + CharSequences.quoteIfChars(value)) +
                    ", " + cause.getMessage(), cause);
        }
    }

    abstract Object set0(final Object instance, final Object value) throws Exception;

    final Method setter;

    @Override
    public final boolean isReadOnly() {
        return false;
    }
}
