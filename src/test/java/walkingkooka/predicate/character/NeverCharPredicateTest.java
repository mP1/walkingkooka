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
import walkingkooka.test.SerializationTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

final public class NeverCharPredicateTest extends CharPredicateTestCase<NeverCharPredicate> implements SerializationTesting<NeverCharPredicate> {

    @Test
    public void testMatches() {
        this.testFalse('a');
    }

    @Test
    public void testToString() {
        assertEquals("<none>", NeverCharPredicate.INSTANCE.toString());
    }

    @Test
    public void testAnd() {
        final NeverCharPredicate predicate = NeverCharPredicate.INSTANCE;
        assertSame(predicate, predicate.and(CharPredicates.fake()));
    }

    @Test
    public void testNot() {
        assertSame(CharPredicates.always(), NeverCharPredicate.INSTANCE.negate());
    }

    @Test
    public void testOr() {
        final CharPredicate other = CharPredicates.fake();
        assertSame(other, NeverCharPredicate.INSTANCE.or(other));
    }

    @Override
    protected NeverCharPredicate createCharPredicate() {
        return NeverCharPredicate.INSTANCE;
    }

    @Override
    public Class<NeverCharPredicate> type() {
        return NeverCharPredicate.class;
    }

    @Override
    public NeverCharPredicate serializableInstance() {
        return NeverCharPredicate.INSTANCE;
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return true;
    }
}
