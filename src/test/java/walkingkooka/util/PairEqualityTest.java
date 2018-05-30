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

package walkingkooka.util;

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

final public class PairEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<Pair<?, ?>> {

    private final static A A = new A(1);

    private final static B B = new B();

    // tests

    @Test
    public void testUnequalFirst() {
        final Pair<A, B> pair1 = Pair.with(new A(0), PairEqualityTest.B);
        final Pair<A, B> pair2 = Pair.with(new A(1), PairEqualityTest.B);
        HashCodeEqualsDefinedEqualityTestCase.checkNotEquals(pair1, pair2);
    }

    @Test
    public void testUnequalSecond() {
        final Pair<A, B> pair1 = Pair.with(new A(0), PairEqualityTest.B);
        final Pair<A, Object> pair2 = Pair.with(new A(0), new Object());
        HashCodeEqualsDefinedEqualityTestCase.checkNotEquals(pair1, pair2);
    }

    // helpers

    final static private class A {
        private A(final int value) {
            this.value = value;
        }

        final int value;

        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public boolean equals(final Object object) {
            return object instanceof PairEqualityTest.A && this.value == ((PairEqualityTest.A) object).value;
        }
    }

    final static private class B {
    }

    @Override
    protected Pair<?, ?> createObject() {
        return Pair.with(PairEqualityTest.A, PairEqualityTest.B);
    }
}
