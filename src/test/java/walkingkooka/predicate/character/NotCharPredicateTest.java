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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;

import static org.junit.Assert.assertEquals;

final public class NotCharPredicateTest extends CharPredicateTestCase<NotCharPredicate> {

    // constants

    private final static CharPredicate PREDICATE = CharPredicates.is('A');

    // tests

    @Test
    public void testWrapNullPredicateFails() {
        try {
            NotCharPredicate.wrap(null);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
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
        assertSame(NotCharPredicateTest.PREDICATE, this.createCharacterPredicate().negate());
    }

    @Test
    public void testUnwrapsAlreadyWrapped() {
        assertSame(NotCharPredicateTest.PREDICATE,
                NotCharPredicate.wrap(this.createCharacterPredicate()));
    }

    @Test
    public void testToString() {
        assertEquals("!" + NotCharPredicateTest.PREDICATE,
                NotCharPredicate.wrap(NotCharPredicateTest.PREDICATE).toString());
    }

    @Override
    protected NotCharPredicate createCharacterPredicate() {
        return Cast.to(NotCharPredicate.wrap(NotCharPredicateTest.PREDICATE));
    }

    @Override
    protected Class<NotCharPredicate> type() {
        return NotCharPredicate.class;
    }
}
