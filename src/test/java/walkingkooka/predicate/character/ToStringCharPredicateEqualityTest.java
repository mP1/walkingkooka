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
import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

final public class ToStringCharPredicateEqualityTest
        extends HashCodeEqualsDefinedEqualityTestCase<ToStringCharPredicate> {

    // constants

    private final static CharPredicate PREDICATE = CharPredicates.fake();

    private final static String TOSTRING = "* toString *";

    // tests

    @Test
    public void testDifferentPredicate() {
        this.checkNotEquals(ToStringCharPredicate.wrap(CharPredicates.fake(),
                ToStringCharPredicateEqualityTest.TOSTRING));
    }

    @Test
    public void testDifferentToString() {
        this.checkNotEquals(ToStringCharPredicate.wrap(ToStringCharPredicateEqualityTest.PREDICATE,
                "different"));
    }

    @Override
    protected ToStringCharPredicate createObject() {
        return Cast.to(ToStringCharPredicate.wrap(ToStringCharPredicateEqualityTest.PREDICATE,
                ToStringCharPredicateEqualityTest.TOSTRING));
    }
}
