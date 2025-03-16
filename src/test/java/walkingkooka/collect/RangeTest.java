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

package walkingkooka.collect;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.RangeTest.CaseInsensitiveString;
import walkingkooka.predicate.PredicateTesting2;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.visit.VisitableTesting;
import walkingkooka.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class RangeTest implements ClassTesting2<Range<CaseInsensitiveString>>,
    ParseStringTesting<Range<CaseInsensitiveString>>,
    PredicateTesting2<Range<CaseInsensitiveString>, CaseInsensitiveString>,
    HashCodeEqualsDefinedTesting2<Range<CaseInsensitiveString>>,
    VisitableTesting<Range<CaseInsensitiveString>> {

    private final static CaseInsensitiveString BELOW_LOWER_VALUE = new CaseInsensitiveString("A");
    private final static CaseInsensitiveString LOWER_VALUE = new CaseInsensitiveString("b");
    private final static CaseInsensitiveString ABOVE_LOWER_VALUE = new CaseInsensitiveString("C");
    private final static CaseInsensitiveString VALUE = new CaseInsensitiveString("d");
    private final static CaseInsensitiveString BELOW_UPPER_VALUE = new CaseInsensitiveString("E");
    private final static CaseInsensitiveString UPPER_VALUE = new CaseInsensitiveString("f");
    private final static CaseInsensitiveString ABOVE_UPPER_VALUE = new CaseInsensitiveString("G");

    // with.............................................................................................................

    @Test
    public void testWithNullLowerFails() {
        assertThrows(
            NullPointerException.class,
            () -> Range.with(
                null,
                RangeBound.<CaseInsensitiveString>all()
            )
        );
    }

    @Test
    public void testWithNullUpperFails() {
        assertThrows(NullPointerException.class, () -> Range.with(RangeBound.<CaseInsensitiveString>all(), null));
    }

    @Test
    public void testWith() {
        final RangeBound<CaseInsensitiveString> lower = RangeBound.inclusive(BELOW_LOWER_VALUE);
        final RangeBound<CaseInsensitiveString> upper = RangeBound.inclusive(ABOVE_UPPER_VALUE);

        final Range<CaseInsensitiveString> range = Range.with(lower, upper);
        assertSame(lower, range.lowerBound(), "lowerBounds");
        assertSame(upper, range.upperBound(), "upperBounds");
    }

    // parameter checks.................................................................................................

    @Test
    public void testAndNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createPredicate().and(null)
        );
    }

    @Test
    public void testAndSame() {
        this.andAndCheck(
            "a",
            "a",
            "a:a"
        );
    }

    @Test
    public void testAndSame2() {
        this.andAndCheck(
            "Z",
            "Z",
            "Z:Z"
        );
    }

    @Test
    public void testAndEquivalent() {
        this.andAndCheck(
            "a",
            "A",
            "a"
        );
    }

    @Test
    public void testAndEquivalent2() {
        this.andAndCheck(
            "A",
            "a",
            "A"
        );
    }

    private void andAndCheck(final String left,
                             final String and,
                             final String expected) {
        this.checkEquals(
            this.parseString(expected),
            this.parseString(left)
                .and(
                    this.parseString(and)
                ),
            () -> left + " and " + and
        );
    }

    // all .............................................................................................................

    @Test
    public void testAll() {
        this.<String>check(
            Range.all(),
            RangeBound.all(),
            RangeBound.all()
        );
    }

    @Test
    public void testAllTest() {
        this.testTrue(LOWER_VALUE);
    }

    @Test
    public void testAllTest2() {
        this.testTrue(ABOVE_LOWER_VALUE);
    }

    @Test
    public void testAllToString() {
        this.toStringAndCheck(Range.all(), "*");
    }

    // singleton .......................................................................................................

    @Test
    public void testSingletonNullValueFails() {
        assertThrows(NullPointerException.class, () -> Range.singleton(null));
    }

    @Test
    public void testSingletonTestValueLess() {
        this.testFalse(singleton(), BELOW_LOWER_VALUE);
    }

    @Test
    public void testSingletonTestValueEqual() {
        this.testTrue(singleton(), LOWER_VALUE);
    }

    @Test
    public void testSingletonTestValueGreater() {
        this.testFalse(singleton(), ABOVE_LOWER_VALUE);
    }

    @Test
    public void testSingletonToString() {
        this.toStringAndCheck(singleton(), "" + LOWER_VALUE);
    }

    private static Range<CaseInsensitiveString> singleton() {
        return Range.singleton(LOWER_VALUE);
    }

    // lessThan ........................................................................................................

    @Test
    public void testLessThan() {
        final int value = 123;
        this.check(Range.lessThan(value), RangeBound.all(), RangeBound.exclusive(value));
    }

    @Test
    public void testLessThanNullValueFails() {
        assertThrows(NullPointerException.class, () -> Range.lessThan(null));
    }

    @Test
    public void testLessThanTestValueLess() {
        this.testTrue(lessThan(), BELOW_LOWER_VALUE);
    }

    @Test
    public void testLessThanTestValueEqual() {
        this.testFalse(lessThan(), LOWER_VALUE);
    }

    @Test
    public void testLessThanTestValueGreater() {
        this.testFalse(lessThan(), ABOVE_LOWER_VALUE);
    }

    @Test
    public void testLessThanToString() {
        this.toStringAndCheck(lessThan(), "<" + LOWER_VALUE);
    }

    private Range<CaseInsensitiveString> lessThan() {
        return Range.lessThan(LOWER_VALUE);
    }

    // lessThanEquals ..................................................................................................

    @Test
    public void testLessThanEquals() {
        final int value = 123;
        this.check(Range.lessThanEquals(value), RangeBound.all(), RangeBound.inclusive(value));
    }

    @Test
    public void testLessThanEqualsNullValueFails() {
        assertThrows(NullPointerException.class, () -> Range.lessThanEquals(null));
    }

    @Test
    public void testLessThanEqualsTestValueLess() {
        this.testTrue(lessThanEquals(), BELOW_LOWER_VALUE);
    }

    @Test
    public void testLessThanEqualsTestValueEqual() {
        this.testTrue(lessThanEquals(), LOWER_VALUE);
    }

    @Test
    public void testLessThanEqualsTestValueGreater() {
        this.testFalse(lessThanEquals(), ABOVE_LOWER_VALUE);
    }

    @Test
    public void testLessThanEqualsToString() {
        this.toStringAndCheck(lessThanEquals(), "<=" + LOWER_VALUE);
    }

    private static Range<CaseInsensitiveString> lessThanEquals() {
        return Range.lessThanEquals(LOWER_VALUE);
    }

    // greaterThan .....................................................................................................

    @Test
    public void testGreaterThan() {
        final int value = 123;
        this.check(Range.greaterThan(value), RangeBound.exclusive(value), RangeBound.all());
    }

    @Test
    public void testGreaterThanNullValueFails() {
        assertThrows(NullPointerException.class, () -> Range.greaterThan(null));
    }

    @Test
    public void testGreaterThanTestValueLess() {
        this.testFalse(greaterThan(), BELOW_LOWER_VALUE);
    }

    @Test
    public void testGreaterThanTestValueEqual() {
        this.testFalse(greaterThan(), LOWER_VALUE);
    }

    @Test
    public void testGreaterThanTestValueGreater() {
        this.testTrue(greaterThan(), ABOVE_LOWER_VALUE);
    }

    @Test
    public void testGreaterThanToString() {
        this.toStringAndCheck(greaterThan(), ">" + LOWER_VALUE);
    }

    private static Range<CaseInsensitiveString> greaterThan() {
        return Range.greaterThan(LOWER_VALUE);
    }

    // greaterThanEquals ...............................................................................................

    @Test
    public void testGreaterThanEquals() {
        final int value = 123;
        this.check(Range.greaterThanEquals(value), RangeBound.inclusive(value), RangeBound.all());
    }

    @Test
    public void testGreaterThanEqualsNullValueFails() {
        assertThrows(NullPointerException.class, () -> Range.greaterThanEquals(null));
    }

    @Test
    public void testGreaterThanEqualsTestValueLess() {
        this.testFalse(greaterThanEquals(), BELOW_LOWER_VALUE);
    }

    @Test
    public void testGreaterThanEqualsTestValueEqual() {
        this.testTrue(greaterThanEquals(), LOWER_VALUE);
    }

    @Test
    public void testGreaterThanEqualsTestValueGreater() {
        this.testTrue(greaterThanEquals(), ABOVE_LOWER_VALUE);
    }

    @Test
    public void testGreaterThanEqualsToString() {
        this.toStringAndCheck(greaterThanEquals(), ">=" + LOWER_VALUE);
    }

    private static Range<CaseInsensitiveString> greaterThanEquals() {
        return Range.greaterThanEquals(LOWER_VALUE);
    }

    // and with all.....................................................................................................

    @Test
    public void testAndAllAndAll() {
        andAllAndCheck(Range.all());
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

    private void andAllAndCheck(final Range<CaseInsensitiveString> range) {
        final Range<CaseInsensitiveString> all = Range.all();
        assertSame(range, range.and(all), () -> range + " and with " + all);
        assertSame(range, all.and(range), () -> range + " and with " + all);
    }

    // and with all.....................................................................................................

    @Test
    public void testAndSingletonAndAll() {
        andSingletonAndCheck(Range.all());
    }

    @Test
    public void testAndSingletonAndSingleton() {
        final Range<CaseInsensitiveString> singleton = singleton();
        final Range<CaseInsensitiveString> singleton2 = singleton();
        assertSame(singleton, singleton.and(singleton2), () -> singleton + " and with " + singleton2);
        assertSame(singleton2, singleton2.and(singleton), () -> singleton2 + " and with " + singleton);
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

    private void andSingletonAndCheck(final Range<CaseInsensitiveString> range) {
        final Range<CaseInsensitiveString> singleton = singleton();
        assertSame(singleton, range.and(singleton), () -> range + " and with " + singleton);
        assertSame(singleton, singleton.and(range), () -> singleton + " and with " + range);
    }

    // and invalid......................................................................................................

    @Test
    public void testAndLessThanAndGreaterThanFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> lessThan()
                .and(greaterThan()
                )
        );
    }

    @Test
    public void testAndGreaterThanAndLessThanFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> greaterThan().and(lessThan())
        );
    }

    @Test
    public void testAndLessThanAndGreaterThanFails2() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Range.lessThan(122)
                .and(
                    Range.greaterThan(123)
                )
        );
    }

    @Test
    public void testAndLessThanEqualAndGreaterThanFails2() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Range.lessThanEquals(122)
                .and(
                    Range.greaterThan(123)
                )
        );
    }

    @Test
    public void testAndLessThanEqualAndSingletonFails2() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Range.lessThanEquals(122)
                .and(
                    Range.singleton(123)
                )
        );
    }

    @Test
    public void testAndSingletonAndGreaterThanFails2() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Range.singleton(122)
                .and(
                    Range.greaterThan(123)
                )
        );
    }

    @Test
    public void testAndSingletonAndGreaterThanEqualsFails2() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Range.singleton(122)
                .and(
                    Range.greaterThanEquals(123)
                )
        );
    }

    @Test
    public void testAndNonOverlappingBoundedRangeBoundedRangeFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Range.greaterThan(222)
                .and(Range.lessThan(333)
                ).and(
                    Range.greaterThan(888)
                        .and(Range.lessThan(999))
                )
        );
    }

    @Test
    public void testAndNonOverlappingBoundedRangeBoundedRangeFails2() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Range.greaterThan(222)
                .and(Range.lessThan(333)
                ).and(Range.greaterThan(333)
                    .and(Range.lessThan(999)
                    )
                )
        );
    }

    // and gt lt ........................................................................................................

    @Test
    public void testTestGreaterThanLessThan_Less() {
        this.testFalse(
            greaterThanLessThan(),
            BELOW_LOWER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanLessThan_LowerValue() {
        this.testFalse(
            greaterThanLessThan(),
            LOWER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanLessThan_LowerAfter() {
        this.testTrue(
            greaterThanLessThan(),
            ABOVE_LOWER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanLessThan_UpperBefore() {
        this.testTrue(
            greaterThanLessThan(), 
            BELOW_UPPER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanLessThan_UpperValue() {
        this.testFalse(
            greaterThanLessThan(),
            UPPER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanLessThan_Above() {
        this.testFalse(
            greaterThanLessThan(),
            ABOVE_UPPER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanLessThanToString() {
        this.checkEquals(
            "[" + LOWER_VALUE + ".." + UPPER_VALUE + "]",
            greaterThanLessThan().toString()
        );
    }

    private Range<CaseInsensitiveString> greaterThanLessThan() {
        return Range.greaterThan(LOWER_VALUE)
            .and(Range.lessThan(UPPER_VALUE)
            );
    }

    // and gte lt ......................................................................................................

    @Test
    public void testTestGreaterThanEqualsLessThan_Less() {
        this.testFalse(greaterThanEqualsLessThan(), BELOW_LOWER_VALUE);
    }

    @Test
    public void testTestGreaterThanEqualsLessThan_LowerValue() {
        this.testTrue(greaterThanEqualsLessThan(), LOWER_VALUE);
    }

    @Test
    public void testTestGreaterThanEqualsLessThan_LowerAfter() {
        this.testTrue(greaterThanEqualsLessThan(), ABOVE_LOWER_VALUE);
    }

    @Test
    public void testTestGreaterThanEqualsLessThan_UpperBefore() {
        this.testTrue(greaterThanEqualsLessThan(), BELOW_UPPER_VALUE);
    }

    @Test
    public void testTestGreaterThanEqualsLessThan_UpperValue() {
        this.testFalse(greaterThanEqualsLessThan(), UPPER_VALUE);
    }

    @Test
    public void testTestGreaterThanEqualsLessThan_Above() {
        this.testFalse(greaterThanEqualsLessThan(), ABOVE_UPPER_VALUE);
    }

    @Test
    public void testTestGreaterThanEqualsLessThanToString() {
        this.checkEquals(
            "(" + LOWER_VALUE + ".." + UPPER_VALUE + "]",
            greaterThanEqualsLessThan().toString()
        );
    }

    private Range<CaseInsensitiveString> greaterThanEqualsLessThan() {
        return Range.greaterThanEquals(LOWER_VALUE)
            .and(Range.lessThan(UPPER_VALUE));
    }

    // and gt lte ......................................................................................................

    @Test
    public void testTestGreaterThanLessThanEquals_Less() {
        this.testFalse(
            greaterThanLessThanEquals(), 
            BELOW_LOWER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanLessThanEquals_LowerValue() {
        this.testFalse(
            greaterThanLessThanEquals(),
            LOWER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanLessThanEquals_LowerAfter() {
        this.testTrue(
            greaterThanLessThanEquals(),
            ABOVE_LOWER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanLessThanEquals_UpperBefore() {
        this.testTrue(
            greaterThanLessThanEquals(),
            BELOW_UPPER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanLessThanEquals_UpperValue() {
        this.testTrue(
            greaterThanLessThanEquals(),
            UPPER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanLessThanEquals_Above() {
        this.testFalse(
            greaterThanLessThanEquals(),
            ABOVE_UPPER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanLessThanEqualsToString() {
        this.checkEquals(
            "[" + LOWER_VALUE + ".." + UPPER_VALUE + ")",
            greaterThanLessThanEquals().toString()
        );
    }

    private Range<CaseInsensitiveString> greaterThanLessThanEquals() {
        return Range.greaterThan(LOWER_VALUE)
            .and(
                Range.lessThanEquals(UPPER_VALUE)
            );
    }

    // and gte lte .....................................................................................................

    @Test
    public void testTestGreaterThanEqualsLessThanEquals_Less() {
        this.testFalse(
            greaterThanEqualsLessThanEquals(),
            BELOW_LOWER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanEqualsLessThanEquals_LowerValue() {
        this.testTrue(
            greaterThanEqualsLessThanEquals(), 
            LOWER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanEqualsLessThanEquals_LowerAfter() {
        this.testTrue(
            greaterThanEqualsLessThanEquals(),
            ABOVE_LOWER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanEqualsLessThanEquals_UpperBefore() {
        this.testTrue(
            greaterThanEqualsLessThanEquals(),
            BELOW_UPPER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanEqualsLessThanEquals_UpperValue() {
        this.testTrue(
            greaterThanEqualsLessThanEquals(),
            UPPER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanEqualsLessThanEquals_Above() {
        this.testFalse(
            greaterThanEqualsLessThanEquals(),
            ABOVE_UPPER_VALUE
        );
    }

    @Test
    public void testTestGreaterThanEqualsLessThanEqualsToString() {
        this.checkEquals(
            "(" + LOWER_VALUE + ".." + UPPER_VALUE + ")", 
            greaterThanEqualsLessThanEquals().toString()
        );
    }

    private Range<CaseInsensitiveString> greaterThanEqualsLessThanEquals() {
        return Range.greaterThanEquals(LOWER_VALUE)
            .and(
                Range.lessThanEquals(UPPER_VALUE)
            );
    }

    @Override
    public Range<CaseInsensitiveString> createPredicate() {
        return Range.all();
    }
    
    // and..............................................................................................................

    @Test
    public void testAndLower() {
        this.andAndCheck(
            greaterThanEqualsLessThanEquals(),
            Range.greaterThan(BELOW_LOWER_VALUE)
        );
    }

    @Test
    public void testAndUpper() {
        this.andAndCheck(
            greaterThanEqualsLessThanEquals(),
            Range.lessThan(ABOVE_UPPER_VALUE)
        );
    }

    @Test
    public void testAndLower2() {
        final Range<CaseInsensitiveString> range = greaterThanEqualsLessThanEquals();
        this.andAndCheck(
            range,
            Range.greaterThan(ABOVE_LOWER_VALUE),
            RangeBound.exclusive(ABOVE_LOWER_VALUE),
            range.upper
        );
    }

    @Test
    public void testAndUpper2() {
        this.andAndCheck(
            greaterThanEqualsLessThanEquals(),
            Range.lessThan(BELOW_UPPER_VALUE),
            greaterThanEquals().lower,
            RangeBound.exclusive(BELOW_UPPER_VALUE)
        );
    }

    private void andAndCheck(final Range<CaseInsensitiveString> range,
                             final Range<CaseInsensitiveString> other) {
        this.andAndCheck(
            range,
            other,
            range.lower,
            range.upper
        );
    }

    private void andAndCheck(final Range<CaseInsensitiveString> range,
                             final Range<CaseInsensitiveString> other,
                             final RangeBound<CaseInsensitiveString> lower,
                             final RangeBound<CaseInsensitiveString> upper) {
        final Range<CaseInsensitiveString> intersected = range.and(other);
        
        this.checkEquals(
            lower, 
            intersected.lower,
            () -> range + " and " + other + " lower"
        );
        this.checkEquals(
            upper,
            intersected.upper,
            () -> range + " and " + other + " upper"
        );
    }

    // more tests ......................................................................................................

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

    // isOverlapping....................................................................................................

    @Test
    public void testIsOverlappingpingNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> Range.all().isOverlapping(null)
        );
    }

    @Test
    public void testIsOverlappingWithout() {
        this.isOverlappingAndCheck(
            Range.greaterThan(5),
            Range.lessThan(3),
            false
        );
    }

    @Test
    public void testIsOverlappingWithout2() {
        this.isOverlappingAndCheck(
            Range.greaterThan(5),
            Range.lessThan(4),
            false
        );
    }

    @Test
    public void testIsOverlappingWithout3() {
        this.isOverlappingAndCheck(
            Range.lessThan(55),
            Range.greaterThan(55),
            false)
        ;
    }

    @Test
    public void testIsOverlappingWithout4() {
        this.isOverlappingAndCheck(
            Range.greaterThanEquals(5),
            Range.lessThanEquals(4),
            false
        );
    }

    @Test
    public void testIsOverlappingWithout5() {
        this.isOverlappingAndCheck(
            Range.greaterThanEquals(66).and(Range.lessThan(99)),
            Range.lessThan(44),
            false
        );
    }

    @Test
    public void testIsOverlappingWithout6() {
        this.isOverlappingAndCheck(
            Range.greaterThanEquals(55).and(Range.lessThan(99)),
            Range.lessThan(54),
            false
        );
    }

    @Test
    public void testIsOverlappingWithout7() {
        this.isOverlappingAndCheck(
            Range.greaterThanEquals(55).and(Range.lessThan(99)),
            Range.greaterThanEquals(44).and(Range.lessThan(54)),
            false
        );
    }

    @Test
    public void testIsOverlappingWithout8() {
        this.isOverlappingAndCheck(
            Range.singleton(44),
            Range.singleton(55),
            false
        );
    }

    @Test
    public void testIsOverlappingWithout9() {
        this.isOverlappingAndCheck(
            Range.lessThan(44),
            Range.greaterThan(44),
            false
        );
    }

    @Test
    public void testIsOverlappingWithout10() {
        this.isOverlappingAndCheck(
            Range.lessThanEquals(44),
            Range.greaterThan(44),
            false
        );
    }

    @Test
    public void testIsOverlappingWithout11() {
        this.isOverlappingAndCheck(
            Range.lessThan(44),
            Range.greaterThanEquals(44),
            false
        );
    }

    @Test
    public void testIsOverlapping() {
        this.isOverlappingAndCheck(
            Range.greaterThanEquals(55).and(Range.lessThan(99)),
            Range.greaterThanEquals(44).and(Range.lessThan(56)),
            true
        ); // at 55
    }

    @Test
    public void testIsOverlapping1() {
        this.isOverlappingAndCheck(
            Range.greaterThanEquals(55),
            Range.greaterThanEquals(44).and(Range.lessThanEquals(55)),
            true
        ); // at 55
    }

    @Test
    public void testIsOverlapping2() {
        this.isOverlappingAndCheck(
            Range.greaterThanEquals(55).and(Range.lessThan(99)),
            Range.greaterThanEquals(44),
            true
        ); // at >=55
    }

    @Test
    public void testIsOverlapping3() {
        this.isOverlappingAndCheck(
            Range.all(),
            Range.greaterThanEquals(44),
            true
        ); // at >=55
    }

    @Test
    public void testIsOverlapping4() {
        this.isOverlappingAndCheck(
            Range.singleton(55),
            Range.singleton(55),
            true
        ); // at 55
    }

    private void isOverlappingAndCheck(final Range<Integer> first,
                                       final Range<Integer> other,
                                       final boolean expected) {
        this.isOverlappingAndCheck0(
            first,
            other,
            expected
        );
        this.isOverlappingAndCheck0(
            other,
            first,
            expected
        );
    }

    private void isOverlappingAndCheck0(final Range<Integer> first,
                                        final Range<Integer> other,
                                        final boolean expected) {
        this.checkEquals(expected, first.isOverlapping(other), () -> first + " " + other);

        boolean and;
        try {
            first.and(other);
            and = true;
        } catch (final Exception fail) {
            and = false;
        }
        this.checkEquals(expected,
            and,
            () -> first + " and " + other + " doesnt match " + first + " isOverlapping " + other);
    }

    // parse ...........................................................................................................

    @Test
    public void testParseNullTextFails() {
        this.parseStringFails(null, NullPointerException.class);
    }

    @Test
    public void testParseEmptyTextFails() {
        this.parseStringFails("", IllegalArgumentException.class);
    }

    @Test
    public void testParseNullFactoryFails() {
        assertThrows(NullPointerException.class, () -> Range.parse("1", ':', null));
    }

    @Test
    public void testParseInvalidCharacterFails() {
        final String text = "1A:2!";

        final InvalidCharacterException thrown = assertThrows(InvalidCharacterException.class, () -> Range.parse(text, ':', (s) -> {
            throw new InvalidCharacterException("2!", 1);
        }));
        this.checkEquals(text, thrown.text(), "text");
    }

    @Test
    public void testParseMissingSeparator() {
        this.parseStringAndCheck(
            "A",
            Range.singleton(
                new CaseInsensitiveString("A")
            )
        );
    }

    @Test
    public void testParseMissingSeparator2() {
        this.parseStringAndCheck(
            "bcd",
            Range.singleton(
                new CaseInsensitiveString("bcd")
            )
        );
    }

    @Test
    public void testParseEmptyLowerRangeFails() {
        this.parseStringFails(":1", new IllegalArgumentException("Empty lower range in \":1\""));
    }

    @Test
    public void testParseEmptyUpperRangeFails() {
        this.parseStringFails("1:", new IllegalArgumentException("Empty upper range in \"1:\""));
    }

    @Test
    public void testParseInvalidLowerRangeFails() {
        this.parseStringFails(
            "!1:2",
            new IllegalArgumentException("Invalid string=!1")
        );
    }

    @Test
    public void testParseInvalidUpperRangeFails() {
        this.parseStringFails(
            "1:!2",
            new IllegalArgumentException("Invalid string=!2")
        );
    }

    @Test
    public void testParseSame() {
        this.parseStringAndCheck(
            "A:A",
            Range.singleton(
                new CaseInsensitiveString("A")
            )
        );
    }

    @Test
    public void testParseEquivalent() {
        this.parseStringAndCheck(
            "A:a",
            Range.singleton(
                new CaseInsensitiveString("A")
            )
        );
    }

    @Test
    public void testParseEquivalent2() {
        this.parseStringAndCheck(
            "b:B",
            Range.singleton(
                new CaseInsensitiveString("b")
            )
        );
    }

    @Test
    public void testParseLowerAndUpper() {
        this.parseStringAndCheck(
            "A:B",
            Range.greaterThanEquals(
                new CaseInsensitiveString("A")
            ).and(
                Range.lessThanEquals(new CaseInsensitiveString("B"))
            )
        );
    }

    @Test
    public void testParseLowerAndUpper2() {
        this.parseStringAndCheck(
            "A:b",
            Range.greaterThanEquals(
                new CaseInsensitiveString("A")
            ).and(
                Range.lessThanEquals(new CaseInsensitiveString("b"))
            )
        );
    }

    @Test
    public void testParseLowerAndUpperDifferentSeparator() {
        this.checkEquals(
            Range.greaterThanEquals(123).and(Range.lessThanEquals(456)),
            Range.parse(
                "123-456",
                '-',
                Integer::parseInt
            )
        );
    }

    @Test
    public void testParseSwapped() {
        this.parseStringAndCheck(
            "B:A",
            Range.greaterThanEquals(
                new CaseInsensitiveString("A")
            ).and(
                Range.lessThanEquals(new CaseInsensitiveString("B"))
            )
        );
    }

    @Test
    public void testParseSwapped2() {
        this.parseStringAndCheck(
            "B:a",
            Range.greaterThanEquals(
                new CaseInsensitiveString("a")
            ).and(
                Range.lessThanEquals(new CaseInsensitiveString("B"))
            )
        );
    }

    public Range<CaseInsensitiveString> parseString(final String text) {
        return Range.parse(
            text,
            ':',
            CaseInsensitiveString::new
        );
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException expected) {
        return expected;
    }

    // hashCode/equals..................................................................................................

    // all..............................................................................................................

    @Test
    public void testEqualsAllSingleton() {
        this.checkNotEquals(
            Range.all(),
            Range.singleton(VALUE)
        );
    }

    @Test
    public void testEqualsAllLessThan() {
        this.checkNotEquals(
            Range.all(),
            Range.lessThan(VALUE)
        );
    }

    @Test
    public void testEqualsAllLessThanEquals() {
        this.checkNotEquals(
            Range.all(),
            Range.lessThanEquals(VALUE)
        );
    }

    @Test
    public void testEqualsAllGreaterThan() {
        this.checkNotEquals(
            Range.greaterThan(VALUE)
        );
    }

    @Test
    public void testEqualsAllGreaterThanEquals() {
        this.checkNotEquals(
            Range.all(), 
            Range.greaterThanEquals(VALUE)
        );
    }

    // lessThan.........................................................................................................

    @Test
    public void testEqualsLessThan() {
        final Range<CaseInsensitiveString> range = Range.lessThanEquals(VALUE);
        this.checkEqualsAndHashCode(
            range,
            range
        );
    }

    @Test
    public void testEqualsLessThanAll() {
        this.checkNotEquals(
            Range.lessThan(VALUE),
            Range.all()
        );
    }

    @Test
    public void testEqualsLessThanSingleton() {
        this.checkNotEquals(
            Range.lessThan(VALUE),
            Range.singleton(VALUE)
        );
    }

    @Test
    public void testEqualsLessThanLessThan() {
        this.checkEqualsAndHashCode(
            Range.lessThan(VALUE), 
            Range.lessThan(VALUE)
        );
    }

    @Test
    public void testEqualsLessThanLessThanEquals() {
        this.checkNotEquals(
            Range.lessThan(VALUE), 
            Range.lessThanEquals(VALUE)
        );
    }

    @Test
    public void testEqualsLessThanGreaterThan() {
        this.checkNotEquals(
            Range.lessThan(VALUE), 
            Range.greaterThan(VALUE)
        );
    }

    @Test
    public void testEqualsLessThanGreaterThanEquals() {
        this.checkNotEquals(
            Range.lessThan(VALUE),
            Range.greaterThanEquals(VALUE)
        );
    }

    // lessThan.........................................................................................................

    @Test
    public void testEqualsLessThanEqualsAll() {
        this.checkNotEquals(
            Range.lessThanEquals(VALUE),
            Range.all()
        );
    }

    @Test
    public void testEqualsLessThanEqualsSingleton() {
        this.checkNotEquals(
            Range.lessThanEquals(VALUE),
            Range.singleton(VALUE)
        );
    }

    @Test
    public void testEqualsLessThanEqualsLessThan() {
        this.checkNotEquals(
            Range.lessThanEquals(VALUE),
            Range.lessThan(VALUE)
        );
    }

    @Test
    public void testEqualsLessThanEqualsLessThanEquals() {
        this.checkEqualsAndHashCode(
            Range.lessThanEquals(VALUE), 
            Range.lessThanEquals(VALUE)
        );
    }

    @Test
    public void testEqualsLessThanEqualsGreaterThan() {
        this.checkNotEquals(
            Range.lessThanEquals(VALUE),
            Range.greaterThan(VALUE)
        );
    }

    @Test
    public void testEqualsLessThanEqualsGreaterThanEquals() {
        this.checkNotEquals(
            Range.lessThanEquals(VALUE),
            Range.greaterThanEquals(VALUE)
        );
    }

    @Override
    public Range<CaseInsensitiveString> createObject() {
        return this.createPredicate();
    }

    // RangeVisitor.....................................................................................................

    @Test
    public void testAcceptGreaterThan() {
        final StringBuilder visited = new StringBuilder();

        final CaseInsensitiveString lower = LOWER_VALUE;
        final Range<CaseInsensitiveString> range = Range.greaterThan(lower);

        range.accept(new FakeRangeVisitor<>() {

            @Override
            protected Visiting startVisit(final Range<CaseInsensitiveString> r) {
                assertSame(range, r);
                visited.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected Visiting startBetween(final RangeBound<CaseInsensitiveString> lowerBound, final RangeBound<CaseInsensitiveString> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("2");
                return Visiting.CONTINUE;
            }

            @Override
            protected void lowerBoundExclusive(final CaseInsensitiveString value) {
                assertSame(lower, value, "lowerBoundExclusive");
                visited.append("3");
            }

            @Override
            protected void upperBoundAll() {
                visited.append("4");
            }

            @Override
            protected void endBetween(final RangeBound<CaseInsensitiveString> lowerBound, final RangeBound<CaseInsensitiveString> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("5");
            }

            @Override
            protected void endVisit(final Range<CaseInsensitiveString> range) {
                visited.append("6");
                super.endVisit(range);
            }
        });

        this.checkEquals(visited.toString(), "123456");
    }

    // helper...........................................................................................................

    private <CC extends Comparable<CC>> void check(final Range<CC> range,
                                                   final RangeBound<CC> lower,
                                                   final RangeBound<CC> upper) {
        this.checkEquals(
            lower,
            range.lowerBound(),
            () -> "lower " + range
        );
        this.checkEquals(
            upper,
            range.upperBound(),
            () -> "upper " + range
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<Range<CaseInsensitiveString>> type() {
        return Cast.to(Range.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public void testAllMethodsVisibility() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    /**
     * A {@link Comparable} where compare = 0 is not always equals.
     */
    static public class CaseInsensitiveString implements Comparable<CaseInsensitiveString> {

        CaseInsensitiveString(final String string) {
            if (string.contains("!")) {
                throw new IllegalArgumentException("Invalid string=" + string);
            }
            this.string = string;
        }

        @Override
        public int hashCode() {
            return this.string.hashCode();
        }


        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof CaseInsensitiveString && this.equals0((CaseInsensitiveString) other);
        }

        private boolean equals0(final CaseInsensitiveString other) {
            return this.string.equals(other.string);
        }

        @Override
        public int compareTo(final CaseInsensitiveString other) {
            return CaseSensitivity.INSENSITIVE.comparator().compare(
                this.string,
                other.string
            );
        }

        private final String string;

        @Override
        public String toString() {
            return this.string;
        }
    }
}
