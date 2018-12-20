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
import walkingkooka.InvalidCharacterException;
import walkingkooka.NeverError;
import walkingkooka.compare.Range;
import walkingkooka.compare.RangeBound;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <a href=https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Range"></a>
 * <pre>
 * The Content-Range response HTTP header indicates where in a full body message a partial message belongs.
 * ...
 * Content-Range: <unit> <range-start>-<range-end>/<size>
 * Content-Range: <unit> <range-start>-<range-end>/*
 * Content-Range: <unit> * /<size>
 *
 * <unit>
 * The unit in which ranges are specified. This is usually bytes.
 * <range-start>
 * An integer in the given unit indicating the beginning of the request range.
 * <range-end>
 * An integer in the given unit indicating the end of the requested range.
 * <size>
 * The total size of the document (or '*' if unknown).
 *
 * Content-Range: bytes 200-1000/67589
 * </pre>
 */
public final class ContentRange implements HeaderValue {

    /**
     * Constant to be used when no range is present or known.
     * The header text form would be something like
     * <pre>
     * content-range: bytes * / 1234
     * </pre>
     * without any spaces.
     */
    public static Optional<Range<Long>> NO_RANGE = Optional.empty();

    /**
     * Constant to be used when no size is present or known.
     */
    public static Optional<Long> NO_SIZE = Optional.empty();

    /**
     * Parses a header value into its {@link List} of {@link ContentRange} equivalent.
     * <a href="https://tools.ietf.org/html/rfc7233#section-4.2"></a>
     * <pre>
     * 4.2.  Content-Range
     *
     *    The "Content-Range" header field is sent in a single part 206
     *    (Partial Content) response to indicate the partial range of the
     *    selected representation enclosed as the message payload, sent in each
     *    part of a multipart 206 response to indicate the range enclosed
     *    within each body part, and sent in 416 (Range Not Satisfiable)
     *    responses to provide information about the selected representation.
     *
     *      Content-Range       = byte-content-range
     *                          / other-content-range
     *
     *      byte-content-range  = bytes-unit SP
     *                            ( byte-range-resp / unsatisfied-range )
     *
     *      byte-range-resp     = byte-range "/" ( complete-length / "*" )
     *      byte-range          = first-byte-pos "-" last-byte-pos
     *      unsatisfied-range   = "* /" complete-length
     *
     *      complete-length     = 1*DIGIT
     *
     *      other-content-range = other-range-unit SP other-range-resp
     *      other-range-resp    = *CHAR
     *
     *    If a 206 (Partial Content) response contains a Content-Range header
     *    field with a range unit (Section 2) that the recipient does not
     *    understand, the recipient MUST NOT attempt to recombine it with a
     *    stored representation.  A proxy that receives such a message SHOULD
     *    forward it downstream.
     *
     *    For byte ranges, a sender SHOULD indicate the complete length of the
     *    representation from which the range has been extracted, unless the
     *    complete length is unknown or difficult to determine.  An asterisk
     *    character ("*") in place of the complete-length indicates that the
     *    representation length was unknown when the header field was
     *    generated.
     *
     *    The following example illustrates when the complete length of the
     *    selected representation is known by the sender to be 1234 bytes:
     *
     *      Content-Range: bytes 42-1233/1234
     *
     *    and this second example illustrates when the complete length is
     *    unknown:
     *
     *      Content-Range: bytes 42-1233/ *
     *
     *    A Content-Range field value is invalid if it contains a
     *    byte-range-resp that has a last-byte-pos value less than its
     *    first-byte-pos value, or a complete-length value less than or equal
     *    to its last-byte-pos value.  The recipient of an invalid
     *    Content-Range MUST NOT attempt to recombine the received content with
     *    a stored representation.
     *
     *    A server generating a 416 (Range Not Satisfiable) response to a
     *    byte-range request SHOULD send a Content-Range header field with an
     *    unsatisfied-range value, as in the following example:
     *
     *      Content-Range: bytes * /1234
     *
     *    The complete-length in a 416 response indicates the current length of
     *    the selected representation.
     *
     *    The Content-Range header field has no meaning for status codes that
     *    do not explicitly describe its semantic.  For this specification,
     *    only the 206 (Partial Content) and 416 (Range Not Satisfiable) status
     *    codes describe a meaning for Content-Range.
     *
     *    The following are examples of Content-Range values in which the
     *    selected representation contains a total of 1234 bytes:
     *
     *    o  The first 500 bytes:
     *
     *         Content-Range: bytes 0-499/1234
     *
     *    o  The second 500 bytes:
     *
     *         Content-Range: bytes 500-999/1234
     *
     *    o  All except for the first 500 bytes:
     *
     *         Content-Range: bytes 500-1233/1234
     *
     *    o  The last 500 bytes:
     *
     *         Content-Range: bytes 734-1233/1234
     * </pre>
     */
    public static ContentRange parse(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");

        try {
            return parse0(text);
        } catch (final IllegalArgumentException cause) {
            throw new HeaderValueException(cause.getMessage(), cause);
        }
    }

