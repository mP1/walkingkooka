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

import walkingkooka.net.header.NotAcceptableHeaderException;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpStatus;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Wraps a {@link HttpResponse} sets if the content-length in {@link #setBody(byte[])} if necessary or verifies
 * if the existing content-length matches.
 */
final class AutoContentLengthHttpResponse extends WrapperHttpRequestHttpResponse {

    /**
     * Factory that creates a new {@link AutoContentLengthHttpResponse}
     */
    static AutoContentLengthHttpResponse with(final HttpRequest request,
                                              final HttpResponse response) {
        check(request, response);

        return new AutoContentLengthHttpResponse(request, response);
    }

    /**
     * Private ctor use factory
     */
    private AutoContentLengthHttpResponse(final HttpRequest request,
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
        this.response.addHeader(name, value);
    }

    /**
     * If the content-length request header is set verify the body length and complain if they are different.
     */
    @Override
    public void setBody(final byte[] body) {
        Objects.requireNonNull(body, "body");

        final long contentLength = body.length;
        final Optional<Long> maybeResponseContentLength = HttpHeaderName.CONTENT_LENGTH.headerValue(this.response.headers());
        if (maybeResponseContentLength.isPresent()) {
            final long responseContentLength = maybeResponseContentLength.get();
            if (responseContentLength != contentLength) {
                throw new NotAcceptableHeaderException("Body length=" + responseContentLength +
                        " doesnt match " +
                        HttpHeaderName.CONTENT_LENGTH + "=" + responseContentLength);
            }
        } else {
            this.addHeader(HttpHeaderName.CONTENT_LENGTH, Long.valueOf(contentLength));
        }
        this.response.setBody(body);
    }

    @Override
    public void setBodyText(final String text) {
        this.response.setBodyText(text);
    }
}
