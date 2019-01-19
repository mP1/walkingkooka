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

package walkingkooka.net.header;

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.collect.list.Lists;
import walkingkooka.compare.Range;
import walkingkooka.compare.RangeBound;
import walkingkooka.text.CharSequences;
import walkingkooka.text.CharacterConstant;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents the value of a Range header. Note the unit is not validate except that it must not be empty.
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Range"></a>
 * <pre>
 * The Range HTTP request header indicates the part of a document that the server should return.
 * Several parts can be requested with one Range header at once, and the server may send back these ranges in a multipart
 * document. If the server sends back ranges, it uses the 206 Partial Content for the response. If the ranges are invalid,
 * the server returns the 416 Range Not Satisfiable error. The server can also ignore the Range header and return the whole
 * document with a 200 status code.
 * ...
 * Range: <unit>=<range-start>-
 * Range: <unit>=<range-start>-<range-end>
 * Range: <unit>=<range-start>-<range-end>, <range-start>-<range-end>
 * Range: <unit>=<range-start>-<range-end>, <range-start>-<range-end>, <range-start>-<range-end>
 * ...
 * <unit>
 * The unit in which ranges are specified. This is usually bytes.
 * <range-start>
 * An integer in the given unit indicating the beginning of the request range.
 * <range-end>
 * An integer in the given unit indicating the end of the requested range. This value is optional and, if omitted, the end of the document is taken as the end of the range.
 * ...
 * Range: bytes=200-1000, 2000-6576, 19000-
 * </pre>
 */
public final class RangeHeaderValue implements HeaderValue,
        Value<List<Range<Long>>> {

    public final static CharacterConstant BETWEEN = CharacterConstant.with('-');

    /**
     * Parsers a header value.
     */
    public static RangeHeaderValue parse(final String header) {
        CharSequences.failIfNullOrEmpty(header, "header");

        final int equalsAfterUnit = header.indexOf('=');
        if (-1 == equalsAfterUnit) {
            throw new HeaderValueException("Missing unit and '=' from header value " + CharSequences.quote(header));
        }

        final List<Range<Long>> ranges = Arrays.stream(header.substring(equalsAfterUnit + 1).split(SEPARATOR.string()))
                .map(RangeHeaderValue::range)
                .collect(Collectors.toList());

        try {
            final RangeHeaderValueUnit unit = RangeHeaderValueUnit.parse(header.substring(0, equalsAfterUnit));
            unit.rangeCheck();
            checkValue(ranges);

            return new RangeHeaderValue(unit, ranges);
        } catch (final IllegalArgumentException cause) {
            throw new HeaderValueException(cause.getMessage(), cause);
        }
    }

    private static Range<Long> range(final String value) {
        final int dash = value.indexOf(BETWEEN.character());
        if (-1 == dash) {
            throw new HeaderValueException("Missing " +
                    CharSequences.quoteIfChars(BETWEEN.character()) +
                    " from header value " +
                    CharSequences.quote(value));
        }

        if (0 == dash) {
            throw new HeaderValueException("Header range missing start " + CharSequences.quote(value));
        }

        final Range<Long> from = Range.greaterThanEquals(Long.parseLong(value.substring(0, dash).trim()));
        final String to = value.substring(dash + 1);
        return to.isEmpty() ?
                from :
                from.and(Range.lessThanEquals(Long.parseLong(to)));
    }

    /**
     * Factory that creates a new {@link RangeHeaderValue}
     */
    public static RangeHeaderValue with(final RangeHeaderValueUnit unit, final List<Range<Long>> ranges) {
        checkUnit(unit);

        return new RangeHeaderValue(unit, copyAndCheckValue(ranges));
    }

    /**
     * Private ctor use factory.
     */
    private RangeHeaderValue(final RangeHeaderValueUnit unit, final List<Range<Long>> ranges) {
        super();
        this.unit = unit;
        this.ranges = ranges;
    }

    // unit ........................................................................................

    public RangeHeaderValueUnit unit() {
        return this.unit;
    }

    public RangeHeaderValue setUnit(final RangeHeaderValueUnit unit) {
        checkUnit(unit);
        return this.unit.equals(unit) ?
                this :
                this.replace(unit, this.ranges);
    }

    private final RangeHeaderValueUnit unit;

    private static void checkUnit(final RangeHeaderValueUnit unit) {
        Objects.requireNonNull(unit, "unit");

        unit.rangeCheck();
    }

    // value ........................................................................................

    @Override
    public List<Range<Long>> value() {
        return this.ranges;
    }

    public RangeHeaderValue setValue(final List<Range<Long>> value) {
        final List<Range<Long>> copy = copyAndCheckValue(value);
        return this.ranges.equals(copy) ?
                this :
                this.replace(this.unit, copy);
    }

    private final List<Range<Long>> ranges;

    /**
     * The charsets of ranges must not be empty, contain nulls or overlapping ranges. If any of these tests fail a
     * {@link HeaderValueException} will be thrown.
     */
    private static List<Range<Long>> copyAndCheckValue(final List<Range<Long>> ranges) {
        Objects.requireNonNull(ranges, "ranges");

        final List<Range<Long>> copy = Lists.array();
        copy.addAll(ranges);

        final int count = ranges.size();
        if (0 == count) {
            throw new HeaderValueException("Missing range");
        }
        final int last = count - 1;
        for (int i = 0; i < last; i++) {
            final Range<Long> range = copy.get(i);

            for (int j = i + 1; j < count; j++) {
                final Range<Long> other = copy.get(j);
                if (range.isOverlapping(other)) {
                    throw new HeaderValueException("Range overlap bewteen " + range + " and " + other);
                }
            }
        }

        return ranges;
    }

    private static void checkValue(final List<Range<Long>> ranges) {
        final int count = ranges.size();
        if (0 == count) {
            throw new HeaderValueException("Missing range");
        }
        final int last = count - 1;
        for (int i = 0; i < last; i++) {
            final Range<Long> range = ranges.get(i);

            for (int j = i + 1; j < count; j++) {
                final Range<Long> other = ranges.get(j);
                if (range.isOverlapping(other)) {
                    throw new HeaderValueException("Range overlap bewteen " + range + " and " + other);
                }
            }
        }
    }

    // replace.............................................................................

    private RangeHeaderValue replace(final RangeHeaderValueUnit unit, final List<Range<Long>> ranges) {
        return new RangeHeaderValue(unit, ranges);
    }

    // HeaderValue.............................................................................

    @Override
    public String toHeaderText() {
        return this.toString();
    }

    @Override
    public boolean isWildcard() {
        return false;
    }

    // HasHeaderScope ....................................................................................................

    final static boolean IS_MULTIPART = false;

    @Override
    public final boolean isMultipart() {
        return IS_MULTIPART;
    }

    final static boolean IS_REQUEST = true;

    @Override
    public final boolean isRequest() {
        return IS_REQUEST;
    }

    final static boolean IS_RESPONSE = true;

    @Override
    public final boolean isResponse() {
        return IS_RESPONSE;
    }

    // Object.............................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.unit, this.ranges);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof RangeHeaderValue &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final RangeHeaderValue other) {
        return this.unit.equals(other.unit) &
                this.ranges.equals(other.ranges);
    }

    @Override
    public String toString() {
        return this.unit.toHeaderText() + PARAMETER_NAME_VALUE_SEPARATOR.character() + this.ranges.stream()
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
