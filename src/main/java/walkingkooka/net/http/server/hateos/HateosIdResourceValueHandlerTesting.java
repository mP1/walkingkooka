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

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin interface for testing {@link HateosIdResourceValueHandler}
 */
public interface HateosIdResourceValueHandlerTesting<H extends HateosIdResourceValueHandler<I, R, S>,
        I extends Comparable<I>,
        R extends HateosResource<?>,
        S extends HateosResource<?>>
        extends HateosHandlerTesting<H, I, R, S> {

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

    default Object handle(final I id,
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

    default void handleAndCheck(final HateosIdResourceValueHandler<I, R, S> handler,
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

    default <T extends Throwable> T handleFails(final HateosIdResourceValueHandler<I, R, S> handler,
                                                final I id,
                                                final Optional<R> resource,
                                                final Map<HttpRequestAttribute<?>, Object> parameters,
                                                final Class<T> thrown) {
        return assertThrows(thrown, () -> {
            handler.handle(id, resource, parameters);
        });
    }

    Optional<R> resource();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNameSuffix() {
        return HateosIdResourceValueHandler.class.getSimpleName();
    }
}
