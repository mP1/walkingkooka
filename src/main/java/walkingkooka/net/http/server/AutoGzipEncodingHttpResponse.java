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

package walkingkooka.net.http.server;

import walkingkooka.Binary;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.header.AcceptEncoding;
import walkingkooka.net.header.ContentEncoding;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.util.Optionals;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Wraps a {@link HttpResponse} and if possible encoding any bytes with gzip compression.
 * Compression will only happen if the request accept-encoding includes gzip. It is also possible to disable
 * compression if the content-encoding is not set to gzip.
 */
final class AutoGzipEncodingHttpResponse extends WrapperHttpRequestHttpResponse {

    /**
     * Factory that creates a new {@link AutoGzipEncodingHttpResponse}
     */
    static AutoGzipEncodingHttpResponse with(final HttpRequest request,
                                             final HttpResponse response) {
        check(request, response);

        return new AutoGzipEncodingHttpResponse(request, response);
    }

    /**
     * Private ctor use factory
     */
    private AutoGzipEncodingHttpResponse(final HttpRequest request,
                                         final HttpResponse response) {
        super(request, response);
    }

    @Override
    public void setStatus(final HttpStatus status) {
        this.response.setStatus(status);
    }

    /**
     * If the content-encoding request header includes gzip then the bytes will be compressed.
     */
    @Override
    public void addEntity(final HttpEntity entity) {
        Objects.requireNonNull(entity, "entity");

        HttpEntity add = entity;

        final Optional<List<AcceptEncoding>> acceptEncodings = HttpHeaderName.ACCEPT_ENCODING.headerValue(this.request.headers());

        if(Optionals.stream(acceptEncodings)
                .flatMap(l -> l.stream())
                .filter(a -> a.test(ContentEncoding.GZIP))
                .limit(1)
                .count() == 1) {
            // found an accept-encoding that matches GZIP...now double check if already GZIPPED

            final Optional<List<ContentEncoding>> contentEncodings = HttpHeaderName.CONTENT_ENCODING.headerValue(add.headers());
            if(!contentEncodings.isPresent()) {
                // content-encoding absent so gzip
                add = add.addHeader(HttpHeaderName.CONTENT_ENCODING, GZIP);
                add = add.setBody(gzip(add.body()));
            }
        }

        this.response.addEntity(add);
    }

    /**
     * Returns the GZIP compressed form of the given byte array.
     */
    // @VisibleForTesting
    static Binary gzip(final Binary body) {
        try {
            return body.gzip();
        } catch (final IOException cause) {
            throw new HttpServerException("Failed to gzip compress bytes, " + cause, cause);
        }
    }

    private final static List<ContentEncoding> GZIP = Lists.of(ContentEncoding.GZIP);
}
