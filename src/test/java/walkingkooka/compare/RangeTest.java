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

package walkingkooka.compare;

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.predicate.PredicateTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class RangeTest extends PredicateTestCase<Range<Integer>, Integer> {

    private final static Integer BELOW_LOWER_VALUE = 1000;
    private final static Integer LOWER_VALUE = 2000;
    private final static Integer ABOVE_LOWER_VALUE = 3000;
    private final static Integer BELOW_UPPER_VALUE = 4000;
    private final static Integer UPPER_VALUE = 5000;
    private final static Integer ABOVE_UPPER_VALUE = 6000;

    // parameter checks................................................

    @Test(expected = NullPointerException.class)
    public void testAndNullFails() {
        this.createPredicate().and(null);
    }

    // all ..............................................................................................

    @Test
    public void testAll() {
        this.check(Range.all(), RangeBound.all(), RangeBound.all());
    }

    @Test
    public void testAllTest() {
        this.testTrue(LOWER_VALUE);
    }

    @Test
    public void testAllTest2() {
        this.testTrue(LOWER_VALUE + 1);
    }

    // singleton ....................................................................................................

    @Test(expected = NullPointerException.class)
    public void testSingletonNullValueFails() {
        Range.singleton(null);
    }

    @Test
    public void testSingletonTestValueLess() {
        this.testFalse(singleton(), LOWER_VALUE - 1);
    }

    @Test
    public void testSingletonTestValueEqual() {
        this.testTrue(singleton(), LOWER_VALUE);
    }

    @Test
    public void testSingletonTestValueGreater() {
        this.testFalse(singleton(), LOWER_VALUE + 1);
    }

    @Test
    public void testSingletonToString() {
        assertEquals("" + LOWER_VALUE, singleton().toString());
    }

    private static Range<Integer> singleton() {
        return Range.singleton(LOWER_VALUE);
    }

    // lessThan ....................................................................................................

    @Test
    public void testLessThan() {
        final int value = 123;
        this.check(Range.lessThan(value), RangeBound.all(), RangeBound.exclusive(value));
    }

    @Test(expected = NullPointerException.class)
    public void testLessThanNullValueFails() {
        Range.lessThan(null);
    }

    @Test
    public void testLessThanTestValueLess() {
        this.testTrue(lessThan(), LOWER_VALUE - 1);
    }

    @Test
    public void testLessThanTestValueEqual() {
        this.testFalse(lessThan(), LOWER_VALUE);
    }

    @Test
    public void testLessThanTestValueGreater() {
        this.testFalse(lessThan(), LOWER_VALUE + 1);
    }

    @Test
    public void testLessThanToString() {
        assertEquals("<" + LOWER_VALUE, lessThan().toString());
    }

    private Range<Integer> lessThan() {
        return Range.lessThan(LOWER_VALUE);
    }

    // lessThanEquals ....................................................................................................

    @Test
    public void testLessThanEquals() {
        final int value = 123;
        this.check(Range.lessThanEquals(value), RangeBound.all(), RangeBound.inclusive(value));
    }

    @Test(expected = NullPointerException.class)
    public void testLessThanEqualsNullValueFails() {
        Range.lessThanEquals(null);
    }

    @Test
    public void testLessThanEqualsTestValueLess() {
        this.testTrue(lessThanEquals(), LOWER_VALUE - 1);
    }

    @Test
    public void testLessThanEqualsTestValueEqual() {
        this.testTrue(lessThanEquals(), LOWER_VALUE);
    }

    @Test
    public void testLessThanEqualsTestValueGreater() {
        this.testFalse(lessThanEquals(), LOWER_VALUE + 1);
    }

    @Test
    public void testLessThanEqualsToString() {
        assertEquals("<=" + LOWER_VALUE, lessThanEquals().toString());
    }

    private static Range<Integer> lessThanEquals() {
        return Range.lessThanEquals(LOWER_VALUE);
    }

    // greaterThan ....................................................................................................

    @Test
    public void testGreaterThan() {
        final int value = 123;
        this.check(Range.greaterThan(value), RangeBound.exclusive(value), RangeBound.all());
    }

    @Test(expected = NullPointerException.class)
    public void testGreaterThanNullValueFails() {
        Range.greaterThan(null);
    }

    @Test
    public void testGreaterThanTestValueLess() {
        this.testFalse(greaterThan(), LOWER_VALUE - 1);
    }

    @Test
    public void testGreaterThanTestValueEqual() {
        this.testFalse(greaterThan(), LOWER_VALUE);
    }

    @Test
    public void testGreaterThanTestValueGreater() {
        this.testTrue(greaterThan(), LOWER_VALUE + 1);
    }

    @Test
    public void testGreaterThanToString() {
        assertEquals(">" + LOWER_VALUE, greaterThan().toString());
    }

    private static Range<Integer> greaterThan() {
        return Range.greaterThan(LOWER_VALUE);
    }

    // greaterThanEquals ....................................................................................................

    @Test
    public void testGreaterThanEquals() {
        final int value = 123;
        this.check(Range.greaterThanEquals(value), RangeBound.inclusive(value), RangeBound.all());
    }

    @Test(expected = NullPointerException.class)
    public void testGreaterThanEqualsNullValueFails() {
        Range.greaterThanEquals(null);
    }

    @Test
    public void testGreaterThanEqualsTestValueLess() {
        this.testFalse(greaterThanEquals(), LOWER_VALUE - 1);
    }

    @Test
    public void testGreaterThanEqualsTestValueEqual() {
        this.testTrue(greaterThanEquals(), LOWER_VALUE);
    }

    @Test
    public void testGreaterThanEqualsTestValueGreater() {
        this.testTrue(greaterThanEquals(), LOWER_VALUE + 1);
    }

    @Test
    public void testGreaterThanEqualsToString() {
        assertEquals(">=" + LOWER_VALUE, greaterThanEquals().toString());
    }

    private static Range<Integer> greaterThanEquals() {
        return Range.greaterThanEquals(LOWER_VALUE);
    }

    // and with all..........................................................................................

    @Test
    public void testAndAllAndAll() {
        andAllAndCheck(all());
    }

    @Test
    public void testAndSingletonAllAll() {
        andAllAndCheck(singleton());
    }

    @Test
    public void testAndLessThanAllAll() {
        andAllAndCheck(lessThan());
    }

    @Test
    public void testAndLessThanEqualsAllAll() {
        andAllAndCheck(lessThanEquals());
    }

    @Test
    public void testAndGreaterThanAllAll() {
        andAllAndCheck(greaterThan());
    }

    @Test
    public void testAndGreaterThanEqualsAllAll() {
        andAllAndCheck(greaterThanEquals());
    }

    private void andAllAndCheck(final Range<Integer> range) {
        final Range<Integer> all = all();
        assertSame(range + " and with " + all, range, range.and(all));
        assertSame(range + " and with " + all, range, all.and(range));
    }

    // and with all..........................................................................................

    @Test
    public void testAndSingletonAndAll() {
        andSingletonAndCheck(all());
    }

    @Test
    public void testAndSingletonAndSingleton() {
        final Range<Integer> singleton = singleton();
        final Range<Integer> singleton2 = singleton();
        assertSame(singleton + " and with " + singleton2, singleton, singleton.and(singleton2));
        assertSame(singleton2 + " and with " + singleton, singleton2, singleton2.and(singleton));
    }

    @Test
    public void testAndSingletonAndLessThan() {
        andSingletonAndCheck(lessThan());
    }

    @Test
    public void testAndSingletonAndLessThanEquals() {
        andSingletonAndCheck(lessThanEquals());
    }

    @Test
    public void testAndSingletonAndGreaterThan() {
        andSingletonAndCheck(greaterThan());
    }

    @Test
    public void testAndSingletonAndGreaterThanEquals() {
        andSingletonAndCheck(greaterThanEquals());
    }

    private void andSingletonAndCheck(final Range<Integer> range) {
        final Range<Integer> singleton = singleton();
        assertSame(range + " and with " + singleton, singleton, range.and(singleton));
        assertSame(singleton + " and with " + range, singleton, singleton.and(range));
    }

    // and invalid.......................................................

    @Test(expected = IllegalArgumentException.class)
    public void testAndLessThanAndGreaterThanFails() {
        lessThan().and(greaterThan());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAndGreaterThanAndLessThanFails() {
        greaterThan().and(lessThan());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAndLessThanAndGreaterThanFails2() {
        Range.lessThan(122).and(Range.greaterThan(123));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAndLessThanEqualAndGreaterThanFails2() {
        Range.lessThanEquals(122).and(Range.greaterThan(123));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAndLessThanEqualAndSingletonFails2() {
        Range.lessThanEquals(122).and(Range.singleton(123));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAndSingletonAndGreaterThanFails2() {
        Range.singleton(122).and(Range.greaterThan(123));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAndSingletonAndGreaterThanEqualsFails2() {
        Range.singleton(122).and(Range.greaterThanEquals(123));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAndNonOverlappingBoundedRangeBoundedRangeFails() {
        Range.greaterThan(222).and(Range.lessThan(333))
                .and(Range.greaterThan(888).and(Range.lessThan(999)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAndNonOverlappingBoundedRangeBoundedRangeFails2() {
        Range.greaterThan(222).and(Range.lessThan(333))
                .and(Range.greaterThan(333).and(Range.lessThan(999)));
    }

    // and gt lt ......................................................................

    @Test
    public void testGreaterThanLessThan_Less() {
        this.testFalse(greaterThanLessThan(), LOWER_VALUE - 1);
    }

    @Test
    public void testGreaterThanLessThan_LowerValue() {
        this.testFalse(greaterThanLessThan(), LOWER_VALUE);
    }

    @Test
    public void testGreaterThanLessThan_LowerAfter() {
        this.testTrue(greaterThanLessThan(), LOWER_VALUE + 1);
    }

    @Test
    public void testGreaterThanLessThan_UpperBefore() {
        this.testTrue(greaterThanLessThan(), UPPER_VALUE - 1);
    }

    @Test
    public void testGreaterThanLessThan_UpperValue() {
        this.testFalse(greaterThanLessThan(), UPPER_VALUE);
    }

    @Test
    public void testGreaterThanLessThan_Above() {
        this.testFalse(greaterThanLessThan(), UPPER_VALUE + 1);
    }

    @Test
    public void testGreaterThanLessThanToString() {
        assertEquals("[" + LOWER_VALUE + ".." + UPPER_VALUE + "]", greaterThanLessThan().toString());
    }

    private Range<Integer> greaterThanLessThan() {
        return Range.greaterThan(LOWER_VALUE).and(Range.lessThan(UPPER_VALUE));
    }

    // and gte lt ......................................................................

    @Test
    public void testGreaterThanEqualsLessThan_Less() {
        this.testFalse(greaterThanEqualsLessThan(), LOWER_VALUE - 1);
    }

    @Test
    public void testGreaterThanEqualsLessThan_LowerValue() {
        this.testTrue(greaterThanEqualsLessThan(), LOWER_VALUE);
    }

    @Test
    public void testGreaterThanEqualsLessThan_LowerAfter() {
        this.testTrue(greaterThanEqualsLessThan(), LOWER_VALUE + 1);
    }

    @Test
    public void testGreaterThanEqualsLessThan_UpperBefore() {
        this.testTrue(greaterThanEqualsLessThan(), UPPER_VALUE - 1);
    }

    @Test
    public void testGreaterThanEqualsLessThan_UpperValue() {
        this.testFalse(greaterThanEqualsLessThan(), UPPER_VALUE);
    }

    @Test
    public void testGreaterThanEqualsLessThan_Above() {
        this.testFalse(greaterThanEqualsLessThan(), UPPER_VALUE + 1);
    }

    @Test
    public void testGreaterThanEqualsLessThanToString() {
        assertEquals("(" + LOWER_VALUE + ".." + UPPER_VALUE + "]", greaterThanEqualsLessThan().toString());
    }

    private Range<Integer> greaterThanEqualsLessThan() {
        return Range.greaterThanEquals(LOWER_VALUE).and(Range.lessThan(UPPER_VALUE));
    }

    // and gt lte ......................................................................

    @Test
    public void testGreaterThanLessThanEquals_Less() {
        this.testFalse(greaterThanLessThanEquals(), LOWER_VALUE - 1);
    }

    @Test
    public void testGreaterThanLessThanEquals_LowerValue() {
        this.testFalse(greaterThanLessThanEquals(), LOWER_VALUE);
    }

    @Test
    public void testGreaterThanLessThanEquals_LowerAfter() {
        this.testTrue(greaterThanLessThanEquals(), LOWER_VALUE + 1);
    }

    @Test
    public void testGreaterThanLessThanEquals_UpperBefore() {
        this.testTrue(greaterThanLessThanEquals(), UPPER_VALUE - 1);
    }

    @Test
    public void testGreaterThanLessThanEquals_UpperValue() {
        this.testTrue(greaterThanLessThanEquals(), UPPER_VALUE);
    }

    @Test
    public void testGreaterThanLessThanEquals_Above() {
        this.testFalse(greaterThanLessThanEquals(), UPPER_VALUE + 1);
    }

    @Test
    public void testGreaterThanLessThanEqualsToString() {
        assertEquals("[" + LOWER_VALUE + ".." + UPPER_VALUE + ")", greaterThanLessThanEquals().toString());
    }

    private Range<Integer> greaterThanLessThanEquals() {
        return Range.greaterThan(LOWER_VALUE).and(Range.lessThanEquals(UPPER_VALUE));
    }

    // and gte lte ......................................................................

    @Test
    public void testGreaterThanEqualsLessThanEquals_Less() {
        this.testFalse(greaterThanEqualsLessThanEquals(), LOWER_VALUE - 1);
    }

    @Test
    public void testGreaterThanEqualsLessThanEquals_LowerValue() {
        this.testTrue(greaterThanEqualsLessThanEquals(), LOWER_VALUE);
    }

    @Test
    public void testGreaterThanEqualsLessThanEquals_LowerAfter() {
        this.testTrue(greaterThanEqualsLessThanEquals(), LOWER_VALUE + 1);
    }

    @Test
    public void testGreaterThanEqualsLessThanEquals_UpperBefore() {
        this.testTrue(greaterThanEqualsLessThanEquals(), UPPER_VALUE - 1);
    }

    @Test
    public void testGreaterThanEqualsLessThanEquals_UpperValue() {
        this.testTrue(greaterThanEqualsLessThanEquals(), UPPER_VALUE);
    }

    @Test
    public void testGreaterThanEqualsLessThanEquals_Above() {
        this.testFalse(greaterThanEqualsLessThanEquals(), UPPER_VALUE + 1);
    }

    @Test
    public void testGreaterThanEqualsLessThanEqualsToString() {
        assertEquals("(" + LOWER_VALUE + ".." + UPPER_VALUE + ")", greaterThanEqualsLessThanEquals().toString());
    }

    private Range<Integer> greaterThanEqualsLessThanEquals() {
        return Range.greaterThanEquals(LOWER_VALUE).and(Range.lessThanEquals(UPPER_VALUE));
    }

    // and..............................................................................................

    @Test
    public void testAndLower() {
        this.andAndCheck(greaterThanEqualsLessThanEquals(),
                Range.greaterThan(BELOW_LOWER_VALUE));
    }

    @Test
    public void testAndUpper() {
        this.andAndCheck(greaterThanEqualsLessThanEquals(),
                Range.lessThan(ABOVE_UPPER_VALUE));
    }

    @Test
    public void testAndLower2() {
        final Range<Integer> range = greaterThanEqualsLessThanEquals();
        this.andAndCheck(range,
                Range.greaterThan(ABOVE_LOWER_VALUE),
                RangeBound.exclusive(ABOVE_LOWER_VALUE),
                range.upper);
    }

    @Test
    public void testAndUpper2() {
        this.andAndCheck(greaterThanEqualsLessThanEquals(),
                Range.lessThan(BELOW_UPPER_VALUE),
                greaterThanEquals().lower,
                RangeBound.exclusive(BELOW_UPPER_VALUE));
    }

    private void andAndCheck(final Range<Integer> range,
                                      final Range<Integer> other) {
        this.andAndCheck(range, other, range.lower, range.upper);
    }

    private void andAndCheck(final Range<Integer> range,
                                      final Range<Integer> other,
                                      final RangeBound<Integer> lower,
                                      final RangeBound<Integer> upper) {
        final Range<Integer> intersected = range.and(other);
        assertEquals(range + " and " + other + " lower", lower, intersected.lower);
        assertEquals(range + " and " + other + " upper", upper, intersected.upper);
    }

    // more tests ...................................................................................................

    @Test
    public void testMultipleValuesCharacters() {
        final Range<Character> range = Range.greaterThan('C').and(Range.lessThan('M'));
        this.testFalse(range, 'B');
        this.testFalse(range, 'C');
        this.testTrue(range, 'D');
        this.testTrue(range, 'L');
        this.testFalse(range, 'M');
        this.testFalse(range, 'N');
    }

    @Test
    public void testMultipleValuesStrings() {
        final Range<String> range = Range.greaterThan("CAT").and(Range.lessThan("MOUSE"));
        this.testFalse(range, "BAT");
        this.testFalse(range, "CAT");
        this.testTrue(range, "COG");
        this.testTrue(range, "LOG");
        this.testTrue(range, "MICE");
        this.testFalse(range, "MOUSE");
        this.testFalse(range, "NUT");
    }

    @Test
    public void testMultipleValuesStrings2() {
        final Range<String> range = Range.greaterThanEquals("CAT").and(Range.lessThanEquals("MOUSE"));
        this.testFalse(range, "BAT");
        this.testTrue(range, "CAT");
        this.testTrue(range, "COG");
        this.testTrue(range, "LOG");
        this.testTrue(range, "MICE");
        this.testTrue(range, "MOUSE");
        this.testFalse(range, "NUT");
    }

    // misc ...................................................................................................

    @Test
    @Ignore
    @Override
    public void testAllMethodsVisibility() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Ignore
    @Override
    public void testCheckNaming() {
        throw new UnsupportedOperationException();
    }

    // helper...........................................................................................

    @Override
    protected Range<Integer> createPredicate() {
        return Range.all();
    }

    private static Range<Integer> all() {
        return Range.all();
    }

    private void check(final Range<Integer> range, final RangeBound<Integer> lower, final RangeBound<Integer> upper) {
        assertEquals("lower " + range, lower, range.lowerBound());
        assertEquals("upper " + range, upper, range.upperBound());
    }

    @Override
    protected boolean typeMustBePublic() {
        return true;
    }

    @Override
    protected Class<Range<Integer>> type() {
        return Cast.to(Range.class);
    }
}
