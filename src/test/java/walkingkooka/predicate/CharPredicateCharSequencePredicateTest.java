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
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class CharPredicateCharSequencePredicateTest
    extends PredicateTestCase<CharPredicateCharSequencePredicate, CharSequence>
    implements HashCodeEqualsDefinedTesting2<CharPredicateCharSequencePredicate> {

    // constants

    private final static String CHARS = "abcdef";

    // tests

    @Test
    public void testWithNullCharPredicateFails() {
        assertThrows(NullPointerException.class, () -> CharPredicateCharSequencePredicate.with(null));
    }

    @Test
    public void testEmpty() {
        this.testFalse("");
    }

    @Test
    public void testMatchesAll() {
        this.testTrue("fed");
    }

    @Test
    public void testMatchesNone() {
        this.testFalse("xyz");
    }

    @Test
    public void testMatchesSome() {
        this.testFalse("abcxyz");
    }

    @Test
    public void testEqualsDifferentPredicate() {
        this.checkNotEquals(CharPredicateCharSequencePredicate.with(CharPredicates.any("different")));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPredicate(), CharSequences.quote(CHARS).toString());
    }

    @Override
    public CharPredicateCharSequencePredicate createPredicate() {
        return Cast.to(CharPredicateCharSequencePredicate.with(CharPredicates.any(CHARS)));
    }

    @Override
    public Class<CharPredicateCharSequencePredicate> type() {
        return CharPredicateCharSequencePredicate.class;
    }

    @Override
    public CharPredicateCharSequencePredicate createObject() {
        return this.createPredicate();
    }
}
