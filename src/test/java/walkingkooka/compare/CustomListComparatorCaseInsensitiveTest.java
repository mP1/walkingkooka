

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

import java.util.List;

public final class CustomListComparatorCaseInsensitiveTest extends CustomListComparatorTestCase<CustomListComparatorCaseInsensitive> {

    @Test
    public void testCompareCustomListEquals() {
        this.compareAndCheckEquals(
            HI,
            HI
        );
    }

    @Test
    public void testCompareCustomListNotCustomEquals() {
        this.compareAndCheckEquals(
            "AAA",
            "AAA"
        );
    }

    @Test
    public void testCompareCustomListNotCustomEqualsDifferentCase() {
        this.compareAndCheckEquals(
            "AAA",
            "aaa"
        );
    }

    @Test
    public void testCompareCustomListNotCustomLess() {
        this.compareAndCheckLess(
            "AAA",
            "bbb"
        );
    }

    @Test
    public void testCompareCustomListNotCustomLessDifferentCase() {
        this.compareAndCheckLess(
            "bbb",
            "CCC"
        );
    }

    @Test
    public void testCompareCustomListFirst() {
        this.compareAndCheckLess(
            HI,
            "AAA"
        );
    }

    @Test
    public void testCompareCustomListFirstDifferentCase() {
        this.compareAndCheckLess(
            "hI",
            "AAA"
        );
    }

    @Test
    public void testCompareCustomListAndUnknown() {
        this.compareAndCheckLess(
            "meD",
            "AAA"
        );
    }

    @Test
    public void testSortDifferentCustomListCase() {
        final String lo = "LO";

        this.checkNotEquals(
            lo,
            LO
        );

        this.comparatorArraySortAndCheck(
            "AAA", "bbb", lo, MED, "hi",
            "hi", MED, lo, "AAA", "bbb"
        );
    }

    @Test
    public void testSortDifferentCustomListCase2() {
        final String lo = "LO";

        this.checkNotEquals(
            lo,
            LO
        );

        this.comparatorArraySortAndCheck(
            "AAA", "bbb", lo, MED, "hi", "HI",
            "hi", "HI", MED, lo, "AAA", "bbb"
        );
    }

    @Override
    CustomListComparatorCaseInsensitive createComparator(final List<CharSequence> customList) {
        return CustomListComparatorCaseInsensitive.with(customList);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createObject(),
            "CustomList [Hi, Med, Lo](CaseInsensitive)"
        );
    }

    @Override
    public Class<CustomListComparatorCaseInsensitive> type() {
        return Cast.to(CustomListComparatorCaseInsensitive.class);
    }
}