    private static ContentRange parse0(final String text) {
        final char wildcard = WILDCARD.character();

        int mode = MODE_UNIT;
        int i = 0;

        RangeHeaderValueUnit unit = null;
        long lower = 0;
        long upper = 0;
        Range<Long> range = null;
        long sizeValue = 0;
        Optional<Long> size = Optional.empty();

        for (char c : text.toCharArray()) {
            switch (mode) {
                case MODE_UNIT:
                    if (UNIT.test(c)) {
                        break;
                    }
                    if (WHITESPACE.test(c)) {
                        if (i == 0) {
                            throw new HeaderValueException("Unit missing from " + CharSequences.quote(text));
                        }
                        unit = RangeHeaderValueUnit.parse(text.substring(0, i));
                        mode = MODE_RANGE_START_INITIAL;
                        break;
                    }
                    throw new InvalidCharacterException(text, i);
                case MODE_RANGE_START_INITIAL:
                    if (wildcard == c) {
                        mode = MODE_RANGE_SIZE_SEPARATOR;
                        break;
                    }
                    if (DIGIT.test(c)) {
                        lower = accumulateValue(c, lower);
                        mode = MODE_RANGE_START;
                        break;
                    }
                    throw new InvalidCharacterException(text, i);
                case MODE_RANGE_START:
                    if (DIGIT.test(c)) {
                        lower = accumulateValue(c, lower);
                        break;
                    }
                    if (BETWEEN == c) {
                        range = Range.greaterThanEquals(lower);
                        mode = MODE_RANGE_END;
                        break;
                    }
                    throw new InvalidCharacterException(text, i);
                case MODE_RANGE_END_INITIAL:
                    if (DIGIT.test(c)) {
                        upper = accumulateValue(c, upper);
                        break;
                    }
                    throw new InvalidCharacterException(text, i);
                case MODE_RANGE_END:
                    if (DIGIT.test(c)) {
                        upper = accumulateValue(c, upper);
                        break;
                    }
                    if (RANGE_SIZE_SEPARATOR == c) {
                        if (upper <= lower) {
                            throw new HeaderValueException("Invalid upper bounds " + upper + " < " + lower + " in " + CharSequences.quote(text));
                        }
                        range = range.and(Range.lessThanEquals(upper));
                        mode = MODE_SIZE_INITIAL;
                        break;
                    }
                    throw new InvalidCharacterException(text, i);
                case MODE_RANGE_SIZE_SEPARATOR:
                    if (RANGE_SIZE_SEPARATOR == c) {
                        mode = MODE_SIZE_INITIAL;
                        break;
                    }
                    throw new InvalidCharacterException(text, i);
                case MODE_SIZE_INITIAL:
                    if (WILDCARD.character() == c) {
                        mode = MODE_RANGE_SIZE_SEPARATOR;
                        break;
                    }
                    mode = MODE_SIZE;
                    // intentional fall thru
                case MODE_SIZE:
                    if (DIGIT.test(c)) {
                        sizeValue = accumulateValue(c, sizeValue);
                        break;
                    }
                    throw new InvalidCharacterException(text, i);
                default:
                    NeverError.unhandledCase(mode, MODE_UNIT, MODE_RANGE_START_INITIAL, MODE_RANGE_START, MODE_RANGE_END_INITIAL, MODE_RANGE_END, MODE_RANGE_SIZE_SEPARATOR, MODE_SIZE_INITIAL, MODE_SIZE);
            }
            i++;
        }

        switch (mode) {
            case MODE_UNIT:
                failMissing("unit", text);
            case MODE_RANGE_START_INITIAL:
                failMissing("range start", text);
                break;
            case MODE_RANGE_START:
            case MODE_RANGE_END_INITIAL:
                failMissing("range end", text);
            case MODE_RANGE_END:
            case MODE_SIZE_INITIAL:
                failMissing("file size", text);
            case MODE_SIZE:
                size = Optional.of(sizeValue);
                break;
        }

        return new ContentRange(unit,
                Optional.ofNullable(range),
                size);
    }

