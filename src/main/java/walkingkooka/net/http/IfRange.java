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
import walkingkooka.Value;
import walkingkooka.net.header.HeaderValue;
import walkingkooka.net.header.HeaderValueConverter;
import walkingkooka.net.header.HeaderValueConverters;
import walkingkooka.text.CharSequences;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * An if-range header.
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/If-Range"></a>
 * <pre>
 * The If-Range HTTP request header makes a range request conditional: if the condition is fulfilled, the range request
 * will be issued and the server sends back a 206 Partial Content answer with the appropriate body.
 * If the condition is not fulfilled, the full resource is sent back, with a 200 OK status.
 *
 * This header can be used either with a Last-Modified validator, or with an ETag, but not with both.
 *
 * The most common use case is to resume a download, to guarantee that the stored resource has not been modified since
 * the last fragment has been received.
 * ...
 * If-Range: <day-name>, <day> <month> <year> <hour>:<minute>:<second> GMT
 * If-Range: <etag>
 * ...
 * <etag>
 * An entity tag uniquely representing the requested resource.
 * It is a string of ASCII characters placed between double quotes (Like "675af34563dc-tr34") and may be prefixed by
 * W/ to indicate that the weak comparison algorithm should be used.
 * <day-name>
 * One of "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", or "Sun" (case-sensitive).
 * <day>
 * 2 digit day number, e.g. "04" or "23".
 * <month>
 * One of "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" (case sensitive).
 * <year>
 * 4 digit year number, e.g. "1990" or "2016".
 * <hour>
 * 2 digit hour number, e.g. "09" or "23".
 * <minute>
 * 2 digit minute number, e.g. "04" or "59".
 * <second>
 * 2 digit second number, e.g. "04" or "59".
 * GMT
 * Greenwich Mean Time. HTTP dates are always expressed in GMT, never in local time.
 * </pre>
 */
public abstract class IfRange<T> implements HeaderValue, Value<T> {

    /**
     * The {@link HttpHeaderName}.
     */
    final static HttpHeaderName<IfRange<?>> HEADER_NAME = HttpHeaderName.IF_RANGE;

    /**
     * Parses an if-range header into an {@link IfRange}.
     */
    public static IfRange<?> parse(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");

        return IfRangeHttpHeaderValueConverter.INSTANCE.parse(text, HEADER_NAME);
    }

    final static HeaderValueConverter<ETag> ETAG = HttpHeaderValueConverter.eTag();
    final static HeaderValueConverter<LocalDateTime> DATE_TIME = HeaderValueConverters.localDateTime();

    /**
     * Factory that creates an {@link IfRange} expecting an etag or date/time.
     */
    public static <T> IfRange<T> with(final T value) {
        check(value);

        return Cast.to(value instanceof ETag ?
                IfRangeETag.etag(ETag.class.cast(value)) :
                IfRangeLastModified.lastModified(LocalDateTime.class.cast(value)));
    }

    /**
     * Package private to limit sub classing.
     */
    IfRange(final T value) {
        super();
        this.value = value;
    }

    /**
     * Returns the value which will be either the etag or last-modified date.
     */
    @Override
    public final T value() {
        return this.value;
    }

    /**
     * Returns an instance with the given value creating a new instance if necessary.
     */
    public <TT> IfRange<TT> setValue(final TT value) {
        check(value);

        return this.value.equals(value) ?
                Cast.to(this) :
                this.replace(value);
    }

    private final T value;


    private static void check(final Object value) {
        Objects.requireNonNull(value, "value");

        if (false == value instanceof ETag && false == value instanceof LocalDateTime) {
            throw new IllegalArgumentException("Expected etag or datetime but got " + CharSequences.quoteIfChars(value));
        }
    }

    private <TT> IfRange<TT> replace(final TT value) {
        return Cast.to(with(value));
    }

    /**
     * Returns true if the value is an etag.
     */
    abstract public boolean isETag();

    /**
     * Returns true if the value is a last modified.
     */
    abstract public boolean isLastModified();

    /**
     * Casts this {@link IfRange} if it has a etag or fail.
     */
    abstract public IfRange<ETag> etag();

    /**
     * Casts this {@link IfRange} if it has a last modified or fail.
     */
    abstract public IfRange<LocalDateTime> lastModified();

    @Override
    public final HttpHeaderScope httpHeaderScope() {
        return HttpHeaderScope.REQUEST;
    }

    @Override
    public final String toHeaderText() {
        return this.converter().toText(this.value(), HEADER_NAME);
    }

    /**
     * A converter used to format the value as a header text.
     */
    abstract HeaderValueConverter<T> converter();

    // Object .........................................................................................

    @Override
    public final int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final IfRange<?> other) {
        return this.value.equals(other.value);
    }

    @Override
    public final String toString() {
        return this.toHeaderText();
    }
}
