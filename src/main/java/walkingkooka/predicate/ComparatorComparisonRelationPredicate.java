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
 *
 */

package walkingkooka.predicate;

import walkingkooka.compare.ComparisonRelation;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * A {@link Predicate} that compares a fixed {@link Comparable} with the test value, returning true if the
 * {@link ComparisonRelation} is satisfied.
 */
final class ComparableComparisonRelationPredicate<C extends Comparable<C>> implements Predicate<C> {

    /**
     * Factory that creates a new {@link ComparableComparisonRelationPredicate}
     */
    static <C extends Comparable<C>> ComparableComparisonRelationPredicate<C> with(final C left,
                                                                                   final ComparisonRelation relation) {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(relation, "relation");

        return new ComparableComparisonRelationPredicate(left, relation);
    }

    /**
     * Private ctor use factory
     */
    private ComparableComparisonRelationPredicate(final C left, final ComparisonRelation relation) {
        this.left = left;
        this.relation = relation;
    }

    @Override
    public boolean test(final C c) {
        return relation.test(this.left.compareTo(c));
    }

    /**
     * The left or first of the {@link Comparable#compareTo(Object)}
     */
    private final C left;

    /**
     * The {@link ComparisonRelation} that tests the {@link Comparable#compareTo(Object)}.
     */
    private final ComparisonRelation relation;

    @Override
    public String toString() {
        return this.left + " " + this.relation.toString();
    }
}
