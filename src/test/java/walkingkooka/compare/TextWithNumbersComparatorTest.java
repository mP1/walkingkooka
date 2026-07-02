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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextWithNumbersComparatorTest implements ComparatorTesting2<TextWithNumbersComparator, CharSequence> {

    // compare..........................................................................................................

    @Test
    public void testCompareWithNullLeft() {
        assertThrows(
            NullPointerException.class,
            () -> TextWithNumbersComparator.INSTANCE.compare(
                null,
                "2"
            )
        );
    }

    @Test
    public void testCompareWithNullRight() {
        assertThrows(
            NullPointerException.class,
            () -> TextWithNumbersComparator.INSTANCE.compare(
                "1",
                null
            )
        );
    }

    @Test
    public void testCompareEmptyAndNonEmptyLetters() {
        this.compareAndCheckLess(
            "",
            "AAA"
        );
    }

    @Test
    public void testCompareEmptyAndNonEmptyDigits() {
        this.compareAndCheckLess(
            "",
            "111"
        );
    }

    @Test
    public void testCompareNonNumbers() {
        this.compareAndCheckLess(
            "AAA",
            "BBB"
        );
    }

    @Test
    public void testCompareNumberAndNonNumber() {
        this.compareAndCheckLess(
            "1",
            "A"
        );
    }

    @Test
    public void testCompareNumberAndNonNumber2() {
        this.compareAndCheckLess(
            "12",
            "AB"
        );
    }

    @Test
    public void testCompareNumberAndNonNumber3() {
        this.compareAndCheckLess(
            "1Z",
            "AZ"
        );
    }

    @Test
    public void testCompareNumbers() {
        this.compareAndCheckLess(
            "123",
            "456"
        );
    }

    @Test
    public void testCompareNumberShorter() {
        this.compareAndCheckLess(
            "123",
            "4567"
        );
    }

    @Test
    public void testCompareNumberShorter2() {
        this.compareAndCheckLess(
            "234",
            "1234"
        );
    }

    @Test
    public void testCompareNumberShorter3() {
        this.compareAndCheckLess(
            "234A",
            "1234A"
        );
    }

    @Test
    public void testCompareNumberLeadingZeroesEqualNumericValue() {
        this.compareAndCheckEquals(
            "001",
            "0001"
        );
    }

    @Test
    public void testCompareNumberLeadingZeroesEqualNumericValue2() {
        this.compareAndCheckEquals(
            "001A",
            "0001A"
        );
    }

    @Test
    public void testCompareNumberLeadingZeroesEqualNumericValueDifferentLettersAfter() {
        this.compareAndCheckLess(
            "001A",
            "0001B"
        );
    }

    @Test
    public void testCompareNumberLeadingZeroes() {
        this.compareAndCheckLess(
            "001",
            "0002"
        );
    }

    @Test
    public void testCompareNumberLeadingZeroes2() {
        this.compareAndCheckLess(
            "00123",
            "000234"
        );
    }

    @Test
    public void testCompareNumberLeadingZeroes3() {
        this.compareAndCheckLess(
            ".001",
            ".0002"
        );
    }

    @Test
    public void testCompareNumberLeadingZeroes4() {
        this.compareAndCheckLess(
            "000001",
            "0002"
        );
    }

    @Test
    public void testCompareNumberLeadingZeroes5() {
        this.compareAndCheckLess(
            "00000123",
            "000234"
        );
    }

    @Test
    public void testCompareNumberLeadingZeroes6() {
        this.compareAndCheckLess(
            "00000123A",
            "000234A"
        );
    }

    @Test
    public void testCompareTextNumberTextNumber() {
        this.compareAndCheckLess(
            "A1B2",
            "A1B3"
        );
    }

    @Test
    public void testCompareTextNumberTextNumber2() {
        this.compareAndCheckLess(
            "A1B02",
            "A1B03"
        );
    }

    @Test
    public void testCompareTextNumberTextNumber3() {
        this.compareAndCheckLess(
            "A1B002",
            "A1B03"
        );
    }

    @Test
    public void testCompareNumbersIncludingDecimal() {
        this.compareAndCheckLess(
            "0001.5",
            "002.2"
        );
    }

    @Test
    public void testSort() {
        this.comparatorArraySortAndCheck(
            "",
            "A",
            "12",
            "2",
            "",
            "2",
            "12",
            "A"
        );
    }

    @Test
    public void testSort2() {
        this.comparatorArraySortAndCheck(
            "",
            "A",
            "12",
            "2",
            "B2",
            "B0001",
            "",
            "2",
            "12",
            "A",
            "B0001",
            "B2"
        );
    }

    @Override
    public TextWithNumbersComparator createComparator() {
        return TextWithNumbersComparator.INSTANCE;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            TextWithNumbersComparator.INSTANCE,
            "TextWithNumbersComparator"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextWithNumbersComparator> type() {
        return TextWithNumbersComparator.class;
    }
}
