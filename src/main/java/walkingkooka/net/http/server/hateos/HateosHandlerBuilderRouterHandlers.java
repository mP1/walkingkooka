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

import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.tree.Node;

import java.util.function.Function;

/**
 * Holds all the handlers for a {@link HateosHandlerBuilderRouterKey}
 */
final class HateosHandlerBuilderRouterHandlers<N extends Node<N, ?, ?, ?>> {

    /**
     * Created by the builder with one for each resource name.
     */
    static <N extends Node<N, ?, ?, ?>> HateosHandlerBuilderRouterHandlers<N> with() {
        return new HateosHandlerBuilderRouterHandlers<>();
    }

    private HateosHandlerBuilderRouterHandlers() {
        super();
    }

    /**
     * Makes a defensive copy of this handlers. This is necessary when the router is created and the map copied from the builder.
     */
    HateosHandlerBuilderRouterHandlers<N> copy() {
        final HateosHandlerBuilderRouterHandlers<N> handlers = new HateosHandlerBuilderRouterHandlers<>();

        handlers.id = this.id;

        handlers.get = this.get;
        handlers.post = this.post;
        handlers.put = this.put;
        handlers.delete = this.delete;

        return handlers;
    }

    /**
     * A parser function that converts a {@link String} from the url path and returns the {@link Comparable id}.
     */
    Function<String, Comparable<?>> id;

    /**
     * These handlers will be null to indicate the method is not supported, otherwise the handler is invoked.
     */
    HateosGetHandler<?, N> get;
    HateosPostHandler<?, N> post;
    HateosPutHandler<?, N> put;
    HateosDeleteHandler<?, N> delete;

    @Override
    public String toString() {
        final ToStringBuilder b = ToStringBuilder.empty();

        b.label(HttpMethod.GET.toString()).value(this.get);
        b.label(HttpMethod.POST.toString()).value(this.post);
        b.label(HttpMethod.PUT.toString()).value(this.put);
        b.label(HttpMethod.DELETE.toString()).value(this.delete);

        return b.build();
    }
}
