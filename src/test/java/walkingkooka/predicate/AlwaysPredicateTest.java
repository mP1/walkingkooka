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

package walkingkooka.predicate;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertSame;

final public class AlwaysPredicateTest extends PredicateTestCase<AlwaysPredicate<Object>, Object> {

    @Test
    public void testTestNull() {
        this.testTrue(null);
    }

    @Test
    public void testMatches() {
        this.testTrue(new Object());
    }

    @Test
    public void testAnd() {
        final Predicate<Object> other = Predicates.fake();
        assertSame(other, AlwaysPredicate.instance().and(other));
    }

    @Test
    public void testNot() {
        assertSame(Predicates.never(), AlwaysPredicate.instance().negate());
    }

    @Test
    public void testOr() {
        final AlwaysPredicate<Object> predicate = AlwaysPredicate.instance();
        assertSame(predicate, predicate.or(Predicates.fake()));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(AlwaysPredicate.instance(), "*");
    }

    @Override
    public AlwaysPredicate<Object> createPredicate() {
        return AlwaysPredicate.instance();
    }

    @Override
    public Class<AlwaysPredicate<Object>> type() {
        return Cast.to(AlwaysPredicate.class);
    }
}
