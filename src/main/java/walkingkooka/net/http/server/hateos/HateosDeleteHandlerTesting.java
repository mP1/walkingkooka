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
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.tree.Node;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin interface for testing {@link HateosDeleteHandler}
 */
public interface HateosDeleteHandlerTesting<H extends HateosDeleteHandler<I, N>, I extends Comparable<I>, N extends Node<N, ?, ?, ?>> extends HateosHandlerTesting<H, I, N> {

    @Test
    default void testDeleteNullIdFails() {
        this.deleteFails(null,
                this.resource(),
                this.parameters(),
                this.createContext(),
                NullPointerException.class);
    }

    @Test
    default void testDeleteNullResourceFails() {
        this.deleteFails(this.id(),
                null,
                this.parameters(),
                this.createContext(),
                NullPointerException.class);
    }

    @Test
    default void testDeleteNullParametersFails() {
        this.deleteFails(this.id(),
                this.resource(),
                null,
                this.createContext(),
                NullPointerException.class);
    }

    @Test
    default void testDeleteNullContextFails() {
        this.deleteFails(this.id(),
                this.resource(),
                this.parameters(),
                null,
                NullPointerException.class);
    }

    default Optional<N> delete(final I id,
                               final Optional<N> resource,
                               final Map<HttpRequestAttribute<?>, Object> parameters,
                               final HateosHandlerContext<N> context) {
        return this.createHandler().delete(id, resource, parameters, context);
    }

    default void deleteAndCheck(final I id,
                                final Optional<N> resource,
                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                final HateosHandlerContext<N> context,
                                final Optional<N> result) {
        this.deleteAndCheck(this.createHandler(), id, resource, parameters, context, result);
    }

    default void deleteAndCheck(final HateosDeleteHandler<I, N> handler,
                                final I id,
                                final Optional<N> resource,
                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                final HateosHandlerContext<N> context,
                                final Optional<N> result) {
        assertEquals(result,
                handler.delete(id, resource, parameters, context),
                () -> handler + " id=" + id + ", resource: " + resource + ", context: " + context);
    }

    default <T extends Throwable> T deleteFails(final I id,
                                                final Optional<N> resource,
                                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                                final HateosHandlerContext<N> context,
                                                final Class<T> thrown) {
        return this.deleteFails(this.createHandler(),
                id,
                resource,
                parameters,
                context,
                thrown);
    }

    default <T extends Throwable> T deleteFails(final HateosDeleteHandler<I, N> handler,
                                                final I id,
                                                final Optional<N> resource,
                                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                                final HateosHandlerContext<N> context,
                                                final Class<T> thrown) {
        return assertThrows(thrown, () -> {
            handler.delete(id, resource, parameters, context);
        });
    }

    @Test
    default void testDeleteCollectionNullIdRangeFails() {
        this.deleteCollectionFails(null,
                this.resource(),
                this.parameters(),
                this.createContext(),
                NullPointerException.class);
    }

    @Test
    default void testDeleteCollectionNullParametersFails() {
        this.deleteCollectionFails(this.collection(),
                this.resource(),
                null,
                this.createContext(),
                NullPointerException.class);
    }

    @Test
    default void testDeleteCollectionNullContextFails() {
        this.deleteCollectionFails(this.collection(),
                this.resource(),
                this.parameters(),
                null,
                NullPointerException.class);
    }

    default void deleteCollection(final Range<I> collection,
                                  final Optional<N> resource,
                                  final Map<HttpRequestAttribute<?>, Object> parameters,
                                  final HateosHandlerContext<N> context) {
        this.createHandler().deleteCollection(collection,
                resource,
                parameters,
                context);
    }

    default void deleteCollectionAndCheck(final Range<I> ids,
                                          final Optional<N> resource,
                                          final Map<HttpRequestAttribute<?>, Object> parameters,
                                          final HateosHandlerContext<N> context,
                                          final Optional<N> result) {
        this.deleteCollectionAndCheck(this.createHandler(), ids, resource, parameters, context, result);
    }

    default void deleteCollectionAndCheck(final HateosDeleteHandler<I, N> handler,
                                          final Range<I> ids,
                                          final Optional<N> resource,
                                          final Map<HttpRequestAttribute<?>, Object> parameters,
                                          final HateosHandlerContext<N> context,
                                          final Optional<N> result) {
        assertEquals(result,
                handler.deleteCollection(ids, resource, parameters, context),
                () -> handler + " ids=" + ids + ", resource: " + resource + ", context: " + context);
    }

    default <T extends Throwable> T deleteCollectionFails(final Range<I> ids,
                                                          final Optional<N> resource,
                                                          final Map<HttpRequestAttribute<?>, Object> parameters,
                                                          final HateosHandlerContext<N> context,
                                                          final Class<T> thrown) {
        return this.deleteCollectionFails(this.createHandler(),
                ids,
                resource,
                parameters,
                context,
                thrown);
    }

    default <T extends Throwable> T deleteCollectionFails(final HateosDeleteHandler<I, N> handler,
                                                          final Range<I> ids,
                                                          final Optional<N> resource,
                                                          final Map<HttpRequestAttribute<?>, Object> parameters,
                                                          final HateosHandlerContext<N> context,
                                                          final Class<T> thrown) {
        return assertThrows(thrown, () -> {
            handler.deleteCollection(ids, resource, parameters, context);
        });
    }

    I id();

    Optional<N> resource();

    Range<I> collection();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNameSuffix() {
        return "DeleteHandler";
    }
}
