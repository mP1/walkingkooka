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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.predicate.character.CharPredicates;

import static org.junit.Assert.assertEquals;

final public class InitialAndPartCharPredicateCharSequencePredicateTest
        extends PredicateTestCase<InitialAndPartCharPredicateCharSequencePredicate, CharSequence> {

    // tests

    @Test(expected = NullPointerException.class)
    public void testWithNullInitialCharacterPredicateFails() {
        InitialAndPartCharPredicateCharSequencePredicate.with(null, CharPredicates.never());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullRemainingCharacterPredicateFails() {
        InitialAndPartCharPredicateCharSequencePredicate.with(CharPredicates.never(), null);
    }

    @Test
    public void testEmpty() {
        this.testFalse("");
    }

    @Test
    public void testMatchesBoth1() {
        this.testTrue("A123");
    }

    @Test
    public void testMatchesBoth2() {
        this.testTrue(new StringBuilder("A123"));
    }

    @Test
    public void testOnlyFirst1() {
        this.testFalse("ABC");
    }

    @Test
    public void testOnlyFirst2() {
        this.testFalse(new StringBuilder("ABC"));
    }

    @Test
    public void testOnlyRemaining1() {
        this.testFalse("123");
    }

    @Test
    public void testOnlyRemaining2() {
        this.testFalse(new StringBuilder("123"));
    }

    @Test
    public void testToString() {
        assertEquals("(\"ABC\", \"123\"*)",
                this.createPredicate().toString());
    }

    @Override
    protected InitialAndPartCharPredicateCharSequencePredicate createPredicate() {
        return Cast.to(InitialAndPartCharPredicateCharSequencePredicate.with(CharPredicates.any("ABC"),
                CharPredicates.any("123")));
    }

    @Override
    protected Class<InitialAndPartCharPredicateCharSequencePredicate> type() {
        return InitialAndPartCharPredicateCharSequencePredicate.class;
    }
}
