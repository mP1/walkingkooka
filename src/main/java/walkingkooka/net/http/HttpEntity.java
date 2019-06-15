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

package walkingkooka.net.http;

import walkingkooka.Binary;
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.collect.map.Maps;
import walkingkooka.compare.Range;
import walkingkooka.net.header.CharsetName;
import walkingkooka.net.header.ContentRange;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.header.MediaTypeParameterName;
import walkingkooka.net.header.NotAcceptableHeaderException;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.Ascii;
import walkingkooka.text.CharSequences;
import walkingkooka.text.CharacterConstant;
import walkingkooka.text.LineEnding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A http entity containing headers and body.
 */
public final class HttpEntity implements HasHeaders, HashCodeEqualsDefined {

    /**
     * {@link Binary} with no body or bytes.
     */
    public final static Binary NO_BODY = Binary.EMPTY;

    /**
     * The line ending used in http requests/responses.
     */
    public final static LineEnding LINE_ENDING = LineEnding.CRNL;

    /**
     * The separator that follows a header name and comes before a any values.
     */
    public final static CharacterConstant HEADER_NAME_SEPARATOR = CharacterConstant.with(':');

    /**
     * Creates an {@link HttpEntity} after encoding the text as bytes using a negotiated charset.
     * The returned entity will only have 2 headers set: content-type and content-length set.
     */
    static HttpEntity text(final MediaType contentType,
                           final String text) {
        Objects.requireNonNull(contentType, "contentType");
        Objects.requireNonNull(text, "text");

        final CharsetName contentTypeCharset = MediaTypeParameterName.CHARSET.parameterValueOrFail(contentType);
        final Optional<Charset> possibleCharset = contentTypeCharset.charset();
        if (!possibleCharset.isPresent()) {
            throw new NotAcceptableHeaderException("Content type " + contentType + " contains unsupported charset.");
        }

        final byte[] body = text.getBytes(possibleCharset.get());

        // content type, content-length
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_TYPE, contentType);
        headers.put(HttpHeaderName.CONTENT_LENGTH, Long.valueOf(body.length));

