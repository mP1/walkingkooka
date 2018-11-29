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

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.compare.Range;
import walkingkooka.net.header.HeaderValueTestCase;
import walkingkooka.text.CharSequences;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class HttpHeaderRangeTest extends HeaderValueTestCase<HttpHeaderRange> {

    private final static String UNIT = "bytes";

    // with.......................................................

    @Test(expected = NullPointerException.class)
    public void testWithNullUnitFails() {
        HttpHeaderRange.with(null, ranges());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyUnitFails() {
        HttpHeaderRange.with("", ranges());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullRangesFails() {
        HttpHeaderRange.with(UNIT, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyRangesFails() {
        HttpHeaderRange.with(UNIT, Lists.empty());
    }

    public void testWith() {
        final HttpHeaderRange range = this.range();
        this.check(range, UNIT, this.ranges());
    }

    // setUnit.......................................................

    @Test(expected = NullPointerException.class)
    public void testSetUnitNullFails() {
        this.range().setUnit(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnitEmptyFails() {
        this.range().setUnit("");
    }

    @Test
    public void testSetUnitSame() {
        final HttpHeaderRange range = this.range();
        assertSame(range, range.setUnit(UNIT));
    }

    @Test
    public void testSetUnitDifferent() {
        final HttpHeaderRange range = this.range();
        final String unit = "different";
        final HttpHeaderRange different = range.setUnit(unit);
        this.check(different, unit, this.ranges());
    }

    // setValue.......................................................

    @Test(expected = NullPointerException.class)
    public void testSetValueNullFails() {
        this.range().setValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueEmptyFails() {
        this.range().setValue(Lists.empty());
    }

    @Test
    public void testSetValueSame() {
        final HttpHeaderRange range = this.range();
        assertSame(range, range.setValue(this.ranges()));
    }

    @Test
    public void testSetValueDifferent() {
        final HttpHeaderRange range = this.range();
        final List<Range<Long>> value = Lists.of(rangeLte1000());
        final HttpHeaderRange different = range.setValue(value);
        this.check(different, UNIT, value);
    }

    private void check(final HttpHeaderRange range, final String unit, final List<Range<Long>> values) {
        assertEquals("unit", unit, range.unit());
        assertEquals("value", values, range.value());
    }

    // parse ...........................................................

    @Test
    public void testParseOpenRange() {
        this.parseAndCheck("bytes=123-",
                UNIT,
                this.rangeGte123());
    }

    @Test
    public void testParseClosedRange() {
        this.parseAndCheck("bytes=123-456",
                UNIT,
                this.rangeGte123().and(this.rangeLte456()));
    }

    @Test
    public void testParseClosedCommaRangeOpen() {
        this.parseAndCheck("bytes=123-456,789-",
                UNIT,
                this.rangeGte123().and(this.rangeLte456()),
                this.rangeGte789());
    }

    @Test
    public void testParseClosedCommaWhitespaceRangeOpen() {
        this.parseAndCheck("bytes=123-456, 789-",
                UNIT,
                this.rangeGte123().and(this.rangeLte456()),
                this.rangeGte789());
    }

    @Test
    public void testParseClosedCommaWhitespaceRangeOpen2() {
        this.parseAndCheck("bytes=123-456,  789-",
                UNIT,
                this.rangeGte123().and(this.rangeLte456()),
                this.rangeGte789());
    }

    @Test
    public void testParseClosedCommaRangeClosed() {
        this.parseAndCheck("bytes=123-456,789-1000",
                UNIT,
                this.rangeGte123().and(this.rangeLte456()),
                this.rangeGte789().and(this.rangeLte1000()));
    }

    @SafeVarargs
    private final void parseAndCheck(final String headerValue, final String unit, final Range<Long>... values) {
        assertEquals("Incorrect result when  parsing " + CharSequences.quote(headerValue),
                HttpHeaderRange.with(unit, Lists.of(values)),
                HttpHeaderRange.parse(headerValue));
    }

    // toString.......................................................

    @Test
    public void testToStringOpenRange() {
        toStringAndCheck("bytes=123-",
                UNIT,
                this.rangeGte123());
    }

    @Test
    public void testToStringClosedRange() {
        toStringAndCheck("bytes=123-456",
                UNIT,
                this.rangeGte123().and(this.rangeLte456()));
    }

    @Test
    public void testToStringClosedRange2() {
        toStringAndCheck("kb=123-456",
                "kb",
                this.rangeGte123().and(this.rangeLte456()));
    }

    @Test
    public void testToStringClosedRangeOpenRange() {
        toStringAndCheck("bytes=123-456, 789-",
                UNIT,
                this.rangeGte123().and(this.rangeLte456()),
                this.rangeGte789());
    }

    @Test
    public void testToStringClosedRangeClosedRange() {
        toStringAndCheck("bytes=123-456, 789-1000",
                UNIT,
                this.rangeGte123().and(this.rangeLte456()),
                this.rangeGte789().and(this.rangeLte1000()));
    }

    @SafeVarargs
    private final void toStringAndCheck(final String toString, final String unit, final Range<Long>... ranges) {
        final HttpHeaderRange range = this.range(unit, ranges);
        assertEquals(toString, range.toString());
        assertEquals(toString, range.headerValue());
    }

    private HttpHeaderRange range() {
        return this.range(UNIT, this.rangeGte123());
    }

    @SafeVarargs
    private final HttpHeaderRange range(final String unit, final Range<Long>... ranges) {
        return HttpHeaderRange.with(unit, Lists.of(ranges));
    }

    private List<Range<Long>> ranges() {
        return Lists.of(this.rangeGte123());
    }

    private Range<Long> rangeGte123() {
        return Range.greaterThanEquals(123L);
    }

    private Range<Long> rangeLte456() {
        return Range.lessThanEquals(456L);
    }

    private Range<Long> rangeGte789() {
        return Range.greaterThanEquals(789L);
    }

    private Range<Long> rangeLte1000() {
        return Range.lessThanEquals(1000L);
    }

    @Override
    protected Class<HttpHeaderRange> type() {
        return HttpHeaderRange.class;
    }
}
