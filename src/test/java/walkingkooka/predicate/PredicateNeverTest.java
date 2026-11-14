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

final public class PredicateNeverTest extends PredicateTestCase<PredicateNever<Object>, Object> {

    @Test
    public void testTestNull() {
        this.testFalse(null);
    }

    @Test
    public void testNotMatched() {
        this.testFalse("not matched");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(PredicateNever.instance(), "<none>");
    }

    @Test
    public void testAnd() {
        final PredicateNever<Object> predicate = PredicateNever.instance();
        assertSame(predicate, predicate.and(Predicates.fake()));
    }

    @Test
    public void testNot() {
        assertSame(Predicates.always(), PredicateNever.instance().negate());
    }

    @Test
    public void testOr() {
        final Predicate<Object> other = Predicates.fake();
        assertSame(other, PredicateNever.instance().or(other));
    }

    @Override
    public PredicateNever<Object> createPredicate() {
        return PredicateNever.instance();
    }

    @Override
    public Class<PredicateNever<Object>> type() {
        return Cast.to(PredicateNever.class);
    }
}