        return new HttpEntity(headers, Binary.with(body));
    }

    /**
     * A constant with no headers.
     */
    public final static Map<HttpHeaderName<?>, Object> NO_HEADERS = Maps.empty();

    /**
     * A {@link HttpEntity} with no headers and no content.
     */
    public final static HttpEntity EMPTY = new HttpEntity(NO_HEADERS, NO_BODY);

    /**
     * Creates a new {@link HttpEntity}
     */
    public static HttpEntity with(final Map<HttpHeaderName<?>, Object> headers, final Binary body) {
        return new HttpEntity(checkHeaders(headers), checkBody(body));
    }

    /**
     * Private ctor
     */
    private HttpEntity(final Map<HttpHeaderName<?>, Object> headers, final Binary body) {
        super();
        this.headers = headers;
        this.body = body;
    }
    // headers ...................................................................................

    @Override
    public Map<HttpHeaderName<?>, Object> headers() {
        return this.headers;
    }

    /**
     * Would be setter that returns a {@link HttpEntity} with the given headers creating a new instance if necessary.
     */
    public HttpEntity setHeaders(final Map<HttpHeaderName<?>, Object> headers) {
        final Map<HttpHeaderName<?>, Object> copy = checkHeaders(headers);

        return this.headers.equals(copy) ?
                this :
                this.replace(headers);

    }

    /**
     * Adds the given header from this entity returning a new instance if the header and value are new.
     */
    public <T> HttpEntity addHeader(final HttpHeaderName<T> header, final T value) {
        check(header);
        Objects.requireNonNull(value, "value");

        return value.equals(this.headers.get(header)) ?
                this :
                this.addHeaderAndReplace(header, value);
    }

    private <T> HttpEntity addHeaderAndReplace(final HttpHeaderName<T> header, final T value) {
        final Map<HttpHeaderName<?>, Object> copy = Maps.ordered();
        copy.putAll(this.headers);
        copy.put(header, header.checkValue(value));
        return this.replace(copy);
    }

    /**
     * Removes the given header from this entity returning a new instance if it existed.
     */
    public HttpEntity removeHeader(final HttpHeaderName<?> header) {
        check(header);

        return this.headers.containsKey(header) ?
                this.removeHeaderAndReplace(header) :
                this;
    }

    private HttpEntity removeHeaderAndReplace(final HttpHeaderName<?> header) {
        return this.replace(this.headers()
                .entrySet()
                .stream()
                .filter(h -> !h.getKey().equals(header))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    private static <T> void check(final HttpHeaderName<T> header) {
        Objects.requireNonNull(header, "header");
    }

    private final Map<HttpHeaderName<?>, Object> headers;

    private static Map<HttpHeaderName<?>, Object> checkHeaders(final Map<HttpHeaderName<?>, Object> headers) {
        Objects.requireNonNull(headers, "headers");

        final Map<HttpHeaderName<?>, Object> copy = Maps.ordered();
        for (Entry<HttpHeaderName<?>, Object> nameAndValue : headers.entrySet()) {
            final HttpHeaderName<?> name = nameAndValue.getKey();
            copy.put(name, name.checkValue(nameAndValue.getValue()));
        }
        return Maps.immutable(copy);
    }

    // body ...................................................................................

    public Binary body() {
        return this.body;
    }

    /**
     * Would be setter that returns a {@link HttpEntity} with the given body creating a new instance if necessary.
     */
    public HttpEntity setBody(final Binary body) {
        checkBody(body);

        return body.equals(this.body) ?
                this :
                this.replace(this.headers, body);
    }

    private final Binary body;

    private static Binary checkBody(final Binary body) {
        return Objects.requireNonNull(body, "body");
    }

    // extractRange ...................................................................................

    /**
     * Extracts the desired range returning an entity with the selected bytes creating a new instance if necessary.
     */
    public HttpEntity extractRange(final Range<Long> range) {
        return this.setBody(this.body.extract(range));
    }

    // replace....................................................................................................

    private HttpEntity replace(final Map<HttpHeaderName<?>, Object> headers) {
        return this.replace(headers, this.body);
    }

    private HttpEntity replace(final Map<HttpHeaderName<?>, Object> headers, final Binary body) {
        return new HttpEntity(headers, body);
    }

    // Multipart..................................................................................................

    /**
     * Writes a multi-part entity.
     * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types#multipartform-data"></a>
     * <pre>
     * HTTP/1.1 206 Partial Content
     * Accept-Ranges: bytes
     * Content-Type: multipart/byteranges; boundary=3d6b6a416f9b5
     * Content-Length: 385
     *
     * --3d6b6a416f9b5
     * Content-Type: text/html
     * Content-Range: bytes 100-200/1270
     *
     * eta http-equiv="Content-type" content="text/html; charset=utf-8" />
     *     <meta name="vieport" content
     * --3d6b6a416f9b5
     * Content-Type: text/html
     * Content-Range: bytes 300-400/1270
     *
     * -color: #f0f0f2;
     *         margin: 0;
     *         padding: 0;
     *         font-family: "Open Sans", "Helvetica
     * --3d6b6a416f9b5--
     * </pre>
     */
    public byte[] headersAndBodyBytes() throws IOException {
        final Map<HttpHeaderName<?>, Object> headers = this.headers;

        if (!headers.containsKey(HttpHeaderName.CONTENT_TYPE)) {
            throw new IllegalStateException("Headers missing " + HttpHeaderName.CONTENT_TYPE + " in " + headers);
        }
        this.contentLengthOrContentRangeOrFail(headers);

        final byte[] body = this.body.value();

        try (final ByteArrayOutputStream bytes = new ByteArrayOutputStream(headers.size() * 80 + body.length)) {
            for (Entry<HttpHeaderName<?>, Object> headerAndValues : headers.entrySet()) {
                final HttpHeaderName<?> header = headerAndValues.getKey();

                // header: value CRNL
                bytes.write(header.value().getBytes()); // should be ascii only chars
                bytes.write(HEADER_VALUE_SEPARATOR);
                bytes.write(header.headerText(Cast.to(headerAndValues.getValue())).getBytes());
                bytes.write(EOL);
            }

            // blank line ends headers...
            bytes.write(EOL);

            // body
            bytes.write(body);

            bytes.flush();

            return bytes.toByteArray();
        }
    }

    /**
     * Requires either the content-length or content-range headers be set and returns the length in bytes or -1,
     * where -1 indicates a content-range: wildcard.
     */
    private void contentLengthOrContentRangeOrFail(final Map<HttpHeaderName<?>, Object> headers) {
        final Optional<Long> maybeContentLength = HttpHeaderName.CONTENT_LENGTH.parameterValue(headers);

        if (maybeContentLength.isPresent()) {
            final Long contentLength = maybeContentLength.get();
            this.checkBodyLength(HttpHeaderName.CONTENT_LENGTH, contentLength, contentLength);
        } else {
            final Optional<ContentRange> maybeContentRange = HttpHeaderName.CONTENT_RANGE.parameterValue(headers);
            if (maybeContentRange.isPresent()) {
                maybeContentRange.ifPresent(this::checkContentRange);
            } else {
                throw new IllegalStateException("Headers missing " + HttpHeaderName.CONTENT_LENGTH + " or " + HttpHeaderName.CONTENT_RANGE + " in " + headers);
            }
        }
    }

    private void checkContentRange(final ContentRange contentRange) {
        contentRange.length()
                .ifPresent(l -> this.checkBodyLength(HttpHeaderName.CONTENT_RANGE, contentRange, l));
    }

    private void checkBodyLength(final HttpHeaderName<?> header,
                                 final Object headerValue,
                                 final long length) {
        final long actualLength = this.body.size();
        if (length != actualLength) {
            throw new IllegalStateException(header + ": " + header.headerText(Cast.to(headerValue)) + " & actual body length " + actualLength + " mismatch.");
        }
    }

    private final static byte[] HEADER_VALUE_SEPARATOR = new byte[]{':', ' '};
    private final static byte[] EOL = new byte[]{'\r', '\n'};

    // Object....................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.headers, this.body);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HttpEntity &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final HttpEntity other) {
        return this.headers.equals(other.headers) &&
                this.body.equals(other.body);
    }

    /**
     * The {@link String} produced looks almost like a http entity, each header will appear on a single, a colon separates
     * the header name and value, and a blank line between headers and body, with the body bytes appearing in hex form.
     */
    @Override
    public String toString() {
        final ToStringBuilder b = ToStringBuilder.empty();
        b.disable(ToStringBuilderOption.QUOTE);
        b.disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);

        final String eol = LINE_ENDING.toString();

        // headers
        b.valueSeparator(eol);
        b.labelSeparator(HEADER_NAME_SEPARATOR.string().concat(" "));
        b.value(this.headers());
        b.append(eol).append(eol);

        // body
        b.valueSeparator("");

        byte[] body = this.body.value();
        final Optional<MediaType> mediaType = HttpHeaderName.CONTENT_TYPE.headerValue(this.headers);
        if (mediaType.isPresent()) {
            final Optional<CharsetName> charsetName = MediaTypeParameterName.CHARSET.parameterValue(mediaType.get());
            if (charsetName.isPresent()) {
                final Optional<Charset> charset = charsetName.get().charset();
                if (charset.isPresent()) {
                    b.value(new String(body, charset.get()));
                    body = null;
                }
            }
        }

        if (null != body) {
            // hex dump
            // offset-HEX_DUMP_WIDTH-chars space hexdigit *HEX_DUMP_WIDTH space separated ascii
            b.enable(ToStringBuilderOption.HEX_BYTES);
            b.valueSeparator(" ");
            b.labelSeparator(" ");

            final int length = body.length;
            for (int i = 0; i < length; i = i + HEX_DUMP_WIDTH) {
                // offset
                b.append(CharSequences.padLeft(Integer.toHexString(i), 8, '0').toString());
                b.append(' ');

                for (int j = 0; j < HEX_DUMP_WIDTH; j++) {
                    final int k = i + j;
                    if (k < length) {
                        b.value(body[k]);
                    } else {
                        b.value("  ");
                    }
                }

                b.append(' ');

                for (int j = 0; j < HEX_DUMP_WIDTH; j++) {
                    final int k = i + j;
                    if (k < length) {
                        final char c = (char) body[k];
                        b.append(Ascii.isPrintable(c) ? c : UNPRINTABLE_CHAR);
                    } else {
                        b.append(' ');
                    }
                }

                b.append(LineEnding.SYSTEM);
            }
        }

        return b.build();
    }

    private final static int HEX_DUMP_WIDTH = 16;
    private final static char UNPRINTABLE_CHAR = '.';
}
