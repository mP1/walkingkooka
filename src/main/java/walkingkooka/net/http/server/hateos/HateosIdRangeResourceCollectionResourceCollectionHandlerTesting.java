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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin interface for testing {@link HateosIdRangeResourceCollectionResourceCollectionHandler}
 */
public interface HateosIdRangeResourceCollectionResourceCollectionHandlerTesting<H extends HateosIdRangeResourceCollectionResourceCollectionHandler<I, R, S>,
        I extends Comparable<I>,
        R extends HateosResource<?>,
        S extends HateosResource<?>>
        extends HateosHandlerTesting<H, I, R, S> {

    @Test
    default void testHandleNullIdRangeFails() {
        this.handleFails(null,
                this.resourceCollection(),
                this.parameters(),
                NullPointerException.class);
    }

    @Test
    default void testHandleNullParametersFails() {
        this.handleFails(this.collection(),
                this.resourceCollection(),
                null,
                NullPointerException.class);
    }

    default void handle(final Range<I> collection,
                        final List<R> resources,
                        final Map<HttpRequestAttribute<?>, Object> parameters) {
        this.createHandler().handle(collection,
                resources,
                parameters);
    }

    default void handleAndCheck(final Range<I> ids,
                                final List<R> resources,
                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                final List<S> result) {
        this.handleAndCheck(this.createHandler(), ids, resources, parameters, result);
    }

    default void handleAndCheck(final HateosIdRangeResourceCollectionResourceCollectionHandler<I, R, S> handler,
                                final Range<I> ids,
                                final List<R> resources,
                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                final List<S> result) {
        assertEquals(result,
                handler.handle(ids, resources, parameters),
                () -> handler + " ids=" + ids + ", resources: " + resources);
    }

    default <T extends Throwable> T handleFails(final Range<I> ids,
                                                final List<R> resources,
                                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                                final Class<T> thrown) {
        return this.handleFails(this.createHandler(),
                ids,
                resources,
                parameters,
                thrown);
    }

    default <T extends Throwable> T handleFails(final HateosIdRangeResourceCollectionResourceCollectionHandler<I, R, S> handler,
                                                final Range<I> ids,
                                                final List<R> resources,
                                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                                final Class<T> thrown) {
        return assertThrows(thrown, () -> {
            handler.handle(ids, resources, parameters);
        });
    }

    Range<I> collection();

    List<R> resourceCollection();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNameSuffix() {
        return HateosIdRangeResourceCollectionResourceCollectionHandler.class.getSimpleName();
    }
}
