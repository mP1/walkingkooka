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
 *
 */

package walkingkooka.net.http.server.hateos;

import org.junit.jupiter.api.Test;
import walkingkooka.compare.Range;
import walkingkooka.tree.Node;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin interface for testing {@link HateosDeleteHandler}
 */
public interface HateosDeleteHandlerTesting<H extends HateosDeleteHandler<I, N>, I extends Comparable<I>, N extends Node<N, ?, ?, ?>> extends HateosHandlerTesting<H, I, N> {

    @Test
    default void testDeleteNullIdFails() {
        assertThrows(NullPointerException.class, () -> {
            this.delete(null,
                    this.createContext());
        });
    }

    @Test
    default void testDeleteNullContextFails() {
        assertThrows(NullPointerException.class, () -> {
            this.delete(this.id(),
                    null);
        });
    }

    default void delete(final I id,
                        final HateosHandlerContext<N> context) {
        this.createHandler().delete(id, context);
    }

    @Test
    default void testDeleteCollectionNullIdRangeFails() {
        assertThrows(NullPointerException.class, () -> {
            this.deleteCollection(null,
                    this.createContext());
        });
    }

    @Test
    default void testDeleteCollectionNullContextFails() {
        assertThrows(NullPointerException.class, () -> {
            this.deleteCollection(this.collection(),
                    null);
        });
    }

    default void deleteCollection(final Range<I> collection,
                                  final HateosHandlerContext<N> context) {
        this.createHandler().deleteCollection(collection, context);
    }

    I id();

    Range<I> collection();

    N resource();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNameSuffix() {
        return "DeleteHandler";
    }
}
