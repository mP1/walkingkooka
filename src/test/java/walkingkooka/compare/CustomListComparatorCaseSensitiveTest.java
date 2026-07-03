
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

public final class CustomListComparatorCaseSensitiveTest extends CustomListComparatorTestCase<CustomListComparatorCaseSensitive> {

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
    public void testCompareCustomListNotCustomLess() {
        this.compareAndCheckLess(
            "AAA",
            "bbb"
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
    public void testCompareCustomListAndUnknown() {
        this.compareAndCheckLess(
            MED,
            "AAA"
        );
    }

    @Test
    public void testSortDifferentCustomListCase() {
        this.comparatorArraySortAndCheck(
            "AAA", "bbb", LO, MED, "hi",
            MED, LO, "AAA", "bbb", "hi"
        );
    }

    @Override
    CustomListComparatorCaseSensitive createComparator(final List<CharSequence> customList) {
        return CustomListComparatorCaseSensitive.with(customList);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createObject(),
            "CustomList [Hi, Med, Lo](CaseSensitive)"
        );
    }

    @Override
    public Class<CustomListComparatorCaseSensitive> type() {
        return Cast.to(CustomListComparatorCaseSensitive.class);
    }
}
