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
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.TypeNameTesting;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin interface for testing {@link HateosHandler}
 */
public interface HateosMappingHandlerTesting<H extends HateosHandler<I, R>, I extends Comparable<I>, R extends HateosResource<?>>
        extends ClassTesting2<H>,
        TypeNameTesting<H> {

    @Test
    default void testHandleNullIdFails() {
        this.handleFails(null,
                this.resource(),
                this.parameters(),
                NullPointerException.class);
    }

    @Test
    default void testHandleNullResourceFails() {
        this.handleFails(this.id(),
                null,
                this.parameters(),
                NullPointerException.class);
    }

    @Test
    default void testHandleNullParametersFails() {
        this.handleFails(this.id(),
                this.resource(),
                null,
                NullPointerException.class);
    }

    default Optional<R> handle(final I id,
                               final Optional<R> resource,
                               final Map<HttpRequestAttribute<?>, Object> parameters) {
        return this.createHandler().handle(id, resource, parameters);
    }

    default void handleAndCheck(final I id,
                                final Optional<R> resource,
                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                final int result) {
        this.handleAndCheck(this.createHandler(), id, resource, parameters, result);
    }

    default void handleAndCheck(final HateosHandler<I, R> handler,
                                final I id,
                                final Optional<R> resource,
                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                final int result) {
        assertEquals(result,
                handler.handle(id, resource, parameters),
                () -> handler + " id=" + id + ", resource: " + resource);
    }

    default <T extends Throwable> T handleFails(final I id,
                                                final Optional<R> resource,
                                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                                final Class<T> thrown) {
        return this.handleFails(this.createHandler(),
                id,
                resource,
                parameters,
                thrown);
    }

    default <T extends Throwable> T handleFails(final HateosHandler<I, R> handler,
                                                final I id,
                                                final Optional<R> resource,
                                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                                final Class<T> thrown) {
        return assertThrows(thrown, () -> {
            handler.handle(id, resource, parameters);
        });
    }

    @Test
    default void testHandleCollectionNullIdRangeFails() {
        this.handleCollectionFails(null,
                this.resourceCollection(),
                this.parameters(),
                NullPointerException.class);
    }

    @Test
    default void testHandleCollectionNullParametersFails() {
        this.handleCollectionFails(this.collection(),
                this.resourceCollection(),
                null,
                NullPointerException.class);
    }

    default void handleCollection(final Range<I> collection,
                                  final List<R> resources,
                                  final Map<HttpRequestAttribute<?>, Object> parameters) {
        this.createHandler().handleCollection(collection,
                resources,
                parameters);
    }

    default void handleCollectionAndCheck(final Range<I> ids,
                                          final List<R> resources,
                                          final Map<HttpRequestAttribute<?>, Object> parameters,
                                          final List<R> result) {
        this.handleCollectionAndCheck(this.createHandler(), ids, resources, parameters, result);
    }

    default void handleCollectionAndCheck(final HateosHandler<I, R> handler,
                                          final Range<I> ids,
                                          final List<R> resources,
                                          final Map<HttpRequestAttribute<?>, Object> parameters,
                                          final List<R> result) {
        assertEquals(result,
                handler.handleCollection(ids, resources, parameters),
                () -> handler + " ids=" + ids + ", resources: " + resources);
    }

    default <T extends Throwable> T handleCollectionFails(final Range<I> ids,
                                                          final List<R> resources,
                                                          final Map<HttpRequestAttribute<?>, Object> parameters,
                                                          final Class<T> thrown) {
        return this.handleCollectionFails(this.createHandler(),
                ids,
                resources,
                parameters,
                thrown);
    }

    default <T extends Throwable> T handleCollectionFails(final HateosHandler<I, R> handler,
                                                          final Range<I> ids,
                                                          final List<R> resources,
                                                          final Map<HttpRequestAttribute<?>, Object> parameters,
                                                          final Class<T> thrown) {
        return assertThrows(thrown, () -> {
            handler.handleCollection(ids, resources, parameters);
        });
    }

    H createHandler();

    Map<HttpRequestAttribute<?>, Object> parameters();

    I id();

    Optional<R> resource();

    Range<I> collection();

    List<R> resourceCollection();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNameSuffix() {
        return HateosHandler.class.getSimpleName();
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }
}
