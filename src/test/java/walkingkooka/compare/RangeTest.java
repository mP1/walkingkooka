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

package walkingkooka.compare;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.InvalidCharacterException;
import walkingkooka.predicate.PredicateTesting2;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.map.FromJsonNodeContext;
import walkingkooka.tree.json.map.FromJsonNodeException;
import walkingkooka.tree.json.map.JsonNodeMappingTesting;
import walkingkooka.type.JavaVisibility;
import walkingkooka.visit.VisitableTesting;
import walkingkooka.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class RangeTest implements ClassTesting2<Range<Integer>>,
        JsonNodeMappingTesting<Range<Integer>>,
        ParseStringTesting<Range<Integer>>,
        PredicateTesting2<Range<Integer>, Integer>,
        HashCodeEqualsDefinedTesting<Range<Integer>>,
        VisitableTesting<Range<Integer>> {

    private final static Integer BELOW_LOWER_VALUE = 1000;
    private final static Integer LOWER_VALUE = 2000;
    private final static Integer ABOVE_LOWER_VALUE = 3000;
    private final static Integer VALUE = 3500;
    private final static Integer BELOW_UPPER_VALUE = 4000;
    private final static Integer UPPER_VALUE = 5000;
    private final static Integer ABOVE_UPPER_VALUE = 6000;

    // parameter checks................................................

    @Test
    public void testAndNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createPredicate().and(null);
        });
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

    @Test
    public void testAllToString() {
        this.toStringAndCheck(Range.all(), "*");
    }

    // singleton ....................................................................................................

    @Test
    public void testSingletonNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            Range.singleton(null);
        });
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
        this.toStringAndCheck(singleton(), "" + LOWER_VALUE);
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

    @Test
    public void testLessThanNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            Range.lessThan(null);
        });
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
        this.toStringAndCheck(lessThan(), "<" + LOWER_VALUE);
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

    @Test
    public void testLessThanEqualsNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            Range.lessThanEquals(null);
        });
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
        this.toStringAndCheck(lessThanEquals(), "<=" + LOWER_VALUE);
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

    @Test
    public void testGreaterThanNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            Range.greaterThan(null);
        });
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
        this.toStringAndCheck(greaterThan(), ">" + LOWER_VALUE);
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

    @Test
    public void testGreaterThanEqualsNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            Range.greaterThanEquals(null);
        });
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
        this.toStringAndCheck(greaterThanEquals(), ">=" + LOWER_VALUE);
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
        assertSame(range, range.and(all), () -> range + " and with " + all);
        assertSame(range, all.and(range), () -> range + " and with " + all);
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

    private void andSingletonAndCheck(final Range<Integer> range) {
        final Range<Integer> singleton = singleton();
        assertSame(singleton, range.and(singleton), () -> range + " and with " + singleton);
        assertSame(singleton, singleton.and(range), () -> singleton + " and with " + range);
    }

    // and invalid.......................................................

    @Test
    public void testAndLessThanAndGreaterThanFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            lessThan().and(greaterThan());
        });
    }

    @Test
    public void testAndGreaterThanAndLessThanFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            greaterThan().and(lessThan());
        });
    }

    @Test
    public void testAndLessThanAndGreaterThanFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            Range.lessThan(122).and(Range.greaterThan(123));
        });
    }

    @Test
    public void testAndLessThanEqualAndGreaterThanFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            Range.lessThanEquals(122).and(Range.greaterThan(123));
        });
    }

    @Test
    public void testAndLessThanEqualAndSingletonFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            Range.lessThanEquals(122).and(Range.singleton(123));
        });
    }

    @Test
    public void testAndSingletonAndGreaterThanFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            Range.singleton(122).and(Range.greaterThan(123));
        });
    }

    @Test
    public void testAndSingletonAndGreaterThanEqualsFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            Range.singleton(122).and(Range.greaterThanEquals(123));
        });
    }

    @Test
    public void testAndNonOverlappingBoundedRangeBoundedRangeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Range.greaterThan(222).and(Range.lessThan(333))
                    .and(Range.greaterThan(888).and(Range.lessThan(999)));
        });
    }

    @Test
    public void testAndNonOverlappingBoundedRangeBoundedRangeFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            Range.greaterThan(222).and(Range.lessThan(333))
                    .and(Range.greaterThan(333).and(Range.lessThan(999)));
        });
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
        assertEquals(lower, intersected.lower, () -> range + " and " + other + " lower");
        assertEquals(upper, intersected.upper, () -> range + " and " + other + " upper");
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

    // isOverlapping.............................................................................................

    @Test
    public void testIsOverlappingpingNullFails() {
        assertThrows(NullPointerException.class, () -> {
            Range.all().isOverlapping(null);
        });
    }

    @Test
    public void testIsOverlappingWithout() {
        this.isOverlappingAndCheck(
                Range.greaterThan(5),
                Range.lessThan(3),
                false);
    }

    @Test
    public void testIsOverlappingWithout2() {
        this.isOverlappingAndCheck(
                Range.greaterThan(5),
                Range.lessThan(4),
                false);
    }

    @Test
    public void testIsOverlappingWithout3() {
        this.isOverlappingAndCheck(
                Range.lessThan(55),
                Range.greaterThan(55),
                false);
    }

    @Test
    public void testIsOverlappingWithout4() {
        this.isOverlappingAndCheck(
                Range.greaterThanEquals(5),
                Range.lessThanEquals(4),
                false);
    }

    @Test
    public void testIsOverlappingWithout5() {
        this.isOverlappingAndCheck(
                Range.greaterThanEquals(66).and(Range.lessThan(99)),
                Range.lessThan(44),
                false);
    }

    @Test
    public void testIsOverlappingWithout6() {
        this.isOverlappingAndCheck(
                Range.greaterThanEquals(55).and(Range.lessThan(99)),
                Range.lessThan(54),
                false);
    }

    @Test
    public void testIsOverlappingWithout7() {
        this.isOverlappingAndCheck(
                Range.greaterThanEquals(55).and(Range.lessThan(99)),
                Range.greaterThanEquals(44).and(Range.lessThan(54)),
                false);
    }

    @Test
    public void testIsOverlappingWithout8() {
        this.isOverlappingAndCheck(
                Range.singleton(44),
                Range.singleton(55),
                false);
    }

    @Test
    public void testIsOverlappingWithout9() {
        this.isOverlappingAndCheck(
                Range.lessThan(44),
                Range.greaterThan(44),
                false);
    }

    @Test
    public void testIsOverlappingWithout10() {
        this.isOverlappingAndCheck(
                Range.lessThanEquals(44),
                Range.greaterThan(44),
                false);
    }

    @Test
    public void testIsOverlappingWithout11() {
        this.isOverlappingAndCheck(
                Range.lessThan(44),
                Range.greaterThanEquals(44),
                false);
    }

    @Test
    public void testIsOverlapping() {
        this.isOverlappingAndCheck(
                Range.greaterThanEquals(55).and(Range.lessThan(99)),
                Range.greaterThanEquals(44).and(Range.lessThan(56)),
                true); // at 55
    }

    @Test
    public void testIsOverlapping1() {
        this.isOverlappingAndCheck(
                Range.greaterThanEquals(55),
                Range.greaterThanEquals(44).and(Range.lessThanEquals(55)),
                true); // at 55
    }

    @Test
    public void testIsOverlapping2() {
        this.isOverlappingAndCheck(
                Range.greaterThanEquals(55).and(Range.lessThan(99)),
                Range.greaterThanEquals(44),
                true); // at >=55
    }

    @Test
    public void testIsOverlapping3() {
        this.isOverlappingAndCheck(
                Range.all(),
                Range.greaterThanEquals(44),
                true); // at >=55
    }

    @Test
    public void testIsOverlapping4() {
        this.isOverlappingAndCheck(
                Range.singleton(55),
                Range.singleton(55),
                true); // at 55
    }

    private void isOverlappingAndCheck(final Range<Integer> first, final Range<Integer> other, final boolean expected) {
        this.isOverlappingAndCheck0(first, other, expected);
        this.isOverlappingAndCheck0(other, first, expected);
    }

    private void isOverlappingAndCheck0(final Range<Integer> first, final Range<Integer> other, final boolean expected) {
        assertEquals(expected, first.isOverlapping(other), () -> first + " " + other);

        boolean and;
        try {
            first.and(other);
            and = true;
        } catch (final Exception fail) {
            and = false;
        }
        assertEquals(expected,
                and,
                () -> first + " and " + other + " doesnt match " + first + " isOverlapping " + other);
    }

    // misc ...................................................................................................

    @Override
    public void testAllMethodsVisibility() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    // parse ....................................................................................................

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
        assertThrows(NullPointerException.class, () -> {
            Range.parse("1", ':', null);
        });
    }

    @Test
    public void testParseInvalidCharacterFails() {
        final String text = "1A:2!";

        final InvalidCharacterException thrown = assertThrows(InvalidCharacterException.class, () -> {
            Range.parse(text, ':', (s) -> {
                throw new InvalidCharacterException("2!", 1);
            });
        });
        assertEquals(text, thrown.text(), "text");
    }

    @Test
    public void testParseMissingSeparatorFails() {
        this.parseStringFails("1", new IllegalArgumentException("Missing separator \':\' in \"1\""));
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
        this.parseStringFails("1A:2", new IllegalArgumentException("java.lang.NumberFormatException: For input string: \"1A\""));
    }

    @Test
    public void testParseInvalidUpperRangeFails() {
        this.parseStringFails("1:2B", new IllegalArgumentException("java.lang.NumberFormatException: For input string: \"2B\""));
    }

    @Test
    public void testParseLowerAndUpper() {
        this.parseStringAndCheck("123:456", Range.greaterThanEquals(123).and(Range.lessThanEquals(456)));
    }

    @Test
    public void testParseLowerAndUpperDifferentSeparator() {
        assertEquals(Range.greaterThanEquals(123).and(Range.lessThanEquals(456)),
                Range.parse("123-456", '-', Integer::parseInt));
    }

    // HashCodeEqualsDefined.....................................................................................

    // all.....................................................................................

    @Test
    public void testEqualsAllSingleton() {
        this.checkNotEquals(Range.all(), Range.singleton(VALUE));
    }

    @Test
    public void testEqualsAllLessThan() {
        this.checkNotEquals(Range.all(), Range.lessThan(VALUE));
    }

    @Test
    public void testEqualsAllLessThanEquals() {
        this.checkNotEquals(Range.all(), Range.lessThanEquals(VALUE));
    }

    @Test
    public void testEqualsAllGreaterThan() {
        this.checkNotEquals(Range.greaterThan(VALUE));
    }

    @Test
    public void testEqualsAllGreaterThanEquals() {
        this.checkNotEquals(Range.all(), Range.greaterThanEquals(VALUE));
    }

    // lessThan.....................................................................................

    @Test
    public void testEqualsLessThan() {
        final Range<Integer> range = Range.lessThanEquals(VALUE);
        this.checkEqualsAndHashCode(range, range);
    }

    @Test
    public void testEqualsLessThanAll() {
        this.checkNotEquals(Range.lessThan(VALUE), Range.all());
    }

    @Test
    public void testEqualsLessThanSingleton() {
        this.checkNotEquals(Range.lessThan(VALUE), Range.singleton(VALUE));
    }

    @Test
    public void testEqualsLessThanLessThan() {
        this.checkEqualsAndHashCode(Range.lessThan(VALUE), Range.lessThan(VALUE));
    }

    @Test
    public void testEqualsLessThanLessThanEquals() {
        this.checkNotEquals(Range.lessThan(VALUE), Range.lessThanEquals(VALUE));
    }

    @Test
    public void testEqualsLessThanGreaterThan() {
        this.checkNotEquals(Range.lessThan(VALUE), Range.greaterThan(VALUE));
    }

    @Test
    public void testEqualsLessThanGreaterThanEquals() {
        this.checkNotEquals(Range.lessThan(VALUE), Range.greaterThanEquals(VALUE));
    }

    // lessThan...........................................................................

    @Test
    public void testEqualsLessThanEqualsAll() {
        this.checkNotEquals(Range.lessThanEquals(VALUE), Range.all());
    }

    @Test
    public void testEqualsLessThanEqualsSingleton() {
        this.checkNotEquals(Range.lessThanEquals(VALUE), Range.singleton(VALUE));
    }

    @Test
    public void testEqualsLessThanEqualsLessThan() {
        this.checkNotEquals(Range.lessThanEquals(VALUE), Range.lessThan(VALUE));
    }

    @Test
    public void testEqualsLessThanEqualsLessThanEquals() {
        this.checkEqualsAndHashCode(Range.lessThanEquals(VALUE), Range.lessThanEquals(VALUE));
    }

    @Test
    public void testEqualsLessThanEqualsGreaterThan() {
        this.checkNotEquals(Range.lessThanEquals(VALUE), Range.greaterThan(VALUE));
    }

    @Test
    public void testEqualsLessThanEqualsGreaterThanEquals() {
        this.checkNotEquals(Range.lessThanEquals(VALUE), Range.greaterThanEquals(VALUE));
    }

    // JsonNodeContext..................................................................................................

    @Test
    public void testFromJsonNodeMissingLowerBoundFails() {
        this.fromJsonNodeFails("{\n" +
                "  \"upper-bound\": {\n" +
                "    \"all\": {}\n" +
                "  }\n" +
                "}",
                FromJsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeMissingUpperBoundFails() {
        this.fromJsonNodeFails("{\n" +
                "  \"lower-bound\": {\n" +
                "    \"all\": {}\n" +
                "  }\n" +
                "}",
                FromJsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeLowerBoundUnknownPropertyFails() {
        this.fromJsonNodeFails("{\n" +
                "  \"lower-bound\": {\n" +
                "    \"unknown\": {}\n" +
                "  }\n" +
                "}",
                FromJsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeAllUnknownPropertyFails() {
        this.fromJsonNodeFails("{\n" +
                "  \"lower-bound\": {\n" +
                "    \"all\": {\n" +
                "       \"unknown\": 1\n" +
                "    }\n" +
                "  }\n" +
                "}",
                FromJsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeUnknownPropertyFails() {
        this.fromJsonNodeFails("{\"unknown\": true}",
                FromJsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeAll() {
        this.fromJsonNodeAndCheck("{\n" +
                        "  \"lower-bound\": {\n" +
                        "    \"all\": {}\n" +
                        "  },\n" +
                        "  \"upper-bound\": {\n" +
                        "    \"all\": {}\n" +
                        "  }\n" +
                        "}",
                Range.all());
    }

    @Test
    public void testToJsonNodeAll() {
        this.toJsonNodeAndCheck(Range.all(),
                "{\n" +
                        "  \"lower-bound\": {\n" +
                        "    \"all\": {}\n" +
                        "  },\n" +
                        "  \"upper-bound\": {\n" +
                        "    \"all\": {}\n" +
                        "  }\n" +
                        "}");
    }

    @Test
    public void testFromJsonNodeLowerInclusive() {
        this.fromJsonNodeAndCheck("{\n" +
                        "  \"lower-bound\": {\n" +
                        "    \"inclusive\": {\n" +
                        "      \"type\": \"int\",\n" +
                        "      \"value\": 1\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"upper-bound\": {\n" +
                        "    \"all\": {}\n" +
                        "  }\n" +
                        "}",
                Range.greaterThanEquals(1));
    }

    @Test
    public void testToJsonNodeLowerInclusive() {
        this.toJsonNodeAndCheck(Range.greaterThanEquals(1),
                "{\n" +
                        "  \"lower-bound\": {\n" +
                        "    \"inclusive\": {\n" +
                        "      \"type\": \"int\",\n" +
                        "      \"value\": 1\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"upper-bound\": {\n" +
                        "    \"all\": {}\n" +
                        "  }\n" +
                        "}");
    }

    @Test
    public void testFromJsonNodeUpperInclusive() {
        this.fromJsonNodeAndCheck("{\n" +
                        "  \"lower-bound\": {\n" +
                        "    \"all\": {}\n" +
                        "  },\n" +
                        "  \"upper-bound\": {\n" +
                        "    \"exclusive\": {\n" +
                        "      \"type\": \"int\",\n" +
                        "      \"value\": 1\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                Range.lessThan(1));
    }

    @Test
    public void testToJsonNodeUpperInclusive() {
        this.toJsonNodeAndCheck(Range.lessThan(1),
                "{\n" +
                        "  \"lower-bound\": {\n" +
                        "    \"all\": {}\n" +
                        "  },\n" +
                        "  \"upper-bound\": {\n" +
                        "    \"exclusive\": {\n" +
                        "      \"type\": \"int\",\n" +
                        "      \"value\": 1\n" +
                        "    }\n" +
                        "  }\n" +
                        "}");
    }

    @Test
    public void testFromJsonNodeLowerInclusiveAndUpperInclusive() {
        this.fromJsonNodeAndCheck("{\n" +
                        "  \"lower-bound\": {\n" +
                        "    \"inclusive\": {\n" +
                        "      \"type\": \"int\",\n" +
                        "      \"value\": 1\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"upper-bound\": {\n" +
                        "    \"inclusive\": {\n" +
                        "      \"type\": \"int\",\n" +
                        "      \"value\": 22\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                Range.greaterThanEquals(1).and(Range.lessThanEquals(22)));
    }

    @Test
    public void testToJsonNodeLowerInclusiveAndUpperInclusive() {
        this.toJsonNodeAndCheck(Range.greaterThanEquals(1).and(Range.lessThanEquals(22)),
                "{\n" +
                        "  \"lower-bound\": {\n" +
                        "    \"inclusive\": {\n" +
                        "      \"type\": \"int\",\n" +
                        "      \"value\": 1\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"upper-bound\": {\n" +
                        "    \"inclusive\": {\n" +
                        "      \"type\": \"int\",\n" +
                        "      \"value\": 22\n" +
                        "    }\n" +
                        "  }\n" +
                        "}");
    }

    @Test
    public void testFromJsonNodeLowerExclusiveAndUpperExclusive() {
        this.fromJsonNodeAndCheck("{\n" +
                        "  \"lower-bound\": {\n" +
                        "    \"exclusive\": {\n" +
                        "      \"type\": \"int\",\n" +
                        "      \"value\": 1\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"upper-bound\": {\n" +
                        "    \"exclusive\": {\n" +
                        "      \"type\": \"int\",\n" +
                        "      \"value\": 22\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                Range.greaterThan(1).and(Range.lessThan(22)));
    }

    @Test
    public void testToJsonNodeLowerExclusiveAndUpperExclusive() {
        this.toJsonNodeAndCheck(Range.greaterThan(1).and(Range.lessThan(22)),
                "{\n" +
                        "  \"lower-bound\": {\n" +
                        "    \"exclusive\": {\n" +
                        "      \"type\": \"int\",\n" +
                        "      \"value\": 1\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"upper-bound\": {\n" +
                        "    \"exclusive\": {\n" +
                        "      \"type\": \"int\",\n" +
                        "      \"value\": 22\n" +
                        "    }\n" +
                        "  }\n" +
                        "}");
    }

    @Test
    public void testToJsonNodeRoundtripAll() {
        this.toJsonNodeRoundTripTwiceAndCheck(Range.all());
    }

    @Test
    public void testToJsonNodeRoundtripSingleton() {
        this.toJsonNodeRoundTripTwiceAndCheck(Range.singleton(123));
    }

    @Test
    public void testToJsonNodeRoundtripExclusiveExclusive() {
        this.toJsonNodeRoundTripTwiceAndCheck(Range.greaterThan(123).and(Range.greaterThan(456)));
    }

    @Test
    public void testToJsonNodeRoundtripExclusiveInclusive() {
        this.toJsonNodeRoundTripTwiceAndCheck(Range.greaterThan(123).and(Range.greaterThanEquals(456)));
    }

    @Test
    public void testToJsonNodeRoundtripInclusiveExclusive() {
        this.toJsonNodeRoundTripTwiceAndCheck(Range.greaterThanEquals(123).and(Range.greaterThan(456)));
    }

    @Test
    public void testToJsonNodeRoundtripInclusiveInclusive() {
        this.toJsonNodeRoundTripTwiceAndCheck(Range.greaterThanEquals(123).and(Range.greaterThanEquals(456)));
    }

    // RangeVisitor....................................................................................................

    @Test
    public void testAcceptGreaterThan() {
        final StringBuilder visited = new StringBuilder();

        final Integer lower = 55;
        final Range<Integer> range = Range.greaterThan(lower);

        range.accept(new FakeRangeVisitor<Integer>() {

            @Override
            protected Visiting startVisit(final Range<Integer> r) {
                assertSame(range, r);
                visited.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected Visiting startBetween(final RangeBound<Integer> lowerBound, final RangeBound<Integer> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("2");
                return Visiting.CONTINUE;
            }

            @Override
            protected void lowerBoundExclusive(final Integer value) {
                assertSame(lower, value, "lowerBoundExclusive");
                visited.append("3");
            }

            @Override
            protected void upperBoundAll() {
                visited.append("4");
            }

            @Override
            protected void endBetween(final RangeBound<Integer> lowerBound, final RangeBound<Integer> upperBound) {
                check(range, lowerBound, upperBound);
                visited.append("5");
            }

            @Override
            protected void endVisit(final Range<Integer> range) {
                visited.append("6");
                super.endVisit(range);
            }
        });

        assertEquals(visited.toString(), "123456");
    }

    // Parse ........................................................................................................

    public Range<Integer> parseString(final String text) {
        return Range.parse(text, ':', Integer::valueOf);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException expected) {
        return expected;
    }

    // helper...........................................................................................

    @Override
    public Range<Integer> createPredicate() {
        return Range.all();
    }

    @Override
    public Range<Integer> createObject() {
        return this.createPredicate();
    }

    private static Range<Integer> all() {
        return Range.all();
    }

    private void check(final Range<Integer> range, final RangeBound<Integer> lower, final RangeBound<Integer> upper) {
        assertEquals(lower, range.lowerBound(), () -> "lower " + range);
        assertEquals(upper, range.upperBound(), () -> "upper " + range);
    }

    // ClassTesting.........................................................................................

    @Override
    public Class<Range<Integer>> type() {
        return Cast.to(Range.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // JsonNodeMapTesting...............................................................................................

    @Override
    public Range<Integer> createJsonNodeMappingValue() {
        return this.createPredicate();
    }

    @Override
    public Range<Integer> fromJsonNode(final JsonNode from,
                                       final FromJsonNodeContext context) {
        return Range.fromJsonNode(from, context);
    }
}
