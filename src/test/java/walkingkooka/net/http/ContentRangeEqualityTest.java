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
import walkingkooka.compare.Range;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

import java.util.Optional;

public final class ContentRangeEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<ContentRange> {

    private final static HttpRangeUnit UNIT = HttpRangeUnit.BYTES;
    private final static Optional<Long> SIZE = Optional.of(123L);

    @Test
    public void testDifferentRange() {
        this.checkNotEquals(this.range(UNIT,
                this.range(456, 789),
                SIZE));
    }

    @Test
    public void testDifferentRange2() {
        this.checkNotEquals(this.range(UNIT,
                ContentRange.NO_RANGE,
                SIZE));
    }

    @Test
    public void testDifferentSize() {
        this.checkNotEquals(this.range(UNIT,
                this.range(),
                ContentRange.NO_SIZE));
    }

    @Test
    public void testDifferentSize2() {
        this.checkNotEquals(this.range(UNIT,
                range(),
                Optional.of(456L)));
    }

    @Override
    protected ContentRange createObject() {
        return this.range(UNIT, this.range(), SIZE);
    }

    private Optional<Range<Long>> range() {
        return this.range(123, 456);
    }

    private Optional<Range<Long>> range(final long lower, final long upper) {
        return Optional.of(Range.greaterThanEquals(lower).and(Range.lessThanEquals(upper)));
    }

    private final ContentRange range(final HttpRangeUnit unit,
                                     final Optional<Range<Long>> range,
                                     final Optional<Long> size) {
        return ContentRange.with(unit, range, size);
    }
}
