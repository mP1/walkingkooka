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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;

import static org.junit.Assert.assertEquals;

final public class NaturalOrderingComparatorTest
        extends ComparatorTestCase<NaturalOrderingComparator<Integer>, Integer> {

    @Test
    public void testNullFirstFails() {
        this.compareFail(null, Integer.valueOf(1));
    }

    @Test
    public void testNullSecondFails() {
        this.compareFail(Integer.valueOf(1), null);
    }

    private void compareFail(final Integer value1, final Integer value2) {
        try {
            this.compare(value1, value2);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testLess() {
        this.compareAndCheckLess(Integer.valueOf(1), Integer.valueOf(2));
    }

    @Test
    public void testToString() {
        assertEquals("natural", NaturalOrderingComparator.<Integer>instance().toString());
    }

    // factory

    @Override
    protected NaturalOrderingComparator<Integer> createComparator() {
        return NaturalOrderingComparator.instance();
    }

    @Override
    protected Class<NaturalOrderingComparator<Integer>> type() {
        return Cast.to(NaturalOrderingComparator.class);
    }
}
