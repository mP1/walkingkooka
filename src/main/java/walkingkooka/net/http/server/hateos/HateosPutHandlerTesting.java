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
import walkingkooka.tree.Node;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin interface for testing {@link HateosPutHandler}
 */
public interface HateosPutHandlerTesting<H extends HateosPutHandler<I, N>, I extends Comparable<I>, N extends Node<N, ?, ?, ?>> extends HateosHandlerTesting<H, I, N> {

    @Test
    default void testPutNullIdFails() {
        assertThrows(NullPointerException.class, () -> {
            this.put(null,
                    this.resource(),
                    this.createContext());
        });
    }

    @Test
    default void testPutNullResourceFails() {
        assertThrows(NullPointerException.class, () -> {
            this.put(this.id(),
                    null,
                    this.createContext());
        });
    }

    @Test
    default void testPutNullContextFails() {
        assertThrows(NullPointerException.class, () -> {
            this.put(this.id(),
                    this.resource(),
                    null);
        });
    }

    default N put(final I id,
                  final N resource,
                  final HateosHandlerContext<N> context) {
        return this.put(this.createHandler(), id, resource, context);
    }

    default N put(final HateosPutHandler<I, N> handler,
                  final I id,
                  final N resource,
                  final HateosHandlerContext<N> context) {
        return handler.put(id, resource, context);
    }

    default void putAndCheck(final I id,
                             final N resource,
                             final HateosHandlerContext<N> context,
                             final N result) {
        this.putAndCheck(this.createHandler(), id, resource, context, result);
    }

    default void putAndCheck(final HateosPutHandler<I, N> handler,
                             final I id,
                             final N resource,
                             final HateosHandlerContext<N> context,
                             final N result) {
        assertEquals(result,
                this.put(handler, id, resource, context),
                () -> handler + " id=" + id + ", resource: " + resource + ", context: " + context);
    }

    I id();

    N resource();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNameSuffix() {
        return "PutHandler";
    }
}
