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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

final public class PairTest extends ClassTestCase<Pair<?, ?>>
        implements HashCodeEqualsDefinedTesting<Pair<?, ?>>, SerializationTesting<Pair<?, ?>> {

    private final static A A = new A(1);

    private final static B B = new B();

    // tests

    @Test
    public void testNullFirst() {
        this.createAndCheck(null, B);
    }

    @Test
    public void testNullSecond() {
        this.createAndCheck(A, null);
    }

    @Test
    public void testTwoNulls() {
        this.createAndCheck(null, null);
    }

    @Test
    public void testWith() {
        this.createAndCheck(A, B);
    }

    private void createAndCheck(final A left, final B right) {
        final Pair<A, B> pair = Pair.with(left, right);
        assertSame(left, pair.first(), "first value");
        assertSame(right, pair.second(), "second value");
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testDifferentFirst() {
        final Pair<A, B> pair1 = Pair.with(new A(0), B);
        final Pair<A, B> pair2 = Pair.with(new A(1), B);
        this.checkNotEquals(pair1, pair2);
    }

    @Test
    public void testDifferentSecond() {
        final Pair<A, B> pair1 = Pair.with(new A(0), B);
        final Pair<A, Object> pair2 = Pair.with(new A(0), new Object());
        this.checkNotEquals(pair1, pair2);
    }

    // toString..................................................................

    @Test
    public void testToString() {
        final Pair<A, B> pair = Pair.with(A, B);
        assertEquals(pair.toString(), A.toString() + " & " + B.toString());
    }

    @Test
    public void testToStringNullFirst() {
        final Pair<A, B> pair = Pair.with(null, B);
        assertEquals(pair.toString(), "null & " + B.toString());
    }

    @Test
    public void testToStringNullSecond() {
        final Pair<A, B> pair = Pair.with(A, null);
        assertEquals(pair.toString(), A.toString() + " & null");
    }

    @Test
    public void testToStringNullFirstAndSecond() {
        final Pair<A, B> pair = Pair.with(null, null);
        assertEquals(pair.toString(), "null & null");
    }

    @Test
    public void testToStringCharSequenceFirst() {
        assertEquals(Pair.with("FIRST", B).toString(), "\"FIRST\" & " + B);
    }

    @Test
    public void testToStringCharSequenceSecond() {
        assertEquals(Pair.with(A, "SECOND").toString(),
                A + " & \"SECOND\"");
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
            return object instanceof A && this.value == ((A) object).value;
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
    public Class<Pair<?, ?>> type() {
        return Cast.to(Pair.class);
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public Pair<?, ?> createObject() {
        return Pair.with(A, B);
    }

    @Override
    public Pair<String, String> serializableInstance() {
        return Pair.with("1st", "2nd");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
