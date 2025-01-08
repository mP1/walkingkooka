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

final public class OrCharPredicateTest extends LogicalCharPredicateTestCase<OrCharPredicate> {

    private final static CharPredicate PREDICATE = CharPredicates.is('s');

    // tests

    @Test
    public void testWrapWithLeftAlways() {
        final CharPredicate always = CharPredicates.always();
        assertSame(always, OrCharPredicate.wrap(always, PREDICATE));
    }

    @Test
    public void testWrapWithRightAlways() {
        final CharPredicate always = CharPredicates.always();
        assertSame(always, OrCharPredicate.wrap(PREDICATE, always));
    }

    @Test
    public void testWrapWithLeftNever() {
        final CharPredicate never = CharPredicates.never();
        assertSame(PREDICATE,
            OrCharPredicate.wrap(never, PREDICATE));
    }

    @Test
    public void testWrapWithRightNever() {
        final CharPredicate never = CharPredicates.never();
        assertSame(PREDICATE,
            OrCharPredicate.wrap(PREDICATE, never));
    }

    @Test
    public void testWrapWithSame() {
        assertSame(PREDICATE,
            OrCharPredicate.wrap(PREDICATE, PREDICATE));
    }

    @Test
    public void testLeftMatchesShortSkipsRight() {
        this.testTrue(OrCharPredicate.wrap(this.is('m'), CharPredicates.fake()), 'm');
    }

    @Test
    public void testRightTrue() {
        this.testTrue(OrCharPredicate.wrap(this.is('d'), this.is('m')), 'm');
    }

    @Test
    public void testNeither() {
        final CharPredicate predicate = OrCharPredicate.wrap(this.not(this.is('m')),
            this.not(CharPredicates.caseInsensitive('m')));
        this.testFalse(predicate, 'm');
        this.testTrue(predicate, 'd');
    }

    @Test
    public void testMatchNot2() {
        final CharPredicate predicate = OrCharPredicate.wrap(this.not(this.is('m')),
            this.not(this.is('d')));
        this.testTrue(predicate, 'm');
        this.testTrue(predicate, 'd');
        this.testTrue(predicate, 'e');
    }

    private CharPredicate is(final char c) {
        return CharPredicates.is(c);
    }

    private CharPredicate not(final CharPredicate predicate) {
        return CharPredicates.not(predicate);
    }

    @Override
    OrCharPredicate createCharPredicate(final CharPredicate left, final CharPredicate right) {
        return Cast.to(OrCharPredicate.wrap(left, right));
    }

    @Override
    public Class<OrCharPredicate> type() {
        return OrCharPredicate.class;
    }
}
