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

import org.junit.jupiter.api.Test;
import walkingkooka.InvalidCharacterException;
import walkingkooka.compare.Range;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ContentRangeTest extends HeaderValueTestCase<ContentRange> implements ParseStringTesting<ContentRange> {

    private final static RangeHeaderValueUnit UNIT = RangeHeaderValueUnit.BYTES;
    private final static Optional<Long> SIZE = Optional.of(789L);

    // with.

    @Test
    public void testWithNullUnitFails() {
        assertThrows(NullPointerException.class, () -> {
            ContentRange.with(null, range(), SIZE);
        });
    }

    @Test
    public void testWithNullRangeFails() {
        assertThrows(NullPointerException.class, () -> {
            ContentRange.with(UNIT, null, SIZE);
        });
    }

    @Test
    public void testWithNullSizeFails() {
        assertThrows(NullPointerException.class, () -> {
            ContentRange.with(UNIT, this.range(), null);
        });
    }

    @Test
    public void testWithNegativeRangeLowerFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ContentRange.with(UNIT, this.range(-1, 123), SIZE);
        });
    }

    @Test
    public void testWithRangeLowerExclusiveFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ContentRange.with(UNIT, Optional.of(Range.greaterThan(123L)), SIZE);
        });
    }

    @Test
    public void testWithRangeUpperBeforeRangeLowerFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ContentRange.with(UNIT, this.range(10, 9), SIZE);
        });
    }

    @Test
    public void testWithRangeUpperExclusiveFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ContentRange.with(UNIT,
                    Optional.of(Range.greaterThanEquals(123L).and(Range.lessThan(456L))),
                    SIZE);
        });
    }

    @Test
    public void testWithNegativeSizeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ContentRange.with(UNIT, this.range(), Optional.of(-1L));
        });
    }

    @Test
    public void testWith() {
        this.check(this.contentRange(), UNIT, this.range(), SIZE);
    }

    @Test
    public void testWithNoRange() {
        final Optional<Range<Long>> range = ContentRange.NO_RANGE;
        final ContentRange contentRange = ContentRange.with(UNIT, range, SIZE);
        this.check(contentRange, UNIT, range, SIZE);
    }

    @Test
    public void testWithNoSize() {
        final ContentRange contentRange = ContentRange.with(UNIT, this.range(), ContentRange.NO_SIZE);
        this.check(contentRange, UNIT, this.range(), ContentRange.NO_SIZE);
    }

    // setUnit.....................................................................................................

    @Test
    public void testSetUnitNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.contentRange().setUnit(null);
        });
    }

    @Test
    public void testSetUnitNoneFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.contentRange().setUnit(RangeHeaderValueUnit.NONE);
        });
    }

    @Test
    public void testSetUnitSame() {
        final ContentRange contentRange = this.contentRange();
        assertSame(contentRange, contentRange.setUnit(UNIT));
    }

    // setRange.....................................................................................................

    @Test
    public void testSetRangeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.contentRange().setRange(null);
        });
    }

    @Test
    public void testSetRangeNegativeLowerBoundsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.contentRange().setRange(this.range(-1, 123));
        });
    }

    @Test
    public void testSetRangeUpperLessThanLowerBoundsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.contentRange().setRange(this.range(10, 9));
        });
    }

    @Test
    public void testSetRangeSame() {
        final ContentRange contentRange = this.contentRange();
        assertSame(contentRange, contentRange.setRange(this.range()));
    }

    @Test
    public void testSetRangeDifferent() {
        this.setRangeDifferentAndCheck(this.range(999, 1000));
    }

    @Test
    public void testSetRangeDifferentNoRange() {
        this.setRangeDifferentAndCheck(ContentRange.NO_RANGE);
    }

    private void setRangeDifferentAndCheck(final Optional<Range<Long>> range) {
        final ContentRange contentRange = this.contentRange();

        final ContentRange different = contentRange.setRange(range);

        this.check(different, UNIT, range, SIZE);
        this.check(contentRange);
    }

    // setSize.....................................................................................................

    @Test
    public void testSetSizeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.contentRange().setSize(null);
        });
    }

    @Test
    public void testSetSizeNegativeSizeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.contentRange().setSize(Optional.of(-1L));
        });
    }

    @Test
    public void testSetSizeSame() {
        final ContentRange contentRange = this.contentRange();
        assertSame(contentRange, contentRange.setSize(SIZE));
    }

    @Test
    public void testSetSizeDifferent() {
        final ContentRange contentRange = this.contentRange();

        final Optional<Long> size = Optional.of(456L);
        final ContentRange different = contentRange.setSize(size);

        this.check(different, UNIT, range(), size);
        this.check(contentRange);
    }

    @Test
    public void testSetSizeDifferentNoSize() {
        final ContentRange contentRange = this.contentRange();

        final Optional<Long> size = ContentRange.NO_SIZE;
        final ContentRange different = contentRange.setSize(size);

        this.check(different, UNIT, range(), size);
        this.check(contentRange);
    }

    // check ..................................................................................................

    private void check(final ContentRange contentRange) {
        this.check(contentRange, UNIT, this.range(), SIZE);
    }

    private void check(final ContentRange contentRange,
                       final RangeHeaderValueUnit unit,
                       final Optional<Range<Long>> range,
                       final Optional<Long> size) {
        assertEquals(unit, contentRange.unit(), "unit");
        assertEquals(range, contentRange.range(), "range");
        assertEquals(size, contentRange.size(), "size");
    }

    // check ..................................................................................................

    @Test
    public void testIsWildcard() {
        this.isWildcardAndCheck(false);
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testEqualsDifferentRange() {
        this.checkNotEquals(this.range(UNIT,
                this.range(456, 789),
                SIZE));
    }

    @Test
    public void testEqualsDifferentRange2() {
        this.checkNotEquals(this.range(UNIT,
                ContentRange.NO_RANGE,
                SIZE));
    }

    @Test
    public void testEqualsDifferentSize() {
        this.checkNotEquals(this.range(UNIT,
                this.range(),
                ContentRange.NO_SIZE));
    }

    @Test
    public void testEqualsDifferentSize2() {
        this.checkNotEquals(this.range(UNIT,
                range(),
                Optional.of(456L)));
    }

    // parse ......................................................................................................

    @Test
    public void testParseNullFails() {
        assertThrows(NullPointerException.class, () -> {
            ContentRange.parse(null);
        });
    }

    @Test
    public void testParseEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ContentRange.parse("");
        });
    }

    @Test
    public void testParseUnitUnknownFails() {
        this.parseFails("b", "Missing unit from \"b\"");
    }

    @Test
    public void testParseUnitInvalidFails() {
        this.parseFails("bytes-0-100/*", '-');
    }

    @Test
    public void testParseUnitLowerBoundInvalidFails() {
        this.parseFails("bytes 0/", '/');
    }

    @Test
    public void testParseInvalidLowerBoundCharacterFails() {
        this.parseFails("bytes ?0-100/*", '?');
    }

    @Test
    public void testParseInvalidLowerBoundCharacterFails2() {
        this.parseFails("bytes 1?0-100/*", '?');
    }

    @Test
    public void testParseMissingRangeSeparatorFails() {
        this.parseFails("bytes 100?200/*", '?');
    }

    @Test
    public void testParseInvalidUpperBoundCharacterFails() {
        this.parseFails("bytes 100-?00/*", '?');
    }

    @Test
    public void testParseInvalidUpperBoundCharacterFails2() {
        this.parseFails("bytes 100-2?00/*", '?');
    }

    @Test
    public void testParseInvalidSizeCharacterFails() {
        this.parseFails("bytes 100-200/?*", '?');
    }

    @Test
    public void testParseInvalidSizeCharacterFails2() {
        this.parseFails("bytes 100-200/1?", '?');
    }

    @Test
    public void testParseInvalidSizeCharacterFails3() {
        this.parseFails("bytes 100-200/1*", '*');
    }

    @Test
    public void testParseMissingRange() {
        this.parseAndCheck("bytes */*",
                UNIT,
                ContentRange.NO_RANGE,
                ContentRange.NO_SIZE);
    }

    @Test
    public void testParseMissingRangeWithFileSize() {
        this.parseAndCheck("bytes */789",
                UNIT,
                ContentRange.NO_RANGE,
                SIZE);
    }

    @Test
    public void testParseSizeMissing() {
        this.parseAndCheck("bytes 123-456/*",
                UNIT,
                this.range(),
                ContentRange.NO_SIZE);
    }

    @Test
    public void testParseSizePresent() {
        this.parseAndCheck("bytes 123-456/789",
                UNIT,
                this.range(),
                SIZE);
    }

    @Test
    public void testParseSmallRange() {
        this.parseAndCheck("bytes 1-2/3",
                UNIT,
                this.range(1, 2),
                Optional.of(3L));
    }

    @Test
    public void testParseTab() {
        this.parseAndCheck("bytes\t123-456/789",
                UNIT,
                this.range(),
                SIZE);
    }

    private void parseFails(final String text, final char invalid) {
        this.parseFails(text,
                new InvalidCharacterException(text, text.indexOf(invalid)).getMessage());
    }

    private void parseFails(final String text, final String message) {
        final HeaderValueException expected = assertThrows(HeaderValueException.class, () -> {
            ContentRange.parse(text);
        });
        assertEquals(message, expected.getMessage(), "message");
    }

    private void parseAndCheck(final String headerValue,
                               final RangeHeaderValueUnit unit,
                               final Optional<Range<Long>> range,
                               final Optional<Long> size) {
        assertEquals(ContentRange.with(unit, range, size),
                ContentRange.parse(headerValue),
                "Incorrect result when parsing " + CharSequences.quote(headerValue));
    }

    // toHeaderText.................................................................................................

    @Test
    public void testToHeaderTextNoRange() {
        this.toHeaderTextAndCheck("bytes */*",
                UNIT,
                ContentRange.NO_RANGE,
                ContentRange.NO_SIZE);
    }


    @Test
    public void testToHeaderTextMissingRange() {
        this.toHeaderTextAndCheck("bytes */789",
                UNIT,
                ContentRange.NO_RANGE,
                SIZE);
    }

    @Test
    public void testToHeaderTextMissingFilesize() {
        this.toHeaderTextAndCheck("bytes 123-456/*",
                UNIT,
                this.range(),
                ContentRange.NO_SIZE);
    }

    @Test
    public void testToHeaderTextWithFilesize() {
        this.toHeaderTextAndCheck("bytes 123-456/789",
                UNIT,
                this.range(),
                SIZE);
    }

    private final void toHeaderTextAndCheck(final String headerText,
                                            final RangeHeaderValueUnit unit,
                                            final Optional<Range<Long>> range,
                                            final Optional<Long> size) {
        this.toHeaderTextAndCheck(
                ContentRange.with(unit, range, size),
                headerText);
    }

    // toString.................................................................................................

    @Test
    public void testToStringTextNoRange() {
        this.toStringAndCheck(UNIT, ContentRange.NO_RANGE, ContentRange.NO_SIZE, "bytes */*"
        );
    }

    @Test
    public void testToStringTextMissingFilesize() {
        this.toStringAndCheck(UNIT, this.range(), ContentRange.NO_SIZE, "bytes 123-456/*"
        );
    }

    @Test
    public void testToStringTextWithFilesize() {
        this.toStringAndCheck(UNIT, this.range(), SIZE, "bytes 123-456/789"
        );
    }

    private final void toStringAndCheck(final RangeHeaderValueUnit unit,
                                        final Optional<Range<Long>> range,
                                        final Optional<Long> size,
                                        final String toString) {
        this.toStringAndCheck(ContentRange.with(unit, range, size).toString(),
                toString);
    }

    private ContentRange contentRange() {
        return ContentRange.with(UNIT, this.range(), SIZE);
    }

    private Optional<Range<Long>> range() {
        return range(123, 456);
    }

    private Optional<Range<Long>> range(final long lower, final long upper) {
        return Optional.of(Range.greaterThanEquals(lower).and(Range.lessThanEquals(upper)));
    }

    private final ContentRange range(final RangeHeaderValueUnit unit,
                                     final Optional<Range<Long>> range,
                                     final Optional<Long> size) {
        return ContentRange.with(unit, range, size);
    }

    @Override
    public ContentRange createHeaderValue() {
        return ContentRange.with(UNIT, this.range(), SIZE);
    }

    @Override
    public boolean isMultipart() {
        return true;
    }

    @Override
    public boolean isRequest() {
        return false;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    @Override
    public Class<ContentRange> type() {
        return ContentRange.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    // ParseStringTesting ........................................................................................

    @Override
    public ContentRange parse(final String text) {
        return ContentRange.parse(text);
    }
}
