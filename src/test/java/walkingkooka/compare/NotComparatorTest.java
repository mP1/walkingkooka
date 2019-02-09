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
import walkingkooka.predicate.Notable;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.type.MemberVisibility;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class NotComparatorTest implements ClassTesting2<NotComparator<Object>>,
        ComparatorTesting<NotComparator<Object>, Object>,
        HashCodeEqualsDefinedTesting<NotComparator<Object>>,
        SerializationTesting<NotComparator<Object>> {

    private final static Comparator<Object> COMPARATOR = Comparators.fake();

    @Test
    public void testWrapNullFails() {
        assertThrows(NullPointerException.class, () -> {
            NotComparator.wrap(null);
        });
    }

    @Test
    public void testWrapNotable() {
        assertSame(COMPARATOR, NotComparator.wrap(new NotableComparator()));
    }

    private static class NotableComparator
            implements Comparator<Object>, Notable<Comparator<Object>> {

        @Override
        public Comparator<Object> negate() {
            return COMPARATOR;
        }

        @Override
        public int compare(final Object o1, final Object o2) {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    public void testInvertsResult() {
        final Object object1 = new Object();
        final Object object2 = new Object();
        final Comparator<Object> comparator = NotComparator.wrap(new Comparator<Object>() {

            @Override
            public int compare(final Object o1, final Object o2) {
                assertSame(object1, o1);
                assertSame(object2, o2);
                return +2;
            }
        });
        checkEquals(-2, comparator.compare(object1, object2));
    }

    @Test
    public void testWrapNot() {
        final Comparator<Object> wrapped = Comparators.fake();
        final Comparator<Object> not = NotComparator.wrap(NotComparator.wrap(wrapped));
        assertSame(wrapped, not);
    }

    @Test
    public void testEqualsDifferentPredicate() {
        this.checkNotEquals(NotComparator.wrap(String.CASE_INSENSITIVE_ORDER));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createComparator(), "NOT (" + COMPARATOR.toString() + ")");
    }

    @Override
    public NotComparator<Object> createComparator() {
        return Cast.to(NotComparator.wrap(COMPARATOR));
    }

    @Override
    public Class<NotComparator<Object>> type() {
        return Cast.to(NotComparator.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public NotComparator<Object> createObject() {
        return this.createComparator();
    }

    @Override
    public NotComparator<Object> serializableInstance() {
        return Cast.to(NotComparator.wrap(Comparators.<String>naturalOrdering()));
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
