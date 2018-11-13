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
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

final public class InitialAndPartCharPredicateCharSequencePredicateEqualityTest
        extends PredicateEqualityTestCase<InitialAndPartCharPredicateCharSequencePredicate, CharSequence> {

    @Test
    public void testDifferentInitialCharPredicate() {
        this.checkNotEquals(this.create(CharPredicates.asciiPrintable(), null));
    }

    @Test
    public void testDifferentRemainingCharPredicate() {
        this.checkNotEquals(this.create(null, CharPredicates.asciiPrintable()));
    }

    // helpers

    @Override
    protected InitialAndPartCharPredicateCharSequencePredicate createObject() {
        return this.create(null, null);
    }

    private InitialAndPartCharPredicateCharSequencePredicate create(final CharPredicate initial, final CharPredicate remaining) {
        return Cast.to(InitialAndPartCharPredicateCharSequencePredicate.with(
                null !=initial ? initial : CharPredicates.ascii(),
                null != remaining ? remaining : CharPredicates.asciiControl()));
    }
}
