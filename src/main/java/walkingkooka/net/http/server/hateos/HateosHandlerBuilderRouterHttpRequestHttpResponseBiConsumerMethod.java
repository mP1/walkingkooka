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

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.compare.Range;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.header.AcceptCharset;
import walkingkooka.net.header.CharsetName;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.header.MediaTypeParameterName;
import walkingkooka.net.header.NotAcceptableHeaderException;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.net.http.server.HttpRequestAttributes;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.net.http.server.HttpServerException;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.Node;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;

/**
 * Router which accepts a request and then dispatches after testing the {@link HttpMethod}. This is the product of
 * {@link HateosHandlerBuilder}.
 */
abstract class HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerMethod<N extends Node<N, ?, ?, ?>, V> {

    /**
     * Package private ctor use factory.
     */
    HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerMethod(final HateosHandlerBuilderRouter<N, V> router,
                                                                      final HttpRequest request,
                                                                      final HttpResponse response) {
        super();
        this.router = router;
        this.request = request;
        this.parameters = request.routingParameters();
        this.response = response;
    }

    final void execute() {
        Loop: //
        do {
            // verify correctly dispatched...
            int pathIndex = this.router.consumeBasePath(this.parameters);
            if (-1 == pathIndex) {
                badRequest("Bad routing.");
                break;
            }

            // extract resource name......................................................................................
            final String resourceNameString = this.pathComponentOrNull(pathIndex);
            if (null == resourceNameString) {
                this.badRequest("Resource name missing.");
                break;
            }

            HateosResourceName resourceName;
            try {
                resourceName = HateosResourceName.with(resourceNameString);
            } catch (final IllegalArgumentException invalid) {
                this.badRequest("Invalid resource name " + CharSequences.quoteAndEscape(resourceNameString));
                break;
            }

            // id or range ..............................................................................................
            final String idOrRange = this.pathComponentOrNull(pathIndex + 1);
            if (CharSequences.isNullOrEmpty(idOrRange)) {
                this.idMissing(resourceName, pathIndex);
                break;
            }
            if ("*".equals(idOrRange)) {
                this.wildcard(resourceName, pathIndex);
                break;
            }

            boolean escaped = false;
            final StringBuilder component = new StringBuilder();
            String begin = null;

            for(char c : idOrRange.toCharArray()) {
                if(escaped) {
                    escaped = false;
                    component.append(c);
                    continue;
                }
                if(SEPARATOR ==c) {
                    if(null==begin) {
                        begin = component.toString();
                        component.setLength(0);
                        continue;
                    }
                    // second dash found error!!!
                    this.badRequest("Invalid character within range "+ CharSequences.quoteAndEscape(component));
                    break Loop;
                }
                if('\\' == c) {
                    escaped = true;
                    break;
                }
                component.append(c);
            }

            if(null==begin) {
                this.id(resourceName, component.toString(), pathIndex);
                break;
            }
            this.collection(resourceName,
                    begin,
                    component.toString(),
                    idOrRange,
                    pathIndex);
        } while (false);
    }

    /**
     * A dash is used to separate ids.
     */
    final static char SEPARATOR = '-';

    // ID.............................................................................................................

    private void idMissing(final HateosResourceName resourceName,
                           final int pathIndex) {
        final LinkRelation<?> linkRelation = this.linkRelationOrDefaultOrResponseBadRequest(pathIndex + 2);
        if (null != linkRelation) {
            this.idMissing(resourceName, linkRelation);
        }
    }

    abstract void idMissing(final HateosResourceName resourceName,
                            final LinkRelation<?> linkRelation);

    // WILDCARD.............................................................................................................

    private void wildcard(final HateosResourceName resourceName,
                          final int pathIndex) {
        final LinkRelation<?> linkRelation = this.linkRelationOrDefaultOrResponseBadRequest(pathIndex + 2);
        if (null != linkRelation) {
            this.wildcard(resourceName, linkRelation);
        }
    }

    abstract void wildcard(final HateosResourceName resourceName,
                           final LinkRelation<?> linkRelation);

    // ID.............................................................................................................

