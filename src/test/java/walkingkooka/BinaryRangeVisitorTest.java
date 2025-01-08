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

package walkingkooka;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.Range;
import walkingkooka.collect.RangeVisitorTesting;
import walkingkooka.reflect.JavaVisibility;

public final class BinaryRangeVisitorTest implements RangeVisitorTesting<BinaryRangeVisitor, Long>,
    ToStringTesting<BinaryRangeVisitor> {

    @Test
    public void testExtractAll() {
        final Binary binary = Binary.with(new byte[]{0, 1, 2, 3});
        this.extractAndCheck(binary, Range.all(), binary);
    }

    @Test
    public void testExtractSingleton() {
        this.extractAndCheck(Binary.with(new byte[]{0, 1, 2, 3}),
            Range.singleton(2L),
            Binary.with(new byte[]{2}));
    }

    @Test
    public void testExtractBeforeEquals() {
        this.extractAndCheck(Binary.with(new byte[]{0, 1, 2, 3}),
            Range.lessThanEquals(2L),
            Binary.with(new byte[]{0, 1, 2}));
    }

    @Test
    public void testExtractBefore() {
        this.extractAndCheck(Binary.with(new byte[]{0, 1, 2, 3}),
            Range.lessThan(2L),
            Binary.with(new byte[]{0, 1}));
    }

    @Test
    public void testExtractAfterEquals() {
        this.extractAndCheck(Binary.with(new byte[]{0, 1, 2, 3, 4}),
            Range.greaterThanEquals(2L),
            Binary.with(new byte[]{2, 3, 4}));
    }

    @Test
    public void testExtractAfter() {
        this.extractAndCheck(Binary.with(new byte[]{0, 1, 2, 3, 4}),
            Range.greaterThan(2L),
            Binary.with(new byte[]{3, 4}));
    }

    @Test
    public void testExtractRange() {
        this.extractAndCheck(Binary.with(new byte[]{0, 1, 2, 3, 4, 5}),
            Range.greaterThanEquals(2L).and(Range.lessThanEquals(4L)),
            Binary.with(new byte[]{2, 3, 4}));
    }

    private void extractAndCheck(final Binary binary,
                                 final Range<Long> range,
                                 final Binary expected) {
        this.checkEquals(expected,
            BinaryRangeVisitor.extract(binary, range),
            () -> binary + " range: " + range);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createVisitor(), this.range().toString());
    }

    @Override
    public BinaryRangeVisitor createVisitor() {
        return new BinaryRangeVisitor(this.range());
    }

    private Range<Long> range() {
        return Range.greaterThanEquals(12L).and(Range.lessThanEquals(34L));
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return Binary.class.getSimpleName();
    }

    @Override
    public Class<BinaryRangeVisitor> type() {
        return BinaryRangeVisitor.class;
    }
}