    private final static int MODE_UNIT = 1;
    private final static int MODE_RANGE_START_INITIAL = MODE_UNIT + 1;
    private final static int MODE_RANGE_START = MODE_RANGE_START_INITIAL + 1;
    private final static int MODE_RANGE_END_INITIAL = MODE_RANGE_START + 1;
    private final static int MODE_RANGE_END = MODE_RANGE_END_INITIAL + 1;
    private final static int MODE_RANGE_SIZE_SEPARATOR = MODE_RANGE_END + 1;
    private final static int MODE_SIZE_INITIAL = MODE_RANGE_SIZE_SEPARATOR + 1;
    private final static int MODE_SIZE = MODE_SIZE_INITIAL + 1;

    private final static CharPredicate UNIT = CharPredicates.letter();
    private final static CharPredicate WHITESPACE = CharPredicates.any(" \t");
    private final static CharPredicate DIGIT = CharPredicates.digit();
    private final static char BETWEEN = '-';
    private final static char RANGE_SIZE_SEPARATOR = '/';

    private static long accumulateValue(final char c, final long value) {
        return value * 10 + Character.digit(c, 10);
    }

    private static void failMissing(final String component, final String text) {
        throw new HeaderValueException("Missing " + component + " from " + CharSequences.quote(text));
    }

    /**
     * Factory that creates a new {@link ContentRange} with the provided name and parameter.
     */
    public static ContentRange with(final RangeHeaderValueUnit unit,
                                    final Optional<Range<Long>> range,
                                    final Optional<Long> size) {
        checkUnit(unit);
        checkRange(range);
        checkSize(size);

        return new ContentRange(unit, range, size);
    }

    /**
     * Private use factory.
     */
    private ContentRange(final RangeHeaderValueUnit unit,
                         final Optional<Range<Long>> range,
                         final Optional<Long> size) {
        super();
        this.unit = unit;
        this.range = range;
        this.size = size;
    }

    // unit ..................................................................................................

    /**
     * Returns the unit.
     */
    public final RangeHeaderValueUnit unit() {
        return this.unit;
    }

    /**
     * Would be setter that returns a {@link ContentRange} with the given unit value
     * creating a new instance if necessary.
     */
    public final ContentRange setUnit(final RangeHeaderValueUnit unit) {
        checkUnit(unit);

        return this.unit().equals(unit) ?
                this :
                this.replace(unit.rangeCheck(), this.range, this.size);
    }

    private final RangeHeaderValueUnit unit;

    private static void checkUnit(final RangeHeaderValueUnit unit) {
        Objects.requireNonNull(unit, "unit");
    }

    // range ..................................................................................................

    /**
     * Returns the range.
     */
    public final Optional<Range<Long>> range() {
        return this.range;
    }

