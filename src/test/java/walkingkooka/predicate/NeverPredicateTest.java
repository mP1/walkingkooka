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

package walkingkooka.predicate;

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.test.SerializationTesting;

import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class NeverPredicateTest extends PredicateTestCase<NeverPredicate<Object>, Object>
        implements SerializationTesting<NeverPredicate<Object>> {
    @Test
    @Ignore
    @Override
    public void testTestNullFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testNotMatched() {
        this.testFalse("not matched");
    }

    @Test
    public void testToString() {
        assertEquals("<none>", NeverPredicate.instance().toString());
    }

    @Test
    public void testAnd() {
        final NeverPredicate<Object> predicate = NeverPredicate.instance();
        assertSame(predicate, predicate.and(Predicates.fake()));
    }

    @Test
    public void testNot() {
        assertSame(Predicates.always(), NeverPredicate.instance().negate());
    }

    @Test
    public void testOr() {
        final Predicate<Object> other = Predicates.fake();
        assertSame(other, NeverPredicate.instance().or(other));
    }

    @Override
    protected NeverPredicate<Object> createPredicate() {
        return NeverPredicate.instance();
    }

    @Override
    public Class<NeverPredicate<Object>> type() {
        return Cast.to(NeverPredicate.class);
    }

    @Override
    public NeverPredicate<Object> serializableInstance() {
        return NeverPredicate.instance();
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return true;
    }
}
