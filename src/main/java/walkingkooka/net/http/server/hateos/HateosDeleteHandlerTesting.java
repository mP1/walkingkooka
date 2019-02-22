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
        this.deleteFails(null,
                this.createContext(),
                NullPointerException.class);
    }

    @Test
    default void testDeleteNullContextFails() {
        this.deleteFails(this.id(),
                null,
                NullPointerException.class);
    }

    default void delete(final I id,
                        final HateosHandlerContext<N> context) {
        this.createHandler().delete(id, context);
    }

    default <T extends Throwable> T deleteFails(final I id,
                                                final HateosHandlerContext<N> context,
                                                final Class<T> thrown) {
        return this.deleteFails(this.createHandler(),
                id,
                context,
                thrown);
    }

    default <T extends Throwable> T deleteFails(final HateosDeleteHandler<I, N> handler,
                                                final I id,
                                                final HateosHandlerContext<N> context,
                                                final Class<T> thrown) {
        return assertThrows(thrown, () -> {
            handler.delete(id, context);
        });
    }

    @Test
    default void testDeleteCollectionNullIdRangeFails() {
        this.deleteCollectionFails(null,
                this.createContext(),
                NullPointerException.class);
    }

    @Test
    default void testDeleteCollectionNullContextFails() {
        this.deleteCollectionFails(this.collection(),
                null,
                NullPointerException.class);
    }

    default void deleteCollection(final Range<I> collection,
                                  final HateosHandlerContext<N> context) {
        this.createHandler().deleteCollection(collection, context);
    }

    default <T extends Throwable> T deleteCollectionFails(final Range<I> ids,
                                                          final HateosHandlerContext<N> context,
                                                          final Class<T> thrown) {
        return this.deleteCollectionFails(this.createHandler(),
                ids,
                context,
                thrown);
    }

    default <T extends Throwable> T deleteCollectionFails(final HateosDeleteHandler<I, N> handler,
                                                          final Range<I> ids,
                                                          final HateosHandlerContext<N> context,
                                                          final Class<T> thrown) {
        return assertThrows(thrown, () -> {
            handler.deleteCollection(ids, context);
        });
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
