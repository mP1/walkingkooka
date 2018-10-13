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
    public void testIntersectionNullFails() {
        this.createPredicate().intersection(null);
    }

    // all ..............................................................................................

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

    // intersection with all..........................................................................................

    @Test
    public void testIntersectionAllAndAll() {
        intersectionAllAndCheck(all());
    }

    @Test
    public void testIntersectionSingletonAllAll() {
        intersectionAllAndCheck(singleton());
    }

    @Test
    public void testIntersectionLessThanAllAll() {
        intersectionAllAndCheck(lessThan());
    }

    @Test
    public void testIntersectionLessThanEqualsAllAll() {
        intersectionAllAndCheck(lessThanEquals());
    }

    @Test
    public void testIntersectionGreaterThanAllAll() {
        intersectionAllAndCheck(greaterThan());
    }

    @Test
    public void testIntersectionGreaterThanEqualsAllAll() {
        intersectionAllAndCheck(greaterThanEquals());
    }

    private void intersectionAllAndCheck(final Range<Integer> range) {
        final Range<Integer> all = all();
        assertSame(range + " intersection with " + all, range, range.intersection(all));
        assertSame(range + " intersection with " + all, range, all.intersection(range));
    }

    // intersection with all..........................................................................................

    @Test
    public void testIntersectionSingletonAndAll() {
        intersectionSingletonAndCheck(all());
    }

    @Test
    public void testIntersectionSingletonAndSingleton() {
        final Range<Integer> singleton = singleton();
        final Range<Integer> singleton2 = singleton();
        assertSame(singleton + " intersection with " + singleton2, singleton, singleton.intersection(singleton2));
        assertSame(singleton2 + " intersection with " + singleton, singleton2, singleton2.intersection(singleton));
    }

    @Test
    public void testIntersectionSingletonAndLessThan() {
        intersectionSingletonAndCheck(lessThan());
    }

    @Test
    public void testIntersectionSingletonAndLessThanEquals() {
        intersectionSingletonAndCheck(lessThanEquals());
    }

    @Test
    public void testIntersectionSingletonAndGreaterThan() {
        intersectionSingletonAndCheck(greaterThan());
    }

    @Test
    public void testIntersectionSingletonAndGreaterThanEquals() {
        intersectionSingletonAndCheck(greaterThanEquals());
    }

    private void intersectionSingletonAndCheck(final Range<Integer> range) {
        final Range<Integer> singleton = singleton();
        assertSame(range + " intersection with " + singleton, singleton, range.intersection(singleton));
        assertSame(singleton + " intersection with " + range, singleton, singleton.intersection(range));
    }

    // intersection invalid.......................................................

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionLessThanAndGreaterThanFails() {
        lessThan().intersection(greaterThan());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionGreaterThanAndLessThanFails() {
        greaterThan().intersection(lessThan());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionLessThanAndGreaterThanFails2() {
        Range.lessThan(122).intersection(Range.greaterThan(123));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionLessThanEqualAndGreaterThanFails2() {
        Range.lessThanEquals(122).intersection(Range.greaterThan(123));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionLessThanEqualAndSingletonFails2() {
        Range.lessThanEquals(122).intersection(Range.singleton(123));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionSingletonAndGreaterThanFails2() {
        Range.singleton(122).intersection(Range.greaterThan(123));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionSingletonAndGreaterThanEqualsFails2() {
        Range.singleton(122).intersection(Range.greaterThanEquals(123));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionNonOverlappingBoundedRangeBoundedRangeFails() {
        Range.greaterThan(222).intersection(Range.lessThan(333))
                .intersection(Range.greaterThan(888).intersection(Range.lessThan(999)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionNonOverlappingBoundedRangeBoundedRangeFails2() {
        Range.greaterThan(222).intersection(Range.lessThan(333))
                .intersection(Range.greaterThan(333).intersection(Range.lessThan(999)));
    }

    // intersection gt lt ......................................................................

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
        return Range.greaterThan(LOWER_VALUE).intersection(Range.lessThan(UPPER_VALUE));
    }

    // intersection gte lt ......................................................................

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
        return Range.greaterThanEquals(LOWER_VALUE).intersection(Range.lessThan(UPPER_VALUE));
    }

    // intersection gt lte ......................................................................

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
        return Range.greaterThan(LOWER_VALUE).intersection(Range.lessThanEquals(UPPER_VALUE));
    }

    // intersection gte lte ......................................................................

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
        return Range.greaterThanEquals(LOWER_VALUE).intersection(Range.lessThanEquals(UPPER_VALUE));
    }

    // intersection..............................................................................................

    @Test
    public void testIntersectionLower() {
        this.intersectionAndCheck(greaterThanEqualsLessThanEquals(),
                Range.greaterThan(BELOW_LOWER_VALUE));
    }

    @Test
    public void testIntersectionUpper() {
        this.intersectionAndCheck(greaterThanEqualsLessThanEquals(),
                Range.lessThan(ABOVE_UPPER_VALUE));
    }

    @Test
    public void testIntersectionLower2() {
        final Range<Integer> range = greaterThanEqualsLessThanEquals();
        this.intersectionAndCheck(range,
                Range.greaterThan(ABOVE_LOWER_VALUE),
                RangeBound.exclusive(ABOVE_LOWER_VALUE),
                range.upper);
    }

    @Test
    public void testIntersectionUpper2() {
        this.intersectionAndCheck(greaterThanEqualsLessThanEquals(),
                Range.lessThan(BELOW_UPPER_VALUE),
                greaterThanEquals().lower,
                RangeBound.exclusive(BELOW_UPPER_VALUE));
    }

    private void intersectionAndCheck(final Range<Integer> range,
                                      final Range<Integer> other) {
        this.intersectionAndCheck(range, other, range.lower, range.upper);
    }

    private void intersectionAndCheck(final Range<Integer> range,
                                      final Range<Integer> other,
                                      final RangeBound<Integer> lower,
                                      final RangeBound<Integer> upper) {
        final Range<Integer> intersected = range.intersection(other);
        assertEquals(range + " and " + other + " lower", lower, intersected.lower);
        assertEquals(range + " and " + other + " upper", upper, intersected.upper);
    }

    // more tests ...................................................................................................

    @Test
    public void testMultipleValuesCharacters() {
        final Range<Character> range = Range.greaterThan('C').intersection(Range.lessThan('M'));
        this.testFalse(range, 'B');
        this.testFalse(range, 'C');
        this.testTrue(range, 'D');
        this.testTrue(range, 'L');
        this.testFalse(range, 'M');
        this.testFalse(range, 'N');
    }

    @Test
    public void testMultipleValuesStrings() {
        final Range<String> range = Range.greaterThan("CAT").intersection(Range.lessThan("MOUSE"));
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
        final Range<String> range = Range.greaterThanEquals("CAT").intersection(Range.lessThanEquals("MOUSE"));
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

    @Override
    protected boolean typeMustBePublic() {
        return true;
    }

    @Override
    protected Class<Range<Integer>> type() {
        return Cast.to(Range.class);
    }
}
