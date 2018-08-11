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

package walkingkooka.predicate;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;

import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

final public class NotPredicateTest extends PredicateTestCase<NotPredicate<String>, String> {

    // constants

    private final static String MATCHED = "matched";

    private final static String DIFFERENT = "different";

    private final static Predicate<String> PREDICATE = Predicates.is(NotPredicateTest.MATCHED);

    // tests

    @Test
    public void testWrapNullPredicateFails() {
        try {
            NotPredicate.wrap(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testWrapNotPredicate() {
        assertSame(NotPredicateTest.PREDICATE,
                NotPredicate.wrap(NotPredicate.wrap(NotPredicateTest.PREDICATE)));
    }

    @Test
    public void testWrapAlways() {
        assertSame(Predicates.never(), NotPredicate.wrap(Predicates.always()));
    }

    @Test
    public void testWrapNever() {
        assertSame(Predicates.always(), NotPredicate.wrap(Predicates.never()));
    }

    @Override
    @Test
    public void testNullFails() {
        // nop
    }

    @Test
    public void testNotMatched() {
        final Predicate<String> not = NotPredicate.wrap(NotPredicateTest.PREDICATE);
        Assert.assertNotSame(NotPredicateTest.PREDICATE, not);
        Assert.assertTrue(NotPredicateTest.PREDICATE.test(NotPredicateTest.MATCHED));
        Assert.assertFalse(not.test(NotPredicateTest.MATCHED));
    }

    @Test
    public void testMatched() {
        final Predicate<String> predicate = Predicates.is(NotPredicateTest.MATCHED);
        final Predicate<String> not = NotPredicate.wrap(predicate);
        Assert.assertNotSame(predicate, not);
        Assert.assertFalse(predicate.test(NotPredicateTest.DIFFERENT));
        Assert.assertTrue(not.test(NotPredicateTest.DIFFERENT));
    }

    @Test
    public void testWrapNotable() {
        final Predicate<String> notted = NotPredicateTest.PREDICATE;
        assertSame(notted, NotPredicate.wrap(new TestNotablePredicate(notted)));
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
    public void testToString() {
        final Predicate<Object> predicate = Predicates.fake();
        assertEquals("!" + predicate, NotPredicate.wrap(predicate).toString());
    }

    @Override
    protected NotPredicate<String> createPredicate() {
        return this.createPredicate(NotPredicateTest.PREDICATE);
    }

    private NotPredicate<String> createPredicate(final Predicate<String> predicate) {
        return Cast.to(NotPredicate.wrap(predicate));
    }

    @Override
    protected Class<NotPredicate<String>> type() {
        return Cast.to(NotPredicate.class);
    }
}
