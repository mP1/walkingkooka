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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.test.SerializationTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

final public class NaturalOrderingComparatorTest
        extends ComparatorTestCase<NaturalOrderingComparator<Integer>, Integer>
        implements SerializationTesting<NaturalOrderingComparator<Integer>> {

    @Test
    public void testLess() {
        this.compareAndCheckLess(Integer.valueOf(1), Integer.valueOf(2));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(NaturalOrderingComparator.<Integer>instance(), "natural");
    }

    // factory

    @Override
    protected NaturalOrderingComparator<Integer> createComparator() {
        return NaturalOrderingComparator.instance();
    }

    @Override
    public Class<NaturalOrderingComparator<Integer>> type() {
        return Cast.to(NaturalOrderingComparator.class);
    }

    @Override
    public NaturalOrderingComparator<Integer> serializableInstance() {
        return NaturalOrderingComparator.instance();
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return true;
    }
}
