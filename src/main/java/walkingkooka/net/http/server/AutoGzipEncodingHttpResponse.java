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
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.TokenHeaderValue;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpStatus;

import java.io.IOException;
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

        //byte[] possiblyCompressed = body;
        if (this.isAcceptEncodingGzipSupported()) {
            final Optional<TokenHeaderValue> contentEncoding = HttpHeaderName.CONTENT_ENCODING
                    .headerValue(add.headers());
            if (contentEncoding.isPresent()) {
                if (this.isGzipSupported(contentEncoding.get())) {
                    add = add.setBody(gzip(add.body()));
                }
            } else {
                add = add.addHeader(HttpHeaderName.CONTENT_ENCODING, GZIP);
                add = add.setBody(gzip(add.body()));
            }

        }
        this.response.addEntity(add);
    }

    private boolean isAcceptEncodingGzipSupported() {
        return HttpHeaderName.ACCEPT_ENCODING
                .headerValueOrFail(this.request.headers())
                .stream()
                .anyMatch(this::isGzipSupported);
    }

    private boolean isGzipSupported(final TokenHeaderValue token) {
        return token.isWildcard() || GZIP.equals(token.setParameters(TokenHeaderValue.NO_PARAMETERS));
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

    private final static TokenHeaderValue GZIP = TokenHeaderValue.with("gzip");
}
