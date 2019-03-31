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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin interface for testing {@link HateosIdResourceCollectionResourceCollectionHandler}
 */
public interface HateosIdResourceCollectionResourceCollectionHandlerTesting<H extends HateosIdResourceCollectionResourceCollectionHandler<I, R, S>,
        I extends Comparable<I>,
        R extends HateosResource<?>,
        S extends HateosResource<?>>
        extends HateosHandlerTesting<H, I, R, S> {

    @Test
    default void testHandleCollectionNullIdRangeFails() {
        this.handleFails(null,
                this.resourceCollection(),
                this.parameters(),
                NullPointerException.class);
    }

    @Test
    default void testHandleCollectionNullParametersFails() {
        this.handleFails(this.id(),
                this.resourceCollection(),
                null,
                NullPointerException.class);
    }

    default void handle(final I collection,
                        final List<R> resources,
                        final Map<HttpRequestAttribute<?>, Object> parameters) {
        this.createHandler().handle(collection,
                resources,
                parameters);
    }

    default void handleAndCheck(final I id,
                                final List<R> resources,
                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                final List<S> result) {
        this.handleAndCheck(this.createHandler(), id, resources, parameters, result);
    }

    default void handleAndCheck(final HateosIdResourceCollectionResourceCollectionHandler<I, R, S> handler,
                                final I id,
                                final List<R> resources,
                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                final List<S> result) {
        assertEquals(result,
                handler.handle(id, resources, parameters),
                () -> handler + " id=" + id + ", resources: " + resources);
    }

    default <T extends Throwable> T handleFails(final I id,
                                                final List<R> resources,
                                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                                final Class<T> thrown) {
        return this.handleFails(this.createHandler(),
                id,
                resources,
                parameters,
                thrown);
    }

    default <T extends Throwable> T handleFails(final HateosIdResourceCollectionResourceCollectionHandler<I, R, S> handler,
                                                final I id,
                                                final List<R> resources,
                                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                                final Class<T> thrown) {
        return assertThrows(thrown, () -> {
            handler.handle(id, resources, parameters);
        });
    }

    List<R> resourceCollection();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNameSuffix() {
        return HateosIdResourceCollectionResourceCollectionHandler.class.getSimpleName();
    }
}
