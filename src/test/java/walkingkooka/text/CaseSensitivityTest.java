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

package walkingkooka.text;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import walkingkooka.compare.Comparators;
import walkingkooka.predicate.PredicateTesting;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.util.SystemProperty;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertSame;

final public class CaseSensitivityTest implements ClassTesting2<CaseSensitivity>,
        PredicateTesting {

    @Test
    public void testInvertSensitive() {
        this.invertAndCheck(CaseSensitivity.SENSITIVE, CaseSensitivity.INSENSITIVE);
    }

    @Test
    public void testInvertInsensitive() {
        this.invertAndCheck(CaseSensitivity.INSENSITIVE, CaseSensitivity.SENSITIVE);
    }

    private void invertAndCheck(final CaseSensitivity sensitivity, final CaseSensitivity inverted) {
        assertSame(inverted, sensitivity.invert(), "Invert " + sensitivity);
    }

    // compare

    @Test
    public void testCompareSensitiveDifferent() {
        this.compareAndCheck(CaseSensitivity.SENSITIVE, 'a', 'b', Comparators.LESS);
    }

    @Test
    public void testCompareSensitiveDifferent2() {
        this.compareAndCheck(CaseSensitivity.SENSITIVE, 'b', 'a', Comparators.MORE);
    }

    @Test
    public void testCompareSensitiveDifferentSameButDifferentCase() {
        this.compareAndCheck(CaseSensitivity.SENSITIVE, 'a', 'A', Comparators.MORE);
    }

    @Test
    public void testCompareSensitiveSame() {
        this.compareAndCheck(CaseSensitivity.SENSITIVE, 'a', 'a', Comparators.EQUAL);
    }

    @Test
    public void testCompareInsensitiveDifferent() {
        this.compareAndCheck(CaseSensitivity.INSENSITIVE, 'a', 'b', Comparators.LESS);
    }

    @Test
    public void testCompareInsensitiveDifferent2() {
        this.compareAndCheck(CaseSensitivity.INSENSITIVE, 'b', 'a', Comparators.MORE);
    }

    @Test
    public void testCompareInsensitiveDifferentSameButDifferentCase() {
        this.compareAndCheck(CaseSensitivity.INSENSITIVE, 'a', 'A', Comparators.EQUAL);
    }

    @Test
    public void testCompareInsensitiveSame() {
        this.compareAndCheck(CaseSensitivity.INSENSITIVE, 'a', 'a', Comparators.EQUAL);
    }

    private void compareAndCheck(final CaseSensitivity sensitivity, final char c, final char other,
                                 final int expected) {
        final int actual = sensitivity.compare(c, other);
        if (Comparators.normalize(expected) != Comparators.normalize(actual)) {
            this.checkEquals(expected,
                    actual,
                    () -> sensitivity + " comparing " + CharSequences.quoteAndEscape(c) + "," + CharSequences.quoteAndEscape(other));
        }
    }

    // equals

    @Test
    public void testEqualsSensitiveDifferentLength() {
        this.equalsAndCheck(CaseSensitivity.SENSITIVE, "apple", "different", false);
    }

    @Test
    public void testEqualsSensitiveSameCase() {
        this.equalsAndCheck(CaseSensitivity.SENSITIVE, "apple", "apple", true);
    }

    @Test
    public void testEqualsSensitiveDifferentCase() {
        this.equalsAndCheck(CaseSensitivity.SENSITIVE, "apple", "APPLE", false);
    }

    @Test
    public void testEqualsInsensitiveDifferentLength() {
        this.equalsAndCheck(CaseSensitivity.INSENSITIVE, "apple", "different", false);
    }

    @Test
    public void testEqualsInsensitiveSameCase() {
        this.equalsAndCheck(CaseSensitivity.INSENSITIVE, "apple", "apple", true);
    }

    @Test
    public void testEqualsInsensitiveDifferentCase() {
        this.equalsAndCheck(CaseSensitivity.INSENSITIVE, "apple", "APPLE", true);
    }

    private void equalsAndCheck(final CaseSensitivity sensitivity,
                                final CharSequence chars,
                                final CharSequence otherChars,
                                final boolean expected) {
        final boolean result = sensitivity.equals(chars, otherChars);
        if (expected != result) {
            this.checkEquals(expected,
                    result,
                    () -> sensitivity + " equals( " + CaseSensitivityTest.quote(chars) + "," + CaseSensitivityTest.quote(otherChars) + ")");
        }
    }

    // startsWith sensitivity

    @Test
    public void testStartsWithInsensitiveDifferent() {
        this.startsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "diffe", false);
    }

    @Test
    public void testStartsWithInsensitiveStartsWithLonger() {
        this.startsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "longer", false);
    }

    @Test
    public void testStartsWithInsensitiveStartsWithContainsCharsLonger() {
        this.startsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "apple.longer", false);
    }

    @Test
    public void testStartsWithInsensitiveEqual() {
        this.startsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "apple", true);
    }

    @Test
    public void testStartsWithInsensitiveEqualDifferentCase() {
        this.startsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "APPLE", true);
    }

    @Test
    public void testStartsWithInsensitiveOneLetterSameCase() {
        this.startsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "a", true);
    }

    @Test
    public void testStartsWithInsensitiveOneLetterDifferentCase() {
        this.startsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "A", true);
    }

    @Test
    public void testStartsWithInsensitiveTwoLettersSameCase() {
        this.startsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "ap", true);
    }

    @Test
    public void testStartsWithInsensitiveTwoLettersDifferentCase() {
        this.startsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "AP", true);
    }

    @Test
    public void testStartsWithInsensitiveOneLetterFalsePositiveSameCase() {
        this.startsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "a@", false);
    }

    @Test
    public void testStartsWithInsensitiveOneLetterFalsePositiveDifferentCase() {
        this.startsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "A@", false);
    }

    @Test
    public void testStartsWithInsensitiveTwoLettersFalsePositiveSameCase() {
        this.startsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "ap@", false);
    }

    @Test
    public void testStartsWithInsensitiveTwoLettersFalsePositiveDifferentCase() {
        this.startsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "AP@", false);
    }

    // startsWith sensitive

    @Test
    public void testStartsWithSensitiveDifferent() {
        this.startsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "diffe", false);
    }

    @Test
    public void testStartsWithSensitiveStartsWithLonger() {
        this.startsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "longer", false);
    }

    @Test
    public void testStartsWithSensitiveStartsWithContainsCharsLonger() {
        this.startsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "apple.longer", false);
    }

    @Test
    public void testStartsWithSensitiveEqual() {
        this.startsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "apple", true);
    }

    @Test
    public void testStartsWithSensitiveEqualDifferentCase() {
        this.startsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "APPLE", false);
    }

    @Test
    public void testStartsWithSensitiveOneLetterSameCase() {
        this.startsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "a", true);
    }

    @Test
    public void testStartsWithSensitiveOneLetterDifferentCase() {
        this.startsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "A", false);
    }

    @Test
    public void testStartsWithSensitiveTwoLettersSameCase() {
        this.startsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "ap", true);
    }

    @Test
    public void testStartsWithSensitiveTwoLettersDifferentCase() {
        this.startsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "AP", false);
    }

    @Test
    public void testStartsWithSensitiveOneLetterFalsePositiveSameCase() {
        this.startsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "a@", false);
    }

    @Test
    public void testStartsWithSensitiveOneLetterFalsePositiveDifferentCase() {
        this.startsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "A@", false);
    }

    @Test
    public void testStartsWithSensitiveTwoLettersFalsePositiveSameCase() {
        this.startsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "ap@", false);
    }

    @Test
    public void testStartsWithSensitiveTwoLettersFalsePositiveDifferentCase() {
        this.startsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "AP@", false);
    }

    private void startsWithAndCheck(final CaseSensitivity sensitivity,
                                    final CharSequence chars,
                                    final CharSequence startsWith,
                                    final boolean expected) {
        final boolean result = sensitivity.startsWith(
                chars,
                startsWith
        );

        if (expected != result) {
            this.checkEquals(
                    expected,
                    result,
                    () -> sensitivity +
                            " startsWith( " +
                            CaseSensitivityTest.quote(chars) +
                            "," +
                            CaseSensitivityTest.quote(startsWith) +
                            ")"
            );
        }
    }

    @Test
    public void testStartsWithOffsetNegativeFails() {
        this.startsWithAndCheck(
                CaseSensitivity.SENSITIVE,
                "abc",
                "a",
                -1,
                false
        );
    }

    @Test
    public void testStartsWithOffsetOutOfBounds() {
        this.startsWithAndCheck(
                CaseSensitivity.SENSITIVE,
                "abc",
                "a",
                3,
                false
        );
    }

    @Test
    public void testStarsWithSensitiveOffsetTrue() {
        this.startsWithAndCheck(
                CaseSensitivity.SENSITIVE,
                "abcdef",
                "cde",
                2,
                true
        );
    }

    @Test
    public void testStarsWithSensitiveOffsetFalse() {
        this.startsWithAndCheck(
                CaseSensitivity.SENSITIVE,
                "abcdef",
                "cde",
                1,
                false
        );
    }

    @Test
    public void testStarsWithSensitiveOffsetFalse2() {
        this.startsWithAndCheck(
                CaseSensitivity.SENSITIVE,
                "abcdef",
                "cdX",
                2,
                false
        );
    }

    @Test
    public void testStarsWithSensitiveOffsetIncomplete() {
        this.startsWithAndCheck(
                CaseSensitivity.SENSITIVE,
                "abcdef",
                "cdefg",
                2,
                false
        );
    }

    @Test
    public void testStarsWithInsensitiveOffsetTrue() {
        this.startsWithAndCheck(
                CaseSensitivity.INSENSITIVE,
                "abcdef",
                "CDE",
                2,
                true
        );
    }

    @Test
    public void testStarsWithInsensitiveOffsetTrue2() {
        this.startsWithAndCheck(
                CaseSensitivity.INSENSITIVE,
                "abcdef",
                "cdE",
                2,
                true
        );
    }

    @Test
    public void testStarsWithInsensitiveOffsetFalse() {
        this.startsWithAndCheck(
                CaseSensitivity.INSENSITIVE,
                "abcdef",
                "CDE",
                1,
                false
        );
    }

    @Test
    public void testStarsWithInsensitiveOffsetFalse2() {
        this.startsWithAndCheck(
                CaseSensitivity.INSENSITIVE,
                "abcdef",
                "CDX",
                2,
                false
        );
    }

    @Test
    public void testStarsWithInsensitiveOffsetIncomplete() {
        this.startsWithAndCheck(
                CaseSensitivity.INSENSITIVE,
                "abcdef",
                "CDEFG",
                2,
                false
        );
    }

    private void startsWithAndCheck(final CaseSensitivity sensitivity,
                                    final CharSequence chars,
                                    final CharSequence startsWith,
                                    final int offset,
                                    final boolean expected) {
        final boolean result = sensitivity.startsWith(
                chars,
                startsWith,
                offset
        );

        if (expected != result) {
            this.checkEquals(
                    expected,
                    result,
                    () -> sensitivity +
                            " startsWith( " +
                            CaseSensitivityTest.quote(chars) +
                            "," +
                            CaseSensitivityTest.quote(startsWith) +
                            "," +
                            offset +
                            ")"
            );
        }
    }

    // endsWith sensitivity

    @Test
    public void testEndsWithInsensitiveDifferent() {
        this.endsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "diffe", false);
    }

    @Test
    public void testEndsWithInsensitiveEndsWithLonger() {
        this.endsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "longer", false);
    }

    @Test
    public void testEndsWithInsensitiveEndsWithContainsCharsLonger() {
        this.endsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "longer.apple", false);
    }

    @Test
    public void testEndsWithInsensitiveEqual() {
        this.endsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "apple", true);
    }

    @Test
    public void testEndsWithInsensitiveEqualDifferentCase() {
        this.endsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "APPLE", true);
    }

    @Test
    public void testEndsWithInsensitiveOneLetterSameCase() {
        this.endsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "e", true);
    }

    @Test
    public void testEndsWithInsensitiveOneLetterDifferentCase() {
        this.endsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "E", true);
    }

    @Test
    public void testEndsWithInsensitiveTwoLettersSameCase() {
        this.endsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "LE", true);
    }

    @Test
    public void testEndsWithInsensitiveTwoLettersDifferentCase() {
        this.endsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "LE", true);
    }

    @Test
    public void testEndsWithInsensitiveOneLetterFalsePositiveSameCase() {
        this.endsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "@e", false);
    }

    @Test
    public void testEndsWithInsensitiveOneLetterFalsePositiveDifferentCase() {
        this.endsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "@e", false);
    }

    @Test
    public void testEndsWithInsensitiveTwoLettersFalsePositiveSameCase() {
        this.endsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "@le", false);
    }

    @Test
    public void testEndsWithInsensitiveTwoLettersFalsePositiveDifferentCase() {
        this.endsWithAndCheck(CaseSensitivity.INSENSITIVE, "apple", "@LE", false);
    }

    // endsWith sensitive

    @Test
    public void testEndsWithSensitiveDifferent() {
        this.endsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "diffe", false);
    }

    @Test
    public void testEndsWithSensitiveEndsWithLonger() {
        this.endsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "longer", false);
    }

    @Test
    public void testEndsWithSensitiveEndsWithContainsCharsLonger() {
        this.endsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "longer.apple", false);
    }

    @Test
    public void testEndsWithSensitiveEqual() {
        this.endsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "apple", true);
    }

    @Test
    public void testEndsWithSensitiveEqualDifferentCase() {
        this.endsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "APPLE", false);
    }

    @Test
    public void testEndsWithSensitiveOneLetterSameCase() {
        this.endsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "e", true);
    }

    @Test
    public void testEndsWithSensitiveOneLetterDifferentCase() {
        this.endsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "E", false);
    }

    @Test
    public void testEndsWithSensitiveTwoLettersSameCase() {
        this.endsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "le", true);
    }

    @Test
    public void testEndsWithSensitiveTwoLettersDifferentCase() {
        this.endsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "LE", false);
    }

    @Test
    public void testEndsWithSensitiveOneLetterFalsePositiveSameCase() {
        this.endsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "@e", false);
    }

    @Test
    public void testEndsWithSensitiveOneLetterFalsePositiveDifferentCase() {
        this.endsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "@e", false);
    }

    @Test
    public void testEndsWithSensitiveTwoLettersFalsePositiveSameCase() {
        this.endsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "@le", false);
    }

    @Test
    public void testEndsWithSensitiveTwoLettersFalsePositiveDifferentCase() {
        this.endsWithAndCheck(CaseSensitivity.SENSITIVE, "apple", "@LE", false);
    }

    private void endsWithAndCheck(final CaseSensitivity sensitivity, final CharSequence chars,
                                  final CharSequence otherChars, final boolean expected) {
        final boolean result = sensitivity.endsWith(chars, otherChars);
        if (expected != result) {
            this.checkEquals(expected,
                    result,
                    () -> sensitivity + " endsWith( " + CaseSensitivityTest.quote(chars) + "," + CaseSensitivityTest.quote(otherChars) + ")");
        }
    }

    // indexOf insensitive

    @Test
    public void testIndexOfInsensitiveStartsWithSameCase() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "a", 0);
    }

    @Test
    public void testIndexOfInsensitiveStartsWithSameCase2() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "ab", 0);
    }

    @Test
    public void testIndexOfInsensitiveStartsWithSameCase3() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "abc", 0);
    }

    @Test
    public void testIndexOfInsensitiveMiddleSameCase() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "c", 2);
    }

    @Test
    public void testIndexOfInsensitiveMiddleSameCase2() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "cd", 2);
    }

    @Test
    public void testIndexOfInsensitiveMiddleSameCase3() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "cde", 2);
    }

    @Test
    public void testIndexOfInsensitiveEndsWithSameCase() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "e", 4);
    }

    @Test
    public void testIndexOfInsensitiveEndsWithSameCase2() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "de", 3);
    }

    @Test
    public void testIndexOfInsensitiveEndsWithSameCase3() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "cde", 2);
    }

    @Test
    public void testIndexOfInsensitiveStartsWithDifferentCase() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "A", 0);
    }

    @Test
    public void testIndexOfInsensitiveStartsWithDifferentCase2() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "AB", 0);
    }

    @Test
    public void testIndexOfInsensitiveStartsWithDifferentCase3() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "ABC", 0);
    }

    @Test
    public void testIndexOfInsensitiveMiddleDifferentCase() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "C", 2);
    }

    @Test
    public void testIndexOfInsensitiveMiddleDifferentCase2() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "CD", 2);
    }

    @Test
    public void testIndexOfInsensitiveMiddleDifferentCase3() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "CDE", 2);
    }

    @Test
    public void testIndexOfInsensitiveEndsWithDifferentCase() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "E", 4);
    }

    @Test
    public void testIndexOfInsensitiveEndsWithDifferentCase2() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "DE", 3);
    }

    @Test
    public void testIndexOfInsensitiveEndsWithDifferentCase3() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "CDE", 2);
    }

    @Test
    public void testIndexOfInsensitiveFalsePositive() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "a abcdef", "ab", 2);
    }

    @Test
    public void testIndexOfInsensitiveFalsePositive2() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "a abcdef", "abc", 2);
    }

    @Test
    public void testIndexOfInsensitiveFalsePositiveDifferentCase() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "a abcdef", "ABC", 2);
    }

    @Test
    public void testIndexOfInsensitiveFalsePositiveDifferentCase2() {
        this.indexOfAndCheck(CaseSensitivity.INSENSITIVE, "ab abcdef", "ABC", 3);
    }

    @Test
    public void testIndexOfInsensitiveAbsent() {
        this.indexOfFail(CaseSensitivity.INSENSITIVE, "abcde", "@");
    }

    // indexOf sensitive

    @Test
    public void testIndexOfSensitiveStartsWithSameCase() {
        this.indexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "a", 0);
    }

    @Test
    public void testIndexOfSensitiveStartsWithSameCase2() {
        this.indexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "ab", 0);
    }

    @Test
    public void testIndexOfSensitiveStartsWithSameCase3() {
        this.indexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "abc", 0);
    }

    @Test
    public void testIndexOfSensitiveMiddleSameCase() {
        this.indexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "c", 2);
    }

    @Test
    public void testIndexOfSensitiveMiddleSameCase2() {
        this.indexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "cd", 2);
    }

    @Test
    public void testIndexOfSensitiveMiddleSameCase3() {
        this.indexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "cde", 2);
    }

    @Test
    public void testIndexOfSensitiveEndsWithSameCase() {
        this.indexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "e", 4);
    }

    @Test
    public void testIndexOfSensitiveEndsWithSameCase2() {
        this.indexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "de", 3);
    }

    @Test
    public void testIndexOfSensitiveEndsWithSameCase3() {
        this.indexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "cde", 2);
    }

    @Test
    public void testIndexOfSensitiveStartsWithDifferentCase() {
        this.indexOfFail(CaseSensitivity.SENSITIVE, "abcde", "A");
    }

    @Test
    public void testIndexOfSensitiveStartsWithDifferentCase2() {
        this.indexOfFail(CaseSensitivity.SENSITIVE, "abcde", "AB");
    }

    @Test
    public void testIndexOfSensitiveStartsWithDifferentCase3() {
        this.indexOfFail(CaseSensitivity.SENSITIVE, "abcde", "ABC");
    }

    @Test
    public void testIndexOfSensitiveMiddleDifferentCase() {
        this.indexOfFail(CaseSensitivity.SENSITIVE, "abcde", "C");
    }

    @Test
    public void testIndexOfSensitiveMiddleDifferentCase2() {
        this.indexOfFail(CaseSensitivity.SENSITIVE, "abcde", "CD");
    }

    @Test
    public void testIndexOfSensitiveMiddleDifferentCase3() {
        this.indexOfFail(CaseSensitivity.SENSITIVE, "abcde", "CDE");
    }

    @Test
    public void testIndexOfSensitiveEndsWithDifferentCase() {
        this.indexOfFail(CaseSensitivity.SENSITIVE, "abcde", "E");
    }

    @Test
    public void testIndexOfSensitiveEndsWithDifferentCase2() {
        this.indexOfFail(CaseSensitivity.SENSITIVE, "abcde", "DE");
    }

    @Test
    public void testIndexOfSensitiveEndsWithDifferentCase3() {
        this.indexOfFail(CaseSensitivity.SENSITIVE, "abcde", "CDE");
    }

    @Test
    public void testIndexOfSensitiveAbsent() {
        this.indexOfFail(CaseSensitivity.SENSITIVE, "abcde", "@");
    }

    @Test
    public void testIndexOfSensitiveFalsePositive() {
        this.indexOfAndCheck(CaseSensitivity.SENSITIVE, "a abcdef", "ab", 2);
    }

    @Test
    public void testIndexOfSensitiveFalsePositive2() {
        this.indexOfAndCheck(CaseSensitivity.SENSITIVE, "a abcdef", "abc", 2);
    }

    @Test
    public void testIndexOfSensitiveFalsePositiveDifferentCase() {
        this.indexOfFail(CaseSensitivity.SENSITIVE, "a abcdef", "ABC");
    }

    @Test
    public void testIndexOfSensitiveFalsePositiveDifferentCase2() {
        this.indexOfFail(CaseSensitivity.SENSITIVE, "ab abcdef", "ABC");
    }

    private void indexOfAndCheck(final CaseSensitivity sensitivity,
                                 final CharSequence chars,
                                 final CharSequence searchFor,
                                 final int expected) {
        this.indexOfAndCheck0(sensitivity, chars, searchFor, expected);
        // try with offset

        final int offset = searchFor.length();
        this.indexOfOffsetAndCheck(sensitivity,
                new StringBuilder(searchFor).append(chars),
                searchFor,
                offset, // offset should skip initial $searchFor
                offset + expected);
    }

    private void indexOfFail(final CaseSensitivity sensitivity,
                             final CharSequence chars,
                             final CharSequence searchFor) {
        this.indexOfAndCheck0(sensitivity,
                chars,
                searchFor,
                -1);
        // try with offset
        this.indexOfOffsetAndCheck(sensitivity,
                new StringBuilder(searchFor).append(chars),
                searchFor,
                searchFor.length(), // offset should skip initial $searchFor
                -1);
    }

    private void indexOfAndCheck0(final CaseSensitivity sensitivity,
                                  final CharSequence chars,
                                  final CharSequence searchFor,
                                  final int expected) {
        final int result = sensitivity.indexOf(chars, searchFor);
        if (expected != result) {
            this.checkEquals(expected,
                    result,
                    () -> sensitivity + " indexOf( " + CaseSensitivityTest.quote(chars) + ", " + CaseSensitivityTest.quote(searchFor) + ")");
        }
    }

    private void indexOfOffsetAndCheck(final CaseSensitivity sensitivity,
                                       final CharSequence chars,
                                       final CharSequence searchFor,
                                       final int offset,
                                       final int expected) {
        final int result = sensitivity.indexOf(chars, searchFor, offset);
        if (expected != result) {
            this.checkEquals(expected,
                    result,
                    () -> sensitivity + " indexOf( " +
                            CaseSensitivityTest.quote(chars) + ", " +
                            CaseSensitivityTest.quote(searchFor) + ", " +
                            offset + ")");
        }
    }

    // lastIndexOf INSENSITIVE ......................................................................................

    @Test
    public void testLastIndexOfInsensitiveStartsWithSameCase() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "a", 0);
    }

    @Test
    public void testLastIndexOfInsensitiveStartsWithSameCase2() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "ab", 0);
    }

    @Test
    public void testLastIndexOfInsensitiveStartsWithSameCase3() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "abc", 0);
    }

    @Test
    public void testLastIndexOfInsensitiveMiddleSameCase() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "c", 2);
    }

    @Test
    public void testLastIndexOfInsensitiveMiddleSameCase2() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "cd", 2);
    }

    @Test
    public void testLastIndexOfInsensitiveMiddleSameCase3() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "cde", 2);
    }

    @Test
    public void testLastIndexOfInsensitiveEndsWithSameCase() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "e", 4);
    }

    @Test
    public void testLastIndexOfInsensitiveEndsWithSameCase2() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "de", 3);
    }

    @Test
    public void testLastIndexOfInsensitiveEndsWithSameCase3() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "cde", 2);
    }

    @Test
    public void testLastIndexOfInsensitiveStartsWithDifferentCase() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "A", 0);
    }

    @Test
    public void testLastIndexOfInsensitiveStartsWithDifferentCase2() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "AB", 0);
    }

    @Test
    public void testLastIndexOfInsensitiveStartsWithDifferentCase3() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "ABC", 0);
    }

    @Test
    public void testLastIndexOfInsensitiveMiddleDifferentCase() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "C", 2);
    }

    @Test
    public void testLastIndexOfInsensitiveMiddleDifferentCase2() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "CD", 2);
    }

    @Test
    public void testLastIndexOfInsensitiveMiddleDifferentCase3() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "CDE", 2);
    }

    @Test
    public void testLastIndexOfInsensitiveEndsWithDifferentCase() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "E", 4);
    }

    @Test
    public void testLastIndexOfInsensitiveEndsWithDifferentCase2() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "DE", 3);
    }

    @Test
    public void testLastIndexOfInsensitiveEndsWithDifferentCase3() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, "abcde", "CDE", 2);
    }

    @Test
    public void testLastIndexOfInsensitiveFalsePositive() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, " abcdef a", "ab", 1);
    }

    @Test
    public void testLastIndexOfInsensitiveFalsePositive2() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, " abcdef ab", "abc", 1);
    }

    @Test
    public void testLastIndexOfInsensitiveFalsePositiveDifferentCase() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, " abcdef AB", "ABC", 1);
    }

    @Test
    public void testLastIndexOfInsensitiveFalsePositiveDifferentCase2() {
        this.lastIndexOfAndCheck(CaseSensitivity.INSENSITIVE, " abc AB def", "ABC", 1);
    }

    @Test
    public void testLastIndexOfInsensitiveAbsent() {
        this.lastIndexOfFail(CaseSensitivity.INSENSITIVE, "abcde", "@");
    }

    // lastIndexOf SENSITIVE...........................................................................................

    @Test
    public void testLastIndexOfSensitiveStartsWithSameCase() {
        this.lastIndexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "a", 0);
    }

    @Test
    public void testLastIndexOfSensitiveStartsWithSameCase2() {
        this.lastIndexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "ab", 0);
    }

    @Test
    public void testLastIndexOfSensitiveStartsWithSameCase3() {
        this.lastIndexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "abc", 0);
    }

    @Test
    public void testLastIndexOfSensitiveMiddleSameCase() {
        this.lastIndexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "c", 2);
    }

    @Test
    public void testLastIndexOfSensitiveMiddleSameCase2() {
        this.lastIndexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "cd", 2);
    }

    @Test
    public void testLastIndexOfSensitiveMiddleSameCase3() {
        this.lastIndexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "cde", 2);
    }

    @Test
    public void testLastIndexOfSensitiveEndsWithSameCase() {
        this.lastIndexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "e", 4);
    }

    @Test
    public void testLastIndexOfSensitiveEndsWithSameCase2() {
        this.lastIndexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "de", 3);
    }

    @Test
    public void testLastIndexOfSensitiveEndsWithSameCase3() {
        this.lastIndexOfAndCheck(CaseSensitivity.SENSITIVE, "abcde", "cde", 2);
    }

    @Test
    public void testLastIndexOfSensitiveStartsWithDifferentCase() {
        this.lastIndexOfFail(CaseSensitivity.SENSITIVE, "abcde", "A");
    }

    @Test
    public void testLastIndexOfSensitiveStartsWithDifferentCase2() {
        this.lastIndexOfFail(CaseSensitivity.SENSITIVE, "abcde", "AB");
    }

    @Test
    public void testLastIndexOfSensitiveStartsWithDifferentCase3() {
        this.lastIndexOfFail(CaseSensitivity.SENSITIVE, "abcde", "ABC");
    }

    @Test
    public void testLastIndexOfSensitiveMiddleDifferentCase() {
        this.lastIndexOfFail(CaseSensitivity.SENSITIVE, "abcde", "C");
    }

    @Test
    public void testLastIndexOfSensitiveMiddleDifferentCase2() {
        this.lastIndexOfFail(CaseSensitivity.SENSITIVE, "abcde", "CD");
    }

    @Test
    public void testLastIndexOfSensitiveMiddleDifferentCase3() {
        this.lastIndexOfFail(CaseSensitivity.SENSITIVE, "abcde", "CDE");
    }

    @Test
    public void testLastIndexOfSensitiveEndsWithDifferentCase() {
        this.lastIndexOfFail(CaseSensitivity.SENSITIVE, "abcde", "E");
    }

    @Test
    public void testLastIndexOfSensitiveEndsWithDifferentCase2() {
        this.lastIndexOfFail(CaseSensitivity.SENSITIVE, "abcde", "DE");
    }

    @Test
    public void testLastIndexOfSensitiveEndsWithDifferentCase3() {
        this.lastIndexOfFail(CaseSensitivity.SENSITIVE, "abcde", "CDE");
    }

    @Test
    public void testLastIndexOfSensitiveAbsent() {
        this.lastIndexOfFail(CaseSensitivity.SENSITIVE, "abcde", "@");
    }

    @Test
    public void testLastIndexOfSensitiveFalsePositive() {
        this.lastIndexOfAndCheck(CaseSensitivity.SENSITIVE, " abcdef aX", "ab", 1);
    }

    @Test
    public void testLastIndexOfSensitiveFalsePositive2() {
        this.lastIndexOfAndCheck(CaseSensitivity.SENSITIVE, " abcdef aXX", "abc", 1);
    }

    @Test
    public void testLastIndexOfSensitiveFalsePositiveDifferentCase() {
        this.lastIndexOfFail(CaseSensitivity.SENSITIVE, " abcdef ABX", "ABC");
    }

    @Test
    public void testLastIndexOfSensitiveFalsePositiveDifferentCase2() {
        this.lastIndexOfFail(CaseSensitivity.SENSITIVE, " abcdef abX", "ABC");
    }

    private void lastIndexOfAndCheck(final CaseSensitivity sensitivity,
                                     final CharSequence chars,
                                     final CharSequence searchFor,
                                     final int expected) {
        this.lastIndexOfAndCheck0(sensitivity, chars, searchFor, expected);
        // try with offset

        this.lastIndexOfOffsetAndCheck(sensitivity,
                new StringBuilder(chars).append(searchFor),
                searchFor,
                chars.length() - 1,
                expected);
    }

    private void lastIndexOfFail(final CaseSensitivity sensitivity,
                                 final CharSequence chars,
                                 final CharSequence searchFor) {
        this.lastIndexOfAndCheck0(sensitivity,
                chars,
                searchFor,
                -1);
        // try with offset
        this.lastIndexOfOffsetAndCheck(sensitivity,
                new StringBuilder(chars.toString()).append(searchFor),
                searchFor,
                chars.length() - searchFor.length(), // offset should skip initial $searchFor
                -1);
    }

    private void lastIndexOfAndCheck0(final CaseSensitivity sensitivity,
                                      final CharSequence chars,
                                      final CharSequence searchFor,
                                      final int expected) {
        final int result = sensitivity.lastIndexOf(chars, searchFor);
        if (expected != result) {
            this.checkEquals(expected,
                    result,
                    () -> sensitivity + " lastIndexOf( " + CaseSensitivityTest.quote(chars) + ", " + CaseSensitivityTest.quote(searchFor) + ")");
        }
    }

    private void lastIndexOfOffsetAndCheck(final CaseSensitivity sensitivity,
                                           final CharSequence chars,
                                           final CharSequence searchFor,
                                           final int offset,
                                           final int expected) {
        final int result = sensitivity.lastIndexOf(chars, searchFor, offset);
        if (expected != result) {
            this.checkEquals(expected,
                    result,
                    () -> sensitivity + " lastIndexOf( " +
                            CaseSensitivityTest.quote(chars) + ", " +
                            CaseSensitivityTest.quote(searchFor) + ", " +
                            offset + ")");
        }
    }

    // hash insensitive

    @Test
    public void testHashInsensitiveNull() {
        this.hashAndCheck(CaseSensitivity.INSENSITIVE, null, null);
    }

    @Test
    public void testHashInsensitiveMixedCase() {
        final String string = "mixed CASE";
        this.hashAndCheck(CaseSensitivity.INSENSITIVE, string, string.toLowerCase());
    }

    @Test
    public void testHashInsensitiveLowerCase() {
        final String string = "lower cased ";
        this.hashAndCheck(CaseSensitivity.INSENSITIVE, string, string);
    }

    @Test
    public void testHashSensitiveNull() {
        this.hashAndCheck(CaseSensitivity.SENSITIVE, null, null);
    }

    // hash sensitive

    @Test
    public void testHashSensitiveMixedCase() {
        final String string = "mixed CASE";
        this.hashAndCheck(CaseSensitivity.SENSITIVE, string, string);
    }

    @Test
    public void testHashSensitiveLowerCase() {
        final String string = "lower cased ";
        this.hashAndCheck(CaseSensitivity.SENSITIVE, string, string);
    }

    @Test
    public void testHashSensitiveUpperCase() {
        final String string = "UPPERCASE";
        this.hashAndCheck(CaseSensitivity.SENSITIVE, string, string);
    }

    private void hashAndCheck(final CaseSensitivity sensitivity,
                              final CharSequence chars,
                              final String result) {
        this.checkEquals(sensitivity.hash(chars),
                null == result ? 0 : result.hashCode(),
                () -> sensitivity + ".hash(" + CaseSensitivityTest.quote(chars) + ")");
    }

    private static CharSequence quote(final CharSequence chars) {
        return null == chars ? null : CharSequences.quote(chars);
    }

    // predicate SENSITIVE...............................................................................................

    @Test
    public void testPredicateSensitiveTrue() {
        this.testTrue(predicate(), PREDICATE_TEXT);
    }

    @Test
    public void testPredicateSensitiveDifferentCaseFalse() {
        this.testFalse(predicate(), PREDICATE_TEXT.toString().toUpperCase());
    }

    @Test
    public void testPredicateSensitiveFalse() {
        this.testFalse(predicate(), "qrst");
    }

    private Predicate<CharSequence> predicate() {
        return CaseSensitivity.SENSITIVE.predicate(PREDICATE_TEXT);
    }

    // predicate INSENSITIVE............................................................................................

    @Test
    public void testPredicateInsensitiveTrue() {
        this.testTrue(predicateInsensitive(), PREDICATE_TEXT);
    }

    @Test
    public void testPredicateInsensitiveDifferentCaseFalse() {
        this.testTrue(predicateInsensitive(), PREDICATE_TEXT.toString().toUpperCase());
    }

    @Test
    public void testPredicateInsensitiveDifferentFalse() {
        this.testFalse(predicateInsensitive(), "qrst");
    }

    private Predicate<CharSequence> predicateInsensitive() {
        return CaseSensitivity.INSENSITIVE.predicate(PREDICATE_TEXT);
    }

    private final static CharSequence PREDICATE_TEXT = "abcXYZ";

    // predicateContains SENSITIVE......................................................................................

    @Test
    public void testPredicateContainsSensitiveTrue() {
        this.testTrue(predicateContains(), "<" + PREDICATE_CONTAINS_TEXT);
    }

    @Test
    public void testPredicateContainsSensitiveDifferentCaseFalse() {
        this.testFalse(predicateContains(), "<" + PREDICATE_CONTAINS_TEXT.toString().toUpperCase());
    }

    @Test
    public void testPredicateContainsSensitiveFalse() {
        this.testFalse(predicateContains(), "qrst");
    }

    private Predicate<CharSequence> predicateContains() {
        return CaseSensitivity.SENSITIVE.predicateContains(PREDICATE_CONTAINS_TEXT);
    }

    // predicateContains INSENSITIVE....................................................................................

    @Test
    public void testPredicateContainsInsensitiveTrue() {
        this.testTrue(predicateContainsInsensitive(), "<" + PREDICATE_CONTAINS_TEXT);
    }

    @Test
    public void testPredicateContainsInsensitiveDifferentCaseFalse() {
        this.testTrue(predicateContainsInsensitive(), "<" + PREDICATE_CONTAINS_TEXT.toString().toUpperCase());
    }

    @Test
    public void testPredicateContainsInsensitiveDifferentFalse() {
        this.testFalse(predicateContainsInsensitive(), "qrst");
    }

    private Predicate<CharSequence> predicateContainsInsensitive() {
        return CaseSensitivity.INSENSITIVE.predicateContains(PREDICATE_CONTAINS_TEXT);
    }

    private final static CharSequence PREDICATE_CONTAINS_TEXT = "abcXYZ";

    // predicateEndsWith SENSITIVE......................................................................................

    @Test
    public void testPredicateEndsWithSensitiveTrue() {
        this.testTrue(predicateEndsWith(), "<" + PREDICATE_ENDS_WITH_TEXT);
    }

    @Test
    public void testPredicateEndsWithSensitiveDifferentCaseFalse() {
        this.testFalse(predicateEndsWith(), "<" + PREDICATE_ENDS_WITH_TEXT.toString().toUpperCase());
    }

    @Test
    public void testPredicateEndsWithSensitiveFalse() {
        this.testFalse(predicateEndsWith(), "qrst");
    }

    private Predicate<CharSequence> predicateEndsWith() {
        return CaseSensitivity.SENSITIVE.predicateEndsWith(PREDICATE_ENDS_WITH_TEXT);
    }

    // predicateEndsWith INSENSITIVE.....................................................................................

    @Test
    public void testPredicateEndsWithInsensitiveTrue() {
        this.testTrue(predicateEndsWithInsensitive(), "<" + PREDICATE_ENDS_WITH_TEXT);
    }

    @Test
    public void testPredicateEndsWithInsensitiveDifferentCaseFalse() {
        this.testTrue(predicateEndsWithInsensitive(), "<" + PREDICATE_ENDS_WITH_TEXT.toString().toUpperCase());
    }

    @Test
    public void testPredicateEndsWithInsensitiveDifferentFalse() {
        this.testFalse(predicateEndsWithInsensitive(), "qrst");
    }

    private Predicate<CharSequence> predicateEndsWithInsensitive() {
        return CaseSensitivity.INSENSITIVE.predicateEndsWith(PREDICATE_ENDS_WITH_TEXT);
    }

    private final static CharSequence PREDICATE_ENDS_WITH_TEXT = "abcXYZ";

    // predicateStartsWith SENSITIVE....................................................................................

    @Test
    public void testPredicateStartsWithSensitiveTrue() {
        this.testTrue(predicateStartsWith(), PREDICATE_STARTS_WITH_TEXT + ">");
    }

    @Test
    public void testPredicateStartsWithSensitiveDifferentCaseFalse() {
        this.testFalse(predicateStartsWith(), PREDICATE_STARTS_WITH_TEXT.toString().toUpperCase() + ">");
    }

    @Test
    public void testPredicateStartsWithSensitiveFalse() {
        this.testFalse(predicateStartsWith(), "qrst");
    }

    @Test
    public void testPredicateStartsWithSensitiveEndsWithFalse() {
        this.testFalse(predicateStartsWith(), "<" + PREDICATE_STARTS_WITH_TEXT);
    }

    private Predicate<CharSequence> predicateStartsWith() {
        return CaseSensitivity.SENSITIVE.predicateStartsWith(PREDICATE_STARTS_WITH_TEXT);
    }

    // predicateStartsWith INSENSITIVE..................................................................................

    @Test
    public void testPredicateStartsWithInsensitiveTrue() {
        this.testTrue(predicateStartsWithInsensitive(),  PREDICATE_STARTS_WITH_TEXT + ">");
    }

    @Test
    public void testPredicateStartsWithInsensitiveDifferentCaseFalse() {
        this.testTrue(predicateStartsWithInsensitive(), PREDICATE_STARTS_WITH_TEXT.toString().toUpperCase() + ">");
    }

    @Test
    public void testPredicateStartsWithInsensitiveDifferentFalse() {
        this.testFalse(predicateStartsWithInsensitive(), "qrst");
    }

    @Test
    public void testPredicateStartsWithInsensitiveEndsWithFalse() {
        this.testFalse(predicateStartsWithInsensitive(), "<" + PREDICATE_STARTS_WITH_TEXT);
    }

    private Predicate<CharSequence> predicateStartsWithInsensitive() {
        return CaseSensitivity.INSENSITIVE.predicateStartsWith(PREDICATE_STARTS_WITH_TEXT);
    }

    private final static CharSequence PREDICATE_STARTS_WITH_TEXT = "abcXYZ";

    // fileSystem.......................................................................................................

    @Test
    public void testSystemPropertyTrue() {
        CaseSensitivity.FILE_SYSTEM = null;
        CaseSensitivity.FILE_SYSTEM_PROPERTY.set(String.valueOf(Boolean.TRUE));
        this.checkEquals(CaseSensitivity.SENSITIVE, CaseSensitivity.fileSystem());
    }

    @Test
    public void testSystemPropertyFalse() {
        CaseSensitivity.FILE_SYSTEM = null;
        CaseSensitivity.FILE_SYSTEM_PROPERTY.set(String.valueOf(Boolean.FALSE));
        this.checkEquals(CaseSensitivity.INSENSITIVE, CaseSensitivity.fileSystem());
    }

    @Test
    // TODO add an if for windows and linux.
    public void testWithoutSystemPropertyOnOSX() {
        CaseSensitivity.FILE_SYSTEM = null;

        final String osName = SystemProperty.OS_NAME.propertyValue().orElse("");
        if (osName.contains("OS X")) {
            this.checkEquals(CaseSensitivity.SENSITIVE, CaseSensitivity.fileSystem());
        }
    }

    // glob............................................................................................................

    @Test
    public void testGlobPattern() {
        final GlobPattern glob = CaseSensitivity.INSENSITIVE.globPattern("*.txt");

        this.checkEquals(
                true,
                glob.test("matched.TXT")
        );

        this.checkEquals(
                false,
                glob.test("not.matched")
        );
    }

    @AfterEach
    public void after() {
        CaseSensitivity.FILE_SYSTEM = null;
    }

    @Override
    public Class<CaseSensitivity> type() {
        return CaseSensitivity.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
