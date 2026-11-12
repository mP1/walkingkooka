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

import static org.junit.jupiter.api.Assertions.assertSame;

final public class CharPredicateNeverTest extends CharPredicateTestCase<CharPredicateNever> {

    @Test
    public void testMatches() {
        this.testFalse('a');
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(CharPredicateNever.INSTANCE, "<none>");
    }

    @Test
    public void testAnd() {
        final CharPredicateNever predicate = CharPredicateNever.INSTANCE;
        assertSame(predicate, predicate.and(CharPredicates.fake()));
    }

    @Test
    public void testNot() {
        assertSame(CharPredicates.always(), CharPredicateNever.INSTANCE.negate());
    }

    @Test
    public void testOr() {
        final CharPredicate other = CharPredicates.fake();
        assertSame(other, CharPredicateNever.INSTANCE.or(other));
    }

    @Override
    public CharPredicateNever createCharPredicate() {
        return CharPredicateNever.INSTANCE;
    }

    @Override
    public Class<CharPredicateNever> type() {
        return CharPredicateNever.class;
    }
}
