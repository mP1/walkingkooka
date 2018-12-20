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

import org.junit.Test;
import walkingkooka.InvalidCharacterException;
import walkingkooka.compare.Range;
import walkingkooka.text.CharSequences;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public final class ContentRangeTest extends HeaderValueTestCase<ContentRange> {

    private final static RangeHeaderValueUnit UNIT = RangeHeaderValueUnit.BYTES;
    private final static Optional<Long> SIZE = Optional.of(789L);

    // with.

    @Test(expected = NullPointerException.class)
    public void testWithNullUnitFails() {
        ContentRange.with(null, range(), SIZE);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullRangeFails() {
        ContentRange.with(UNIT, null, SIZE);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullSizeFails() {
        ContentRange.with(UNIT, this.range(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNegativeRangeLowerFails() {
        ContentRange.with(UNIT, this.range(-1, 123), SIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithRangeLowerExclusiveFails() {
        ContentRange.with(UNIT, Optional.of(Range.greaterThan(123L)), SIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithRangeUpperBeforeRangeLowerFails() {
        ContentRange.with(UNIT, this.range(10, 9), SIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithRangeUpperExclusiveFails() {
        ContentRange.with(UNIT,
                Optional.of(Range.greaterThanEquals(123L).and(Range.lessThan(456L))),
                SIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNegativeSizeFails() {
        ContentRange.with(UNIT, this.range(), Optional.of(-1L));
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

    @Test(expected = NullPointerException.class)
    public void testSetUnitNullFails() {
        this.contentRange().setUnit(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnitNoneFails() {
        this.contentRange().setUnit(RangeHeaderValueUnit.NONE);
    }

    @Test
    public void testSetUnitSame() {
        final ContentRange contentRange = this.contentRange();
        assertSame(contentRange, contentRange.setUnit(UNIT));
    }

    // setRange.....................................................................................................

    @Test(expected = NullPointerException.class)
    public void testSetRangeNullFails() {
        this.contentRange().setRange(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRangeNegativeLowerBoundsFails() {
        this.contentRange().setRange(this.range(-1, 123));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRangeUpperLessThanLowerBoundsFails() {
        this.contentRange().setRange(this.range(10, 9));
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

    @Test(expected = NullPointerException.class)
    public void testSetSizeNullFails() {
        this.contentRange().setSize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSizeNegativeSizeFails() {
        this.contentRange().setSize(Optional.of(-1L));
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
        assertEquals("unit", unit, contentRange.unit());
        assertEquals("range", range, contentRange.range());
        assertEquals("size", size, contentRange.size());
    }

    // parse ......................................................................................................

    @Test(expected = NullPointerException.class)
    public void testParseNullFails() {
        ContentRange.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyFails() {
        ContentRange.parse("");
    }

    @Test
    public void testParseWhitespaceEqualsFails() {
        this.parseFails("bytes-0-100/*", '-');
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
        try {
            ContentRange.parse(text);
            fail(message);
        } catch (final HeaderValueException expected) {
            assertEquals("message", message, expected.getMessage());
        }
    }

    private void parseAndCheck(final String headerValue,
                               final RangeHeaderValueUnit unit,
                               final Optional<Range<Long>> range,
                               final Optional<Long> size) {
        assertEquals("Incorrect result when parsing " + CharSequences.quote(headerValue),
                ContentRange.with(unit, range, size),
                ContentRange.parse(headerValue));
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
        this.toStringAndCheck("bytes */*",
                UNIT,
                ContentRange.NO_RANGE,
                ContentRange.NO_SIZE);
    }

    @Test
    public void testToStringTextMissingFilesize() {
        this.toStringAndCheck("bytes 123-456/*",
                UNIT,
                this.range(),
                ContentRange.NO_SIZE);
    }

    @Test
    public void testToStringTextWithFilesize() {
        this.toStringAndCheck("bytes 123-456/789",
                UNIT,
                this.range(),
                SIZE);
    }

    private final void toStringAndCheck(final String toString,
                                        final RangeHeaderValueUnit unit,
                                        final Optional<Range<Long>> range,
                                        final Optional<Long> size) {
        assertEquals("toString",
                toString,
                ContentRange.with(unit, range, size).toString());
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

    @Override
    protected ContentRange createHeaderValue() {
        return ContentRange.with(UNIT, this.range(), SIZE);
    }

    @Override
    protected boolean isMultipart() {
        return false;
    }

    @Override
    protected boolean isRequest() {
        return false;
    }

    @Override
    protected boolean isResponse() {
        return true;
    }

    @Override
    protected Class<ContentRange> type() {
        return ContentRange.class;
    }
}
