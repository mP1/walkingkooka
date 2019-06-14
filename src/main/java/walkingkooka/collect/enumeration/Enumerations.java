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

package walkingkooka.collect.enumeration;

import walkingkooka.type.PublicStaticHelper;

import java.util.Enumeration;
import java.util.Iterator;

final public class Enumerations implements PublicStaticHelper {

    /**
     * {@see ArrayEnumeration}
     */
    public static <E> Enumeration<E> array(final Object[] array) {
        return ArrayEnumeration.with(array);
    }

    /**
     * {@see ChainEnumeration}
     */
    public static <E> Enumeration<E> chain(final Enumeration<E> first,
                                           final Enumeration<E>... enumerations) {
        return ChainEnumeration.with(first, enumerations);
    }

    /**
     * {@see EmptyEnumeration}
     */
    public static <T> Enumeration<T> empty() {
        return EmptyEnumeration.instance();
    }

    /**
     * {@see FakeEnumeration}
     */
    public static <T> Enumeration<T> fake() {
        return FakeEnumeration.create();
    }

    /**
     * {@see IteratorEnumeration}.
     */
    public static <E> Enumeration<E> iterator(final Iterator<E> iterator) {
        return IteratorEnumeration.adapt(iterator);
    }

    /**
     * Stop creation
     */
    private Enumerations() {
        throw new UnsupportedOperationException();
    }
}
