
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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class CustomListComparatorTestCase<C extends CustomListComparator> implements ComparatorTesting2<C, CharSequence>,
    HashCodeEqualsDefinedTesting2<C>,
    ToStringTesting<C> {

    final static String HI = "Hi";

    final static String MED = "Med";

    final static String LO = "Lo";

    final List<CharSequence> CUSTOM_LIST = Lists.of(
        HI,
        MED,
        LO
    );

    CustomListComparatorTestCase() {
        super();
    }

    @Test
    public final void testWithNullCustomListFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createComparator(null)
        );
    }

    @Test
    public final void testWithEmpty() {
        this.createComparator(Lists.empty());
    }

    @Test
    public final void testCompareWithNullLeftFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.compare(
                null,
                "RIGHT"
            )
        );
    }

    @Test
    public final void testCompareWithNullRightFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.compare(
                "LEFT",
                null
            )
        );
    }

    public final void testSort() {
        this.comparatorArraySortAndCheck(
            "AAA", "bbb", LO, MED, HI,
            HI, MED, LO, "AAA", "bbb"
        );
    }

    @Override
    public C createComparator() {
        return this.createComparator(CUSTOM_LIST);
    }

    abstract C createComparator(final List<CharSequence> customList);

    // hashCode/equals..................................................................................................

    @Test
    public final void testEqualsDifferentCustomList() {
        this.checkNotEquals(
            this.createComparator(
                Lists.of(
                    "Different1",
                    "Different2"
                )
            )
        );
    }

    @Override
    public final C createObject() {
        return this.createComparator();
    }

    // class............................................................................................................

    @Override
    public final void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
