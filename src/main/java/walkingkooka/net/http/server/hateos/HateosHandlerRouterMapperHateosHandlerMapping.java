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

/**
 * Bridge that eventually calls a {@link HateosHandler}.
 */
abstract class HateosHandlerRouterMapperHateosHandlerMapping<H extends HateosHandler<I, R, S>,
        I extends Comparable<I>,
        R extends HateosResource<?>,
        S extends HateosResource<?>> {

    /**
     * {@see HateosHandlerRouterMapperHateosIdResourceResourceHandlerMapping}
     */
    static <I extends Comparable<I>,
            R extends HateosResource<?>,
            S extends HateosResource<?>>
    HateosHandlerRouterMapperHateosIdHandlerMapping<HateosIdResourceResourceHandler<I, R, S>, I, R, S> idResourceResource(final HateosIdResourceResourceHandler<I, R, S> handler) {
        return HateosHandlerRouterMapperHateosIdResourceResourceHandlerMapping.with(handler);
    }

    static <I extends Comparable<I>,
            R extends HateosResource<?>,
            S extends HateosResource<?>>
    HateosHandlerRouterMapperHateosIdRangeResourceCollectionResourceCollectionHandlerMapping<I, R, S> idRangeResourceCollectionResourceCollection(final HateosIdRangeResourceCollectionResourceCollectionHandler<I, R, S> handler) {
        return HateosHandlerRouterMapperHateosIdRangeResourceCollectionResourceCollectionHandlerMapping.with(handler);
    }

    /**
     * Package private to limit sub classing.
     */
    HateosHandlerRouterMapperHateosHandlerMapping(final H handler) {
        super();
        this.handler = handler;
    }

    final H handler;

    public final String toString() {
        return this.handler.toString();
    }
}
