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

package walkingkooka.naming;

/**
 * Base class for testing a {@link Name} with mostly helpers to check construction failure.
 */
public interface ValueNameTesting<N extends ValueName<V>, C extends Comparable<C>, V> extends NameTesting<N, C> {

    default void typeAndCheck(final N name,
                              final V expected) {
        this.checkEquals(
            expected,
            name.type(),
            name::toString
        );
    }
}
