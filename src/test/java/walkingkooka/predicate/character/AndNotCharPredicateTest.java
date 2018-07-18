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
import walkingkooka.Cast;

final public class AndNotCharPredicateTest extends LogicalCharPredicateTestCase<AndNotCharPredicate> {

    private final static CharPredicate PREDICATE = CharPredicates.is('a');

    // tests

    @Test
    public void testWrapWithLeftAlways() {
        final CharPredicate always = CharPredicates.always();
        assertSame(PREDICATE, AndNotCharPredicate.wrap(always, PREDICATE));
    }

    @Test
    public void testWrapWithRightAlways() {
        final CharPredicate always = CharPredicates.always();
        assertSame(PREDICATE, AndNotCharPredicate.wrap(PREDICATE, always));
    }

    @Test
    public void testWrapWithLeftNever() {
        final CharPredicate never = CharPredicates.never();
        assertSame(never, AndNotCharPredicate.wrap(never, PREDICATE));
    }

    @Test
    public void testWrapWithSame() {
        assertSame(CharPredicates.never(), AndNotCharPredicate.wrap(PREDICATE, PREDICATE));
    }

    @Test
    public void testLeftFalseSkipsRight() {
        final CharPredicate left = CharPredicates.is('a');
        final CharPredicate right = CharPredicates.fake();
        this.testFalse(AndNotCharPredicate.wrap(left, right), 'd');
    }

    @Test
    public void testLeftTrueRightFalse() {
        final CharPredicate left = CharPredicates.is('a');
        final CharPredicate right = CharPredicates.is('d');
        this.testTrue(AndNotCharPredicate.wrap(left, right), 'a');
    }

    @Test
    public void testLeftTrueRightFalse2() {
        final CharPredicate p = CharPredicates.is('a')
                .andNot(CharPredicates.is('d'));
        this.testTrue(p, 'a');
    }

    @Test
    public void testBoth() {
        final CharPredicate left = CharPredicates.is('a');
        final CharPredicate right = CharPredicates.caseInsensitive('A');
        assertNotEquals(left, right);
        this.testFalse(AndNotCharPredicate.wrap(left, right), 'a');
    }

    @Test
    public void testNever() {
        final CharPredicate left = CharPredicates.is('a');
        final CharPredicate right = CharPredicates.is('b');
        assertNotEquals(left, right);
        this.testFalse(AndNotCharPredicate.wrap(left, right), 'z');
    }

    @Override
    AndNotCharPredicate createCharacterPredicate(final CharPredicate left, final CharPredicate right) {
        return Cast.to(AndNotCharPredicate.wrap(left, right));
    }

    @Override
    protected Class<AndNotCharPredicate> type() {
        return AndNotCharPredicate.class;
    }
}
