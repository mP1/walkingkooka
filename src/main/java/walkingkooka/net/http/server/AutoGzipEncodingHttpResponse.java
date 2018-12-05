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

import walkingkooka.Cast;
import walkingkooka.net.header.HeaderValueToken;
import walkingkooka.net.header.NotAcceptableHeaderValueException;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.GZIPOutputStream;

/**
 * Wraps a {@link HttpResponse} and if possible encoding any bytes with gzip compression.
 * Compression will only happen if the request accept-encoding includes gzip. It is also possible to disable
 * compression if the content-encoding is not set to gzip.
 */
final class AutoGzipEncodingHttpResponse extends WrapperHttpResponse {

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

    @Override
    public Map<HttpHeaderName<?>, Object> headers() {
        return this.response.headers();
    }

    @Override
    public <T> void addHeader(final HttpHeaderName<T> name, final T value) {
        if (HttpHeaderName.CONTENT_ENCODING.equals(name)) {
            final HeaderValueToken token = Cast.to(value);
            if (token.isWildcard()) {
                throw new NotAcceptableHeaderValueException(HttpHeaderName.CONTENT_ENCODING + "=" + value);
            }
        }
        this.response.addHeader(name, value);
    }

    /**
     * If the content-encoding request header includes gzip then the bytes will be compressed.
     */
    @Override
    public void setBody(final byte[] body) {
        Objects.requireNonNull(body, "body");

        byte[] possiblyCompressed = body;
        if (this.isAcceptEncodingGzipSupported()) {
            final Optional<HeaderValueToken> contentEncoding = HttpHeaderName.CONTENT_ENCODING
                    .headerValue(this);
            if (contentEncoding.isPresent()) {
                if (this.isGzipSupported(contentEncoding.get())) {
                    possiblyCompressed = this.gzip(body);
                }
            } else {
                this.addHeader(HttpHeaderName.CONTENT_ENCODING, GZIP);
                possiblyCompressed = this.gzip(body);
            }

        }
        this.response.setBody(possiblyCompressed);
    }

    private boolean isAcceptEncodingGzipSupported() {
        return HttpHeaderName.ACCEPT_ENCODING
                .headerValueOrFail(this.request)
                .stream()
                .anyMatch(this::isGzipSupported);
    }

    private boolean isGzipSupported(final HeaderValueToken token) {
        return token.isWildcard() || GZIP.equals(token.setParameters(HeaderValueToken.NO_PARAMETERS));
    }

    /**
     * Returns the GZIP compressed form of the given byte array.
     */
    // @VisibleForTesting
    static byte[] gzip(final byte[] body) {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            try (final GZIPOutputStream gzip = new GZIPOutputStream(outputStream)) {
                gzip.write(body);
                gzip.flush();
            }
            return outputStream.toByteArray();
        } catch (final IOException cause) {
            throw new HttpServerException("Failed to gzip compress bytes, " + cause, cause);
        }
    }

    @Override
    public void setBodyText(final String text) {
        this.response.setBodyText(text);
    }

    private final static HeaderValueToken GZIP = HeaderValueToken.with("gzip");
}
