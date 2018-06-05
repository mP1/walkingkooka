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

package walkingkooka.build.chain;

/**
 * A tag interface that adds no methods but should be implemented by any interface that wraps two of
 * the same interface.
 */
public interface Chained<T> {

    /**
     * Getter that returns all the chained items.
     */
    T[] chained();

    /**
     * Returns the {@link ChainType}
     */
    ChainType chainType();
}