    private void id(final HateosResourceName resourceName,
                    final String id,
                    final int pathIndex) {
        final LinkRelation<?> linkRelation = this.linkRelationOrDefaultOrResponseBadRequest(pathIndex + 2);
        if (null != linkRelation) {
            this.id(resourceName, id, linkRelation);
        }
    }

    abstract void id(final HateosResourceName resourceName,
                     final String id,
                     final LinkRelation<?> linkRelation);

    // COLLECTION.....................................................................................................

    private void collection(final HateosResourceName resourceName,
                            final String begin,
                            final String end,
                            final String rangeText,
                            final int pathIndex) {
        final LinkRelation<?> linkRelation = this.linkRelationOrDefaultOrResponseBadRequest(pathIndex + 2);
        if (null != linkRelation) {
            this.collection(resourceName, begin, end, rangeText, linkRelation);
        }
    }

    abstract void collection(final HateosResourceName resourceName,
                             final String begin,
                             final String end,
                             final String rangeText,
                             final LinkRelation<?> linkRelation);

    // HELPERS ......................................................................................................

    /**
     * Parses or converts the id text, reporting a bad request if this fails.
     */
    final Comparable<?> idOrBadRequest(final String id,
                                       final HateosHandlerBuilderRouterHandlers<N> handlers) {
        return this.idOrBadRequest(id, handlers, "id", id);
    }

    /**
     * Parses or converts the id text, reporting a bad request if this fails.
     */
    private Comparable<?> idOrBadRequest(final String id,
                                         final HateosHandlerBuilderRouterHandlers<N> handlers,
                                         final String label, // id, range begin, range end
                                         final String text) {
        Comparable<?> parsed = null;
        try {
            parsed = handlers.id.apply(id);
        } catch (final RuntimeException failed) {
            this.badRequest("Invalid " + label + " " + CharSequences.quote(text));
        }
        return parsed;
    }

    /**
     * If not empty parse the relation otherwise return a default of {@link LinkRelation#SELF}, a null indicates an invalid relation.
     */
    private LinkRelation<?> linkRelationOrDefaultOrResponseBadRequest(final int pathIndex) {
        LinkRelation<?> relation = LinkRelation.SELF;

        final Optional<UrlPathName> relationPath = HttpRequestAttributes.pathComponent(pathIndex)
                .parameterValue(this.parameters);

        if (relationPath.isPresent()) {
            final String relationString = relationPath.get().value();
            if (!CharSequences.isNullOrEmpty(relationString)) {
                try {
                    relation = LinkRelation.with(relationString);
                } catch (final IllegalArgumentException invalid) {
                    relation = null;
                    badRequest("Invalid link relation " + CharSequences.quoteAndEscape(relationString));
                }
            }
        }

        return relation;
    }

    final Range<Comparable<?>> rangeOrBadRequest(final String begin,
                                                 final String end,
                                                 final HateosHandlerBuilderRouterHandlers<N> handlers,
                                                 final String rangeText) {
        Range<Comparable<?>> range = null;

        final Comparable beginComparable = this.idOrBadRequest(begin, handlers, "range begin", rangeText);
        if(null!=beginComparable) {
            final Comparable endComparable = this.idOrBadRequest(end, handlers, "range end", rangeText);
            if (null != endComparable) {
                range = Cast.to(Range.greaterThanEquals(beginComparable)
                        .and(Range.lessThanEquals(endComparable)));
            }
        }

        return range;
    }

    /**
     * Reads and parses the request body into a {@link Node}
     */
    final N resource() {
        Charset charset = Charset.defaultCharset();

        final Optional<MediaType> contentType = HttpHeaderName.CONTENT_TYPE.headerValue(this.request.headers());
        if (contentType.isPresent()) {
            final Optional<CharsetName> possible = MediaTypeParameterName.CHARSET.parameterValue(contentType.get());
            if (possible.isPresent()) {
                charset = possible.get().charset().orElse(charset);
            }
        }

        try (final StringReader reader = new StringReader(new String(request.body(), charset))) {
            final StringBuilder text = new StringBuilder();
            final char[] buffer = new char[4096];

            for (; ; ) {
                final int fill = reader.read(buffer);
                if (-1 == fill) {
                    break;
                }
                text.append(buffer, 0, fill);
            }

            // TODO need a DocumentBuilder factory to support XML
            return this.router.contentType.parse(null, text.toString());
        } catch (final IOException cause) {
            throw new HttpServerException(cause.getMessage(), cause);
        }
    }

