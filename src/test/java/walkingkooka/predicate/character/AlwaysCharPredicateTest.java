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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class AlwaysCharPredicateTest extends CharPredicateTestCase<AlwaysCharPredicate> {

    @Test
    public void testMatches() {
        this.testTrue('a');
    }

    @Test
    public void testAnd() {
        final CharPredicate other = CharPredicates.fake();
        assertSame(other, AlwaysCharPredicate.INSTANCE.and(other));
    }

    @Test
    public void testNot() {
        assertSame(CharPredicates.never(), AlwaysCharPredicate.INSTANCE.negate());
    }

    @Test
    public void testOr() {
        final AlwaysCharPredicate predicate = AlwaysCharPredicate.INSTANCE;
        assertSame(predicate, predicate.or(CharPredicates.fake()));
    }

    @Test
    public void testToString() {
        assertEquals("*", AlwaysCharPredicate.INSTANCE.toString());
    }

    @Override
    protected AlwaysCharPredicate createCharacterPredicate() {
        return AlwaysCharPredicate.INSTANCE;
    }

    @Override
    protected Class<AlwaysCharPredicate> type() {
        return AlwaysCharPredicate.class;
    }
}
