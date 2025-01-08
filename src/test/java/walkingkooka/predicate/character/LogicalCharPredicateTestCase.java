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
import walkingkooka.HashCodeEqualsDefinedTesting2;

import static org.junit.jupiter.api.Assertions.assertThrows;

abstract public class LogicalCharPredicateTestCase<P extends LogicalCharPredicate>
    implements CharPredicateTesting<P>,
    HashCodeEqualsDefinedTesting2<P> {

    LogicalCharPredicateTestCase() {
        super();
    }

    @Test final public void testWrapNullFirstPredicateFails() {
        assertThrows(NullPointerException.class, () -> this.createCharPredicate(null, CharPredicates.fake()));
    }

    @Test final public void testWrapNullSecondPredicateFails() {
        assertThrows(NullPointerException.class, () -> this.createCharPredicate(CharPredicates.fake(), null));
    }

    @Test final public void testEqualsDifferentFirstCharPredicate() {
        this.checkNotEquals(this.createCharPredicate(CharPredicates.is('d'), CharPredicates.is('r')));
    }

    @Test final public void testEqualsDifferentSecondCharPredicate() {
        this.checkNotEquals(this.createCharPredicate(CharPredicates.is('l'), CharPredicates.is('d')));
    }

    @Test
    public void testToString() {
        final CharPredicate first = CharPredicates.fake();
        final CharPredicate second = CharPredicates.fake();
        final P predicate = this.createCharPredicate(first, second);
        this.toStringAndCheck(predicate,
            first + " " + this.operator(predicate) + " " + second);
    }

    private String operator(final LogicalCharPredicate predicate) {
        return predicate.operator();
    }

    @Override
    public final P createCharPredicate() {
        return this.createCharPredicate(CharPredicates.letter(), CharPredicates.letterOrDigit());
    }

    abstract P createCharPredicate(CharPredicate first, CharPredicate second);

    @Override
    public P createObject() {
        return this.createCharPredicate(CharPredicates.is('l'), CharPredicates.is('r'));
    }
}
