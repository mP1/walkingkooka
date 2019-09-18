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

package walkingkooka.collect.set;

import walkingkooka.collect.CollectionTesting2;
import walkingkooka.test.TypeNameTesting;

import java.util.Set;

public interface SetTesting2<S extends Set<E>, E> extends CollectionTesting2<S, E>, TypeNameTesting<S> {

    default S createCollection() {
        return this.createSet();
    }

    S createSet();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return Set.class.getSimpleName();
    }
}
