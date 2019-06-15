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
 * A {@link PojoProperty} that uses two {@link Method} to get and set a property, for void and set methods that dont
 * return something like this.
 */
final class ReflectionMutableWritablePojoProperty extends ReflectionWritablePojoProperty {

    /**
     * Ctor only called by {@link ReflectionPojoNodeContext}
     */
    ReflectionMutableWritablePojoProperty(final PojoName name, final Method getter, final Method setter) {
        super(name, getter, setter);
    }

    @Override
    Object set0(final Object instance, final Object value) throws Exception {
        this.setter.invoke(instance, value);
        return null;
    }
}