    /**
     * Would be setter that returns a {@link ContentRange} with the given range value
     * creating a new instance if necessary.
     */
    public final ContentRange setRange(final Optional<Range<Long>> range) {
        checkRange(range);

        return this.range().equals(range) ?
                this :
                this.replace(this.unit, range, this.size);
    }

    private final Optional<Range<Long>> range;

    private static void checkRange(final Optional<Range<Long>> range) {
        Objects.requireNonNull(range, "range");

        if (range.isPresent()) {
            checkRange0(range.get());
        }
    }

    private static void checkRange0(final Range<Long> range) {
        checkRangeBound(range.lowerBound(), range);
        checkRangeBound(range.upperBound(), range);
    }

    private static void checkRangeBound(final RangeBound<Long> bound,
                                        final Range<Long> range) {
        if (bound.isAll()) {
            throw new IllegalArgumentException("Range includes wildcard bound=" + range);
        }
        if (bound.isExclusive()) {
            throw new IllegalArgumentException("Range includes exclusive bound=" + range);
        }
        final long value = bound.value().get();
        if (value < 0) {
            throw new IllegalArgumentException("Range includes exclusive bound values must be > 0=" + range);
        }
    }

    // size ..................................................................................................

    /**
     * Returns the size.
     */
    public final Optional<Long> size() {
        return this.size;
    }

    /**
     * Would be setter that returns a {@link ContentRange} with the given size
     * creating a new instance if necessary.
     */
    public final ContentRange setSize(final Optional<Long> size) {
        checkSize(size);

        return this.size == size ?
                this :
                this.replace(this.unit, this.range, size);
    }

    private final Optional<Long> size;

    private static void checkSize(final Optional<Long> size) {
        Objects.requireNonNull(size, "size");

        if (size.isPresent()) {
            final long sizeValue = size.get();
            if (sizeValue < 0) {
                throw new IllegalArgumentException("Range size " + sizeValue + " when present must be > 0");
            }
        }
    }

    // replace ..................................................................................................

    /**
     * Factory that creates a new {@link ContentRange}
     */
    private ContentRange replace(final RangeHeaderValueUnit unit,
                                 final Optional<Range<Long>> range,
                                 final Optional<Long> size) {
        return new ContentRange(unit, range, size);
    }

    // HasHeaderScope....................................................................

    @Override
    public boolean isMultipart() {
        return true;
    }

    @Override
    public boolean isRequest() {
        return false;
    }

    /**
     * Content-Range is a http response header.
     */
    @Override
    public boolean isResponse() {
        return true;
    }

    // HeaderValue........................................................................................

    /**
     * <pre>
     * Content-Range: bytes 200-1000/67589
     * </pre>
     */
    @Override
    public String toHeaderText() {
        return this.unit.toHeaderText() + " " +
                toHeaderTextRange(this.range) +
                RANGE_SIZE_SEPARATOR +
                toHeaderTextValue(this.size);
    }

    private static String toHeaderTextRange(final Optional<Range<Long>> range) {
        return range.isPresent() ?
                toHeaderTextRange0(range.get()) :
                WILDCARD.string();

    }

    private static String toHeaderTextRange0(final Range<Long> range) {
        return toHeaderTextBound(range.lowerBound()) +
                BETWEEN +
                toHeaderTextBound(range.upperBound());

    }

    private static String toHeaderTextBound(final RangeBound<Long> value) {
        return String.valueOf(value.value().get());
    }

    private static String toHeaderTextValue(final Optional<Long> value) {
        return value.isPresent() ?
                String.valueOf(value.get()) :
                WILDCARD.string();
    }

    // Object.........................................................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(this.unit, this.range, this.size);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof ContentRange &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final ContentRange other) {
        return this.unit.equals(other.unit) &&
                this.range.equals(other.range) &&
                this.size.equals(other.size);
    }

    @Override
    public final String toString() {
        return this.toHeaderText();
    }
}
