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

package walkingkooka.predicate.character;

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

final public class CharPredicateCharSequencePredicateEqualityTest
        extends HashCodeEqualsDefinedEqualityTestCase<CharPredicateCharSequencePredicate> {

    // constants
    private final static CharPredicate PREDICATE = CharPredicates.always();

    // tests

    @Test
    public void testDifferentCharacterPredicate() {
        this.checkNotEquals(CharPredicateCharSequencePredicate.adapt(CharPredicates.never()));
    }

    @Override
    protected CharPredicateCharSequencePredicate createObject() {
        return CharPredicateCharSequencePredicate.adapt(
                CharPredicateCharSequencePredicateEqualityTest.PREDICATE);
    }
}
