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
 */

package walkingkooka.compare;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

import java.util.Set;
import java.util.SortedSet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

final public class TextWithNumbersCharSequenceComparatorTest
        extends ComparatorTestCase<TextWithNumbersCharSequenceComparator<String>, String> {

    @Test(expected = NullPointerException.class)
    public void testWithNullCaseSensitivityFails() {
        TextWithNumbersCharSequenceComparator.with(null, CharPredicates.fake());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullDecimalFails() {
        TextWithNumbersCharSequenceComparator.with(CaseSensitivity.INSENSITIVE, null);
    }

    // testCaseSensitives CASE SENSITIVE

    @Test
    public void testCaseSensitiveLessWithoutDigits() {
        this.compareAndCheckLessCaseSensitive("a", "b");
    }

    @Test
    public void testCaseSensitiveSameWithoutDigits() {
        this.compareAndCheckEqualCaseSensitive("a");
    }

    @Test
    public void testCaseSensitiveSameWithoutDigits2() {
        this.compareAndCheckEqualCaseSensitive("abc");
    }

    @Test
    public void testCaseSensitiveSameWithDigits() {
        this.compareAndCheckEqualCaseSensitive("abc123");
    }

    @Test
    public void testCaseSensitiveSameWithOnlyDigits() {
        this.compareAndCheckEqualCaseSensitive("abc123");
    }

    @Test
    public void testCaseSensitiveDigitAndNon() {
        this.compareAndCheckLessCaseSensitive("0", "a");
    }

    @Test
    public void testCaseSensitiveSameNumericalValueDifferentDigits() {
        this.compareAndCheckEqualCaseSensitive("1", "01");
    }

    @Test
    public void testCaseSensitiveSameNumericalValueDifferentDigits2() {
        this.compareAndCheckEqualCaseSensitive("123", "00123");
    }

    @Test
    public void testCaseSensitiveDifferentAndUnequalNumericalValue() {
        this.compareAndCheckLessCaseSensitive("2", "3");
    }

    @Test
    public void testCaseSensitiveDifferentAndUnequalNumericalValue2() {
        this.compareAndCheckLessCaseSensitive("02", "3");
    }

    @Test
    public void testCaseSensitiveDifferentAndUnequalNumericalValue3() {
        this.compareAndCheckLessCaseSensitive("2", "10");
    }

    @Test
    public void testCaseSensitiveDifferentAndUnequalNumericalValue4() {
        this.compareAndCheckLessCaseSensitive("02", "10");
    }

    @Test
    public void testCaseSensitiveDifferentAndUnequalNumericalValue5() {
        this.compareAndCheckLessCaseSensitive("02", "0010");
    }

    @Test
    public void testCaseSensitiveDifferentAndUnequalNumericalValue6() {
        this.compareAndCheckLessCaseSensitive("002", "3");
    }

    @Test
    public void testCaseSensitiveDifferentAndUnequalNumericalValue7() {
        this.compareAndCheckLessCaseSensitive("023", "034500");
    }

    @Test
    public void testCaseSensitiveDifferentAndUnequalNumericalValue8() {
        this.compareAndCheckLessCaseSensitive("23", "034500");
    }

    @Test
    public void testCaseSensitiveUnequalNumericalFollowedByEqualLetters() {
        this.compareAndCheckEqual("002 after", "2 after");
    }

    @Test
    public void testCaseSensitiveUnequalNumericalFollowedByEqualLetters2() {
        this.compareAndCheckLessCaseSensitive("002 after", "3 after");
    }

    @Test
    public void testCaseSensitiveSameNumericalValueDifferentDigitsAndLetters() {
        this.compareAndCheckEqual("before 1 after", "before 01 after");
    }

    @Test
    public void testCaseSensitiveUnequalLettersAfterSameNumber() {
        this.compareAndCheckLessCaseSensitive("before 1 after", "before 01 zebra");
    }

    @Test
    public void testCaseSensitiveUnequalLettersBeforeEqualNumber() {
        this.compareAndCheckLessCaseSensitive("before 1 after", "zebra 01 after");
    }

    @Test
    public void testCaseSensitiveDifferentNumberAfterLetters() {
        this.compareAndCheckLessCaseSensitive("before 12 after", "before 34 after");
    }

    @Test
    public void testCaseSensitiveDifferentNumberAfterLetters2() {
        this.compareAndCheckLessCaseSensitive("before 012 after", "before 0034 after");
    }

    @Test
    public void testCaseSensitiveExactDecimal() {
        this.compareAndCheckEqualCaseSensitive("1$2", "1$2");
    }

    @Test
    public void testCaseSensitiveLessDecimal() {
        this.compareAndCheckLessCaseSensitive("12$3", "12$30");
    }

    @Test
    public void testCaseSensitiveLessDecimalAfterLetters() {
        this.compareAndCheckLessCaseSensitive("before 1$2 after", "before 1$23 after");
    }

    @Test
    public void testCaseSensitiveLessAfterNumber() {
        this.compareAndCheckLessCaseSensitive("1 after", "1 zebra");
    }

    @Test
    public void testCaseSensitiveLessAfterNumber2() {
        this.compareAndCheckLessCaseSensitive("01 after", "1 zebra");
    }

    @Test
    public void testCaseSensitiveLessAfterNumber3() {
        this.compareAndCheckLessCaseSensitive("1 after", "01 zebra");
    }

    @Test
    public void testCaseSensitiveLessAfterNumber4() {
        this.compareAndCheckLessCaseSensitive("01 after", "01 zebra");
    }

    private void compareAndCheckEqualCaseSensitive(final String value) {
        this.compareAndCheckEqualCaseSensitive(value, value);
    }

    private void compareAndCheckEqualCaseSensitive(final String value1, final String value2) {
        this.compareAndCheckEqual(this.createComparatorCaseSensitive(), value1, value2);
    }

    private void compareAndCheckLessCaseSensitive(final String value1, final String value2) {
        this.compareAndCheckLess(this.createComparatorCaseSensitive(), value1, value2);
    }

    @Test
    public void testCaseSensitiveSort() {
        final Set<String> sorted = Sets.sorted(this.createComparatorCaseSensitive());
        sorted.add("0");
        sorted.add("100");
        sorted.add("0200");
        sorted.add("3");
        sorted.add("40");
        sorted.add("apple");

        assertArrayEquals(new String[]{"0", "3", "40", "100", "0200", "apple"},
                sorted.toArray(new String[0]));
    }

    @Test
    public void testCaseSensitiveSort2() {
        final Set<String> sorted = Sets.sorted(this.createComparatorCaseSensitive());
        sorted.add("apple");
        sorted.add("40");
        sorted.add("0200");
        sorted.add("0");
        sorted.add("100");
        sorted.add("3");

        assertArrayEquals(new String[]{"0", "3", "40", "100", "0200", "apple"},
                sorted.toArray(new String[0]));
    }

    // INSENSITIVE

    @Test
    public void testCaseInsensitiveSameWithoutDigits() {
        this.compareAndCheckEqualCaseInsensitive("a", "A");
    }

    @Test
    public void testCaseInsensitiveSameWithoutDigits2() {
        this.compareAndCheckEqualCaseInsensitive("same");
    }

    @Test
    public void testCaseInsensitiveSameDifferentCaseWithoutDigits() {
        this.compareAndCheckEqualCaseInsensitive("same", "SAME");
    }

    @Test
    public void testCaseInsensitiveSameWithDigits() {
        this.compareAndCheckEqualCaseInsensitive("abc123");
    }

    @Test
    public void testCaseInsensitiveSameWithOnlyDigits() {
        this.compareAndCheckEqualCaseInsensitive("123");
    }

    @Test
    public void testCaseInsensitiveSameNumericalValueDifferentDigits() {
        this.compareAndCheckEqualCaseInsensitive("1", "01");
    }

    @Test
    public void testCaseInsensitiveSameNumericalValueDifferentDigits2() {
        this.compareAndCheckEqualCaseInsensitive("123", "00123");
    }

    @Test
    public void testCaseInsensitiveDifferentAndUnequalNumericalValue() {
        this.compareAndCheckLessCaseInsensitive("02", "3");
    }

    @Test
    public void testCaseInsensitiveDifferentAndUnequalNumericalValue2() {
        this.compareAndCheckLessCaseInsensitive("002", "3");
    }

    @Test
    public void testCaseInsensitiveDifferentAndUnequalNumericalValue3() {
        this.compareAndCheckLessCaseInsensitive("023", "034500");
    }

    @Test
    public void testCaseInsensitiveDifferentAndUnequalNumericalValue4() {
        this.compareAndCheckLessCaseInsensitive("23", "034500");
    }

    @Test
    public void testCaseInsensitiveUnequalNumericalFollowedByEqualLetters() {
        this.compareAndCheckEqualCaseInsensitive("002 after", "2 after");
    }

    @Test
    public void testCaseInsensitiveUnequalNumericalFollowedByEqualLetters2() {
        this.compareAndCheckLessCaseInsensitive("002 after", "3 AFTER");
    }

    @Test
    public void testCaseInsensitiveSameNumericalValueDifferentDigitsAndLetters() {
        this.compareAndCheckEqual("before 1 after", "before 01 after");
    }

    @Test
    public void testCaseInsensitiveUnequalLettersAfterSameNumber() {
        this.compareAndCheckLessCaseInsensitive("before 1 after", "before 01 ZEBRA");
    }

    @Test
    public void testCaseInsensitiveUnequalLettersBeforeEqualNumber() {
        this.compareAndCheckLessCaseInsensitive("before 1 after", "ZEBRA 01 after");
    }

    @Test
    public void testCaseInsensitiveDifferentNumberAfterLetters() {
        this.compareAndCheckLessCaseInsensitive("before 012 after", "BEFORE 0034 after");
    }

    @Test
    public void testCaseInsensitiveExactDecimal() {
        this.compareAndCheckEqualCaseSensitive("1$2", "1$2");
    }

    @Test
    public void testCaseInsensitiveLessDecimal() {
        this.compareAndCheckLessCaseInsensitive("12$3", "12$30");
    }

    @Test
    public void testCaseInsensitiveLessDecimal2() {
        this.compareAndCheckLessCaseInsensitive("12$03", "12$005");
    }

    @Test
    public void testCaseInsensitiveLessDecimal3() {
        this.compareAndCheckLessCaseInsensitive("012$03", "0012$005");
    }

    @Test
    public void testCaseInsensitiveLessDecimalAfterLetters() {
        this.compareAndCheckLessCaseInsensitive("before 1$2 after", "BEFORE 1$23 after");
    }

    private void compareAndCheckEqualCaseInsensitive(final String value) {
        this.compareAndCheckEqualCaseInsensitive(value, value);
    }

    private void compareAndCheckEqualCaseInsensitive(final String value1, final String value2) {
        this.compareAndCheckEqual(this.createComparatorCaseInsensitive(), value1, value2);
    }

    private void compareAndCheckLessCaseInsensitive(final String value1, final String value2) {
        this.compareAndCheckLess(this.createComparatorCaseInsensitive(), value1, value2);
    }

    @Test
    public void testCaseInsensitiveLessAfterNumber() {
        this.compareAndCheckLessCaseInsensitive("1 after", "1 ZEBRA");
    }

    @Test
    public void testCaseInsensitiveLessAfterNumber2() {
        this.compareAndCheckLessCaseInsensitive("01 after", "1 ZEBRA");
    }

    @Test
    public void testCaseInsensitiveLessAfterNumber3() {
        this.compareAndCheckLessCaseInsensitive("1 after", "01 ZEBRA");
    }

    @Test
    public void testCaseInsensitiveLessAfterNumber4() {
        this.compareAndCheckLessCaseInsensitive("01 after", "01 ZEBRA");
    }

    @Test
    public void testCaseInsensitiveSort() {
        final SortedSet<String> sorted = Sets.sorted(this.createComparatorCaseInsensitive());
        sorted.add("0");
        sorted.add("100");
        sorted.add("0200");
        sorted.add("3");
        sorted.add("40");
        sorted.add("apple");

        assertArrayEquals(new String[]{"0", "3", "40", "100", "0200", "apple"},
                sorted.toArray(new String[0]));
    }

    @Test
    public void testCaseInsensitiveSort2() {
        final SortedSet<String> sorted = Sets.sorted(this.createComparatorCaseInsensitive());
        sorted.add("apple");
        sorted.add("40");
        sorted.add("0200");
        sorted.add("0");
        sorted.add("100");
        sorted.add("3");

        assertArrayEquals(new String[]{"0", "3", "40", "100", "0200", "apple"},
                sorted.toArray(new String[0]));
    }

    @Test
    public void testToString() {
        assertEquals("text with numbers(CASE SENSITIVE)",
                this.createComparator().toString());
    }

    @Override
    protected TextWithNumbersCharSequenceComparator<String> createComparator() {
        return this.createComparatorCaseSensitive();
    }

    private TextWithNumbersCharSequenceComparator<String> createComparatorCaseSensitive() {
        return this.createComparator(CaseSensitivity.SENSITIVE);
    }

    private TextWithNumbersCharSequenceComparator<String> createComparatorCaseInsensitive() {
        return this.createComparator(CaseSensitivity.INSENSITIVE);
    }

    private TextWithNumbersCharSequenceComparator<String> createComparator(
            final CaseSensitivity sensitivity) {
        return TextWithNumbersCharSequenceComparator.with(sensitivity, decimal());
    }

    private CharPredicate decimal() {
        return CharPredicates.is('$');
    }

    @Override
    protected Class<TextWithNumbersCharSequenceComparator<String>> type() {
        return Cast.to(TextWithNumbersCharSequenceComparator.class);
    }
}
