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

import org.junit.jupiter.api.Test;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class CharPredicatePredicateTest extends PredicateTestCase<CharPredicatePredicate, Character> {

    // constants

    private final static CharPredicate PREDICATE = CharPredicates.is('X');

    // tests

    @Test
    public void testAdaptNullCharPredicateFails() {
        assertThrows(NullPointerException.class, () -> {
            CharPredicatePredicate.adapt(null);
        });
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
    protected CharPredicatePredicate createPredicate() {
        return CharPredicatePredicate.adapt(CharPredicatePredicateTest.PREDICATE);
    }

    @Override
    protected Class<CharPredicatePredicate> type() {
        return CharPredicatePredicate.class;
    }
}
