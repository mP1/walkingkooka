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
import walkingkooka.net.header.CharsetHeaderValue;
import walkingkooka.net.header.CharsetName;
import walkingkooka.net.header.HeaderValueException;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.header.MediaTypeParameterName;
import walkingkooka.net.header.NotAcceptableHeaderValueException;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Wraps a {@link HttpResponse} ensuring the content type charset is one of the requested types if {@link HttpHeaderName#ACCEPT_CHARSET}
 * was present.
 */
final class AutoTextEncodingHttpResponse extends WrapperHttpResponse {

    /**
     * Factory that creates a new {@link AutoTextEncodingHttpResponse}
     */
    static AutoTextEncodingHttpResponse with(final HttpRequest request,
                                             final HttpResponse response) {
        check(request, response);

        return new AutoTextEncodingHttpResponse(request, response);
    }

    /**
     * Private ctor use factory
     */
    private AutoTextEncodingHttpResponse(final HttpRequest request,
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

    /**
     * If the header is a content-type, verify its charset is present and matched by any accept-charset charset.
     */
    @Override
    public <T> void addHeader(final HttpHeaderName<T> name, final T value) {
        if (HttpHeaderName.CONTENT_TYPE.equals(name)) {
            this.checkAcceptCharset(this.charset(Cast.to(value)));
        }
        this.response.addHeader(name, value);
    }

    private CharsetName charset(final MediaType contentType) {
        try {
            return MediaTypeParameterName.CHARSET
                    .parameterValueOrFail(contentType);
        } catch (final HeaderValueException cause) {
            throw new NotAcceptableHeaderValueException(cause.getMessage(), cause);
        }
    }

    /**
     * Checks and complains if the content-type charset is not matched by one of the accept-charset charsets.<br>
     * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Charset"></a>
     * <pre>
     * The Accept-Charset request HTTP header advertises which character set the client is able to understand.
     * Using content negotiation, the server then selects one of the proposals, uses it and informs the client of its
     * choice within the Content-Type response header. Browsers usually don't set this header as the default value for
     * each content type is usually correct and transmitting it would allow easier fingerprinting.
     *
     * If the server cannot serve any matching character set, it can theoretically send back a 406 (Not Acceptable)
     * error code. But, for a better user experience, this is rarely done and the more common way is to ignore the
     * Accept-Charset header in this case.
     *
     * In early versions of HTTP/1.1, a default charset (ISO-8859-1) was defined. This is no more the case and now each
     * content type may have its own default
     * </pre>
     */
    private void checkAcceptCharset(final CharsetName contentTypeCharset) {
        final List<CharsetHeaderValue> acceptCharset = HttpHeaderName.ACCEPT_CHARSET
                .headerValueOrFail(this.request);
        if(!acceptCharset
                .stream()
                .anyMatch(h -> this.checkContentTypeCharset(h, contentTypeCharset))) {
            throw new NotAcceptableHeaderValueException("Content type " + contentTypeCharset +
                    " not in accept-charset=" + CharsetHeaderValue.toHeaderTextList(acceptCharset));
        }
    }

    private boolean checkContentTypeCharset(final CharsetHeaderValue charsetHeaderValue,
                                            final CharsetName contentTypeCharset) {
        return charsetHeaderValue.value().matches(contentTypeCharset);
    }

    @Override
    public void setBody(final byte[] body) {
        this.response.setBody(body);
    }

    /**
     * Uses the previously set content-type response header to encode the text into bytes.
     */
    @Override
    public void setBodyText(final String text) {
        Objects.requireNonNull(text, "text");

        final MediaType contentType = HttpHeaderName.CONTENT_TYPE.headerValueOrFail(this);
        this.setBody(text.getBytes(MediaTypeParameterName.CHARSET.parameterValueOrFail(contentType)
                .charset()
                .get()));
    }
}
