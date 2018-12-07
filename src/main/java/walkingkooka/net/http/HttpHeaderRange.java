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
import walkingkooka.collect.list.Lists;
import walkingkooka.compare.Range;
import walkingkooka.compare.RangeBound;
import walkingkooka.net.header.HeaderValue;
import walkingkooka.net.header.HeaderValueException;
import walkingkooka.text.CharSequences;
import walkingkooka.text.CharacterConstant;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents the value of a Range header. Note the unit is not validate except that it must not be empty.
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Range"></a>
 */
public final class HttpHeaderRange implements HeaderValue,
        Value<List<Range<Long>>> {

    public final static CharacterConstant ASSIGNMENT = CharacterConstant.with('=');

    public final static CharacterConstant BETWEEN = CharacterConstant.with('-');

    /**
     * Parsers a header value.
     */
    public static HttpHeaderRange parse(final String header) {
        CharSequences.failIfNullOrEmpty(header, "header");

        final int equalsAfterUnit = header.indexOf('=');
        if (-1 == equalsAfterUnit) {
            throw new HeaderValueException("Missing unit and '=' from header value " + CharSequences.quote(header));
        }

        final List<Range<Long>> ranges = Arrays.stream(header.substring(equalsAfterUnit + 1).split(SEPARATOR.string()))
                .map(HttpHeaderRange::range)
                .collect(Collectors.toList());

        return new HttpHeaderRange(header.substring(0, equalsAfterUnit),
                ranges);
    }

    private static Range<Long> range(final String value) {
        final int dash = value.indexOf(BETWEEN.character());
        if (-1 == dash) {
            throw new HeaderValueException("Missing " +
                    CharSequences.quoteIfChars(BETWEEN.character()) +
                    " from header value " +
                    CharSequences.quote(value));
        }

        final Range<Long> from = Range.greaterThanEquals(Long.parseLong(value.substring(0, dash).trim()));
        final String to = value.substring(dash + 1);
        return to.isEmpty() ?
                from :
                from.and(Range.lessThanEquals(Long.parseLong(to)));
    }

    /**
     * Factory that creates a new {@link HttpHeaderRange}
     */
    public static HttpHeaderRange with(final String unit, final List<Range<Long>> ranges) {
        checkUnit(unit);

        return new HttpHeaderRange(unit, checkValue(ranges));
    }

    /**
     * Private ctor use factory.
     */
    private HttpHeaderRange(final String unit, final List<Range<Long>> ranges) {
        super();
        this.unit = unit;
        this.ranges = ranges;
    }

    // unit ........................................................................................

    public String unit() {
        return this.unit;
    }

    public HttpHeaderRange setUnit(final String unit) {
        checkUnit(unit);
        return this.unit.equals(unit) ?
                this :
                this.replace(unit, this.ranges);
    }

    private final String unit;

    private static void checkUnit(final String unit) {
        CharSequences.failIfNullOrEmpty(unit, "unit");
    }

    // value ........................................................................................

    @Override
    public List<Range<Long>> value() {
        return this.ranges;
    }

    public HttpHeaderRange setValue(final List<Range<Long>> value) {
        final List<Range<Long>> copy = checkValue(value);
        return this.ranges.equals(copy) ?
                this :
                this.replace(this.unit, copy);
    }

    private final List<Range<Long>> ranges;

    private static List<Range<Long>> checkValue(final List<Range<Long>> ranges) {
        Objects.requireNonNull(ranges, "ranges");

        final List<Range<Long>> copy = Lists.array();
        copy.addAll(ranges);

        if (ranges.isEmpty()) {
            throw new IllegalArgumentException("Missing range");
        }
        return ranges;
    }

    // replace.............................................................................

    private HttpHeaderRange replace(final String unit, final List<Range<Long>> ranges) {
        return new HttpHeaderRange(unit, ranges);
    }

    // Object.............................................................................

    @Override
    public String toHeaderText() {
        return this.toString();
    }

    // HasHttpHeaderScope ....................................................................................................

    @Override
    public HttpHeaderScope httpHeaderScope() {
        return HttpHeaderScope.REQUEST_RESPONSE;
    }

    // Object.............................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.unit, this.ranges);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HttpHeaderRange &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final HttpHeaderRange other) {
        return this.unit.equals(other.unit) &
                this.ranges.equals(other.ranges);
    }

    @Override
    public String toString() {
        return this.unit + ASSIGNMENT.character() + this.ranges.stream()
                .map(this::toStringRange)
                .collect(Collectors.joining(SEPARATOR + " "));
    }

    /**
     * Formats a {@link Range} to match the header format.
     */
    private String toStringRange(final Range<Long> range) {
        final StringBuilder b = new StringBuilder();
        b.append(range.lowerBound().value().get());
        b.append(BETWEEN);

        final RangeBound<Long> upper = range.upperBound();
        if (!upper.isAll()) {
            b.append(upper.value().get());
        }
        return b.toString();
    }
}
