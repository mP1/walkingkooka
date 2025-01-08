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
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class InitialAndPartCharPredicateCharSequencePredicateTest
    extends PredicateTestCase<InitialAndPartCharPredicateCharSequencePredicate, CharSequence>
    implements HashCodeEqualsDefinedTesting2<InitialAndPartCharPredicateCharSequencePredicate> {

    // tests

    @Test
    public void testWithNullInitialCharPredicateFails() {
        assertThrows(NullPointerException.class, () -> InitialAndPartCharPredicateCharSequencePredicate.with(null, CharPredicates.never()));
    }

    @Test
    public void testWithNullRemainingCharPredicateFails() {
        assertThrows(NullPointerException.class, () -> InitialAndPartCharPredicateCharSequencePredicate.with(CharPredicates.never(), null));
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
    public void testEqualsDifferentInitialCharPredicate() {
        this.checkNotEquals(this.createPredicate(CharPredicates.asciiPrintable(), this.part()));
    }

    @Test
    public void testEqualsDifferentRemainingCharPredicate() {
        this.checkNotEquals(this.createPredicate(this.initial(), CharPredicates.asciiPrintable()));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPredicate(), "(\"ABC\", \"123\"*)");
    }

    @Override
    public InitialAndPartCharPredicateCharSequencePredicate createPredicate() {
        return Cast.to(InitialAndPartCharPredicateCharSequencePredicate.with(this.initial(), this.part()));
    }

    private CharPredicate initial() {
        return CharPredicates.any("ABC");
    }

    private CharPredicate part() {
        return CharPredicates.any("123");
    }

    @Override
    public Class<InitialAndPartCharPredicateCharSequencePredicate> type() {
        return InitialAndPartCharPredicateCharSequencePredicate.class;
    }

    @Override
    public InitialAndPartCharPredicateCharSequencePredicate createObject() {
        return this.createPredicate();
    }

    private InitialAndPartCharPredicateCharSequencePredicate createPredicate(final CharPredicate initial,
                                                                             final CharPredicate remaining) {
        return Cast.to(InitialAndPartCharPredicateCharSequencePredicate.with(initial, remaining));
    }
}
