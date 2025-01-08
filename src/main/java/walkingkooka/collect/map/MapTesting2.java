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

package walkingkooka.collect.map;

import walkingkooka.ToStringTesting;
import walkingkooka.reflect.TypeNameTesting;

import java.util.Map;

/**
 * Interface with default methods which can be mixed in to assist testing of an {@link Map}.
 */
public interface MapTesting2<M extends Map<K, V>, K, V> extends MapTesting<M, K, V>,
    ToStringTesting<M>,
    TypeNameTesting<M> {

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return Map.class.getSimpleName();
    }
}
