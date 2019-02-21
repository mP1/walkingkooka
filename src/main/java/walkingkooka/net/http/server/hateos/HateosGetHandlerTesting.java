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
import walkingkooka.net.http.server.HttpRequestParameterName;
import walkingkooka.tree.Node;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin interface for testing {@link HateosGetHandler}
 */
public interface HateosGetHandlerTesting<H extends HateosGetHandler<I, N>, I extends Comparable<I>, N extends Node<N, ?, ?, ?>> extends HateosHandlerTesting<H, I, N> {

    @Test
    default void testGetNullIdFails() {
        assertThrows(NullPointerException.class, () -> {
            this.get(null,
                    this.parameters(),
                    this.createContext());
        });
    }

    @Test
    default void testGetNullParametersFails() {
        assertThrows(NullPointerException.class, () -> {
            this.get(this.id(),
                    null,
                    this.createContext());
        });
    }

    @Test
    default void testGetNullContextFails() {
        assertThrows(NullPointerException.class, () -> {
            this.get(this.id(),
                    this.parameters(),
                    null);
        });
    }

    default Optional<N> get(final I id,
                            final Map<HttpRequestParameterName, List<String>> parameters,
                            final HateosHandlerContext<N> context) {
        return this.get(this.createHandler(), id, parameters, context);
    }

    default Optional<N> get(final HateosGetHandler<I, N> handler,
                            final I id,
                            final Map<HttpRequestParameterName, List<String>> parameters,
                            final HateosHandlerContext<N> context) {
        return handler.get(id, parameters, context);
    }

    default void getAndCheck(final HateosGetHandler<I, N> handler,
                             final I id,
                             final Map<HttpRequestParameterName, List<String>> parameters,
                             final HateosHandlerContext<N> context) {
        this.getAndCheck(handler, id, parameters, context, Optional.empty());
    }

    default void getAndCheck(final HateosGetHandler<I, N> handler,
                             final I id,
                             final Map<HttpRequestParameterName, List<String>> parameters,
                             final HateosHandlerContext<N> context,
                             final N result) {
        this.getAndCheck(handler, id, parameters, context, Optional.of(result));
    }

    default void getAndCheck(final HateosGetHandler<I, N> handler,
                             final I id,
                             final Map<HttpRequestParameterName, List<String>> parameters,
                             final HateosHandlerContext<N> context,
                             final Optional<N> result) {
        assertEquals(result,
                this.get(handler, id, parameters, context),
                () -> handler + " id=" + id + ", parameters: " + parameters + ", context: " + context);
    }

    @Test
    default void testGetCollectionNullCollectionFails() {
        assertThrows(NullPointerException.class, () -> {
            this.getCollection(null,
                    this.parameters(),
                    this.createContext());
        });
    }

    @Test
    default void testGetCollectionNullParametCollectionersFails() {
        assertThrows(NullPointerException.class, () -> {
            this.getCollection(this.collection(),
                    null,
                    this.createContext());
        });
    }

    @Test
    default void testGetCollectionNullContextFails() {
        assertThrows(NullPointerException.class, () -> {
            this.getCollection(this.collection(),
                    this.parameters(),
                    null);
        });
    }

    default Optional<N> getCollection(final Range<I> ids,
                                      final Map<HttpRequestParameterName, List<String>> parameters,
                                      final HateosHandlerContext<N> context) {
        return this.getCollection(this.createHandler(), ids, parameters, context);
    }

    default Optional<N> getCollection(final HateosGetHandler<I, N> handler,
                                      final Range<I> ids,
                                      final Map<HttpRequestParameterName, List<String>> parameters,
                                      final HateosHandlerContext<N> context) {
        return handler.getCollection(ids, parameters, context);
    }

    default void getCollectionAndCheck(final HateosGetHandler<I, N> handler,
                                       final Range<I> ids,
                                       final Map<HttpRequestParameterName, List<String>> parameters,
                                       final HateosHandlerContext<N> context) {
        this.getCollectionAndCheck(handler, ids, parameters, context, Optional.empty());
    }

    default void getCollectionAndCheck(final HateosGetHandler<I, N> handler,
                                       final Range<I> ids,
                                       final Map<HttpRequestParameterName, List<String>> parameters,
                                       final HateosHandlerContext<N> context,
                                       final N result) {
        this.getCollectionAndCheck(handler, ids, parameters, context, Optional.of(result));
    }

    default void getCollectionAndCheck(final HateosGetHandler<I, N> handler,
                                       final Range<I> ids,
                                       final Map<HttpRequestParameterName, List<String>> parameters,
                                       final HateosHandlerContext<N> context,
                                       final Optional<N> result) {
        assertEquals(result,
                this.getCollection(handler, ids, parameters, context),
                () -> handler + " ids=" + ids + ", parameters: " + parameters + ", context: " + context);
    }

    I id();

    Range<I> collection();

    Map<HttpRequestParameterName, List<String>> parameters();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNameSuffix() {
        return "GetHandler";
    }
}