    /**
     * Locates the {@link HateosHandlerBuilderRouterHandlers} or writes {@link HttpStatusCode#NOT_FOUND} or {@link HttpStatusCode#METHOD_NOT_ALLOWED}
     */
    final HateosHandlerBuilderRouterHandlers<N> handlersOrResponseNotFound(final HateosResourceName resourceName,
                                                                           final LinkRelation<?> linkRelation) {
        final HateosHandlerBuilderRouterHandlers<N> handlers = this.router.handlers.get(HateosHandlerBuilderRouterKey.with(resourceName, linkRelation));
        if (null == handlers) {
            notFound(resourceName, linkRelation);
        }
        return handlers;
    }

    final <H extends HateosHandler<?, N>> H handlerOrResponseMethodNotAllowed(final HateosResourceName resourceName,
                                                                              final LinkRelation<?> linkRelation,
                                                                              final H handler) {
        if (null == handler) {
            this.methodNotAllowed(resourceName, linkRelation);
        }
        return handler;
    }

    // error reporting.............................................................................................

    final void badRequestIdRequired() {
        this.badRequest("Id required");
    }

    final void badRequestCollectionsUnsupported() {
        this.badRequest("Collections not supported");
    }

    final void badRequest(final String message) {
        this.setStatus(HttpStatusCode.BAD_REQUEST, message);
    }

    final void methodNotAllowed(final HateosResourceName resourceName,
                                final LinkRelation<?> linkRelation) {
        methodNotAllowed(message(resourceName, linkRelation));
    }

    private void methodNotAllowed(final String message) {
        this.setStatus(HttpStatusCode.METHOD_NOT_ALLOWED, message);
    }

    private void notFound(final HateosResourceName resourceName,
                          final LinkRelation<?> linkRelation) {
        this.setStatus(HttpStatusCode.NOT_FOUND,
                this.message(resourceName, linkRelation));
    }

    private String message(final HateosResourceName resourceName,
                           final LinkRelation<?> linkRelation) {
        return "resource: " + resourceName + ", link relation: " + linkRelation;
    }

    /**
     * Sets the status to successful and body to the bytes of the encoded text of {@link Node}.
     */
    final void setStatusAndBody(final String message, final N node) {
        this.setStatus(HttpStatusCode.OK, message);

        final AcceptCharset acceptCharset = HttpHeaderName.ACCEPT_CHARSET.headerValueOrFail(this.request.headers());
        final Optional<Charset> charset = acceptCharset.charset();
        if (!charset.isPresent()) {
            throw new NotAcceptableHeaderException("AcceptCharset " + acceptCharset + " doesnt contain supported charset");
        }

        final HateosContentType<N, V> hateosContentType = this.router.contentType;
        final MediaType contentType = hateosContentType.contentType();

        final String content = hateosContentType.toText(node);
        final byte[] contentBytes = content.getBytes(charset.get());

        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_TYPE, contentType.setCharset(CharsetName.with(charset.get().name())));
        headers.put(HttpHeaderName.CONTENT_LENGTH, Long.valueOf(contentBytes.length));

        this.response.addEntity(HttpEntity.with(headers, contentBytes));
    }

    final void setStatus(final HttpStatusCode statusCode, final String message) {
        this.response.setStatus(statusCode.setMessage(message));
    }

    final HateosHandlerBuilderRouter<N, V> router;
    final HttpRequest request;

    /**
     * Fetches the path component at the path index or returns null.
     */
    private String pathComponentOrNull(final int pathIndex) {
        final Optional<UrlPathName> path = HttpRequestAttributes.pathComponent(pathIndex).parameterValue(this.parameters);
        return path.isPresent() ?
                path.get().value() :
                null;
    }

    final Map<HttpRequestAttribute<?>, Object> parameters;
    final HttpResponse response;

    @Override
    public final String toString() {
        return this.router.toString();
    }
}
