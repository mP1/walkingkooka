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
        this.getFails(null,
                this.parameters(),
                this.createContext(),
                NullPointerException.class);
    }

    @Test
    default void testGetNullParametersFails() {
        this.getFails(this.id(),
                null,
                this.createContext(),
                NullPointerException.class);
    }

    @Test
    default void testGetNullContextFails() {
        this.getFails(this.id(),
                this.parameters(),
                null,
                NullPointerException.class);
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

    default <T extends Throwable> T getFails(final I id,
                                             final Map<HttpRequestParameterName, List<String>> parameters,
                                             final HateosHandlerContext<N> context,
                                             final Class<T> thrown) {
        return this.getFails(id, parameters, context, thrown);
    }

    default <T extends Throwable> T getFails(final HateosGetHandler<I, N> handler,
                                             final I id,
                                             final Map<HttpRequestParameterName, List<String>> parameters,
                                             final HateosHandlerContext<N> context,
                                             final Class<T> thrown) {
        return assertThrows(thrown, () -> {
            this.get(handler, id, parameters, context);
        });
    }

    @Test
    default void testGetCollectionNullCollectionFails() {
       this.getCollectionFails(null,
                    this.parameters(),
                    this.createContext(),
                    NullPointerException.class);
    }

    @Test
    default void testGetCollectionNullParametCollectionersFails() {
        this.getCollectionFails(this.collection(),
                    null,
                    this.createContext(),
                    NullPointerException.class);
    }

    @Test
    default void testGetCollectionNullContextFails() {
        this.getCollectionFails(this.collection(),
                    this.parameters(),
                    null,
                    NullPointerException.class);
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

    default <T extends Throwable> T getCollectionFails(final Range<I> ids,
                                                       final Map<HttpRequestParameterName, List<String>> parameters,
                                                       final HateosHandlerContext<N> context,
                                                       final Class<T> thrown) {
        return this.getCollectionFails(ids, parameters, context, thrown);
    }

    default <T extends Throwable> T getCollectionFails(final HateosGetHandler<I, N> handler,
                                                       final Range<I> ids,
                                                       final Map<HttpRequestParameterName, List<String>> parameters,
                                                       final HateosHandlerContext<N> context,
                                                       final Class<T> thrown) {
        return assertThrows(thrown, () -> {
            this.getCollection(handler, ids, parameters, context);
        });
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
