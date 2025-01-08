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
import walkingkooka.collect.list.Lists;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class AnyPredicateTest extends PredicateTestCase<AnyPredicate<Object>, Object> {

    @Test
    public void testWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> AnyPredicate.with(null)
        );
    }

    @Override
    public void testTestNullFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testTestMatches() {
        this.testTrue("1A");
    }

    @Test
    public void testTestMatches2() {
        this.testTrue("2B");
    }

    @Test
    public void testTestMatchesFalse() {
        this.testFalse("*DOESNT MATCH ANY PREDICATE*");
    }

    @Override
    public AnyPredicate<Object> createPredicate() {
        return AnyPredicate.with(
            Lists.of(
                Predicates.is("1A"),
                Predicates.is("2B"),
                Predicates.is("3C")
            )
        );
    }

    @Test
    public void testTestGenerics() {
        this.testTrue(
            AnyPredicate.with(
                Lists.of(
                    Predicates.is("1A"),
                    Predicates.is((CharSequence) "2B"),
                    Predicates.is("3C")
                )
            ),
            "3C"
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createPredicate(),
            "\"1A\" | \"2B\" | \"3C\""
        );
    }

    // class............................................................................................................

    @Override
    public Class<AnyPredicate<Object>> type() {
        return Cast.to(AnyPredicate.class);
    }
}
