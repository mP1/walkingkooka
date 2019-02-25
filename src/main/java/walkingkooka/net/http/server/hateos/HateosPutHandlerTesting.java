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
import walkingkooka.tree.Node;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin interface for testing {@link HateosPutHandler}
 */
public interface HateosPutHandlerTesting<H extends HateosPutHandler<I, N>, I extends Comparable<I>, N extends Node<N, ?, ?, ?>> extends HateosHandlerTesting<H, I, N> {

    @Test
    default void testPutNullIdFails() {
        this.putFails(null,
                this.resource(),
                this.parameters(),
                this.createContext(),
                NullPointerException.class);
    }

    @Test
    default void testPutNullResourceFails() {
        this.putFails(this.id(),
                null,
                this.parameters(),
                this.createContext(),
                NullPointerException.class);
    }

    @Test
    default void testPutNullParametersFails() {
        this.putFails(this.id(),
                this.resource(),
                null,
                this.createContext(),
                NullPointerException.class);
    }

    @Test
    default void testPutNullContextFails() {
        this.putFails(this.id(),
                this.resource(),
                this.parameters(),
                null,
                NullPointerException.class);
    }

    default Optional<N> put(final I id,
                            final Optional<N> resource,
                            final Map<HttpRequestAttribute<?>, Object> parameters,
                            final HateosHandlerContext<N> context) {
        return this.put(this.createHandler(), id, resource, parameters, context);
    }

    default Optional<N> put(final HateosPutHandler<I, N> handler,
                            final I id,
                            final Optional<N> resource,
                            final Map<HttpRequestAttribute<?>, Object> parameters,
                            final HateosHandlerContext<N> context) {
        return handler.put(id, resource, parameters, context);
    }

    default void putAndCheck(final I id,
                             final Optional<N> resource,
                             final Map<HttpRequestAttribute<?>, Object> parameters,
                             final HateosHandlerContext<N> context,
                             final Optional<N> result) {
        this.putAndCheck(this.createHandler(), id, resource, parameters, context, result);
    }

    default void putAndCheck(final HateosPutHandler<I, N> handler,
                             final I id,
                             final Optional<N> resource,
                             final Map<HttpRequestAttribute<?>, Object> parameters,
                             final HateosHandlerContext<N> context,
                             final Optional<N> result) {
        assertEquals(result,
                this.put(handler, id, resource, parameters, context),
                () -> handler + " id=" + id + ", resource: " + resource + ", context: " + context);
    }

    default <T extends Throwable> T putFails(final I id,
                                             final Optional<N> resource,
                                             final Map<HttpRequestAttribute<?>, Object> parameters,
                                             final HateosHandlerContext<N> context,
                                             final Class<T> thrown) {
        return this.putFails(this.createHandler(),
                id,
                resource,
                parameters,
                context,
                thrown);
    }

    default <T extends Throwable> T putFails(final HateosPutHandler<I, N> handler,
                                             final I id,
                                             final Optional<N> resource,
                                             final Map<HttpRequestAttribute<?>, Object> parameters,
                                             final HateosHandlerContext<N> context,
                                             final Class<T> thrown) {
        return assertThrows(thrown, () -> {
            this.put(handler, id, resource, parameters, context);
        });
    }

    I id();

    Optional<N> resource();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNameSuffix() {
        return "PutHandler";
    }
}
