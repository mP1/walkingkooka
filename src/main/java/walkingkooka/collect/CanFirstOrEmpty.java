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

package walkingkooka.collect;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Helpful to safely retrieve the first element of a {@link java.util.Collection} if one exists.
 */
public interface CanFirstOrEmpty<T> {

    static <T> Optional<T> firstOrEmptyCollection(final Collection<T> collection) {
        Objects.requireNonNull(collection, "collection");

        Optional<T> firstOrEmpty;

        if (collection instanceof CanFirstOrEmpty) {
            firstOrEmpty = ((CanFirstOrEmpty) collection)
                .firstOrEmpty();
        } else {
            firstOrEmpty = Optional.ofNullable(
                collection.isEmpty() ?
                    null :
                    collection instanceof List ?
                        ((List<T>) collection).get(0) :
                        collection.iterator()
                            .next()
            );
        }

        return firstOrEmpty;
    }

    /**
     * Returns the first element or empty.
     */
    Optional<T> firstOrEmpty();
}
