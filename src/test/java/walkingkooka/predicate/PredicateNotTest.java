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

package walkingkooka.predicate;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.HashCodeEqualsDefinedTesting2;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final public class PredicateNotTest extends PredicateTestCase<PredicateNot<String>, String>
    implements HashCodeEqualsDefinedTesting2<PredicateNot<String>> {

    // constants

    private final static String MATCHED = "matched";

    private final static String DIFFERENT = "different";

    private final static Predicate<String> PREDICATE = Predicates.is(MATCHED);

    // tests

    @Test
    public void testWrapNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> PredicateNot.wrap(null));
    }

    @Test
    public void testWrapNotPredicate() {
        assertSame(PREDICATE,
            PredicateNot.wrap(PredicateNot.wrap(PREDICATE)));
    }

    @Test
    public void testWrapAlways() {
        assertSame(Predicates.never(), PredicateNot.wrap(Predicates.always()));
    }

    @Test
    public void testWrapNever() {
        assertSame(Predicates.always(), PredicateNot.wrap(Predicates.never()));
    }

    @Test
    public void testTestNull() {
        this.testTrue(null);
    }

    @Test
    public void testNotMatched() {
        final Predicate<String> not = PredicateNot.wrap(PREDICATE);
        assertNotSame(PREDICATE, not);
        assertTrue(PREDICATE.test(MATCHED));
        assertFalse(not.test(MATCHED));
    }

    @Test
    public void testMatched() {
        final Predicate<String> predicate = Predicates.is(MATCHED);
        final Predicate<String> not = PredicateNot.wrap(predicate);
        assertNotSame(predicate, not);
        assertFalse(predicate.test(DIFFERENT));
        assertTrue(not.test(DIFFERENT));
    }

    @Test
    public void testWrapNotable() {
        final Predicate<String> notted = PREDICATE;
        assertSame(notted, PredicateNot.wrap(new TestNotablePredicate(notted)));
    }

    private static class TestNotablePredicate
        implements Predicate<String>, Notable<Predicate<String>> {

        private TestNotablePredicate(final Predicate<String> predicate) {
            this.inverted = predicate;
        }

        @Override
        public Predicate<String> negate() {
            return this.inverted;
        }

        @Override
        public boolean test(final String value) {
            throw new UnsupportedOperationException();
        }

        private final Predicate<String> inverted;
    }

    @Test
    public void testEqualsDifferentWrapped() {
        this.checkNotEquals(PredicateNot.wrap(Predicates.fake()));
    }

    @Test
    public void testToString() {
        final Predicate<Object> predicate = Predicates.fake();
        this.toStringAndCheck(PredicateNot.wrap(predicate), "!" + predicate);
    }

    @Override
    public PredicateNot<String> createPredicate() {
        return this.createPredicate(PREDICATE);
    }

    private PredicateNot<String> createPredicate(final Predicate<String> predicate) {
        return Cast.to(PredicateNot.wrap(predicate));
    }

    @Override
    public Class<PredicateNot<String>> type() {
        return Cast.to(PredicateNot.class);
    }

    @Override
    public PredicateNot<String> createObject() {
        return this.createPredicate();
    }
}
