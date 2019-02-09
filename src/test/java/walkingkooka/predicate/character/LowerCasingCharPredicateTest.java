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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final public class LowerCasingCharPredicateTest
                implements CharPredicateTesting<LowerCasingCharPredicate>,
        HashCodeEqualsDefinedTesting<LowerCasingCharPredicate>,
        SerializationTesting<LowerCasingCharPredicate> {

    @Test
    public void testWrapNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> {
            LowerCasingCharPredicate.wrap(null);
        });
    }

    @Test
    public void testDoesNotReWrapLowerCasingCharPredicate() {
        final CharPredicate predicate = LowerCasingCharPredicate.wrap(CharPredicates.is('x'));
        assertSame(predicate,
                LowerCasingCharPredicate.wrap(predicate),
                "should not have rewrapped");
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
    public void testWrapCharPredicate() {
        assertTrue(LowerCasingCharPredicate.wrap(CharPredicates.is('a')).test('A'));
    }

    @Test
    public void testEqualsDifferentPredicate() {
        this.checkNotEquals(LowerCasingCharPredicate.wrap(CharPredicates.never()));
    }

    @Test
    public void testToString() {
        final CharPredicate predicate = CharPredicates.fake();
        this.toStringAndCheck(LowerCasingCharPredicate.wrap(predicate), "lowercase then " + predicate);
    }

    @Override
    public LowerCasingCharPredicate createCharPredicate() {
        return Cast.to(LowerCasingCharPredicate.wrap(CharPredicates.is('x')));
    }

    @Override
    public Class<LowerCasingCharPredicate> type() {
        return LowerCasingCharPredicate.class;
    }

    @Override
    public LowerCasingCharPredicate createObject() {
        return this.createCharPredicate();
    }

    @Override
    public LowerCasingCharPredicate serializableInstance() {
        return Cast.to(LowerCasingCharPredicate.wrap(CharPredicates.always()));
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
