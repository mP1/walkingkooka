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
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

final public class PairTest implements ClassTesting2<Pair<?, ?>>,
        HashCodeEqualsDefinedTesting<Pair<?, ?>>,
        SerializationTesting<Pair<?, ?>>,
        ToStringTesting<Pair<?, ?>> {

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
        this.toStringAndCheck(Pair.with(A, B), A.toString() + " & " + B.toString());
    }

    @Test
    public void testToStringNullFirst() {
        this.toStringAndCheck(Pair.with(null, B), "null & " + B.toString());
    }

    @Test
    public void testToStringNullSecond() {
        this.toStringAndCheck(Pair.with(A, null), A.toString() + " & null");
    }

    @Test
    public void testToStringNullFirstAndSecond() {
        this.toStringAndCheck(Pair.with(null, null), "null & null");
    }

    @Test
    public void testToStringCharSequenceFirst() {
        this.toStringAndCheck(Pair.with("FIRST", B).toString(), "\"FIRST\" & " + B);
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
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
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
