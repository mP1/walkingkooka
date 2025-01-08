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

import java.util.Comparator;

public final class NullAwareComparatorBeforeTest extends NullAwareComparatorTestCase<NullAwareComparatorBefore<String>> {

    @Test
    public void testCompareNullLeft() {
        this.compareAndCheckLess(
            null,
            "Abc"
        );
    }

    @Test
    public void testCompareNullRight() {
        this.compareAndCheckMore(
            "Abc",
            null
        );
    }

    @Test
    public void testToString() {
        final Comparator<String> fake = Comparators.fake();
        this.toStringAndCheck(
            NullAwareComparatorBefore.with(fake),
            "null < " + fake
        );
    }

    @Override
    NullAwareComparatorBefore<String> createComparator(final Comparator<String> comparator) {
        return NullAwareComparatorBefore.with(comparator);
    }

    @Override
    public Class<NullAwareComparatorBefore<String>> type() {
        return Cast.to(NullAwareComparatorBefore.class);
    }
}
