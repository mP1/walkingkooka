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

package walkingkooka.predicate.character;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.HashCodeEqualsDefinedTesting2;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class NotCharPredicateTest implements CharPredicateTesting<NotCharPredicate>,
    HashCodeEqualsDefinedTesting2<NotCharPredicate> {

    // constants

    private final static CharPredicate PREDICATE = CharPredicates.is('A');

    // tests

    @Test
    public void testWrapNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> NotCharPredicate.wrap(null));
    }

    @Test
    public void testWrappedReturnsTrue() {
        this.testFalse('A');
    }

    @Test
    public void testWrappedFalseTrue() {
        this.testTrue('B');
    }

    @Test
    public void testNot() {
        assertSame(PREDICATE, this.createCharPredicate().negate());
    }

    @Test
    public void testUnwrapsAlreadyWrapped() {
        assertSame(PREDICATE,
            NotCharPredicate.wrap(this.createCharPredicate()));
    }

    @Test
    public void testEqualsDifferentPredicate() {
        this.checkNotEquals(NotCharPredicate.wrap(CharPredicates.fake()));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(NotCharPredicate.wrap(PREDICATE), "!" + PREDICATE);
    }

    @Override
    public NotCharPredicate createCharPredicate() {
        return Cast.to(NotCharPredicate.wrap(PREDICATE));
    }

    @Override
    public Class<NotCharPredicate> type() {
        return NotCharPredicate.class;
    }

    @Override
    public NotCharPredicate createObject() {
        return this.createCharPredicate();
    }
}
