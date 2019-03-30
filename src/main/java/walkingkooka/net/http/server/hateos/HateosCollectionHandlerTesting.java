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
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.TypeNameTesting;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin interface for testing {@link HateosCollectionHandler}
 */
public interface HateosCollectionHandlerTesting<H extends HateosCollectionHandler<I, R, S>,
        I extends Comparable<I>,
        R extends HateosResource<?>,
        S extends HateosResource<?>>
        extends ClassTesting2<H>,
        TypeNameTesting<H> {

    @Test
    default void testHandleCollectionCollectionNullIdRangeFails() {
        this.handleCollectionFails(null,
                this.resourceCollection(),
                this.parameters(),
                NullPointerException.class);
    }

    @Test
    default void testHandleCollectionCollectionNullParametersFails() {
        this.handleCollectionFails(this.id(),
                this.resourceCollection(),
                null,
                NullPointerException.class);
    }

    default void handleCollection(final I collection,
                                  final List<R> resources,
                                  final Map<HttpRequestAttribute<?>, Object> parameters) {
        this.createHandler().handle(collection,
                resources,
                parameters);
    }

    default void handleCollectionAndCheck(final I id,
                                          final List<R> resources,
                                          final Map<HttpRequestAttribute<?>, Object> parameters,
                                          final List<S> result) {
        this.handleCollectionAndCheck(this.createHandler(), id, resources, parameters, result);
    }

    default void handleCollectionAndCheck(final HateosCollectionHandler<I, R, S> handler,
                                          final I id,
                                          final List<R> resources,
                                          final Map<HttpRequestAttribute<?>, Object> parameters,
                                          final List<S> result) {
        assertEquals(result,
                handler.handle(id, resources, parameters),
                () -> handler + " id=" + id + ", resources: " + resources);
    }

    default <T extends Throwable> T handleCollectionFails(final I id,
                                                          final List<R> resources,
                                                          final Map<HttpRequestAttribute<?>, Object> parameters,
                                                          final Class<T> thrown) {
        return this.handleCollectionFails(this.createHandler(),
                id,
                resources,
                parameters,
                thrown);
    }

    default <T extends Throwable> T handleCollectionFails(final HateosCollectionHandler<I, R, S> handler,
                                                          final I id,
                                                          final List<R> resources,
                                                          final Map<HttpRequestAttribute<?>, Object> parameters,
                                                          final Class<T> thrown) {
        return assertThrows(thrown, () -> {
            handler.handle(id, resources, parameters);
        });
    }

    H createHandler();

    Map<HttpRequestAttribute<?>, Object> parameters();

    I id();

    List<R> resourceCollection();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNameSuffix() {
        return HateosCollectionHandler.class.getSimpleName();
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }
}
