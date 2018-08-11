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
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

import static org.junit.Assert.assertEquals;

final public class CharPredicatePredicateTest
        extends PredicateTestCase<CharacterPredicatePredicate, Character> {

    // constants

    private final static CharPredicate PREDICATE = CharPredicates.is('X');

    // tests

    @Test
    public void testAdaptNullCharacterPredicateFails() {
        try {
            CharacterPredicatePredicate.adapt(null);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testMatchingCharacter() {
        this.testTrue('X');
    }

    @Test
    public void testWrongCharacter() {
        this.testFalse('*');
    }

    @Test
    public void testToString() {
        assertEquals(CharPredicatePredicateTest.PREDICATE.toString(),
                this.createPredicate().toString());
    }

    @Override
    protected CharacterPredicatePredicate createPredicate() {
        return CharacterPredicatePredicate.adapt(CharPredicatePredicateTest.PREDICATE);
    }

    @Override
    protected Class<CharacterPredicatePredicate> type() {
        return CharacterPredicatePredicate.class;
    }
}
