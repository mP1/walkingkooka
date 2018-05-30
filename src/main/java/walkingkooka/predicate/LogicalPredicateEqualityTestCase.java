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

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

import java.util.function.Predicate;

abstract public class LogicalPredicateEqualityTestCase<M extends Predicate<String> & HashCodeEqualsDefined>
        extends HashCodeEqualsDefinedEqualityTestCase<M> {

    LogicalPredicateEqualityTestCase() {
        super();
    }

    private final static Predicate<String> LEFT = Predicates.is("first");

    private final static Predicate<String> RIGHT = Predicates.is("second");

    private final static Predicate<String> DIFFERENT = Predicates.is("different");

    @Test final public void testDifferentFirstPredicate() {
        this.checkNotEquals(this.createObject(LogicalPredicateEqualityTestCase.DIFFERENT,
                LogicalPredicateEqualityTestCase.RIGHT));
    }

    @Test final public void testDifferentSecondPredicate() {
        this.checkNotEquals(this.createObject(LogicalPredicateEqualityTestCase.LEFT,
                LogicalPredicateEqualityTestCase.DIFFERENT));
    }

    @Override
    protected M createObject() {
        return this.createObject(LogicalPredicateEqualityTestCase.LEFT, Predicates.is("second"));
    }

    abstract M createObject(Predicate<String> first, Predicate<String> second);
}
