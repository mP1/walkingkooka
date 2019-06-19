/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.net.http.server.hateos;

import walkingkooka.ToStringBuilder;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpMethodVisitor;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.tree.Node;

/**
 * Handles dispatching a request, defaulting to unsupported methods to a method not allowed response.
 */
final class HateosHandlerRouterHttpRequestHttpResponseBiConsumerHttpMethodVisitor<N extends Node<N, ?, ?, ?>,
        H extends HateosContentType<N>> extends HttpMethodVisitor {

    static <N extends Node<N, ?, ?, ?>,
            H extends HateosContentType<N>> void accept(final HttpRequest request,
                                                        final HttpResponse response,
                                                        final HateosHandlerRouter<N> router) {
        final HateosHandlerRouterHttpRequestHttpResponseBiConsumerHttpMethodVisitor<N, H> visitor = new HateosHandlerRouterHttpRequestHttpResponseBiConsumerHttpMethodVisitor(request,
                response,
                router);
        visitor.accept(request.method());
    }

    HateosHandlerRouterHttpRequestHttpResponseBiConsumerHttpMethodVisitor(final HttpRequest request,
                                                                          final HttpResponse response,
                                                                          final HateosHandlerRouter<N> router) {
        super();
        this.request = request;
        this.response = response;
        this.router = router;
    }

    @Override
    protected void visitHead() {
        this.methodNotAllowed(HttpMethod.HEAD);
    }

    @Override
    protected void visitGet() {
        this.execute();
    }

    @Override
    protected void visitPost() {
        this.execute();
    }

    @Override
    protected void visitPut() {
        this.execute();
    }

    @Override
    protected void visitDelete() {
        this.execute();
    }

    @Override
    protected void visitTrace() {
        this.methodNotAllowed(HttpMethod.TRACE);
    }

    @Override
    protected void visitOptions() {
        this.methodNotAllowed(HttpMethod.OPTIONS);
    }

    @Override
    protected void visitConnect() {
        this.methodNotAllowed(HttpMethod.CONNECT);
    }

    @Override
    protected void visitPatch() {
        this.methodNotAllowed(HttpMethod.PATCH);
    }

    @Override
    protected void visitUnknown(final HttpMethod method) {
        this.methodNotAllowed(method);
    }

    private void execute() {
        HateosHandlerRouterHttpRequestHttpResponseBiConsumerHttpMethodVisitorRequest.with(this.router,
                this.request,
                this.response)
                .execute();
    }

    private void methodNotAllowed(final HttpMethod method) {
        this.response.setStatus(HttpStatusCode.METHOD_NOT_ALLOWED.setMessage(method.value()));
    }

    private final HttpRequest request;
    private final HttpResponse response;
    private final HateosHandlerRouter<N> router;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.router)
                .value(this.request)
                .value(this.response)
                .build();
    }
}
