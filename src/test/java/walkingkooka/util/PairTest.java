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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefinedTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class PairTest extends HashCodeEqualsDefinedTestCase<Pair<?, ?>> {

    private final static A A = new A(1);

    private final static B B = new B();

    // tests

    @Test
    public void testNullFirst() {
        final Pair<A, B> pair = Pair.with(PairTest.A, PairTest.B);
        assertSame("first value", PairTest.A, pair.first());
        assertSame("second value", PairTest.B, pair.second());
    }

    @Test
    public void testNullSecond() {
        final Pair<A, B> pair = Pair.with(PairTest.A, null);
        assertSame("first value", PairTest.A, pair.first());
        Assert.assertNull("second value", pair.second());
    }

    @Test
    public void testTwoNulls() {
        final Pair<A, B> pair = Pair.with(null, null);
        Assert.assertNull("first value", pair.first());
        Assert.assertNull("second value", pair.second());
    }

    @Test
    public void testWith() {
        final Pair<A, B> pair = Pair.with(PairTest.A, PairTest.B);
        assertSame("first value", PairTest.A, pair.first());
        assertSame("second value", PairTest.B, pair.second());
    }

    @Test
    public void testToString() {
        final Pair<A, B> pair = Pair.with(PairTest.A, PairTest.B);
        assertEquals(pair.toString(), PairTest.A.toString() + " & " + PairTest.B.toString());
    }

    @Test
    public void testToStringNullFirst() {
        final Pair<A, B> pair = Pair.with(null, PairTest.B);
        assertEquals(pair.toString(), "null & " + PairTest.B.toString());
    }

    @Test
    public void testToStringNullSecond() {
        final Pair<A, B> pair = Pair.with(PairTest.A, null);
        assertEquals(pair.toString(), PairTest.A.toString() + " & null");
    }

    @Test
    public void testToStringNullFirstAndSecond() {
        final Pair<A, B> pair = Pair.with(null, null);
        assertEquals(pair.toString(), "null & null");
    }

    @Test
    public void testToStringCharSequenceFirst() {
        assertEquals(Pair.with("FIRST", PairTest.B).toString(), "\"FIRST\" & " + PairTest.B);
    }

    @Test
    public void testToStringCharSequenceSecond() {
        assertEquals(Pair.with(PairTest.A, "SECOND").toString(),
                PairTest.A + " & \"SECOND\"");
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
            return object instanceof PairTest.A && this.value == ((PairTest.A) object).value;
        }

        @Override
        public String toString() {
            return "A=" + this.value;
        }
    }

    final static private class B {
        @Override
        public String toString() {
            return "B=" + this.hashCode();
        }
    }

    @Override
    protected Class<? extends Pair<?, ?>> type() {
        return Cast.to(Pair.class);
    }
}
