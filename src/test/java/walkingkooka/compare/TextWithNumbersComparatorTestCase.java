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

public abstract class TextWithNumbersComparatorTestCase<C extends TextWithNumbersComparator> implements ComparatorTesting2<C, CharSequence> {

    TextWithNumbersComparatorTestCase() {
        super();
    }

    // compare..........................................................................................................

    @Test
    public final void testCompareWithNullLeft() {
        assertThrows(
            NullPointerException.class,
            () -> this.createComparator()
                .compare(
                    null,
                    "2"
                )
        );
    }

    @Test
    public final void testCompareWithNullRight() {
        assertThrows(
            NullPointerException.class,
            () -> this.createComparator()
                .compare(
                    "1",
                    null
                )
        );
    }

    @Test
    public final void testCompareEmptyAndNonEmptyLetters() {
        this.compareAndCheckLess(
            "",
            "AAA"
        );
    }

    @Test
    public final void testCompareEmptyAndNonEmptyDigits() {
        this.compareAndCheckLess(
            "",
            "111"
        );
    }

    @Test
    public final void testCompareNonNumbers() {
        this.compareAndCheckLess(
            "AAA",
            "BBB"
        );
    }

    @Test
    public final void testCompareNumberAndNonNumber() {
        this.compareAndCheckLess(
            "1",
            "A"
        );
    }

    @Test
    public final void testCompareNumberAndNonNumber2() {
        this.compareAndCheckLess(
            "12",
            "AB"
        );
    }

    @Test
    public final void testCompareNumberAndNonNumber3() {
        this.compareAndCheckLess(
            "1Z",
            "AZ"
        );
    }

    @Test
    public final void testCompareNumbers() {
        this.compareAndCheckLess(
            "123",
            "456"
        );
    }

    @Test
    public final void testCompareNumberShorter() {
        this.compareAndCheckLess(
            "123",
            "4567"
        );
    }

    @Test
    public final void testCompareNumberShorter2() {
        this.compareAndCheckLess(
            "234",
            "1234"
        );
    }

    @Test
    public final void testCompareNumberShorter3() {
        this.compareAndCheckLess(
            "234A",
            "1234A"
        );
    }

    @Test
    public final void testCompareNumberLeadingZeroesEqualNumericValue() {
        this.compareAndCheckEquals(
            "001",
            "0001"
        );
    }

    @Test
    public final void testCompareNumberLeadingZeroesEqualNumericValue2() {
        this.compareAndCheckEquals(
            "001A",
            "0001A"
        );
    }

    @Test
    public final void testCompareNumberLeadingZeroesEqualNumericValueDifferentLettersAfter() {
        this.compareAndCheckLess(
            "001A",
            "0001B"
        );
    }

    @Test
    public final void testCompareNumberLeadingZeroes() {
        this.compareAndCheckLess(
            "001",
            "0002"
        );
    }

    @Test
    public final void testCompareNumberLeadingZeroes2() {
        this.compareAndCheckLess(
            "00123",
            "000234"
        );
    }

    @Test
    public final void testCompareNumberLeadingZeroes3() {
        this.compareAndCheckLess(
            ".001",
            ".0002"
        );
    }

    @Test
    public final void testCompareNumberLeadingZeroes4() {
        this.compareAndCheckLess(
            "000001",
            "0002"
        );
    }

    @Test
    public final void testCompareNumberLeadingZeroes5() {
        this.compareAndCheckLess(
            "00000123",
            "000234"
        );
    }

    @Test
    public final void testCompareNumberLeadingZeroes6() {
        this.compareAndCheckLess(
            "00000123A",
            "000234A"
        );
    }

    @Test
    public final void testCompareTextNumberTextNumber() {
        this.compareAndCheckLess(
            "A1B2",
            "A1B3"
        );
    }

    @Test
    public final void testCompareTextNumberTextNumber2() {
        this.compareAndCheckLess(
            "A1B02",
            "A1B03"
        );
    }

    @Test
    public final void testCompareTextNumberTextNumber3() {
        this.compareAndCheckLess(
            "A1B002",
            "A1B03"
        );
    }

    @Test
    public final void testCompareNumbersIncludingDecimal() {
        this.compareAndCheckLess(
            "0001.5",
            "002.2"
        );
    }

    @Test
    public final void testSort() {
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
    public final void testSort2() {
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

    // class............................................................................................................

    @Override
    public final String typeNamePrefix() {
        return TextWithNumbersComparator.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
