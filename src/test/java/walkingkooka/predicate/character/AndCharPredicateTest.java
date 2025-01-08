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

import static org.junit.jupiter.api.Assertions.assertSame;

final public class AndCharPredicateTest extends LogicalCharPredicateTestCase<AndCharPredicate> {

    private final static CharPredicate PREDICATE = CharPredicates.is('a');

    // tests

    @Test
    public void testWrapWithLeftAlways() {
        final CharPredicate always = CharPredicates.always();
        assertSame(PREDICATE, AndCharPredicate.wrap(always, PREDICATE));
    }

    @Test
    public void testWrapWithRightAlways() {
        final CharPredicate always = CharPredicates.always();
        assertSame(PREDICATE, AndCharPredicate.wrap(PREDICATE, always));
    }

    @Test
    public void testWrapWithLeftNever() {
        final CharPredicate never = CharPredicates.never();
        assertSame(never, AndCharPredicate.wrap(never, PREDICATE));
    }

    @Test
    public void testWrapWithRightNever() {
        final CharPredicate never = CharPredicates.never();
        assertSame(never, AndCharPredicate.wrap(PREDICATE, never));
    }

    @Test
    public void testWrapWithSame() {
        assertSame(PREDICATE, AndCharPredicate.wrap(PREDICATE, PREDICATE));
    }

    @Test
    public void testLeftFalseMatchesShortSkipsRight() {
        final CharPredicate left = CharPredicates.is('a');
        final CharPredicate right = CharPredicates.fake();
        this.testFalse(AndCharPredicate.wrap(left, right), 'd');
    }

    @Test
    public void testLeftTrueRightFalse() {
        final CharPredicate left = CharPredicates.is('a');
        final CharPredicate right = CharPredicates.is('d');
        this.testFalse(AndCharPredicate.wrap(left, right), 'a');
    }

    @Test
    public void testBoth() {
        final CharPredicate left = CharPredicates.is('a');
        final CharPredicate right = CharPredicates.caseInsensitive('A');
        this.checkNotEquals(left, right);
        this.testTrue(AndCharPredicate.wrap(left, right), 'a');
    }

    @Test
    public void testMatchNot() {
        final CharPredicate left = CharPredicates.caseInsensitive('a');
        final CharPredicate predicate = AndCharPredicate.wrap(this.not(left), this.not('a'));
        this.testTrue(predicate, 'd');
        this.testFalse(predicate, 'a');
    }

    @Test
    public void testMatchNot2() {
        final CharPredicate left = CharPredicates.caseInsensitive('a');
        final CharPredicate predicate = AndCharPredicate.wrap(this.not(left), this.not('d'));
        this.testTrue(predicate, 'x');
        this.testFalse(predicate, 'a');
        this.testFalse(predicate, 'd');
    }

    @Test
    public void testBothNotable() {
        final CharPredicate predicate = AndCharPredicate.wrap(this.not('a'),
            this.not(CharPredicates.letter()));
        this.testFalse(predicate, 'a');
        this.testTrue(predicate, '1');
    }

    private CharPredicate not(final char c) {
        return this.not(CharPredicates.is(c));
    }

    private CharPredicate not(final CharPredicate predicate) {
        return CharPredicates.not(predicate);
    }

    @Override
    AndCharPredicate createCharPredicate(final CharPredicate left, final CharPredicate right) {
        return Cast.to(AndCharPredicate.wrap(left, right));
    }

    @Override
    public Class<AndCharPredicate> type() {
        return AndCharPredicate.class;
    }
}
