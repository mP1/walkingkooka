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
import static org.junit.Assert.assertSame;

final public class LowerCasingCharPredicateTest
        extends CharPredicateTestCase<LowerCasingCharPredicate> {

    @Test
    public void testWrapNullPredicateFails() {
        try {
            LowerCasingCharPredicate.wrap(null);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testDoesNotReWrapLowerCasingCharacterPredicate() {
        final CharPredicate predicate = LowerCasingCharPredicate.wrap(CharPredicates.is('x'));
        assertSame("should not have rewrapped",
                predicate,
                LowerCasingCharPredicate.wrap(predicate));
    }

    @Test
    public void testUpperCase() {
        this.testTrue('X');
    }

    @Test
    public void testSameCase() {
        this.testTrue('x');
    }

    @Test
    public void testDifferent() {
        this.testFalse('d');
    }

    @Test
    public void testWrapCharacterPredicate() {
        Assert.assertTrue(LowerCasingCharPredicate.wrap(CharPredicates.is('a')).test('A'));
    }

    @Test
    public void testToString() {
        final CharPredicate predicate = CharPredicates.fake();
        assertEquals("lowercase then " + predicate,
                LowerCasingCharPredicate.wrap(predicate).toString());
    }

    @Override
    protected LowerCasingCharPredicate createCharacterPredicate() {
        return Cast.to(LowerCasingCharPredicate.wrap(new CharPredicate() {

            @Override
            public boolean test(final char c) {
                return 'x' == c;
            }
        }));
    }

    @Override
    protected Class<LowerCasingCharPredicate> type() {
        return LowerCasingCharPredicate.class;
    }
}
