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

package walkingkooka.net.http;

import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.ToStringBuilderOption;
import walkingkooka.collect.map.Maps;
import walkingkooka.compare.Range;
import walkingkooka.compare.RangeBound;
import walkingkooka.net.header.CharsetHeaderValue;
import walkingkooka.net.header.CharsetName;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.header.MediaTypeParameterName;
import walkingkooka.net.header.NotAcceptableHeaderException;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharacterConstant;
import walkingkooka.text.LineEnding;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A http entity containing headers and body.
 */
public final class HttpEntity implements HasHeaders, HashCodeEqualsDefined {

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
                           final List<CharsetHeaderValue> acceptCharset,
                           final String text) {
        Objects.requireNonNull(contentType, "contentType");
        Objects.requireNonNull(acceptCharset, "acceptCharset");
        Objects.requireNonNull(text, "text");

        final CharsetName contentTypeCharset = MediaTypeParameterName.CHARSET.parameterValueOrFail(contentType);
        if (!acceptCharset
                .stream()
                .anyMatch(v -> v.value().matches(contentTypeCharset))) {
            throw new NotAcceptableHeaderException("Content type " + contentTypeCharset +
                    " not in accept-charset=" + CharsetHeaderValue.toHeaderTextList(acceptCharset));
        }

        final byte[] body = text.getBytes(contentTypeCharset.charset().get());

        // content type, content-length
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_TYPE, contentType);
        headers.put(HttpHeaderName.CONTENT_LENGTH, Long.valueOf(body.length));

        return new HttpEntity(headers, body);
    }

    /**
     * A constant with no body.
     */
    public final static byte[] NO_BODY = new byte[0];

    /**
     * A constant with no headers.
     */
    public final static Map<HttpHeaderName<?>, Object> NO_HEADERS = Maps.empty();

    /**
     * Creates a new {@link HttpEntity}
     */
    public static HttpEntity with(final Map<HttpHeaderName<?>, Object> headers, final byte[] body) {
        return new HttpEntity(checkHeaders(headers), checkBody(body));
    }

    /**
     * Private ctor
     */
    private HttpEntity(final Map<HttpHeaderName<?>, Object> headers, final byte[] body) {
        super();
        this.headers = headers;
        this.body = body;
    }
    // headers ...................................................................................

    @Override
    public Map<HttpHeaderName<?>, Object> headers() {
        return Maps.readOnly(this.headers);
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

    private Map<HttpHeaderName<?>, Object> headers;

    private static Map<HttpHeaderName<?>, Object> checkHeaders(final Map<HttpHeaderName<?>, Object> headers) {
        Objects.requireNonNull(headers, "headers");

        final Map<HttpHeaderName<?>, Object> copy = Maps.ordered();
        for (Entry<HttpHeaderName<?>, Object> nameAndValue : headers.entrySet()) {
            final HttpHeaderName<?> name = nameAndValue.getKey();
            copy.put(name, name.checkValue(nameAndValue.getValue()));
        }
        return copy;
    }

    // body ...................................................................................

    public byte[] body() {
        return this.body;
    }

    /**
     * Would be setter that returns a {@link HttpEntity} with the given body creating a new instance if necessary.
     */
    public HttpEntity setBody(final byte[] body) {
        final byte[] copy = checkBody(body);

        return Arrays.equals(this.body, copy) ?
                this :
                this.replace(this.headers, body);
    }

    private final byte[] body;

    private static byte[] checkBody(final byte[] body) {
        Objects.requireNonNull(body, "body");
        return body.clone();
    }

    // extractRange ...................................................................................

    /**
     * Extracts the desired range returning an entity with the selected bytes creating a new instance if necessary.
     */
    public HttpEntity extractRange(final Range<Long> range) {
        Objects.requireNonNull(range, "range");

        final RangeBound<Long> lowerBound = range.lowerBound();
        checkNotExclusive(lowerBound, range);
        final int from = lowerBound.isAll() ? 0 : lowerBound.value().get().intValue();
        if (from < 0) {
            throw new IllegalArgumentException("Range " + range + " lower bounds <= 0");
        }

        final RangeBound<Long> upperBound = range.upperBound();
        checkNotExclusive(upperBound, range);

        final int bodyLength = this.body.length;
        final int to = upperBound.isAll() ?
                bodyLength :
                1 + upperBound.value().get().intValue(); //adjust $to to point to one + actual desired last byte.

        if(to > bodyLength) {
            throw new IllegalArgumentException("Part of range " + range + " is outside body 0.." + (bodyLength-1));
        }

        return 0 == from && to == bodyLength ?
                this :
                this.extractRange0(from, to - from);
    }

    private static void checkNotExclusive(final RangeBound<Long> bound, final Range<Long> range) {
        if (bound.isExclusive()) {
            throw new IllegalArgumentException("Range " + range + " includes exclusive bound " + bound);
        }
    }

    private HttpEntity extractRange0(final int lowerBounds, final int size) {
        final byte[] rangeBytes = new byte[size];
        System.arraycopy(this.body, lowerBounds, rangeBytes, 0, size);
        return new HttpEntity(this.headers, rangeBytes);
    }

    // replace....................................................................................................

    private HttpEntity replace(final Map<HttpHeaderName<?>, Object> headers) {
        return this.replace(headers, this.body);
    }

    private HttpEntity replace(final Map<HttpHeaderName<?>, Object> headers, final byte[] body) {
        return new HttpEntity(headers, body);
    }

    // Object....................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.headers, Arrays.hashCode(this.body));
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HttpEntity &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final HttpEntity other) {
        return this.headers.equals(other.headers) &&
                Arrays.equals(this.body, other.body);
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
        b.enable(ToStringBuilderOption.HEX_BYTES);
        b.valueLength(80);
        b.value(this.body);

        return b.build();
    }
}
